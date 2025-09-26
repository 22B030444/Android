package com.example.practice20
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PostViewModel : ViewModel() {

    private val _posts = MutableLiveData<List<Post>>()
    val posts: LiveData<List<Post>> get() = _posts

    init {
        loadMockPosts()
    }

    private fun loadMockPosts() {
        val mockPosts = listOf(
            Post(
                id = 1,
                textContent = "Токаев рассказал, где ещё может появиться АЭС",
                date = "Сегодня, 15:10",
                imageUrl = "https://static.tengrinews.kz/userdata/news/2025/news_581741/thumb_b/photo_523760.jpg.webp",
                likesCount = 342
            ),
            Post(
                id = 2,
                textContent = "Упрощёнка в Казахстане: в запретительном списке почти 800 видов бизнеса",
                date = "Сегодня, 19:16",
                imageUrl = "https://static.tengrinews.kz/userdata/news/2025/news_581769/thumb_b/photo_523795.jpg.webp",
                likesCount = 128
            ),
            Post(
                id = 3,
                textContent = "Казахстанцев высадили из самолета за помощь женщине с ручной кладью",
                date = "Сегодня, 19:01",
                imageUrl = "https://static.tengrinews.kz/userdata/news/2025/news_581720/thumb_b/photo_523735.jpg.webp",
                likesCount = 76,
                isLiked = true
            ),
            Post(
                id = 4,
                textContent = "Город Алатау получил особый статус: указ подписан",
                date = "Сегодня, 17:35",
                imageUrl = "https://static.tengrinews.kz/userdata/news/2025/news_581730/thumb_b/photo_523750.jpg.webp",
                likesCount = 204
            ),
            Post(
                id = 5,
                textContent = "Токаев высказался о критике министров и акимов в интернете",
                date = "Сегодня, 15:36",
                imageUrl = "https://static.tengrinews.kz/userdata/news/2025/news_581746/thumb_b/photo_523765.png.webp",
                likesCount = 95
            ),
            Post(
                id = 6,
                textContent = "“Президент нажал на кнопку перезагрузки“ - эксперт о политических перестановках в Казахстане",
                date = "Сегодня, 15:36",
                imageUrl = "https://static.tengrinews.kz/userdata/news/2025/news_581712/thumb_b/photo_523745.jpg.webp",
                likesCount = 95
            )
        )

        _posts.value = mockPosts
    }

    fun onLikeClicked(post: Post) {
        val currentPosts = _posts.value?.toMutableList() ?: return
        val postIndex = currentPosts.indexOfFirst { it.id == post.id }
        if (postIndex != -1) {
            val updatedPost = currentPosts[postIndex].copy(
                isLiked = !currentPosts[postIndex].isLiked,
                likesCount = if (!currentPosts[postIndex].isLiked) currentPosts[postIndex].likesCount + 1 else currentPosts[postIndex].likesCount - 1
            )
            currentPosts[postIndex] = updatedPost
            _posts.value = currentPosts
        }
    }

}
