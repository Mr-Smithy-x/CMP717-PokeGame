package com.charlton.gameengine.world.scenes

import java.io.Serializable

sealed class SceneType(val id: Int): Serializable {
    data object Intro: SceneType(0)
    data object Default: SceneType(1)
    data object Battle : SceneType(2)
    data class Gym(val badge: String = "01") : SceneType(3)
}