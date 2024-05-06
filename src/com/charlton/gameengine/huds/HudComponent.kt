package com.charlton.gameengine.huds

import com.charlton.gameengine.contracts.Renderable
import java.awt.Graphics

abstract class HudComponent(val startX: Int, val startY: Int) : Renderable {

    private var hud: HudGraphics? = null
    var visible = true

    private fun hide() {
        hud?.setClip(0, 0, 0, 0)
    }

    private fun visible() {
        hud?.setClip(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT)
    }

    override fun render(g: Graphics) {
        if (hud == null) {
            hud = HudGraphics(startX, startY, g.create())
        }
        if(visible) {
            visible()
        } else {
            hide()
        }
        onRenderHud(hud, g)
    }

    /**
     * Parameter Hud is where you draw your actual graphics for the hud
     * This is intended for being able to utilize the clip functionality without
     * having to affect the main graphics for the game. using clip on parent
     * prevents parts of the screen from being render, so only clip onto the hud
     * otherwise, you can actually utilize the render function and keep this function empty
     *
     * @param hud    Hud in which you're able to draw on
     * @param parent the parent graphics in the event you need to draw onto the actual graphic
     */
    protected abstract fun onRenderHud(hud: Graphics?, parent: Graphics?)

}

