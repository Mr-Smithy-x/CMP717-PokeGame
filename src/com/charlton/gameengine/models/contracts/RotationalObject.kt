package com.charlton.gameengine.models.contracts

import com.charlton.gameengine.models.Point3D
import kotlin.math.cos
import kotlin.math.floor
import kotlin.math.sin


interface RotationalObject : MovableObject {

    private fun compute(theta: Float): Array<Float> {
        val sinTheta: Float = sin(theta)
        val cosTheta: Float = cos(theta)
        return arrayOf(sinTheta, cosTheta)
    }

    fun rotateX(theta: Float, points: List<Point3D>): List<Point3D> {
        val (sinTheta, cosTheta) = compute(theta)
        for (item in points) {
            val y = item.y
            val z = item.z
            item.y = floor((y * cosTheta) - (z * sinTheta))
            item.z = floor((z * cosTheta) + (y * sinTheta))
        }
        return points;
    }

    fun rotateY(theta: Float, points: List<Point3D>): List<Point3D> {
        val (sinTheta, cosTheta) = compute(theta)
        for (item in points) {
            val x = item.x
            val z = item.z
            item.y = floor((x * cosTheta) - (z * sinTheta))
            item.z = floor((z * cosTheta) + (x * sinTheta))
        }
        return points;
    }

    fun rotateZ(theta: Float, points: List<Point3D>): List<Point3D> {
        val (sinTheta, cosTheta) = compute(theta)
        for (item in points) {
            val x = item.x
            val y = item.y
            item.x = floor((x * cosTheta) - (y * sinTheta))
            item.y = floor((y * cosTheta) + (x * sinTheta))
        }
        return points;
    }

}