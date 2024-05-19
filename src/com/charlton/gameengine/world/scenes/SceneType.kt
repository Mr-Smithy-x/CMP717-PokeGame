package com.charlton.gameengine.world.scenes

import java.io.Serializable

sealed class SceneType(val id: Int): Serializable {
    data object Intro: SceneType(0)
    data object SandScene: SceneType(1)
    data object SandSceneTwo: SceneType(2)
    data object Battle : SceneType(3)
    data class Gym(val badge: String = "01") : SceneType(4)
}