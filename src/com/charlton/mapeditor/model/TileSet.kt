package com.charlton.mapeditor.model

import java.awt.image.BufferedImage
import java.io.File
import java.io.IOException
import javax.imageio.ImageIO

class TileSet(private val perTileWidth: Int, private val perTileHeight: Int, tileSetFile: String) {
    val tileImageList = ArrayList<BufferedImage>()
    var tileSetImage: BufferedImage? = null
        private set

    init {
        val tileSet = File(TILE_FOLDER + tileSetFile)
        try {
            this.tileSetImage = ImageIO.read(tileSet)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        initTileSet()
    }

    private fun initTileSet() {
        val rows = tileSetImage!!.height / perTileHeight
        val columns = tileSetImage!!.width / perTileWidth
        for (rowIndex in 0..<rows) {
            for (columnIndex in 0..<columns) {
                val newTileImage = tileSetImage!!.getSubimage(
                    columnIndex * perTileWidth, rowIndex * perTileHeight,
                    perTileWidth, perTileHeight
                )
                tileImageList.add(newTileImage)
            }
        }
    }

    val tileSetRows: Int
        get() = tileSetImage!!.height / perTileHeight

    val tileSetColumns: Int
        get() = tileSetImage!!.width / perTileWidth

    companion object {
        const val TILE_FOLDER: String = "assets/tiles/"
    }
}