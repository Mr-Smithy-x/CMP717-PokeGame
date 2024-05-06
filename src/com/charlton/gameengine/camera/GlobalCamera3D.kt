package com.charlton.gameengine.camera

import com.charlton.gameengine.helpers.Lookup
import com.charlton.gameengine.models.contracts.Rectable
import com.charlton.gameengine.world.scenes.SceneManager
import kotlin.math.abs

object GlobalCamera3D : BaseCamera() {

    private var zOrigin: Float = 0f

    var windowWidth: Int = 0
        private set
    var windowHeight: Int = 0
        private set


    override fun setOrigin(e: Rectable, manager: SceneManager) {
        manager.getCurrentMap()?.let { map ->
            // Commented out code would center the pixel onto the screen, but with this dynamic, it works much differently
            this.xOrigin = e.getX() - (getWidth() / 2)
            this.yOrigin = e.getY() - (getHeight() / 2)
            this.zOrigin = e.getZ() - (getDepth() / 2)

            this.location.x = xOrigin.toFloat()
            this.location.y = yOrigin.toFloat()

            xOrigin -= e.getWidth() / 2
            yOrigin -= e.getHeight() / 2
            zOrigin -= e.getDepth() - 0.5f
        } ?: run {
            setOrigin(e, getWidth(), getHeight())
        }

    }

    // Bounds onto the screen,
    override fun setOrigin(e: Rectable, screenWidth: Int, screenHeight: Int) {
        xOrigin = e.getX() - (screenWidth / 2) - e.getWidth() / 2
        yOrigin = e.getY() - (screenHeight / 2) - e.getHeight() / 2
        zOrigin = abs(e.getZ() - 0.5f)
        this.location.x = (xOrigin + e.getWidth()).toFloat()
        this.location.y = (yOrigin + e.getHeight()).toFloat()
        this.location.z = (zOrigin + e.getDepth()).toFloat()
        setFrame(screenWidth, screenHeight)
    }

    fun setWindowDimension(windowWidth: Int, windowHeight: Int) {
        this.windowWidth = windowWidth
        this.windowHeight = windowHeight
        this.size.width = windowWidth.toFloat()
        this.size.height = windowHeight.toFloat()
    }

    fun getScaling(): Int {
        return scaling
    }

    fun setScaling(scaling: Int) {
        this.scaling = scaling
    }



    var cosA: Float = 1.0f
    var sinA: Float = 0.0f
    var A: Int = 0

    fun moveForward(d: Int) {
        this.location.x += d * cosA
        this.location.y += d * sinA
    }

    fun moveBackward(d: Int) {
        this.location.x -= d * cosA
        this.location.y -= d * sinA
    }


    fun turnLeft(dA: Int) {
        A -= dA
        if (A < 0) A += 360
        cosA = Lookup.cos[A]
        sinA = Lookup.cos[A]
    }

    fun turnRight(dA: Int) {
        A += dA
        if (A > 360) A -= 360
        cosA = Lookup.cos[A]
        sinA = Lookup.cos[A]
    }


    fun setDirection() {
        if (A < 0) A = (A % 360) + 360
        if (A > 359) A = (A % 360)
        cosA = Lookup.cos[A]
        sinA = Lookup.sin[A]
    }

    fun moveLT(dx: Int) {
        this.location.x -= dx.toFloat()
    }

    fun moveRT(dx: Int) {
        this.location.x += dx.toFloat()
    }


    fun moveUP(dy: Int) {
        this.location.y -= dy.toFloat()
    }

    fun moveDN(dy: Int) {
        this.location.y += dy.toFloat()
    }

    fun moveIN(dz: Int) {
        this.location.z += dz.toFloat()
    }

    fun moveOT(dz: Int) {
        this.location.z -= dz.toFloat()
    }
}
