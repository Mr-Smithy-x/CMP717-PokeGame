package com.charlton.pokemon

import com.charlton.gameengine.GameContainer
import com.charlton.gameengine.camera.GlobalCamera2D
import com.charlton.gameengine.exts.make
import com.charlton.gameengine.utils.TransitionManager
import com.charlton.gameengine.world.scenes.SceneType
import com.charlton.pokemon.world.PokemonGameWorldManager
import java.awt.Color
import java.awt.Graphics
import java.awt.event.KeyEvent
import java.awt.event.MouseEvent
import javax.swing.JComponent
import javax.swing.JFrame
import javax.swing.JRootPane


class PokemonContainer(frame: JFrame, panel: JComponent, private val startScene: SceneType) : GameContainer(frame, panel) {

    lateinit var world: PokemonGameWorldManager
        private set

    override fun onInitialize() = Unit

    override fun onPlay() {
        if(TransitionManager.shouldTrigger()){
            TransitionManager.trigger()
        }
        if(TransitionManager.isPlaying() && TransitionManager.isFinishedTransitioning) {
            world.automate()
            world.manual(keys = pressedKey, typedKey=typedKey)
            for(i in typedKey.indices) {
                typedKey[i] = false
            }
        }
        world.adjust()
    }

    override fun onPaint(g: Graphics) {
        if(this::world.isInitialized) {

            g.color = Color.BLACK
            g.fillRect(0,0, GlobalCamera2D.windowWidth, GlobalCamera2D.windowHeight)
            world.render(g)
            TransitionManager.render(g)
        }
    }

    override fun onPausePaint(g: Graphics) {
        super.onPausePaint(g)
        world.renderPause(g)
    }

    override fun mouseClicked(e: MouseEvent?) {
        super.mouseClicked(e)
    }

    override fun keyReleased(e: KeyEvent) {
        super.keyReleased(e)
        if (e.keyCode == KeyEvent.VK_J) {
            //Do Something
        }
        if (e.keyCode == KeyEvent.VK_P) {
            if (paused) {
                //Set Game State
            }
        }
    }

    override fun keyTyped(e: KeyEvent?) = super.keyTyped(e)

    override fun onStart() {
        super.onStart()
        world = PokemonGameWorldManager(this, startScene = startScene)
    }

    companion object {
        fun frame(width: Int, height: Int, startScene: SceneType): GameContainer {
            val frame: JFrame = make("Test Game", width, height)
            return PokemonContainer(frame, frame.components.first() as JRootPane, startScene)
        }
    }

}