package com.example.practice20

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.practice20.databinding.FragmentPostsBinding

class HomeFragment : Fragment(R.layout.fragment_posts) {

    private var _binding: FragmentPostsBinding? = null
    private val binding get() = _binding!!

    // одна ViewModel на nav_graph (не забудь зависимость navigation-fragment-ktx)
    private val vm: PostViewModel by viewModels()

    private lateinit var adapter: PostAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentPostsBinding.bind(view)

        adapter = PostAdapter(
            onLikeClicked = { post -> vm.onLikeClicked(post) },
            onItemClicked = { post ->
                val action = HomeFragmentDirections.actionHomeFragmentToDetailsFragment(post)
                findNavController().navigate(action)
            }
        )

        binding.recyclerViewPosts.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewPosts.adapter = adapter

        vm.posts.observe(viewLifecycleOwner) { list ->
            adapter.submitList(list)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
