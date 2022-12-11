package com.example.photosnetwork.presentation.main.image

import android.app.Activity
import android.content.Context.INPUT_METHOD_SERVICE
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.PagingData
import androidx.paging.flatMap
import androidx.paging.insertFooterItem
import androidx.paging.map
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.photosnetwork.R
import com.example.photosnetwork.databinding.FragmentDetailImageBinding
import com.example.photosnetwork.domain.model.comment.CommentItem
import com.example.photosnetwork.domain.model.comment.PostCommentItem
import com.example.photosnetwork.domain.model.image.ImageItem
import com.example.photosnetwork.presentation.base.BaseFragment
import com.example.photosnetwork.presentation.main.MainActivity
import com.example.photosnetwork.presentation.main.image.adapter.LoaderStateAdapter
import com.example.photosnetwork.presentation.main.image.adapter.PagingCommentsAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class DetailImageFragment : BaseFragment<FragmentDetailImageBinding>() {

    override var hostActivity: Activity?
        get() = this.activity
        set(value) {}

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentDetailImageBinding
        get() = FragmentDetailImageBinding::inflate

    private val viewModel by viewModels<ImageDetailsViewModel>()

    private var image: ImageItem? = null

    private val adapter by lazy {
        PagingCommentsAdapter()
    }

    override fun setup() {

        (activity as? MainActivity)?.hideFab()
        val backCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().popBackStack()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(backCallback)

        image = arguments?.getSerializable("SELECTED_IMAGE") as? ImageItem

        Glide.with(binding.root).load(image?.url).into(binding.ivItemImageDetail)
        val correctDate = SimpleDateFormat("dd.MM.yyyy mm:ss",
            Locale.ENGLISH).format(Date(image?.date?.times(1000) ?: Date().time))
        binding.tvImageDetailDate.text = correctDate
        initObservers()
        setDataToAdapter()
    }

    private fun setDataToAdapter() {
        binding.rvComments.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvComments.adapter = adapter.withLoadStateFooter(LoaderStateAdapter())
    }

    private fun initObservers() {
        binding.btnSubmitComment.setOnClickListener {
            val commentText = binding.etAddComment.text
            if (commentText.isNotEmpty()) {
                viewModel.postComment(PostCommentItem(commentText.toString()), image?.id.toString())
                commentText.clear()
                val inputMethodManager =
                    activity?.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)
            }
        }
        lifecycleScope.launch {
            viewModel.postComment.collectLatest {
                if (it.message != null) {
                    toast(it.message)
                } else {
                    toast(resources.getString(R.string.comment_inserted))
                    lifecycleScope.launch {
                        viewModel.getPhotosPagingData(image?.id.toString())
                            .collectLatest { pagingData ->
                                adapter.submitData(pagingData)
                            }
                    }
                }
            }
        }
        lifecycleScope.launch {
            viewModel.getPhotosPagingData(image?.id.toString()).collectLatest {
                adapter.submitData(it)
            }
        }
    }

    override fun onStop() {
        (activity as? MainActivity)?.showFab()
        super.onStop()
    }

}