package com.charlton.gameengine.exts

import com.charlton.gameengine.models.Size3D


val Size3D<Int>.width3d: Int
    get() {
        return width / depth
    }

val Size3D<Int>.height3d: Int
    get() {
        return height / depth
    }

val Size3D<Double>.width3d: Double
    get() {
        return width / depth
    }

val Size3D<Double>.height3d: Double
    get() {
        return height / depth
    }

val Size3D<Float>.width3d: Float
    get() {
        return width / depth
    }

val Size3D<Float>.height3d: Float
    get() {
        return height / depth
    }
