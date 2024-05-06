package com.charlton.gameengine.contracts

import com.charlton.gameengine.Debuggable
import com.charlton.gameengine.camera.GlobalCamera2D
import java.awt.Graphics

interface Renderable : Debuggable {

    val WINDOW_WIDTH get() = GlobalCamera2D.windowWidth
    val WINDOW_HEIGHT get() = GlobalCamera2D.windowHeight

    fun render(g: Graphics)
    fun render3d(g: Graphics) {

    }
}
