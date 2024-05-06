package com.charlton.gameengine.world.scenes

import com.charlton.gameengine.camera.GlobalCamera2D
import com.charlton.gameengine.contracts.Renderable
import com.charlton.gameengine.sound.contracts.AudibleTrack
import com.charlton.mapeditor.model.TileMap
import com.charlton.pokemon.models.ClientEnemyPlayer
import java.awt.Graphics

abstract class Scene(protected val sceneable: SceneManager.Sceneable) : Renderable, AudibleTrack {

    open val map: TileMap? = null
    val width: Int get() = GlobalCamera2D.windowWidth

    val height: Int get() = GlobalCamera2D.windowHeight

    abstract fun onDebugDraw(g: Graphics)

    open val name: SceneType = SceneType.Intro
    open fun manual(keys: BooleanArray, typedKey: BooleanArray) = Unit
    open fun automate() = Unit
    open fun getScene(sceneName: SceneType): Scene? = sceneable.manager.getScene(sceneName)
    open fun setHidden() = Unit
    open fun setVisible() = Unit
}
