package com.charlton.gameengine.models

import com.charlton.gameengine.camera.GlobalCamera2D
import com.charlton.gameengine.models.ImageObject.Pose
import com.charlton.spritesheeteditor.models.PoseFileFormat
import java.awt.image.BufferedImage
import java.io.Serializable
import java.util.*
import java.util.logging.Level
import java.util.stream.Collectors


abstract class AnimatedSprite(format: PoseFileFormat, x: Int, y: Int, scaled: Double, delay: Int) :
    AnimatedObject<EnumMap<Pose, Animation>, PoseFileFormat>(format, x, y, scaled, delay), Serializable {

    init {
        check();
    }


    /**
     * validating that you have the basic animations, up down left right
     */
    private fun check() {
        val poses: MutableList<Pose> = mutableListOf(Pose.UP, Pose.DOWN, Pose.LEFT, Pose.RIGHT)
        poses.removeIf { p: Pose -> animDict.keys.contains(p) }
        if (poses.isNotEmpty()) {
            val format =
                String.format("The following poses are missing: %s", poses.stream().map { pose: Pose -> pose.name }
                    .collect(Collectors.joining(",")))
            logger.log(Level.SEVERE, format)
            return
        }
        logger.log(Level.FINE, "All basic poses were initialized!")
    }

    override val currentAnimation: Animation
        get() = animDict[Pose.parse(currentPose.direction.name)]!!

    override val soundEffectFile: String?
        get() = currentPose.soundFileName;

    override fun setupImages(initializeSheet: BufferedImage, delay: Int): EnumMap<Pose, Animation> =
        EnumMap(Pose::class.java)

    override fun setupImages(format: PoseFileFormat, image: BufferedImage, delay: Int): EnumMap<Pose, Animation> {
        val map = EnumMap<Pose, Animation>(Pose::class.java)
        for ((pose, set) in format.poses) {
            val animation = Animation.with(delay)
            for ((x, y, w, h) in set) {
                animation.addFrame(image.getSubimage(x, y, w, h))
            }
            map[Pose.parse(pose)] = animation
        }
        return map
    }


    open fun setSpritePoseAndMove(pose: Pose, x: Float = 5f) {
        currentPose = pose
        move(x * GlobalCamera2D.getScaling())
    }

}