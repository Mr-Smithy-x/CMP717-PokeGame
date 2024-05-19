package com.charlton

import com.charlton.gameengine.camera.GlobalCamera2D
import com.charlton.gameengine.camera.GlobalCamera3D
import com.charlton.gameengine.world.scenes.SceneType
import com.charlton.pokemon.Global
import com.charlton.pokemon.PokemonContainer


fun main() {
    System.setProperty("sun.java2d.opengl", "true")
    val width = 640
    val height = 360
    Global.game = PokemonContainer.frame(width, height, SceneType.SandScene) as PokemonContainer
    GlobalCamera2D.setWindowDimension(Global.game.width, Global.game.height)
    GlobalCamera3D.setWindowDimension(Global.game.width, Global.game.height)
    Global.game.setDebug(true)
    Global.game.start()
}