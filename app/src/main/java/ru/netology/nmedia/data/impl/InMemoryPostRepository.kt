package ru.netology.nmedia.data.impl

import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.data.Post
import ru.netology.nmedia.data.repository.PostRepository

class InMemoryPostRepository : PostRepository {

    private var posts
        get() = checkNotNull(data.value)
        set(value) {
            data.value = value
        }

    override val data : MutableLiveData<List<Post>>

    init {
        val initialPosts = List(1000) { index ->
            Post(
                id = index + 1L,
                author = "Sergey ${index + 1L}",
                content = "Пост №$index",
                published = "29.06.2022",
                likes = 111
            )
        }

        data = MutableLiveData(initialPosts)
    }

    override fun like(postId: Long) {
        data.value = posts.map {
            if (it.id != postId) it
            else it.copy(likedByMe = !it.likedByMe)
        }
        data.value = posts.map {
            if (it.id == postId) {
                if (it.likedByMe) it.copy(likes = it.likes + 1)
                else it.copy(likes = it.likes - 1)
            } else it
        }
    }

    override fun share(postId: Long) {
        data.value = posts.map {
            if (it.id == postId) it.copy(shares = it.shares + 1)
            else it
        }
    }

}