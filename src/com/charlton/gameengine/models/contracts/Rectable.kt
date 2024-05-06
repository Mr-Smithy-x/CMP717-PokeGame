package com.charlton.gameengine.models.contracts

import com.charlton.gameengine.models.Location
import com.charlton.gameengine.models.Size3D
import com.charlton.gameengine.world.scenes.SceneManager

interface Rectable {

    val location: Location
    val size: Size3D<Float>

    val top
        get() = getY()

    val left
        get() = getX()

    val right
        get() = getX() + getWidth()

    val bottom
        get() = getY() + getHeight()

    fun setX(x: Float) {
        this.location.x = x
    }

    fun setY(y: Float) {
        this.location.y = y
    }

    fun setZ(z: Float) {
        this.location.z = z
    }

    fun getX(): Int {
        return this.location.x.toInt()
    }

    fun getY(): Int {
        return this.location.y.toInt()
    }

    fun getZ(): Float {
        return this.location.z
    }

    fun setDepth(depth: Float) {
        this.size.depth = depth
    }

    fun getDepth(): Float {
        return this.size.depth
    }


    fun getOldY(): Int {
        return this.location.old_y.toInt()
    }

    fun getOldX(): Int {
        return this.location.old_x.toInt()
    }

    fun getWidth(): Int {
        return this.size.width.toInt()
    }

    fun getHeight(): Int {
        return this.size.height.toInt()
    }

    fun setWidth(width: Int) {
        this.size.width = width.toFloat()
    }

    fun setHeight(height: Int) {
        this.size.height = height.toFloat()
    }

    fun getDiagonalX(): Int {
        return right - 1;
    }

    fun getDiagonalY(): Int {
        return bottom - 1
    }

    fun overlaps(rectable: Rectable): Boolean {
        val case_one = this.right >= rectable.left
        val case_two = rectable.right >= this.left
        val case_three = this.bottom >= rectable.top
        val case_four = rectable.bottom >= this.top
        return case_one && case_two && case_three && case_four
    }


    fun contains(mx: Int, my: Int): Boolean {
        val case_one = mx >= getX()
        val case_two = mx <= getX() + getWidth()
        val case_three = my >= getY()
        val case_four = my <= getY() + getWidth()
        return case_one && case_two && case_three && case_four
    }


}