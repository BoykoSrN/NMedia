package ru.netology.nmedia.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ru.netology.nmedia.adapter.ViewHolder
import ru.netology.nmedia.databinding.PostFragmentBinding
import ru.netology.nmedia.viewModel.PostViewModel

class PostFragment : Fragment() {

    private val viewModel by viewModels<PostViewModel>(ownerProducer = ::requireParentFragment)
    private val args by navArgs<PostFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = PostFragmentBinding.inflate(
        layoutInflater, container, false
    ).also { binding->
        val viewHolder = ViewHolder(binding.postCard, viewModel)
        viewModel.data.observe(viewLifecycleOwner) { posts ->
            val post = posts.find { it.id == args.postId } ?: run {
                findNavController().navigateUp() // the post was deleted, close the fragment
                return@observe
            }
            viewHolder.bind(post)
        }

        setFragmentResultListener(
            requestKey = PostContentFragment.REQUEST_KEY
        ) { requestKey, bundle ->
            if (requestKey != PostContentFragment.REQUEST_KEY) return@setFragmentResultListener
            val newPostContent = bundle.getString(
                PostContentFragment.RESULT_KEY
            ) ?: return@setFragmentResultListener
            viewModel.onSaveButtonClicked(newPostContent)
        }

        viewModel.navigateToPostContentScreenEvent.observe(viewLifecycleOwner) { initialContent ->
            val direction = PostFragmentDirections.fromPostToPostContentFragment(initialContent)
            findNavController().navigate(direction)
        }
    }.root

}