package com.example.photosnetwork.presentation.main.image

import android.app.Activity
import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.GridLayoutManager
import com.example.photosnetwork.R
import com.example.photosnetwork.databinding.FragmentPhotosBinding
import com.example.photosnetwork.domain.model.image.ImageItem
import com.example.photosnetwork.presentation.base.BaseFragment
import com.example.photosnetwork.presentation.main.image.adapter.LoaderStateAdapter
import com.example.photosnetwork.presentation.main.image.adapter.PagingImagesAdapter
import com.example.photosnetwork.util.Constants.TAG
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ImageFragment : BaseFragment<FragmentPhotosBinding>() {

    override var hostActivity: Activity?
        get() = this.activity
        set(value) {}

    private val viewModel by viewModels<ImageViewModel>()

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentPhotosBinding
        get() = FragmentPhotosBinding::inflate

    private val adapter by lazy {
        PagingImagesAdapter({
            onItemClicked(it)
        }, { item, position ->
            onItemLongClick(item, position)
        })
    }

    private fun onItemLongClick(item: ImageItem, position: Int) {
        lifecycleScope.launch {
            viewModel.deleteImage.collectLatest {
                if (it.message != null) {
                    toast(it.message)
                } else {
                    lifecycleScope.launch {
                        viewModel.getPhotosPagingData().collectLatest { pagingData ->
                            adapter.submitData(pagingData)
                        }
                    }
                    toast(resources.getString(R.string.photo_deleted))
                }
            }
        }

        AlertDialog.Builder(requireContext())
            .setTitle(resources.getString(R.string.delete_image_question))
            .setPositiveButton(resources.getString(R.string.yes)) { _, _ ->
                viewModel.deletePhoto(item.id!!)
            }
            .setNegativeButton(R.string.cancel) { dialog, _ ->
                dialog.dismiss()
            }.show()
    }

    private fun onItemClicked(item: ImageItem) {
        val bundle = Bundle()
        bundle.putSerializable("SELECTED_IMAGE", item)
        findNavController().navigate(R.id.action_photosFragment_to_detailImageFragment, bundle)
    }

    override fun setup() {

        binding.rvImages.adapter = adapter.withLoadStateFooter(LoaderStateAdapter())
        binding.rvImages.layoutManager = GridLayoutManager(requireContext(), 3)

        lifecycleScope.launch {
            viewModel.getPhotosPagingData().collectLatest {
                adapter.submitData(it)
            }
        }

    }

}