package com.charlton.gameengine.models

import com.charlton.gameengine.camera.GlobalCamera2D
import java.awt.Color
import java.awt.Graphics
import java.awt.Image
import java.io.Serializable
import java.util.*


abstract class ImageObject(
    width: Int,
    height: Int,
    override var maxSpeed: Float = 10f,
    override var gravityRate: Float = 0.5f,
    override var maxJump: Float = 15f
) : PlayableObject(), Serializable {

    protected open var scaled: Double = 1.0
    @Transient
    protected open var currentFrame: Image? = null
        set(value) {
            field = value
            this.size.width = getWidth().toFloat()
            this.size.height = getHeight().toFloat()
        }

    open var currentPose = Pose.DOWN

    init {
        this.setWidth(width)
        this.setHeight(height)
    }

    override fun getWidth(): Int {
        return currentFrame?.getWidth(null) ?: super.getWidth()
    }

    override fun getHeight(): Int {
        return currentFrame?.getHeight(null) ?: super.getHeight()
    }

    enum class Pose: Serializable {
        UP(Direction.UP), DOWN(Direction.DOWN), LEFT(Direction.LEFT), RIGHT(Direction.RIGHT),
        ATTACK_UP("attack.wav", Direction.UP), ATTACK_DOWN("attack.wav", Direction.DOWN),
        ATTACK_LEFT("attack.wav", Direction.LEFT), ATTACK_RIGHT("attack.wav", Direction.RIGHT),
        SPIN_ATTACK("spin_attack.wav", Direction.NONE), ALL, JUMP, DEAD("dead.wav", Direction.NONE),
        ROLL_LEFT(Direction.LEFT), ROLL_RIGHT(Direction.RIGHT), ROLL_UP(Direction.UP), ROLL_DOWN(Direction.DOWN),
        ATTACK_UP_01(Direction.UP), ATTACK_DOWN_01(Direction.DOWN),
        ATTACK_LEFT_01(Direction.LEFT), ATTACK_RIGHT_01(Direction.RIGHT);

        val soundFileName: String?
        val direction: Direction

        @JvmOverloads
        constructor(direction: Direction = Direction.NONE) {
            this.soundFileName = null
            this.direction = direction
        }

        constructor(soundFileName: String, direction: Direction) {
            this.soundFileName = soundFileName
            this.direction = direction
        }

        fun hasSoundFile(): Boolean {
            return soundFileName != null
        }

        companion object {
            fun parse(pose: String): Pose {
                if (pose == "NONE") {
                    return DOWN
                }
                return valueOf(pose.uppercase(Locale.getDefault()))
            }
        }
    }


    override fun render(g: Graphics) {
        if (canRender(GlobalCamera2D)) {
            val currentFrame: Image = currentFrame?: return

            g.drawImage(
                currentFrame,
                getDrawImageXPosition(currentFrame),
                getDrawImageYPosition(currentFrame),
                (currentFrame.getWidth(null) * scaled).toInt(),
                (currentFrame.getHeight(null) * scaled).toInt(), null
            )
            if (inDebuggingMode()) {
                drawActualImageBounds(currentFrame, g)
                drawBounds(g)
            }
        }
    }

    /**
     * Need this to override
     *
     * @param currentFrame
     * @return
     */
    protected fun getDrawImageXPosition(currentFrame: Image): Int {
        return getCameraOffsetX(GlobalCamera2D).toInt() - currentFrame.getWidth(null) / 4
    }

    /**
     * To Override
     *
     * @param currentFrame
     * @return
     */
    protected fun getDrawImageYPosition(currentFrame: Image): Int {
        return getCameraOffsetY(GlobalCamera2D).toInt() - currentFrame.getHeight(null) / 4
    }

    fun drawActualImageBounds(currentFrame: Image, g: Graphics) {
        // For debug purposes, draw the bounding box of the sprite.
        g.color = Color.RED
        g.drawRect(
            getDrawImageXPosition(currentFrame),
            getDrawImageYPosition(currentFrame),
            (currentFrame.getWidth(null) * scaled).toInt(),
            (currentFrame.getHeight(null) * scaled).toInt()
        )
    }

    fun drawBounds(g: Graphics) {
        // For debug purposes, draw the bounding box of the sprite.
        g.color = Color.blue
        //render(g)

        //g.color = Color.BLUE
        //g.drawRect(getX() - (getWidth() / 2), getY() - (getHeight() / 2), getWidth(), getHeight())
    }
}