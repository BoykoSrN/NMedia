package ru.netology.nmedia.data.impl

import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.data.Post
import ru.netology.nmedia.data.repository.PostRepository

class InMemoryPostRepository : PostRepository {

    private var nextId = GENERATED_POST_AMOUNT.toLong()

    private var posts
        get() = checkNotNull(data.value)
        set(value) {
            data.value = value
        }

    override val data = MutableLiveData(
        List(GENERATED_POST_AMOUNT) { index ->
            Post(
                id = index + 1L,
                author = "Нетология",
                content = "Пост №$index",
                published = "30.06.2022",
                likes = 9999
            )
        }
    )

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

    override fun delete(postId: Long) {
        data.value = posts.filter { postId != it.id }

    }

    override fun save(post: Post) {
        if (post.id == PostRepository.NEW_POST_ID) insert(post) else update(post)
    }

    private fun update(post: Post) {
        data.value = posts.map {
            if(it.id == post.id) post else it
        }
    }

    private fun insert(post:Post) {
        data.value = listOf(
            post.copy(
                id = ++nextId
            )
        ) + posts
    }

    private companion object {
        const val GENERATED_POST_AMOUNT = 1000
    }

}