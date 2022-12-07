package com.example.photosnetwork.presentation.base

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.example.photosnetwork.presentation.main.MainActivity
import com.example.photosnetwork.presentation.splash_screen.SplashActivity
import com.google.android.material.snackbar.Snackbar

@Suppress("PropertyName", "UNCHECKED_CAST")
abstract class BaseFragment<VB : ViewBinding> : Fragment() {

    abstract var hostActivity: Activity?
    private var _binding: ViewBinding? = null
    abstract val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> VB

    internal val binding: VB
        get() = _binding as VB

    /**
     * abstract function to pre setup the fragment and the data binding.
     */
    abstract fun setup()

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = bindingInflater.invoke(inflater, container, false)
        return requireNotNull(_binding).root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setup()
    }

    fun snackbar(view: View, text: String, listener: View.OnClickListener? = null) {
        Snackbar
            .make(view, text, Snackbar.LENGTH_LONG)
            .setAction("Action", listener)
            .show()
    }

    fun toast(text: String) {
        Toast.makeText(hostActivity, text, Toast.LENGTH_SHORT).show()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        hostActivity = if (context is MainActivity) context
        else context as SplashActivity
    }

    override fun onDetach() {
        super.onDetach()
        hostActivity = null
        _binding = null
    }
}