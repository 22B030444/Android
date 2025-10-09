package com.example.practice20

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import com.example.practice20.databinding.FragmentDetailsBinding

class DetailsFragment : Fragment(R.layout.fragment_details) {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!
    private val args: DetailsFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentDetailsBinding.bind(view)

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
        val post = args.post
        binding.ivCover.load(post.imageUrl)
        binding.tvTitle.text = post.textContent
        binding.tvLikes.text = "Likes: ${post.likesCount}"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
