package com.example.assignment4

import retrofit2.http.GET

interface ApiService {
    // suspend - ключевое слово для функций, которые работают с корутинами
    // @GET - HTTP метод и путь к endpoint
    @GET("posts")
    suspend fun getPosts(): List<Post>
}