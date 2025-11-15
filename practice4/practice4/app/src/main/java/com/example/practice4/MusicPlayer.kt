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
            Log.d("MusicPlayer", "=== STARTING PLAYBACK ===")
            Log.d("MusicPlayer", "Track: ${track.trackName}")
            Log.d("MusicPlayer", "Preview URL: ${track.previewUrl}")

            stop()

            if (track.previewUrl.isNullOrEmpty()) {
                Log.e("MusicPlayer", "❌ No preview URL!")
                return
            }

            currentTrack = track

            mediaPlayer = MediaPlayer().apply {
                // ⭐⭐⭐ КРИТИЧНО! БЕЗ ЭТОГО НЕТ ЗВУКА НА ANDROID 9+ ⭐⭐⭐
                setAudioAttributes(
                    AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .build()
                )

                Log.d("MusicPlayer", "Setting data source...")
                setDataSource(track.previewUrl)

                Log.d("MusicPlayer", "Calling prepareAsync()...")
                prepareAsync()

                setOnPreparedListener { mp ->
                    Log.d("MusicPlayer", "✅ PREPARED! Starting...")
                    mp.start()
                    this@MusicPlayer.isPlaying = true
                    Log.d("MusicPlayer", "🎵 NOW PLAYING: ${track.trackName}")
                    Log.d("MusicPlayer", "Duration: ${mp.duration}ms")
                    Log.d("MusicPlayer", "Volume: L=${mp.getVolume(0)}, R=${mp.getVolume(1)}")
                    Log.d("MusicPlayer", "Is playing: ${mp.isPlaying}")
                }

                setOnCompletionListener {
                    Log.d("MusicPlayer", "⏹ Track completed")
                    this@MusicPlayer.isPlaying = false
                    onCompletion()
                }

                setOnErrorListener { _, what, extra ->
                    Log.e("MusicPlayer", "❌ ERROR: what=$what, extra=$extra")
                    this@MusicPlayer.isPlaying = false
                    true
                }

                setOnInfoListener { _, what, extra ->
                    Log.d("MusicPlayer", "ℹ️ Info: what=$what, extra=$extra")
                    false
                }
            }
        } catch (e: Exception) {
            Log.e("MusicPlayer", "❌ Exception:", e)
            e.printStackTrace()
        }
    }

    // Вспомогательная функция для получения громкости
    private fun MediaPlayer.getVolume(channel: Int): Float {
        return try {
            // Используем reflection для доступа к приватному методу
            val method = MediaPlayer::class.java.getDeclaredMethod("getVolume")
            method.isAccessible = true
            method.invoke(this) as? Float ?: 0f
        } catch (e: Exception) {
            1.0f // Возвращаем 1.0 по умолчанию
        }
    }

    fun pause() {
        try {
            Log.d("MusicPlayer", "⏸ Pausing...")
            mediaPlayer?.pause()
            isPlaying = false
        } catch (e: Exception) {
            Log.e("MusicPlayer", "Pause error", e)
        }
    }

    fun resume() {
        try {
            Log.d("MusicPlayer", "▶ Resuming...")
            mediaPlayer?.start()
            isPlaying = true
        } catch (e: Exception) {
            Log.e("MusicPlayer", "Resume error", e)
        }
    }

    fun stop() {
        try {
            mediaPlayer?.apply {
                if (isPlaying) {
                    Log.d("MusicPlayer", "Stopping...")
                    stop()
                }
                Log.d("MusicPlayer", "Releasing...")
                release()
            }
            mediaPlayer = null
            isPlaying = false
            currentTrack = null
        } catch (e: Exception) {
            Log.e("MusicPlayer", "Stop error", e)
        }
    }

    fun getCurrentTrack() = currentTrack
}