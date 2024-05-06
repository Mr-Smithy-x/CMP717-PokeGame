package com.charlton.gameengine.models

import java.awt.Image
import java.awt.geom.AffineTransform
import java.awt.image.AffineTransformOp
import java.awt.image.BufferedImage
import java.io.File
import java.io.IOException
import java.io.Serializable
import java.util.*
import java.util.logging.Level
import java.util.logging.Logger
import javax.imageio.ImageIO


class Animation private constructor(delay: Int): Serializable {
    private val logger: Logger = Logger.getLogger("GameEngine", null)
    private val timer = Timer()
    private var frames: MutableList<Image> = ArrayList()
    var currentFrameIndex: Int = 0
        private set
    var isPaused: Boolean = false
        internal set

    constructor(delay: Int, prefix: String?, directory: String?) : this(delay) {
        loadFrames(prefix, directory)
    }

    init {
        val task = AnimateTask()
        timer.scheduleAtFixedRate(task, 0, delay.toLong())
    }

    fun loadFrames(prefix: String?, folder: String?) {
        if (isValidDirectory(prefix, folder)) {
            val directory = File(folder)
            val frameFiles = directory.listFiles { file: File ->
                file.name.startsWith(
                    prefix!!
                )
            }
            for (f in frameFiles) {
                this.addFrame(f)
            }
        } else {
            logger.log(Level.INFO, "No images file found for {0}.", prefix)
        }
    }

    fun addFrame(frame: File) = try {
        this.addFrame(ImageIO.read(frame))
        logger.log(Level.INFO, "Loaded image file {0}.", frame.name)
    } catch (e: IOException) {
        logger.log(Level.SEVERE, "Error loading image for animation frame.")
    }


    fun addFrame(image: BufferedImage) {
        frames.add(image)
    }

    fun getCurrentFrame(): Image {
        try {
            return frames[currentFrameIndex]
        } catch (e: IndexOutOfBoundsException) {
            logger.log(Level.SEVERE, "No image files loaded for the current animation!")
            throw NullPointerException("There are not frame to work with")
        }
    }

    fun scale(scale: Int) {
        frames = frames.map { scaledFrames: Image ->
            scaledFrames.getScaledInstance(
                scaledFrames.getWidth(null) * scale,
                scaledFrames.getHeight(null) * scale,
                Image.SCALE_FAST
            )
        }.toMutableList()
    }

    val isLastFrame: Boolean
        get() = currentFrameIndex == frames.size - 1

    val isFirstFrame: Boolean
        get() = currentFrameIndex == 0

    fun getFirstFrame(): Image {
        if (frames.isNotEmpty()) {
            return frames[0]
        }
        throw NullPointerException("Frames are not initialized")
    }

    fun setFirstFrame(image: Image) {
        frames.add(0, image)
    }

    fun holdLastFrame() {
        if (currentFrameIndex == frames.size - 1) {
            currentFrameIndex -= 1
        }
    }

    private fun flipX(image: Image): Image {
        val tx = AffineTransform.getScaleInstance(-1.0, 1.0)
        tx.translate((-image.getWidth(null)).toDouble(), 0.0)
        val op = AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR)
        return op.filter(image as BufferedImage, null)
    }


    private fun transparency(image: Image): Image {
        val tx = AffineTransform.getScaleInstance(-1.0, 1.0)
        tx.translate((-image.getWidth(null)).toDouble(), 0.0)
        val op = AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR)
        return op.filter(image as BufferedImage, null)
    }


    fun flipXFrames() {
        frames = frames.map { image: Image ->
            flipX(image)
        }.toMutableList()
    }

    fun initTransparency() {
        frames = frames.map { image: Image ->
            transparency(image)
        }.toMutableList()
    }

    private inner class AnimateTask : TimerTask(), Serializable {
        override fun run() {
            if (frames.isNotEmpty()) {
                if (!this@Animation.isPaused) {
                    if (this@Animation.currentFrameIndex < frames.size - 1) {
                        this@Animation.currentFrameIndex += 1
                    } else {
                        this@Animation.currentFrameIndex = 0
                    }
                }
            }
        }
    }

    companion object {
        fun with(delay: Int): Animation {
            return Animation(delay)
        }

        fun isValidDirectory(prefix: String?, folder: String?): Boolean {
            val directory = File(folder)
            val frameFiles = directory.listFiles { file: File ->
                file.name.startsWith(
                    prefix!!
                )
            }
            return frameFiles != null && frameFiles.isNotEmpty()
        }
    }
}