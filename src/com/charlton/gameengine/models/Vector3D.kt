package com.charlton.gameengine.models

import java.io.Serializable

data class Vector3D<T>(var x: T, var y: T, var z: T): Serializable {
    var old_x: T = x
    var old_y: T = y
    var old_z: T = z
}