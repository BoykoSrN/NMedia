package ru.netology.nmedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.annotation.DrawableRes
import ru.netology.nmedia.data.Post
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.viewModel.PostViewModel

class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<PostViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.data.observe(this) { post ->
            binding.render(post)
        }

        binding.postLikeButton.setOnClickListener {
            viewModel.onLikeClicked()
        }

        binding.postShareButton.setOnClickListener{
            viewModel.onShareClicked()
        }
    }

    private fun ActivityMainBinding.render(post: Post) {
        author.text = post.author
        content.text = post.content
        published.text = post.published
        postLikeNumber.text = countNumbers(post.likes)
        postShareNumber.text = countNumbers(post.shares)
        postLikeButton.setImageResource(getLikeIconResId(post.likedByMe))
    }

    @DrawableRes
    private fun getLikeIconResId(liked: Boolean) =
        if (liked) R.drawable.ic_favorite_24dp else R.drawable.ic_favorite_border_24dp

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