package com.example.intervalsapp.data.platform

import android.media.AudioManager
import android.media.ToneGenerator
import com.example.intervalsapp.domain.platform.SoundPlayer
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SoundPlayerImpl @Inject constructor(): SoundPlayer {
    private val tg = ToneGenerator(AudioManager.STREAM_MUSIC, 100)

    override fun singleBeep() {
        tg.startTone(ToneGenerator.TONE_PROP_BEEP, 140)
    }

    override fun doubleBeep() {
        tg.startTone(ToneGenerator.TONE_PROP_ACK, 240)
    }

    override fun release() { tg.release() }
}