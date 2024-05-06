package com.charlton.gameengine.helpers

import kotlin.math.cos
import kotlin.math.sin

object Lookup {
    var cos: FloatArray = FloatArray(360)
    var sin: FloatArray = FloatArray(360)


    init {
        generateCos()
        generateSin()
    }

    private fun generateCos(): DoubleArray {
        val cos = DoubleArray(360)

        for (A in 0..359) {
            cos[A] = cos(A * Math.PI / 180)
        }

        return cos
    }

    private fun generateSin(): DoubleArray {
        val sin = DoubleArray(360)

        for (A in 0..359) {
            sin[A] = sin(A * Math.PI / 180)
        }

        return sin
    }
}
