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
        setupBtnSearchClickListener()
        binding.cardCardio.setOnClickListener {
            launchCardioFragment(R.id.action_homeFragment_to_cardioFragment2)
        }
        binding.cardPilates.setOnClickListener {
            launchCardioFragment(R.id.action_homeFragment_to_pilatesFragment3)
        }
        binding.cardMeditation.setOnClickListener {
            launchCardioFragment(R.id.action_homeFragment_to_meditationFragment2)
        }
        binding.cardYoga.setOnClickListener {
            launchCardioFragment(R.id.action_homeFragment_to_yogaFragment2)
        }
    }

    private fun setupBtnSearchClickListener(){
        binding.btnSearch.setOnClickListener {
            launchCardioFragment(R.id.action_homeFragment_to_searchFragment)
        }
    }

    private fun launchCardioFragment(fragmentId:Int){
        findNavController().navigate(fragmentId)
    }

}