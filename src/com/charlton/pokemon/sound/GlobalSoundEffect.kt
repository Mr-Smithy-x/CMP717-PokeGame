package com.charlton.pokemon.sound


import com.charlton.gameengine.sound.BaseSound
import java.util.*
import javax.sound.sampled.Clip

object GlobalSoundEffect : BaseSound() {

    enum class SOUND(val file: String) {
        SELECTION("choice_selection.wav"),
        CANTGO("error_cant_go.wav"),
        ATTACK_SUPER_EFFECTIVE("attack_super_effective.wav"),
        ATTACK_NORMAL("attack_normal_3.wav");

        fun getSoundFileName(): String {
            return file
        }

    }

    private val clips: EnumMap<SOUND, Clip> = EnumMap<SOUND, Clip>(SOUND::class.java)

    init {
        val values: Array<SOUND> = SOUND.entries.toTypedArray()
        for (value in values) {
            val exists = exists(value.getSoundFileName())
            if (exists) {
                clips[value] = createClip(value.getSoundFileName())
            }
        }
    }

    fun play(sound: SOUND) {
        if (clips.containsKey(sound)) {
            val clip = clips[sound]
            play(clip, false)
        }
    }

    override val assetFolder: String
        get() = SOUND_EFFECTS_FOLDER

    /**
     * Add Additional clips in the event you want to play custom clips
     */
    object Clips {
        val SELECTION: Clip = init(get("choice_selection.wav"))!!
        val CANTGO: Clip = init(get("error_cant_go.wav"))!!
    }


}