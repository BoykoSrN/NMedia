package ru.netology.nmedia.data.impl

import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.data.Post
import ru.netology.nmedia.data.repository.PostRepository

class InMemoryPostRepository : PostRepository {

    override val data = MutableLiveData(Post(
        1L,
        "Sergey",
        "Привет, это новая Нетология!",
        "17.06.2022",
        999,
        132
    ))

    override fun like() {
        val currentPost = checkNotNull(data.value) {
            "Data value should not be null"
        }

        val likedPost = currentPost.copy(
            likedByMe = !currentPost.likedByMe
        )

        data.value = if (likedPost.likedByMe) {
            likedPost.copy(likes = likedPost.likes + 1)
        } else {
            likedPost.copy(likes = likedPost.likes - 1)
        }
    }

    override fun share() {
        val currentPost = checkNotNull(data.value) {
            "Data value should not be null"
        }

        val sharedPost = currentPost.copy(
            shares = currentPost.shares + 1
        )

        data.value = sharedPost
    }
}