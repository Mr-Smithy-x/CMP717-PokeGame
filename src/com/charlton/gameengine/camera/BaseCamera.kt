package com.charlton.gameengine.camera


import com.charlton.gameengine.models.Location
import com.charlton.gameengine.models.Size3D
import com.charlton.gameengine.models.contracts.Rectable
import com.charlton.gameengine.world.scenes.SceneManager

interface ICamera: Rectable {

    fun setOrigin(e: Rectable, manager: SceneManager)
    fun setOrigin(e: Rectable, screenWidth: Int, screenHeight: Int)
    fun setFrame(width: Int, height: Int) {
        this.setWidth(width)
        this.setHeight(height)
    }

}

abstract class BaseCamera protected constructor() : ICamera {

    protected var xOrigin: Int = 0
    protected var yOrigin: Int = 0


    override val location: Location = Location(0f, 0f, 0f)
    override val size = Size3D(0f, 0f, 0f)

    protected var vx: Int = 0
    protected var vy: Int = 0
    protected var ay: Int = 0
    protected var av: Int = 0
    internal var scaling: Int = 1
    var gravity: Int = 1

    override fun setOrigin(e: Rectable, manager: SceneManager) {
        manager.getCurrentMap()?.let { map ->
            // Commented out code would center the pixel onto the screen, but with this dynamic, it works much differently
            this.xOrigin = e.getX() - (getWidth() / 2)
            this.yOrigin = e.getY() - (getHeight() / 2)

            this.location.x = xOrigin.toFloat()
            this.location.y = yOrigin.toFloat()

            xOrigin -= e.getWidth() / 2
            yOrigin -= e.getHeight() / 2
        } ?: run {
            setOrigin(e, getWidth(), getHeight())
        }

    }

    // Bounds onto the screen,
    override fun setOrigin(e: Rectable, screenWidth: Int, screenHeight: Int) {
        xOrigin = e.getX() - (screenWidth / 2) - e.getWidth() / 2
        yOrigin = e.getY() - (screenHeight / 2) - e.getHeight() / 2
        this.location.x = (xOrigin + e.getWidth()).toFloat()
        this.location.y = (yOrigin + e.getHeight()).toFloat()
        setFrame(screenWidth, screenHeight)
    }

    companion object {
        const val DEBUG: Boolean = true
    }
}

