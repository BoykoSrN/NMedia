package ru.netology.nmedia.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.netology.nmedia.adapter.PostInteractionListener
import ru.netology.nmedia.data.Post
import ru.netology.nmedia.data.impl.InMemoryPostRepository
import ru.netology.nmedia.data.repository.PostRepository
import ru.netology.nmedia.util.SingleLiveEvent

class PostViewModel : ViewModel(), PostInteractionListener {

    private val repository: PostRepository = InMemoryPostRepository()

    val data by repository::data

    val sharePostContent = SingleLiveEvent<String>()
    val navigateToPostContentScreenEvent = SingleLiveEvent<String>()
    val editPostContentScreenEvent = SingleLiveEvent<String>()
    val playVideo = SingleLiveEvent<String>()

    private val currentPost = MutableLiveData<Post?>(null)

    fun onSaveButtonClick(content:String){
        if (content.isBlank()) return

        val post = currentPost.value?.copy(
            content = content
        )?: Post(
            id = PostRepository.NEW_POST_ID,
            author = "Me",
            content = content,
            published = "Today",
        )
        repository.save(post)
        currentPost.value = null
    }

    fun onAddClicked(){
        navigateToPostContentScreenEvent.call()
    }

    // region PostInteractionListener

    override fun onLikeClicked(post: Post) =
        repository.like(post.id)

    override fun onShareClicked(post: Post) {
        repository.share(post.id)
        sharePostContent.value = post.content
    }


    override fun onDeleteClicked(post: Post) =
        repository.delete(post.id)

    override fun onEditClicked(post: Post) {
        currentPost.value = post
        editPostContentScreenEvent.value = post.content
    }

    override fun onPlayVideoClicked(post: Post) {
        val url:String = requireNotNull(post.video){
            "Video url is broken"
        }
        playVideo.value = url
    }

    // endregion PostInteractionListener

}