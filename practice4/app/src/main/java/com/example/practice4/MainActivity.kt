package com.example.practice4
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var searchEditText: EditText
    private lateinit var searchButton: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var errorText: TextView
    private lateinit var playerLayout: LinearLayout

    private lateinit var albumCover: ImageView
    private lateinit var trackTitle: TextView
    private lateinit var artistName: TextView
    private lateinit var playPauseButton: Button
    private lateinit var prevButton: Button
    private lateinit var nextButton: Button

    private val musicPlayer = MusicPlayer()
    private var trackList: List<Track> = emptyList()
    private var currentIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
        setupListeners()

        searchTracks("Imagine Dragons")
    }

    private fun initViews() {

        searchEditText = findViewById(R.id.searchEditText)
        searchButton = findViewById(R.id.searchButton)
        progressBar = findViewById(R.id.progressBar)
        errorText = findViewById(R.id.errorText)

        playerLayout = findViewById(R.id.playerLayout)
        albumCover = findViewById(R.id.albumCover)
        trackTitle = findViewById(R.id.trackTitle)
        artistName = findViewById(R.id.artistName)
        playPauseButton = findViewById(R.id.playPauseButton)
        prevButton = findViewById(R.id.prevButton)
        nextButton = findViewById(R.id.nextButton)
    }

    private fun setupListeners() {
        searchButton.setOnClickListener {
            val query = searchEditText.text.toString()
            if (query.isNotEmpty()) {
                searchTracks(query)
            }
        }

        playPauseButton.setOnClickListener {
            if (trackList.isNotEmpty()) {
                if (musicPlayer.isPlaying) {
                    musicPlayer.pause()
                    playPauseButton.text = "▶ Play"
                } else {
                    if (musicPlayer.getCurrentTrack() != null) {
                        musicPlayer.resume()
                    } else {
                        playTrack(currentIndex)
                    }
                    playPauseButton.text = "⏸ Pause"
                }
            }
        }

        prevButton.setOnClickListener {
            if (trackList.isNotEmpty()) {
                currentIndex = if (currentIndex > 0) currentIndex - 1 else trackList.size - 1
                playTrack(currentIndex)
            }
        }

        nextButton.setOnClickListener {
            if (trackList.isNotEmpty()) {
                currentIndex = (currentIndex + 1) % trackList.size
                playTrack(currentIndex)
            }
        }
    }

    private fun searchTracks(query: String) {
        lifecycleScope.launch {
            updateUi(UiState.Loading)

            try {

                val response = withContext(Dispatchers.IO) {
                    RetrofitClient.musicApi.searchTracks(query)
                }

                trackList = response.results

                if (trackList.isNotEmpty()) {
                    currentIndex = 0
                    updateUi(UiState.Success(trackList))
                    displayTrack(currentIndex)
                } else {
                    updateUi(UiState.Error("No tracks found"))
                }

            } catch (e: Exception) {
                updateUi(UiState.Error(e.message ?: "Unknown error"))
            }
        }
    }

    private fun playTrack(index: Int) {
        if (index < 0 || index >= trackList.size) return

        val track = trackList[index]
        displayTrack(index)

        musicPlayer.playTrack(track) {

            runOnUiThread {
                playPauseButton.text = "▶ Play"
                nextButton.performClick()
            }
        }

        playPauseButton.text = "⏸ Pause"
    }

    private fun displayTrack(index: Int) {
        val track = trackList[index]

        trackTitle.text = track.trackName
        artistName.text = track.artistName

        Glide.with(this)
            .load(track.artworkUrl)
            .placeholder(R.drawable.ic_launcher_background)
            .into(albumCover)
    }

    private fun updateUi(state: UiState) {
        when (state) {
            is UiState.Loading -> {
                progressBar.visibility = View.VISIBLE
                errorText.visibility = View.GONE
                playerLayout.visibility = View.GONE
            }
            is UiState.Success -> {
                progressBar.visibility = View.GONE
                errorText.visibility = View.GONE
                playerLayout.visibility = View.VISIBLE
            }
            is UiState.Error -> {
                progressBar.visibility = View.GONE
                errorText.visibility = View.VISIBLE
                errorText.text = state.message
                playerLayout.visibility = View.GONE
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        musicPlayer.stop()
    }
}