package com.charlton.gameengine.models

import java.io.Serializable

enum class Direction(var direction: Int): Serializable {
    NONE(0b000),
    LEFT(0b0001),   // 1
    RIGHT(0b0010),  // 2
    UP(0b0100),     // 4
    DOWN(0b1000);   // 8

    fun getDirection(): Direction {
        return this
    }

    companion object {

        fun getDirectionByVelocity(velocity: Velocity): Array<Direction> {
            var direction = 0
            if (velocity.x > 0) {
                direction = direction.or(RIGHT.direction)
            } else if (velocity.x < 0) {
                direction = direction.or(LEFT.direction)
            }
            if (velocity.y > 0) {
                direction = direction.or(DOWN.direction)
            } else if (velocity.y < 0) {
                direction = direction.or(UP.direction)
            }
            return getDirections(direction)
        }

        fun getDirections(value: Int): Array<Direction> {
            if (value == 0) {
                return arrayOf()
            }
            var directions = arrayOf<Direction>()
            for (dt in Direction.entries) {
                //println("Check: $dt, dt: ${dt.direction}, value: $value, value & dt == ${value.and(dt.direction)}")
                if (value.and(dt.direction) == dt.direction) {
                    directions += dt
                }
            }
            return directions
        }
    }
}