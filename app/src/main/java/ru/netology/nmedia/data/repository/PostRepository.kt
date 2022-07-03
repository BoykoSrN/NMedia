package ru.netology.nmedia.data.repository

import androidx.lifecycle.LiveData
import ru.netology.nmedia.data.Post

interface PostRepository {

    val data: LiveData<List<Post>>
    fun like(postId: Long)
    fun share(postId: Long)
}