package com.example.assignment4

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://jsonplaceholder.typicode.com/"

    // Создаём логирующий interceptor
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY // Логируем всё: заголовки и тело
    }

    // Создаём OkHttp клиент с interceptor
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor) // Добавляем логирование
        .build()

    // Создаём Retrofit instance
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient) // Подключаем OkHttp с логированием
        .addConverterFactory(GsonConverterFactory.create()) // Конвертер JSON в объекты
        .build()

    // Создаём API service
    val apiService: ApiService = retrofit.create(ApiService::class.java)
}