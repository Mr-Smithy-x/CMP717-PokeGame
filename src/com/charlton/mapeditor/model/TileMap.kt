package com.charlton.mapeditor.model

import com.charlton.gameengine.camera.CameraContract
import com.charlton.gameengine.contracts.Renderable
import com.charlton.gameengine.models.Location
import com.charlton.gameengine.models.Size3D
import com.charlton.gameengine.models.ImageObject
import com.charlton.gameengine.models.ImageObject.Pose
import java.awt.Graphics
import java.util.*
import java.util.stream.Collectors
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt


class TileMap(private val mapModel: TileMapModel) : Iterable<Location>, CameraContract, Renderable, Network<Tile> {
    private val tileSet = TileSet(mapModel.perTileWidth, mapModel.perTileHeight, mapModel.tileSetFile)
    private val mapWidth = mapModel.perTileWidth * mapModel.mapColumns
    private val mapHeight = mapModel.perTileHeight * mapModel.mapRows
    private var mainLayerTiles = Array(mapModel.mapRows) { arrayOfNulls<Tile>(mapModel.mapColumns) }
    private val objectLayerTiles = Array(mapModel.mapRows) { arrayOfNulls<Tile>(mapModel.mapColumns) }
    private val points: MutableSet<Location> = LinkedHashSet()
    @Transient
    override val location: Location = Location(0f, 0f, 0f)
    @Transient
    override val size: Size3D<Float> = Size3D(mapWidth.toFloat(), mapHeight.toFloat(), 0f)

    private val pointIterable: Iterable<Tile> = object : Iterable<Tile> {
        override fun iterator(): Iterator<Tile> {
            return points.stream().map { point: Location -> mainLayerTiles[point.y.toInt()][point.x.toInt()]!! }
                .iterator()
        }
    }

    fun initializeMap() {
        for (row in 0..<mapModel.mapRows) {
            for (col in 0..<mapModel.mapColumns) {
                val tileID = mapModel.mapLayout[row][col]
                val tileImage = tileSet.tileImageList[tileID]
                val currentTile = Tile(tileImage, tileID)
                currentTile.isCollisionEnabled = mapModel.collisionMap[row][col]
                if (mapModel.objectMap[row][col] != -1) {
                    val objTileID = mapModel.objectMap[row][col]
                    val objectTile = Tile(tileSet.tileImageList[objTileID], objTileID)
                    objectTile.setX(col)
                    objectTile.setY(row)
                    objectLayerTiles[row][col] = objectTile
                }
                currentTile.setX(col)
                currentTile.setY(row)
                currentTile.initBoundsRect()
                val location = Location(col.toFloat(), row.toFloat(), 0f)
                points.add(location)
                currentTile.setMapLocation(location)
                mainLayerTiles[row][col] = currentTile
            }
        }
        initializeNeighbors()
    }

    fun initializeNeighbors() {
        for (row in 0..<mapModel.mapRows) {
            for (col in 0..<mapModel.mapColumns) {
                mainLayerTiles[row][col]!!.calculateNearestNodes(this)
            }
        }
    }

    override fun render(g: Graphics) {
        for (row in 0..<mapModel.mapRows) {
            for (col in 0..<mapModel.mapColumns) {
                mainLayerTiles[row][col]!!.render(g)
                if (objectLayerTiles[row][col] != null) {
                    objectLayerTiles[row][col]?.render(g)
                }
            }
        }
    }

    fun getSurroundingTiles(movable: ImageObject): List<Tile?> {
        return getSurroundingTiles(movable.getX(), movable.getY(), movable.currentPose)
    }

