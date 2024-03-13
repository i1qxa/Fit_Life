package com.fitlife.atfsd.ui.home_screen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.fitlife.atfsd.R
import com.fitlife.atfsd.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private val binding by lazy { FragmentHomeBinding.inflate(layoutInflater) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.cardCardio.setOnClickListener {
            launchCardioFragment()
        }
    }

    private fun launchCardioFragment(){
        findNavController().navigate(R.id.action_homeFragment_to_cardioFragment2)
    }

}