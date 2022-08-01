package ru.netology.nmedia.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.netology.nmedia.adapter.PostInteractionListener
import ru.netology.nmedia.data.Post
import ru.netology.nmedia.data.impl.InMemoryPostRepository
import ru.netology.nmedia.data.repository.PostRepository
import ru.netology.nmedia.util.SingleLiveEvent

class PostViewModel:ViewModel(), PostInteractionListener {

    private val repository: PostRepository = InMemoryPostRepository()
    val data by repository::data
    val navigateToPostContentScreenEvent = SingleLiveEvent<String>()
    val navigateToPostScreenEvent = SingleLiveEvent<Long>()
    private val currentPost = MutableLiveData<Post?>(null)

    fun onSaveButtonClicked(content:String){
        if (content.isBlank()) return

        val newPost =currentPost.value?.copy(
            content = content
        )?: Post(
            id = PostRepository.NEW_POST_ID,
            author = "Me",
            content = content,
            published = "Today"
        )
        repository.save(newPost)
        currentPost.value = null
    }

    fun onAddClicked(){
        navigateToPostContentScreenEvent.call()
    }

    override fun onPostClicked(post: Post){
        navigateToPostScreenEvent.value = post.id
    }

    // region PostInteractionsListener

    override fun onShareClicked(post:Post) = repository.share(post.id)
    override fun onLikeClicked(post:Post) = repository.like(post.id)
    override fun onDeleteClicked(post:Post) = repository.delete(post.id)
    override fun onEditClicked(post: Post) {
        currentPost.value = post
        navigateToPostContentScreenEvent.value = post.content}

    // endregion PostInteractionsListener
}