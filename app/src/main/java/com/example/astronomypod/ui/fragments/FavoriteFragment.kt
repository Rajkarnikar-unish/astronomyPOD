package com.example.astronomypod.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
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

        favPictureAdapter.setOnItemClickListener {
            Log.e(TAG, "UNISHRAJKARNIKAR<<<<<<<<<<<<<<<<<<<<<<<")
            System.out.println(">>>>>>>>>>>>>SUIIIIIIIIIIIII<<<<<<<<<<<<<<<<<")
//            val bundle = Bundle().apply {
//                putSerializable("picture", it)
//            }
//            findNavController().navigate(
//                R.id.action_favoriteFragment_to_homeFragment,
//                bundle
//            )
        }

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