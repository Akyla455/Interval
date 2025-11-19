package com.example.intervalsapp.domain.platform

interface SoundPlayer {
    fun singleBeep()
    fun doubleBeep()
    fun release()
}