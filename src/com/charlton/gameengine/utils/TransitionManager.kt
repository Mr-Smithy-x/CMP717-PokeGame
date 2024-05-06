package com.charlton.gameengine.utils

import com.charlton.gameengine.camera.GlobalCamera2D
import com.charlton.gameengine.world.scenes.Scene
import com.charlton.pokemon.sound.GlobalSoundEffect
import com.charlton.pokemon.sound.GlobalSoundTrack
import java.awt.Color
import java.awt.Graphics
import java.awt.Rectangle
import java.awt.Shape
import java.awt.geom.Area
import java.awt.geom.Ellipse2D
import java.awt.geom.Rectangle2D
import java.util.*
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sqrt


object TransitionManager: BaseHud() {

    internal var tile: Scene? = null
    private var playing = true
        private set
    private var max_bounds = 0
    private val min_bounds = 0
    private val transitionTimer: Timer
    private val transitionTask: TimerTask
    var transition: OnTransition? = null
    private val base = Rectangle(0, 0, (GlobalCamera2D.windowWidth / 0.9).toInt(), (GlobalCamera2D.windowHeight / 0.9).toInt())
    private val circle: Ellipse2D = object : Ellipse2D() {
        private val rect = Rectangle(0, 0, 0, 0)

        override fun getX(): kotlin.Double {
            return rect.x.toDouble()
        }

        override fun getY(): kotlin.Double {
            return rect.y.toDouble()
        }

        override fun getWidth(): kotlin.Double {
            return rect.width.toDouble()
        }

        override fun getHeight(): kotlin.Double {
            return rect.height.toDouble()
        }

        override fun isEmpty(): Boolean {
            return rect.width <= 0 && rect.height <= 0
        }

        override fun setFrame(x: kotlin.Double, y: kotlin.Double, w: kotlin.Double, h: kotlin.Double) {
            rect.setFrame(x, y, w, h)
        }

        override fun getBounds2D(): Rectangle2D {
            return rect
        }
    }

    interface OnTransition {
        fun onTransition(tile: Scene?)
    }

    init {
        this.max_bounds = sqrt((base.width * base.width + base.height * base.height).toDouble()).toInt()
        transitionTimer = Timer()
        transitionTask = TransitionTask()
        transitionTimer.scheduleAtFixedRate(transitionTask, 0, 3)
    }

    // chocolate cake
    // ice cream - fudge, pretzel, brownie
    //

    fun setPlaying(playing: Boolean) {
        this.playing = playing
        if (!playing) {
            set(circle.width.toInt() - 1)
            GlobalSoundEffect.play(GlobalSoundEffect.Clips.CANTGO)
            GlobalSoundTrack.pause()
        } else {
            set(circle.width.toInt() + 1)
            GlobalSoundTrack.resume()
        }
    }

    fun setFor(tile: Scene?) {
        if(this.tile != tile) {
            this.tile = tile
            setPlaying(false)
        }
    }

    fun isPlaying(): Boolean {
        return playing
    }

    fun canTrigger(): Boolean {
        return isFinishedTransitioning && !isPlaying()
    }

    fun trigger() {
        if (canTrigger()) {
            if (tile != null) {
                transition?.onTransition(tile)
                //tile = null
                transition = null
            }
            setPlaying(true)
        } else {
            setPlaying(false)
        }
    }

    val isFinishedTransitioning: Boolean
        get() = (circle.width.toInt()) == max_bounds || (circle.width.toInt()) == min_bounds


    private fun calculateRectOutside(r: Shape): Area {
        val outside = Area(Rectangle2D.Double(0.0, 0.0, base.getWidth(), base.getHeight()))
        outside.subtract(Area(r))
        return outside
    }

    fun set(size: Int) {
        circle.setFrame(
            (GlobalCamera2D.windowWidth / 2 - size / 2).toDouble(),
            (GlobalCamera2D.windowHeight / 2 - size / 2).toDouble(),
            size.toDouble(),
            size.toDouble()
        )
    }


    fun shouldTrigger(): Boolean {
        return (circle.width.toInt()) <= min_bounds
    }

    internal class TransitionTask : TimerTask() {
        override fun run() {
            val size = if (playing) {
                min(min(circle.width + 2, max_bounds.toDouble()), max_bounds.toDouble())
            } else {
                max(max(circle.width - 2, min_bounds.toDouble()), min_bounds.toDouble())
            }
            set(size.toInt())
        }
    }



    override fun onRenderHud(hud: Graphics, parent: Graphics) {
        hud.color = Color.BLACK
        hud.fillRect(base.x, base.y, base.width, base.height)
        hud.clip = calculateRectOutside(circle)
    }
}