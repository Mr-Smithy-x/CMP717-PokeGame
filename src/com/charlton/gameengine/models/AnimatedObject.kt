package com.charlton.gameengine.models


import com.charlton.gameengine.camera.CameraContract
import com.charlton.gameengine.camera.GlobalCamera2D
import com.charlton.gameengine.contracts.Renderable
import com.charlton.gameengine.models.contracts.Rectable
import com.charlton.spritesheeteditor.models.FileFormat
import java.awt.Graphics
import java.awt.Image
import java.awt.image.BufferedImage
import java.io.File
import java.io.IOException
import java.util.*
import java.util.logging.Logger
import javax.imageio.ImageIO


abstract class AnimatedObject<T, F : FileFormat?> : ImageObject, Renderable, CameraContract, Rectable {
    protected var logger: Logger = Logger.getLogger("GameEngine", null)
    protected var animDict: T
    var direction: Direction = Direction.NONE
    var isActive: Boolean = false
        protected set



    override var currentPose: Pose = Pose.DOWN
        set(currentPose) {
            currentAnimation.isPaused = true
            field = currentPose
            this.direction = field.direction
            currentAnimation.isPaused = false
        }


    protected constructor(format: F, x: Int, y: Int, scaled: Double, delay: Int) : super(0, 0) {
        this.scaled = scaled
        animDict = setupImages(format, initializeSheet(format!!.image), delay)
        onInitAnimations()
        setupBox(x, y)
    }

    protected constructor(spriteSheet: String, x: Int, y: Int, scaled: Double, delay: Int) : super(0, 0) {
        this.scaled = scaled
        animDict = setupImages(initializeSheet(spriteSheet), delay)
        onInitAnimations()
        setupBox(x, y)
    }


    // For initializing anymore animations besides 4 basic ones for the projectiles.
    protected abstract fun onInitAnimations()

    abstract val currentAnimation: Animation

    val spriteDirectory: String
        get() = SPRITE_FOLDER + this.javaClass.simpleName.lowercase(Locale.getDefault())

    abstract val soundEffectFile: String?

    private fun setupBox(x: Int, y: Int) {
        val currentFrame: Image = currentAnimation.getCurrentFrame()

    }


    protected abstract fun setupImages(format: F, image: BufferedImage, delay: Int): T

    protected abstract fun setupImages(initializeSheet: BufferedImage, delay: Int): T


    @Throws(IOException::class)
    protected open fun initializeSheet(spriteSheet: String): BufferedImage {
        return ImageIO.read(File(String.format("%s%s", SHEET_DIRECTORY, spriteSheet)))
    }

    protected fun pluck(image: BufferedImage, column: Int, row: Int, width: Int, height: Int): BufferedImage {
        return image.getSubimage((column * width), row * height, width, height)
    }

    protected fun parseAnimation(
        image: BufferedImage,
        column: Int,
        row: Int,
        width: Int,
        height: Int,
        count: Int,
        delay: Int
    ): Animation {
        val images: Animation = Animation.with(delay)
        for (i in 0..<count) {
            val x = (column * width) + (i * width)
            val y = row * height
            images.addFrame(image.getSubimage(x, y, width, height))
        }
        return images
    }

    // Draws the sprite's current image based on its current state.
    override fun render(g: Graphics) {
        if (canRender(GlobalCamera2D)) {
            val currentFrame: Image = currentFrame ?: return
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
        isActive = false
    }

    open fun move(distance: Float) {
        isActive = true
        when (direction) {
            Direction.UP -> moveUp(distance)
            Direction.DOWN -> moveDown(distance)
            Direction.LEFT -> moveLeft(distance)
            Direction.RIGHT -> moveRight(distance)
            Direction.NONE -> Unit
        }
    }

    override var currentFrame: Image?
        get() = if (isActive) currentAnimation.getCurrentFrame() else currentAnimation.getFirstFrame()
        set(value) {
            this.size.width = getWidth().toFloat()
            this.size.height = getHeight().toFloat()
        }


    companion object {
        const val SHEET_DIRECTORY: String = "assets/sheets/"
        const val SPRITE_FOLDER: String = "assets/sprites/"
    }
}