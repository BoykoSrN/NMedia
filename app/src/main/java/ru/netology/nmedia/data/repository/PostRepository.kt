package ru.netology.nmedia.data.repository

import androidx.lifecycle.LiveData
import ru.netology.nmedia.data.Post

interface PostRepository {

    val data: LiveData<Post>
    fun like()
    fun share()
}