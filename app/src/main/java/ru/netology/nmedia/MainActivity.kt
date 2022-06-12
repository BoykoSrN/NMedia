package ru.netology.nmedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val likeButton = findViewById<ImageButton>(R.id.postFavoriteButton)
        likeButton.setOnClickListener {
            likeButton.setImageResource(R.drawable.ic_favorite_24dp)
        }
    }

}