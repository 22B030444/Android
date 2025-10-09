package com.example.practice20

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.practice20.databinding.ItemPostsBinding

class PostAdapter(
    private val onLikeClicked: (Post) -> Unit,
    private val onItemClicked: (Post) -> Unit
) : ListAdapter<Post, PostAdapter.VH>(DIFF) {

    companion object {
        val DIFF = object : DiffUtil.ItemCallback<Post>() {
            override fun areItemsTheSame(a: Post, b: Post) = a.id == b.id
            override fun areContentsTheSame(a: Post, b: Post) = a == b
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemPostsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(binding, onLikeClicked, onItemClicked)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(getItem(position))
    }

    inner class VH(
        private val b: ItemPostsBinding,
        private val onLikeClicked: (Post) -> Unit,
        private val onItemClicked: (Post) -> Unit
    ) : RecyclerView.ViewHolder(b.root) {

        fun bind(post: Post) = with(b) {
            textViewPostContent.text = post.textContent
            imageViewPost.load(post.imageUrl)
            buttonLike.setImageResource(
                if (post.isLiked) R.drawable.ic_like_filled else R.drawable.ic_like_outline
            )
            textViewLikesCount.text = post.likesCount.toString()

            b.cardView.setOnClickListener { onItemClicked(post) }
            buttonLike.setOnClickListener { onLikeClicked(post) }
        }
    }
}
