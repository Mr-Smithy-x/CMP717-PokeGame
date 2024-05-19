package com.charlton.gameengine.sound

import java.io.File
import java.io.IOException
import javax.sound.sampled.*
import kotlin.math.log10
import kotlin.math.pow


abstract class BaseSound {
    protected val frame: HashMap<Clip, Int> = HashMap()
    abstract val assetFolder: String?
    private var clip: Clip? = null

    fun createClip(file: String?): Clip? {
        return init(get(file))
    }

    fun getVolume(clip: Clip): Float {
        val gainControl = clip.getControl(FloatControl.Type.MASTER_GAIN) as FloatControl
        return 10f.pow((gainControl.value / 20f))
    }

    fun setVolume(clip: Clip, volume: Float) {
        require(!(volume < 0f || volume > 1f)) { "Volume not valid: $volume" }
        val gainControl = clip.getControl(FloatControl.Type.MASTER_GAIN) as FloatControl
        gainControl.value = 10f * log10(volume.toDouble()).toFloat()
    }

    protected fun get(file: String?): File {
        return File(String.format("%s/%s", assetFolder, file))
    }

    protected fun exists(file: String?): Boolean {
        val get = get(file)
        return get.exists()
    }

    fun play(clip: Clip?, force: Boolean = false) {
        val actualClip = (clip?:this.clip)
        if (actualClip == null) {
            if (force) {
                this.clip = clip
            }
            return
        }
        if (this.clip != clip) {
            this.clip?.stop()
            this.clip = actualClip
        }
        setVolume(actualClip, 0.05f)
        if (!actualClip.isActive || force) {
            actualClip.framePosition = 0
            actualClip.setLoopPoints(0, -1)
            actualClip.start()
        }
    }

    fun stop(clip: Clip?) {
        val actualClip = clip ?: this.clip
        if (actualClip?.isRunning == true) actualClip.stop()
    }

    fun close(clip: Clip) {
        stop(clip)
        clip.close()
    }

    fun pause(clip: Clip?) {
        val actualClip = clip ?: this.clip
        if (actualClip?.isRunning == true) {
            frame.replace(actualClip, actualClip.framePosition)
            actualClip.stop()
        }
    }

    fun resume(clip: Clip?) {
        if ((clip ?: this.clip)?.isActive == false) {
            if (frame.containsKey(clip?:this.clip)) {
                val integer = frame[clip]
                clip?.framePosition = integer!!
            }
            if(true) return
            clip?.start()
        }
    }

    open fun lastClip(): Clip? {
        return this.clip
    }

    companion object {
        const val SOUND_TRACK_FOLDER: String = "assets/sound/tracks"
        const val SOUND_EFFECTS_FOLDER: String = "assets/sound/effects"

        fun init(file: File?): Clip? {
            try {
                val stream = AudioSystem.getAudioInputStream(file)
                val format = stream.format
                // specify what kind of line you want to create
                val info = DataLine.Info(Clip::class.java, format)

                // create the line
                val clip = AudioSystem.getLine(info) as Clip
                // Load the samples from the stream
                clip.open(stream)
                return clip
            } catch (e: UnsupportedAudioFileException) {
                e.printStackTrace()
            } catch (e: LineUnavailableException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return null
        }
    }
}