package com.example.practice20
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

class PostViewModel(private val state: SavedStateHandle) : ViewModel() {
    companion object { private const val KEY_POSTS = "posts" }

     private val mockPosts = listOf(
        Post(
            id = 1,
            textContent = "Токаев рассказал, где ещё может появиться АЭС",
            imageUrl = "https://static.tengrinews.kz/userdata/news/2025/news_581741/thumb_b/photo_523760.jpg.webp",
            likesCount = 342
        ),
        Post(
            id = 2,
            textContent = "Упрощёнка в Казахстане: в запретительном списке почти 800 видов бизнеса",
            imageUrl = "https://static.tengrinews.kz/userdata/news/2025/news_581769/thumb_b/photo_523795.jpg.webp",
            likesCount = 128
        ),
        Post(
            id = 3,
            textContent = "Казахстанцев высадили из самолета за помощь женщине с ручной кладью",
            imageUrl = "https://static.tengrinews.kz/userdata/news/2025/news_581720/thumb_b/photo_523735.jpg.webp",
            likesCount = 76
        ),
        Post(
            id = 4,
            textContent = "Город Алатау получил особый статус: указ подписан",
            imageUrl = "https://static.tengrinews.kz/userdata/news/2025/news_581730/thumb_b/photo_523750.jpg.webp",
            likesCount = 204
        ),
        Post(
            id = 5,
            textContent = "Токаев высказался о критике министров и акимов в интернете",
            imageUrl = "https://static.tengrinews.kz/userdata/news/2025/news_581746/thumb_b/photo_523765.png.webp",
            likesCount = 95
        ),
        Post(
            id = 6,
            textContent = "“Президент нажал на кнопку перезагрузки“ - эксперт о политических перестановках в Казахстане",
            imageUrl = "https://static.tengrinews.kz/userdata/news/2025/news_581712/thumb_b/photo_523745.jpg.webp",
            likesCount = 95
        )
    )

    private val _posts = MutableLiveData(state.get<List<Post>>(KEY_POSTS) ?: mockPosts)
    val posts: LiveData<List<Post>> = _posts
    fun onLikeClicked(post: Post) {
        val updated = (_posts.value ?: return).map {
            if (it.id == post.id) {
                val nowLiked = !it.isLiked
                it.copy(
                    isLiked = nowLiked,
                    likesCount = (it.likesCount + if (nowLiked) 1 else -1).coerceAtLeast(0)
                )
            } else it
        }
        _posts.value = updated
        state[KEY_POSTS] = updated
    }

}
