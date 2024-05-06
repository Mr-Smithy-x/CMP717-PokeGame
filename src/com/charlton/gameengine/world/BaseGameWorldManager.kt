package com.charlton.gameengine.world

import com.charlton.gameengine.Debuggable
import com.charlton.gameengine.GameContainer
import com.charlton.gameengine.camera.GlobalCamera2D
import com.charlton.gameengine.camera.GlobalCamera3D
import com.charlton.gameengine.contracts.Renderable
import com.charlton.gameengine.huds.EnergyHud
import com.charlton.gameengine.huds.LifeHud
import com.charlton.gameengine.world.scenes.SceneManager
import com.charlton.pokemon.Global
import java.awt.Color
import java.awt.Graphics

abstract class BaseGameWorldManager<T: GameContainer> constructor(protected val container: T): Renderable, Debuggable, SceneManager.Sceneable {

    abstract fun renderGlobalSounds()

    override fun render(g: Graphics) {
        g.color = Color.BLACK
        //g.fillRect(0,0, GlobalCamera.windowWidth, GlobalCamera.windowHeight)
        manager.render(g)
        renderGame(g)
        if(inDebuggingMode()) {
            //TODO: Anything i need to debug on screen i can call debug method
            manager.onDebugDraw(g)
        }
        LifeHud.render(g)
        EnergyHud.render(g)
    }

    open protected fun renderGame(g: Graphics) {

    }

    abstract fun renderPause(g: Graphics)

    abstract fun manual(keys: BooleanArray, typedKey: BooleanArray)
    abstract fun automate()


    fun adjust() {
        GlobalCamera2D.setOrigin(Global.player, manager)
        //GlobalCamera3D.setOrigin(Global.player, manager)
    }


}