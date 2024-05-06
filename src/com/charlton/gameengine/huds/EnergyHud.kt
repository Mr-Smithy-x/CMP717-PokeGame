package com.charlton.gameengine.huds

import java.awt.Color
import java.awt.Graphics

object EnergyHud: HudComponent(20, 40) {
    override fun onRenderHud(hud: Graphics?, parent: Graphics?) {
        hud?.color = Color.ORANGE
        hud?.fillRect(0,0, 200, 20)
        hud?.color = Color.BLACK
        hud?.drawRect(0,0, 200, 20)
    }
}
