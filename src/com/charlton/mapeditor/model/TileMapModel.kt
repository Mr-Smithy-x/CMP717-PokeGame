package com.charlton.mapeditor.model

import java.io.Serializable

class TileMapModel(
    @JvmField val tileSetFile: String,
    @JvmField val perTileWidth: Int,
    @JvmField val perTileHeight: Int,
    @JvmField var mapRows: Int,
    @JvmField val mapColumns: Int
) : Serializable {
    @JvmField
    var mapLayout: Array<IntArray> = Array(mapRows) { IntArray(mapColumns) }
    @JvmField
    var collisionMap: Array<BooleanArray> = Array(mapRows) { BooleanArray(mapColumns) }
    @JvmField
    var objectMap: Array<IntArray> = Array(mapRows) { IntArray(mapColumns) }

    init {
        for (row in 0..<mapRows) {
            for (col in 0..<mapColumns) {
                mapLayout[row][col] = -1
                collisionMap[row][col] = false
                objectMap[row][col] = -1
            }
        }
    }

    companion object {
        const val MAP_FOLDER: String = "assets/maps/"

        // Constant serialization UID so that maps can be deserialized despite running JVM version.
        private const val serialVersionUID = 4112L
    }
}