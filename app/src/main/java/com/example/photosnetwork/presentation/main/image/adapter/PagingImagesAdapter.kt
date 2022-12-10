package com.example.photosnetwork.presentation.main.image.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.photosnetwork.databinding.ItemImageBinding
import com.example.photosnetwork.domain.model.image.ImageItem
import kotlinx.coroutines.NonDisposableHandle
import kotlinx.coroutines.NonDisposableHandle.parent
import java.text.SimpleDateFormat
import java.util.*

class PagingImagesAdapter :
    PagingDataAdapter<ImageItem, PagingImagesAdapter.PagingImagesViewHolder>(ImagesDiffUtilCallback()) {

    class ImagesDiffUtilCallback : DiffUtil.ItemCallback<ImageItem>() {
        override fun areItemsTheSame(oldItem: ImageItem, newItem: ImageItem): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: ImageItem, newItem: ImageItem): Boolean {
            return oldItem.id == newItem.id && oldItem.url == newItem.url
                    && oldItem.date == newItem.date && oldItem.lat == newItem.lat
                    && oldItem.lng == newItem.lng
        }
    }

    class PagingImagesViewHolder(private val binding: ItemImageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val ivItem = binding.ivItemImage
        private val tvDate = binding.tvItemImageDate

        fun bind(item: ImageItem) {
            Glide.with(binding.root).load(item.url).into(ivItem)
            val correctDate =
                SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH).format(Date(item.date))
            tvDate.text = correctDate
        }
    }

    override fun onBindViewHolder(holder: PagingImagesViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagingImagesViewHolder {
        val binding = ItemImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PagingImagesViewHolder(binding)
    }
}