package ru.netology.nmedia.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContract
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.databinding.PostContentActivityBinding

class PostContentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = PostContentActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.editText.requestFocus()
        val postContent = intent.getStringExtra(POST_CONTENT)
        binding.editText.setText(postContent)

        binding.ok.setOnClickListener {
            val intent = Intent()
            val text = binding.editText.text
            if (text.isNullOrBlank()) {
                setResult(Activity.RESULT_CANCELED, intent)
            } else {
                val content = text.toString()
                intent.putExtra(RESULT_KEY, content)
                setResult(Activity.RESULT_OK, intent)
            }
            finish()
        }
    }

    object ResultContract : ActivityResultContract<String, String?>() {
        override fun createIntent(context: Context, input: String): Intent =
            Intent(context, PostContentActivity::class.java).apply { putExtra(POST_CONTENT,input) }

        override fun parseResult(resultCode: Int, intent: Intent?) =
            if (resultCode == Activity.RESULT_OK) {
                intent?.getStringExtra(RESULT_KEY)
            } else null
    }

    companion object {
        private const val RESULT_KEY = "postNewContent"
        private const val POST_CONTENT = "postContent"
    }
}