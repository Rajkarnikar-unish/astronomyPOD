package com.example.astronomypod.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.astronomypod.R
import com.example.astronomypod.databinding.FavoritePicturePreviewBinding
import com.example.astronomypod.models.AstronomyPOD
import java.io.FileNotFoundException

class FavoritePicturesAdapter: RecyclerView.Adapter<FavoritePicturesAdapter.FavoritePictureViewHolder>() {

    inner class FavoritePictureViewHolder(private var itemViewBinding: FavoritePicturePreviewBinding) : RecyclerView.ViewHolder(itemViewBinding.root) {

        fun bind(astronomyPOD: AstronomyPOD) {
            itemViewBinding.apply {
                titleTextview.text = astronomyPOD.title
                try {
                    Glide
                        .with(itemView)
                        .load(astronomyPOD.url.toString())
                        .error(R.drawable.barnard)
                        .into(thumbnailImage)
                } catch (e: FileNotFoundException) {
                    throw e
                }
            }
        }
    }

    private val differCallback = object : DiffUtil.ItemCallback<AstronomyPOD>() {
        override fun areItemsTheSame(oldItem: AstronomyPOD, newItem: AstronomyPOD): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: AstronomyPOD, newItem: AstronomyPOD): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritePictureViewHolder {
        return FavoritePictureViewHolder(
            FavoritePicturePreviewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            )
        )
    }

    override fun onBindViewHolder(holder: FavoritePictureViewHolder, position: Int) {
        val picture = differ.currentList[position]
        holder.bind(picture)
        holder.itemView.apply {
            setOnClickListener {
                onItemClickListener?.let { it(picture) }
            }
        }
    }

    private var onItemClickListener: ((AstronomyPOD) -> Unit)? = null

    fun setOnItemClickListener(listener: (AstronomyPOD) -> Unit) {
        onItemClickListener = listener
    }

    override fun getItemCount(): Int = differ.currentList.size
}