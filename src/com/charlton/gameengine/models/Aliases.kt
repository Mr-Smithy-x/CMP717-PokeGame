package com.charlton.gameengine.models


typealias Velocity = Vector3D<Float>
typealias Location = Vector3D<Float>
typealias Point3D = Vector3D<Float>

operator fun Velocity.contains(direction: Direction): Boolean {
    return direction in Direction.getDirectionByVelocity(this)
}