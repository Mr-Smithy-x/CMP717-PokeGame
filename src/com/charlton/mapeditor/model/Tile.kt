package com.charlton.mapeditor.model

import com.charlton.gameengine.camera.CameraContract
import com.charlton.gameengine.camera.GlobalCamera2D
import com.charlton.gameengine.contracts.Renderable
import com.charlton.gameengine.models.Location
import com.charlton.gameengine.models.Size3D
import com.charlton.gameengine.models.Vector3D
import com.charlton.gameengine.models.contracts.CollisionObject
import java.awt.Color
import java.awt.Graphics
import java.awt.image.BufferedImage
import kotlin.math.sqrt

class Tile : Node<Tile>, Comparable<Node<Tile>>, Renderable, CameraContract, CollisionObject {
    private val tileImage: BufferedImage?
    val tileID: Int
    var boundsRect: CollisionObject? = null
        private set
    var isCollisionEnabled: Boolean = false
    var isCollisionOverride: Boolean = false

    constructor(x: Int, y: Int, width: Int, height: Int) {
        this.tileID = 0
        this.tileImage = null
        this.size.width = width.toFloat()
        this.size.height = height.toFloat()
        this.location.x = x.toFloat()
        this.location.y = y.toFloat()
    }

    constructor(image: BufferedImage, tileID: Int) {
        this.tileImage = image
        this.size.width = image.width.toFloat()
        this.size.height = image.height.toFloat()
        this.tileID = tileID
    }

    override fun render(g: Graphics) {
        if (canRender(GlobalCamera2D)) {
            g.drawImage(tileImage, getCameraOffsetX(GlobalCamera2D).toInt(), getCameraOffsetY(GlobalCamera2D).toInt(), null);
            if (System.getProperty("DEBUG").equals("true") && boundsRect != null) drawBoundsRect(g);
        }
    }

    fun drawBoundsRect(g: Graphics) {
        if (isCollisionEnabled) {
            g.color = Color.red
            //boundsRect.render(g);
        }
    }

    override fun getX(): Int {
        return (location.x * size.width).toInt()
    }

    override fun getY(): Int {
        return (location.y * size.height).toInt()
    }

    fun setX(x: Int) {
        this.location.x = x.toFloat()
    }

    fun setY(y: Int) {
        this.location.y = y.toFloat()
    }

    fun initBoundsRect() {
        this.boundsRect = object : CollisionObject {

            override val location: Vector3D<Float>
                get() = this@Tile.location

            override val size: Size3D<Float>
                get() = this@Tile.size
        }
    }

    override fun calculateNearestNodes(network: Network<*>?) {

    }

    override fun discover(dest: Tile): Double = distanceTo(dest)

    override fun distanceTo(dest: Tile): Double {
        val px = dest.location.x - location.x
        val py = dest.location.y - location.y
        return sqrt((px * px + py * py).toDouble())
    }

    override fun compareTo(other: Node<Tile>): Int {
        return 0
    }

    fun setMapLocation(location: Location) {
        this.location.x = location.x
        this.location.y = location.y
        this.location.z = location.z
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false

        val tile = o as Tile
        if (location.x != tile.location.x) return false
        return location.y == tile.location.y
    }

    override fun hashCode(): Int {
        var result = location.x.toInt()
        result = 31 * result + location.y.toInt()
        return result
    }

    override fun toString(): String {
        return "Tile{(${location.x}, ${location.y})} - Tile{(${location.x}, ${location.y})}"
    }

    @Transient
    override val location: Location = Location(0f, 0f, 0f)

    @Transient
    override val size: Size3D<Float> = Size3D(0f, 0f, 0f)

    override var isValid: Boolean
        get() = !isCollisionEnabled
        set(isValid) {
            super.isValid = isValid
        }

}