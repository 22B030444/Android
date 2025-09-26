package com.example.practice20

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.practice20.databinding.ItemPostsBinding

class PostAdapter(
    private val onLikeClicked: (Post) -> Unit
) : ListAdapter<Post, PostAdapter.PostViewHolder>(PostDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = ItemPostsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding, onLikeClicked)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class PostViewHolder(
        private val binding: ItemPostsBinding,
        private val onLikeClicked: (Post) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(post: Post) {
            binding.textViewPostContent.text = post.textContent
            binding.imageViewPost.load(post.imageUrl) {
                placeholder(R.drawable.ic_placeholder_image)
                error(R.drawable.ic_error_image)
            }
            updateLikeButton(post)
            binding.textViewLikesCount.text = "${post.likesCount} likes"

            binding.buttonLike.setOnClickListener {
                onLikeClicked(post)
            }
        }

        private fun updateLikeButton(post: Post) {
            if (post.isLiked) {
                binding.buttonLike.setImageResource(R.drawable.ic_like_filled)
                binding.buttonLike.setColorFilter(itemView.context.getColor(R.color.liked_color))
            } else {
                binding.buttonLike.setImageResource(R.drawable.ic_like_outline)
                binding.buttonLike.setColorFilter(itemView.context.getColor(R.color.unliked_color))
            }
        }
    }

    class PostDiffCallback : DiffUtil.ItemCallback<Post>() {
        override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
            return oldItem == newItem
        }
    }
}
