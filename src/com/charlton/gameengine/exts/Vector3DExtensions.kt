package com.charlton.gameengine.exts

import com.charlton.gameengine.models.Vector3D

val Vector3D<Int>.x3d: Int
    get() {
        return x / z
    }

val Vector3D<Int>.y3d: Int
    get() {
        return y / z
    }

val Vector3D<Int>.old_x3d: Int
    get() {
        return old_x / old_z
    }

val Vector3D<Int>.old_y3d: Int
    get() {
        return old_y / old_z
    }

val Vector3D<Double>.x3d: Double
    get() {
        return x / z
    }

val Vector3D<Double>.y3d: Double
    get() {
        return y / z
    }

val Vector3D<Double>.old_x3d: Double
    get() {
        return old_x / old_z
    }

val Vector3D<Double>.old_y3d: Double
    get() {
        return old_y / old_z
    }


val Vector3D<Float>.x3d: Float
    get() {
        return x / z
    }

val Vector3D<Float>.y3d: Float
    get() {
        return y / z
    }


val Vector3D<Float>.old_x3d: Float
    get() {
        return old_x / old_z
    }

val Vector3D<Float>.old_y3d: Float
    get() {
        return old_y / old_z
    }
