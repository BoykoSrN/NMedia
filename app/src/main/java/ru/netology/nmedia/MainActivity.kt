package ru.netology.nmedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.DrawableRes
import ru.netology.nmedia.data.Post
import ru.netology.nmedia.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var post = Post(
            1L,
            "Sergey",
            "Привет, это новая Нетология!",
            "17.06.2022",
            999,
            132
        )

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.render(post)
        binding.postFavoriteButton.setOnClickListener {
            post.likedByMe = !post.likedByMe
            post = if (post.likedByMe) {
                post.copy(likes = post.likes + 1)
            } else {
                post.copy(likes = post.likes - 1)
            }
            binding.render(post)
            binding.postFavoriteButton.setImageResource(getLikeIconResId(post.likedByMe))
        }

        binding.postShareButton.setOnClickListener{
            post = post.copy(shares = post.shares + 1)
            binding.render(post)
        }
    }

    private fun ActivityMainBinding.render(post: Post) {
        author.text = post.author
        content.text = post.content
        published.text = post.published
        postFavoriteNumber.text = countNumbers(post.likes)
        postShareNumber.text = countNumbers(post.shares)
        postFavoriteButton.setImageResource(getLikeIconResId(post.likedByMe))
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