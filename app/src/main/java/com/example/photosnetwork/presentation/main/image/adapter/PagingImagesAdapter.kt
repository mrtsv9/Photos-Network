package com.example.photosnetwork.presentation.main.image.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.photosnetwork.databinding.ItemImageBinding
import com.example.photosnetwork.domain.model.image.ImageItem
import java.text.SimpleDateFormat
import java.util.*

class PagingImagesAdapter(
    private val clickListener: (ImageItem) -> Unit,
    private val longClickListener: (ImageItem, Int) -> Unit
) : PagingDataAdapter<ImageItem, PagingImagesAdapter.PagingImagesViewHolder>(ImagesDiffUtilCallback()) {

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

    inner class PagingImagesViewHolder(
        private val binding: ItemImageBinding
    ) :
        RecyclerView.ViewHolder(binding.root) {

        private val ivItem = binding.ivItemImage
        private val tvDate = binding.tvItemImageDate

        fun bind(item: ImageItem, position: Int) {
            Glide.with(binding.root).load(item.url).into(ivItem)
            val correctDate =
                SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH).format(Date(item.date!! * 1000))
            tvDate.text = correctDate
            binding.root.setOnClickListener {
                clickListener(item)
            }
            binding.root.setOnLongClickListener {
                longClickListener(item, position)
                true
            }
        }
    }

    override fun onBindViewHolder(holder: PagingImagesViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it, position) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagingImagesViewHolder {
        val binding = ItemImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PagingImagesViewHolder(binding)
    }
}