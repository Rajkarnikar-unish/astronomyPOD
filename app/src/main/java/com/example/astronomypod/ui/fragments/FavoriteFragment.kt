package com.example.astronomypod.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.astronomypod.R
import com.example.astronomypod.adapters.FavoritePicturesAdapter
import com.example.astronomypod.databinding.FragmentFavoriteBinding
import com.example.astronomypod.ui.MainActivity
import com.example.astronomypod.ui.PODViewModel

class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    lateinit var podViewModel: PODViewModel
    lateinit var favPictureAdapter: FavoritePicturesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoriteBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        podViewModel = (activity as MainActivity).podViewModel

        setupRecyclerView()

        podViewModel.getSavedPicture().observe(viewLifecycleOwner, Observer { pictures ->
            favPictureAdapter.differ.submitList(pictures)
        })
    }

    private fun setupRecyclerView() {
        favPictureAdapter = FavoritePicturesAdapter()
        binding.rvFavoritePictures.apply {
            adapter = favPictureAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}