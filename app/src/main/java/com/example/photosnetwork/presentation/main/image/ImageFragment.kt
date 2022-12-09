package com.example.photosnetwork.presentation.main.image

import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.photosnetwork.databinding.FragmentPhotosBinding
import com.example.photosnetwork.presentation.base.BaseFragment
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

    private val adapter = PagingImagesAdapter()

    override fun setup() {
//        viewModel.postImage("n4cDD1ZhvH8ZyqdfFonUluT6r18fDKabufk4KzXs8GteK7WeIEtSy41Jx8eSP1w4",
//            PostImageDto("", Date().time, 53.889987, 27.537930))

        binding.rvImages.adapter = adapter
        binding.rvImages.layoutManager = GridLayoutManager(requireContext(), 2)

        lifecycleScope.launch {
            viewModel.getPhotosPagingData().collectLatest {
                adapter.submitData(it)
            }
        }

//        lifecycleScope.launch {
//            viewModel.photos.collectLatest {
//                Log.d(TAG, "setup: ${it.toString()}")
//            }
//        }
//        viewModel.getAllPhotos("n4cDD1ZhvH8ZyqdfFonUluT6r18fDKabufk4KzXs8GteK7WeIEtSy41Jx8eSP1w4",
//            0)
    }


}