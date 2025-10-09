package com.example.practice20

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
@Parcelize
data class Post(
    val id: Int,
    val textContent: String,
    val imageUrl: String,
    var isLiked: Boolean = false,
    var likesCount: Int = 0
): Parcelable
