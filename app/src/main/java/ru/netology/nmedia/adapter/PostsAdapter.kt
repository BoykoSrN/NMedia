package ru.netology.nmedia.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.R
import ru.netology.nmedia.data.Post
import ru.netology.nmedia.databinding.PostListItemBinding

class PostsAdapter(
    private val interactionsListener: PostInteractionListener
) : ListAdapter<Post, ViewHolder>(ViewHolder.DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = PostListItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding, interactionsListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class ViewHolder(
    private val binding: PostListItemBinding,
    listener: PostInteractionListener
) : RecyclerView.ViewHolder(binding.root) {

    private lateinit var post: Post

    private val popupMenu by lazy {
        PopupMenu(itemView.context, binding.menu).apply {
            inflate(R.menu.options_post)
            setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.remove -> {
                        listener.onDeleteClicked(post)
                        true
                    }
                    R.id.edit ->{
                        listener.onEditClicked(post)
                        true
                    }
                    else -> false
                }
            }
        }
    }

    init {
        binding.postShareButton.setOnClickListener { listener.onShareClicked(post) }
        binding.postLikeButton.setOnClickListener { listener.onLikeClicked(post) }
        binding.menu.setOnClickListener { popupMenu.show() }
        binding.postCard.setOnClickListener { listener.onPostClicked(post) }
    }

    fun bind(post: Post) {
        this.post = post
        with(binding) {
            author.text = post.author
            content.text = post.content
            published.text = post.published
            postLikeButton.text = countNumbers(post.likes)
            postShareButton.text = countNumbers(post.shares)
            postLikeButton.setIconResource(getLikeIconResId(post.likedByMe))
        }
    }
    private fun countNumbers(likes: Int): String {
        return when (likes) {
            in 1..999 -> "$likes"
            in 1000..1099 -> "${likes / 1000}K"
            in 1100..9999 -> "${likes / 1000}.${likes / 100 % 10}K"
            in 10000..999999 -> "${likes / 1000}K"
            in 1_000_000..1_099_999 -> "${likes / 1000000}M"
            in 1_000_000..9_999_999 -> "${likes / 1000_000}.${likes / 1000_000 % 10}M"
            in 10_000_000..99_999_999 -> "${likes / 1000000}M"
            else -> ""
        }
    }
    @DrawableRes
    fun getLikeIconResId(liked: Boolean) =
        if (liked) R.drawable.ic_favorite_24dp else R.drawable.ic_favorite_border_24dp

    object DiffCallback : DiffUtil.ItemCallback<Post>() {

        override fun areItemsTheSame(oldItem: Post, newItem: Post) = oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Post, newItem: Post) = oldItem == newItem

    }
}
