package com.charlton.gameengine.sound.contracts

import com.charlton.pokemon.sound.GlobalSoundTrack
import javax.sound.sampled.Clip

interface AudibleTrack {

    val mainTrack: GlobalSoundTrack.Track?

    fun pauseTrack(clip: Clip? = null) {
        GlobalSoundTrack.pause(clip ?: GlobalSoundTrack.lastClip())
    }

    fun playTrack(clip: Clip? = null, force: Boolean = false) {
        GlobalSoundTrack.play(clip ?: GlobalSoundTrack.lastClip(), force)
    }

}