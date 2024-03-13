package com.fitlife.atfsd.ui.cardio

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.fitlife.atfsd.R
import com.fitlife.atfsd.databinding.FragmentCardioBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class CardioFragment : Fragment() {

    private val binding by lazy { FragmentCardioBinding.inflate(layoutInflater) }
    private val viewModel by viewModels<CardioViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            viewModel.testLD.collect {
                val a = it
                val b = a
            }
        }
    }
}