package ru.netology.nmedia.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.R
import ru.netology.nmedia.data.Post
import ru.netology.nmedia.databinding.PostListItemBinding

internal class PostsAdapter(
    private val interactionListener: PostInteractionListener
) : ListAdapter<Post, PostsAdapter.ViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = PostListItemBinding.inflate(
            inflater, parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(
        private val binding: PostListItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var post: Post

        private val popupMenu by lazy {
            PopupMenu(itemView.context, binding.menu).apply {
                inflate(R.menu.options_post)
                setOnMenuItemClickListener { menuItem ->
                    when(menuItem.itemId) {
                        R.id.remove -> {
                            interactionListener.onDeleteClicked(post)
                            true
                        }
                        R.id.edit -> {
                            interactionListener.onEditClicked(post)
                            true
                        }

                        else -> false
                    }

                }
            }
        }

        init {
            binding.postLikeButton.setOnClickListener { interactionListener.onLikeClicked(post) }
            binding.postShareButton.setOnClickListener { interactionListener.onShareClicked(post) }
            binding.menu.setOnClickListener { popupMenu.show() }
            binding.videoBanner.setOnClickListener{
                interactionListener.onPlayVideoClicked(post)
            }
        }

        fun bind(post: Post) {
            this.post = post
            with(binding) {
                author.text = post.author
                content.text = post.content
                published.text = post.published
                postShareButton.text = countNumbers(post.shares)
                postLikeButton.text = countNumbers(post.likes)
                postLikeButton.isChecked = post.likedByMe
                videoGroup.isVisible = post.video != null
            }

        }

        private fun countNumbers(number: Int): String {
            return when(number){
                in 1..999 -> "$number"
                in 1000..1099 -> "${number/1000}K"
                in 1100..9999 -> "${number/1000}.${number/100%10}K"
                in 10000..999999 -> "${number/1000}K"
                in 1_000_000..1_099_999 -> "${number/1000000}M"
                in 1_000_000..9_999_999 -> "${number/1000_000}.${number/1000_000%10}M"
                in 10_000_000..99_999_999 -> "${number/1000000}M"
                else -> ""
            }
        }

    }

    private object DiffCallback : DiffUtil.ItemCallback<Post>() {

        override fun areItemsTheSame(oldItem: Post, newItem: Post) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Post, newItem: Post) =
            oldItem == newItem
    }

}
