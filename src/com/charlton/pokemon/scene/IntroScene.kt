package com.charlton.pokemon.scene

import com.charlton.gameengine.world.scenes.Scene
import com.charlton.gameengine.world.scenes.SceneManager
import com.charlton.gameengine.world.scenes.SceneType
import com.charlton.pokemon.sound.GlobalSoundTrack
import java.awt.Graphics

class IntroScene(sceneable: SceneManager.Sceneable) : Scene(sceneable) {

    override val name = SceneType.Intro

    override fun onDebugDraw(g: Graphics) {

    }

    override fun render(g: Graphics) {
        g.drawLine(0, 100, width, 100)
    }

    override val mainTrack: GlobalSoundTrack.Track
        get() = GlobalSoundTrack.Track.DEWFORD_TOWN

}