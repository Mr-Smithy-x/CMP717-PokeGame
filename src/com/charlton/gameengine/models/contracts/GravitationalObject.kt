package com.charlton.gameengine.models.contracts

import com.charlton.gameengine.models.Direction
import com.charlton.gameengine.models.contains
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min


interface GravitationalObject : MovableObject {

    val maxJump: Float
    val gravityRate: Float

    override fun update() {
        if (this.velocity.x > maxSpeed) {
            this.velocity.x = maxSpeed
        } else if (velocity.x < -maxSpeed) {
            this.velocity.x = -maxSpeed
        }
    }

    fun updateGravityX() {
        val velocity = if (Direction.LEFT in velocity) {
            this.velocity.x + (gravityRate / 2)
        } else if (Direction.RIGHT in velocity) {
            this.velocity.x - (gravityRate / 2)
        } else {
            this.velocity.x
        }

        if (abs(velocity) < 0.1) {
            setVelocityX(0f)
        } else {
            setVelocityX(velocity)
        }
    }

    fun updateGravityY() {
        if (this.velocity.y > maxJump) {
            this.velocity.y = maxJump
        }
        if (this.velocity.y < -maxJump) {
            this.velocity.y = -maxJump
        }
        setVelocityY(this.velocity.y + gravityRate)
    }

    fun updateGravity() {
        if (velocity.x < 0) {
            setX(getX() + max(velocity.x, -maxSpeed))
        } else if (velocity.x > 0) {
            setX(getX() + min(velocity.x, maxSpeed))
        }

        setY(getY() + min(velocity.y, maxSpeed))
    }
}