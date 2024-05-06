package com.charlton.gameengine.world.scenes

import com.charlton.gameengine.contracts.Renderable
import com.charlton.gameengine.huds.HudManager
import com.charlton.mapeditor.model.TileMap
import com.charlton.pokemon.sound.GlobalSoundTrack
import java.awt.Graphics
import kotlin.collections.HashMap

class SceneManager : Renderable {

    private val scenes: HashMap<SceneType, Scene> = hashMapOf()

    interface Sceneable {
        val manager: SceneManager
    }

    var currentScene: SceneType? = null
        private set

    fun addScene(scene: Scene) {
        scenes[scene.name] = scene
    }

    override fun render(g: Graphics) {
        scenes[currentScene]?.render(g)
        scenes[currentScene]?.setVisible()
        scenes[currentScene]?.setHidden()
        HudManager.render(g)
    }

    fun getScene(sceneName: SceneType): Scene? {
        return scenes[sceneName]
    }

    fun onDebugDraw(g: Graphics) {
        scenes[currentScene]?.onDebugDraw(g)
    }

    fun setCurrentScene(sceneName: SceneType) {
        this.currentScene = sceneName
        getCurrent()?.let { scene: Scene ->
            GlobalSoundTrack.setTrack(scene)
        }
    }

    fun getCurrentMap(): TileMap? {
        return scenes[currentScene]?.map
    }

    fun getCurrent(): Scene? {
        return scenes[currentScene]
    }

}
