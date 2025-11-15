package com.example.practice4

import com.google.gson.annotations.SerializedName

data class SearchResponse(
    val resultCount: Int,
    val results: List<Track>
)

data class Track(
    val trackId: Long,
    val trackName: String,
    val artistName: String,
    @SerializedName("artworkUrl100")
    val artworkUrl: String,
    @SerializedName("previewUrl")
    val previewUrl: String?
)