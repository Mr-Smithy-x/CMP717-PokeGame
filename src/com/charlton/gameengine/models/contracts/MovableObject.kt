package com.charlton.gameengine.models.contracts

import com.charlton.gameengine.models.Velocity
import kotlin.math.max
import kotlin.math.min

interface MovableObject : Rectable {

    val maxSpeed: Float
    val velocity: Velocity

    fun accelerationX(accel: Float) {
        setVelocityX(velocity.x + accel)
    }

    fun accelerationY(accel: Float) {
        setVelocityY(velocity.y + accel)
    }

    fun setVelocityY(velocity: Float) {
        this.velocity.y = velocity
    }

    fun setVelocityX(velocity: Float) {
        this.velocity.x = velocity
    }

    fun moveBy(x: Float, y: Float) {
        this.location.old_x = getX().toFloat()
        this.location.old_y = getY().toFloat()
        setX(x + getX())
        setY(y + getY())
    }

    fun moveLeft(x: Float) = moveBy(-x, 0f)

    fun moveRight(x: Float) = moveBy(x, 0f)

    fun moveUp(y: Float) = moveBy(0f, -y)

    fun moveDown(y: Float) = moveBy(0f, y)


    fun moveLeft3D(x: Float) {
        val x1 = -(x / location.z)
        moveBy(if (x1 > -1f) -1f else x1, 0f)
    }

    fun moveRight3D(x: Float) {
        val x1 = x / location.z
        moveBy(if (x1 < 1f) 1f else x1, 0f)
    }

    fun moveUp3D(y: Float) {
        val y1 = -y / location.z
        moveBy(0f, if (y1 > -1f) -1f else y1)
    }

    fun moveDown3D(y: Float) {
        val y1 = y / location.z
        moveBy(0f, if (y1 < 1f) 1f else y1)
    }


    fun update() {
        if (this.velocity.x > maxSpeed) {
            this.velocity.x = maxSpeed
        } else if (velocity.x < -maxSpeed) {
            this.velocity.x = -maxSpeed
        }

        if (velocity.x < 0) {
            setX(getX() + max(velocity.x, -maxSpeed))
        } else if (velocity.x > 0) {
            setX(getX() + min(velocity.x, maxSpeed))
        }
    }
}