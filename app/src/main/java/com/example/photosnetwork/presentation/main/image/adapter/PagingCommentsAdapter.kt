package com.example.photosnetwork.presentation.main.image.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.photosnetwork.databinding.ItemCommentBinding
import com.example.photosnetwork.databinding.ItemImageBinding
import com.example.photosnetwork.domain.model.comment.CommentItem
import com.example.photosnetwork.domain.model.image.ImageItem
import java.text.SimpleDateFormat
import java.util.*

class PagingCommentsAdapter() :
    PagingDataAdapter<CommentItem, PagingCommentsAdapter.PagingCommentsViewHolder>(
        ImagesDiffUtilCallback()) {

    class ImagesDiffUtilCallback : DiffUtil.ItemCallback<CommentItem>() {
        override fun areItemsTheSame(oldItem: CommentItem, newItem: CommentItem): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: CommentItem, newItem: CommentItem): Boolean {
            return oldItem.id == newItem.id && oldItem.date == newItem.date && oldItem.text == newItem.text
        }
    }

    inner class PagingCommentsViewHolder(
        binding: ItemCommentBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        private val tvText = binding.tvCommentText
        private val tvDate = binding.tvCommentDate

        fun bind(item: CommentItem) {
            tvText.text = item.text
            val correctDate =
                SimpleDateFormat("dd.MM.yyyy mm:ss", Locale.ENGLISH).format(Date(item.date * 1000))
            tvDate.text = correctDate
        }
    }

    override fun onBindViewHolder(holder: PagingCommentsViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagingCommentsViewHolder {
        val binding = ItemCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PagingCommentsViewHolder(binding)
    }


}