package com.charlton.pokemon.sound


import com.charlton.gameengine.sound.BaseSound
import com.charlton.gameengine.sound.contracts.AudibleTrack
import java.util.*
import javax.sound.sampled.Clip

object GlobalSoundTrack  : BaseSound() {
    private val clips: EnumMap<Track, Clip?>
    private var _track: Track? = null

    init {
        clips = EnumMap(Track::class.java)
        for (value in Track.entries) {
            if (exists(value.trackChoice)) {
                val clip = createClip(value.trackChoice)
                clips[value] = clip
            }
        }
    }

    fun getTrack(): Track? {
        return _track
    }


    fun setTrack(track: AudibleTrack) {
        if(track.mainTrack != null) {
            setTrack(track.mainTrack!!)
        }
    }

    fun setTrack(track: Track) {
        if (this._track != track) {
            val clip = clips[this._track]
            if (clip?.isActive == true) {
                stop(clip)
            }
            this._track = track
            play()
        }
    }

    fun play() {
        play(clips[_track])
    }

    override val assetFolder: String
        get() = SOUND_TRACK_FOLDER

    fun resume() {
        val clip = clips[_track]
        if (clip?.isActive != true) {
            resume(clip)
        }
    }

    fun pause() {
        val clip = clips[_track]
        if (clip?.isActive == true) {
            pause(clip)
        }
    }

    fun stop() {
        val clip = clips[_track]
        if (clip?.isActive == true) {
            stop(clip)
        }
    }

    fun getClip(): Clip? {
        return clips[_track]
    }

    enum class Track(val trackChoice: String) {
        WILD_BATTLE("battle_wild_pkmn.wav"),
        WILD_VICTORY("victory_wild.wav"),

        TRAINER_BATTLE("battle_trainer.wav"),
        TRAINER_VICTORY("victory_trainer.wav"),

        GYM_VICTORY("victory_gym_leader.wav"),
        TEAM_VICTORY("victory_team_aqua.wav"),

        ROUTE120("route_120.wav"),
        ROUTE119("route_119.wav"),
        DEWFORD_TOWN("dewford_town.wav"),
        HELP_ME("help_me.wav"),
        LITTLE_ROOT("little_root_town.wav")
    }

}