    fun getSurroundingTiles(x: Int, y: Int, pose: Pose?): List<Tile?> {
        val row = y / mapModel.perTileHeight
        val col = x / mapModel.perTileWidth
        val tiles: MutableList<Tile?> = ArrayList()
        when (pose) {
            Pose.UP -> for (i in intArrayOf(col, col + 1, col - 1)) tiles.add(getMainLayerTileAt(row - 1, i))
            Pose.DOWN -> for (i in intArrayOf(col, col + 1, col - 1)) tiles.add(getMainLayerTileAt(row + 1, i))
            Pose.LEFT -> for (i in intArrayOf(row - 1, row, row + 1)) tiles.add(getMainLayerTileAt(i, col - 1))
            Pose.RIGHT -> for (i in intArrayOf(row - 1, row, row + 1)) tiles.add(getMainLayerTileAt(i, col + 1))
            Pose.ALL -> {
                for (i in intArrayOf(row - 1, row + 1)) {
                    tiles.add(getMainLayerTileAt(i, col))
                    tiles.add(getMainLayerTileAt(i, col + 1))
                    tiles.add(getMainLayerTileAt(i, col - 1))
                }
                for (i in intArrayOf(col - 1, col + 1)) tiles.add(getMainLayerTileAt(row, i))
            }

            else -> {}
        }
        return tiles.parallelStream().filter { obj: Tile? -> Objects.nonNull(obj) }.collect(Collectors.toList())
    }

    // Returns a nearby tile in any of the 4 directions, with a provided offset for how far to grab a tile from the given point.
    fun getNearbyTile(x: Double, y: Double, direction: Pose?, tileOffset: Int): Tile? {
        val row = (y.toInt()) / mapModel.perTileHeight
        val col = (x.toInt()) / mapModel.perTileWidth
        return when (direction) {
            Pose.UP -> getMainLayerTileAt(row - tileOffset, col)
            Pose.DOWN -> getMainLayerTileAt(row + tileOffset, col)
            Pose.LEFT -> getMainLayerTileAt(row, col - tileOffset)
            Pose.RIGHT -> getMainLayerTileAt(row, col + tileOffset)
            else -> null
        }
    }

    fun getTileAtPoint(x: Double, y: Double): Tile? {
        val row = (y.toInt()) / mapModel.perTileHeight
        val col = (x.toInt()) / mapModel.perTileWidth
        return getMainLayerTileAt(row, col)
    }

    fun getObjectTileAtPoint(x: Double, y: Double): Tile? {
        val row = (y.toInt()) / mapModel.perTileHeight
        val col = (x.toInt()) / mapModel.perTileWidth
        return getObjectLayerTileAt(row, col)
    }

    fun removeObjectTile(x: Double, y: Double) {
        val row = (y.toInt()) / mapModel.perTileHeight
        val col = (x.toInt()) / mapModel.perTileWidth
        objectLayerTiles[row][col] = null
    }

    fun setCollisionOverrideOnTile(x: Double, y: Double) {
        val tile = getTileAtPoint(x, y)
        tile!!.isCollisionOverride = true
    }

    private fun getMainLayerTileAt(row: Int, col: Int): Tile? {
        return try {
            mainLayerTiles[row][col]
        } catch (e: ArrayIndexOutOfBoundsException) {
            null
        }
    }

    private fun getObjectLayerTileAt(row: Int, col: Int): Tile? {
        return try {
            objectLayerTiles[row][col]
        } catch (e: ArrayIndexOutOfBoundsException) {
            null
        }
    }

    val x: Number
        get() = location.x

    val y: Number
        get() = location.y


    val width: Number
        get() = mapWidth

    val height: Number
        get() = mapHeight

    override val nodes: Iterable<Tile>
        get() = pointIterable


    override fun hasCrossDirection(): Boolean {
        return false
    }


    override fun iterator(): MutableIterator<Location> {
        return points.iterator()
    }


    override fun find(col: Int, row: Int): Tile? {
        var col = col
        var row = row
        row = abs(row.toDouble()).toInt()
        col = abs(col.toDouble()).toInt()
        if (mainLayerTiles.size > row) {
            if (mainLayerTiles[row].size > col) {
                return mainLayerTiles[row][col]
            }
        }
        return null
    }

    companion object {
        // Calculates the distance between two tiles via the Euclidean distance formula. (from their center points)
        fun euclideanDistanceBetweenTiles(firstTile: Tile, secondTile: Tile): Int {
            val x1 = (firstTile.getX() + (firstTile.getWidth() / 2))
            val y1 = (firstTile.getY() + (firstTile.getHeight() / 2))
            val x2 = (secondTile.getX() + (secondTile.getWidth() / 2))
            val y2 = (secondTile.getY() + (secondTile.getHeight() / 2))
            return sqrt((x2 - x1).toDouble().pow(2.0) + (y2 - y1).toDouble().pow(2.0)).toInt()
        }
    }
}
