package com.charlton.gameengine.models

import com.charlton.gameengine.camera.CameraContract
import com.charlton.gameengine.contracts.Renderable
import com.charlton.gameengine.models.contracts.CollisionObject
import com.charlton.gameengine.models.contracts.GravitationalObject
import com.charlton.gameengine.models.contracts.RotationalObject
import java.io.Serializable

abstract class PlayableObject : GravitationalObject, RotationalObject, Renderable, CollisionObject, CameraContract, Serializable {

    override val location: Location = Location(0f, 0f, 3f)
    override val velocity: Velocity = Velocity(0f, 0f, 3f)
    override val size = Size3D(0f, 0f, 3f)

    override var maxSpeed: Float = 0f
    override var maxJump: Float = 0f
    override var gravityRate: Float = 1f / 2

    private var angle: Float = 0f

}
