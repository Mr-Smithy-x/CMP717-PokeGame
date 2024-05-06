package com.charlton

import com.charlton.gameengine.models.Direction
import com.charlton.gameengine.models.Velocity
import org.junit.Test

class DirectionTests() {

    @Test
    fun directionLeftDownTest() {
        val directionByVelocity = Direction.getDirectionByVelocity(Velocity(1f, 1f, 0f))

        assert(directionByVelocity.size == 2) {
            println("Direction Array is not of size 2")
        }

        assert(Direction.RIGHT in directionByVelocity) {
            println("Direction should be heading to the right")
        }

        assert(Direction.DOWN in directionByVelocity) {
            println("Direction should be heading down")
        }

    }

    @Test
    fun noDirectionTest() {
        val directionByVelocity = Direction.getDirectionByVelocity(Velocity(0f, 0f, 0f))

        assert(directionByVelocity.isEmpty()) {
            println("Direction Array should be empty as 0,0,0 is no direction")
        }

    }

}