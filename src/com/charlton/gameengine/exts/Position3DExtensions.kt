package com.charlton.gameengine.exts

import com.charlton.gameengine.camera.BaseCamera
import com.charlton.gameengine.camera.GlobalCamera2D
import com.charlton.gameengine.camera.GlobalCamera3D
import com.charlton.gameengine.models.Position3D
import com.charlton.gameengine.models.Size3D
import com.charlton.gameengine.models.Vector3D

fun Vector3D<Int>.to3dI(size: Size3D<Int>): Position3D<Int> {
    val x = size.depth * ((x - size.width / 2) / z)
    val y = size.depth * ((y - size.height) / z)
    return Position3D(x, y, size.width / z, size.height / z)
}

fun Vector3D<Double>.to3dD(size: Size3D<Double>): Position3D<Double> {
    val x = size.depth * ((x - size.width / 2) / z)
    val y = size.depth * ((y - size.height) / z)
    return Position3D(x, y, size.width / z, size.height / z)
}

fun Vector3D<Float>.to3dF(size: Size3D<Float>): Position3D<Float> {
    val x = size.depth * ((x - size.width / 2) / z)
    val y = size.depth * ((y - size.height) / z)
    return Position3D(x, y, size.width / z, size.height / z)
}

//region With Respect to Camera

// d * ((x - Camera3D.x - w/2) / (z - Camera3d.z) + x_origin)
// d * ((y - Camera3D.y - h) / (z - Camera3d.z) + y_origin)

fun Vector3D<Int>.to3dCameraI(size: Size3D<Int>, camera: BaseCamera = GlobalCamera3D): Position3D<Int> {
    val x_origin = camera.location.x
    val y_origin = camera.location.y
    val x: Float = size.depth * ((x - camera.location.x - size.width / 2) / (z - camera.location.z) + x_origin)
    val y: Float = size.depth * ((y - camera.location.y - size.height) / (z - camera.location.z) + y_origin)
    return Position3D(x.toInt(), y.toInt(), size.width / z, size.height / z)
}

fun Vector3D<Double>.to3dCameraD(size: Size3D<Double>, camera: BaseCamera = GlobalCamera3D): Position3D<Double> {
    val x_origin = camera.location.x
    val y_origin = camera.location.y
    val x = size.depth * ((x - camera.location.x - size.width / 2) / (z - camera.location.z) + x_origin)
    val y = size.depth * ((y - camera.location.y - size.height) / (z - camera.location.z) + y_origin)
    return Position3D(x, y, size.width / z, size.height / z)
}

fun Vector3D<Float>.to3dCameraF(size: Size3D<Float>, camera: BaseCamera = GlobalCamera3D): Position3D<Float> {
    val x_origin = 0
    val y_origin = 0
    val x2 = z * ((x - camera.location.x - size.width / 2) / (z - camera.location.z) + x_origin)
    val y2 = z * ((y - camera.location.y - size.height) / (z - camera.location.z) + y_origin)
    if(x2.isNaN() || y2.isNaN() || z <= 0f) return Position3D(x, y, size.width, size.height)
    return Position3D(x2, y2, size.width / z, size.height / z)
}

//endregion