package com.example.practice4

import retrofit2.http.GET
import retrofit2.http.Query

interface MusicApiService {
    @GET("search")
    suspend fun searchTracks(
        @Query("term") searchTerm: String,
        @Query("media") media: String = "music",
        @Query("limit") limit: Int = 30
    ): SearchResponse
}