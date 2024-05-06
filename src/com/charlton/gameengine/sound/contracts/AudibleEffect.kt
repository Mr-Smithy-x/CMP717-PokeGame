package com.charlton.gameengine.sound.contracts

import com.charlton.pokemon.sound.GlobalSoundEffect
import javax.sound.sampled.Clip

interface AudibleEffect {

    fun pauseEffect(clip: Clip? = null) {
        GlobalSoundEffect.pause(clip ?: GlobalSoundEffect.lastClip())
    }

    fun playEffect(clip: Clip? = null, force: Boolean = false) {
        GlobalSoundEffect.play(clip ?: GlobalSoundEffect.lastClip(), force)
    }

}