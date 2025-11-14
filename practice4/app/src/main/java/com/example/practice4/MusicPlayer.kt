package com.example.practice4

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.util.Log

class MusicPlayer {
    private var mediaPlayer: MediaPlayer? = null
    private var currentTrack: Track? = null
    var isPlaying = false
        private set

    fun playTrack(track: Track, onCompletion: () -> Unit = {}) {
        try {

            stop()

            if (track.previewUrl.isNullOrEmpty()) {
                return
            }

            currentTrack = track

            mediaPlayer = MediaPlayer().apply {

                setAudioAttributes(
                    AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .build()
                )
                setDataSource(track.previewUrl)

                prepareAsync()

                setOnPreparedListener { mp ->
                    mp.start()
                    this@MusicPlayer.isPlaying = true
                }

                setOnCompletionListener {
                    this@MusicPlayer.isPlaying = false
                    onCompletion()
                }

                setOnErrorListener { _, what, extra ->
                    this@MusicPlayer.isPlaying = false
                    true
                }

                setOnInfoListener { _, what, extra ->
                    false
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    fun pause() {
        mediaPlayer?.pause()
        isPlaying = false
    }

    fun resume() {
        mediaPlayer?.start()
        isPlaying = true
    }

    fun stop() {
        mediaPlayer?.apply {
            if (isPlaying) stop()
            release()
        }
        mediaPlayer = null
        isPlaying = false
        currentTrack = null
    }

    fun getCurrentTrack() = currentTrack
}