package com.fitlife.atfsd.ui.cardio

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fitlife.atfsd.R
import com.fitlife.atfsd.databinding.FragmentCardioBinding
import com.fitlife.atfsd.domain.TRAINING_ID
import com.fitlife.atfsd.ui.rv_training.TrainingRVAdapter
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.observeOn
import kotlinx.coroutines.launch

class CardioFragment : Fragment() {

    private val binding by lazy { FragmentCardioBinding.inflate(layoutInflater) }
    private val viewModel by viewModels<CardioViewModel>()
    private val rvAdapter by lazy { TrainingRVAdapter() }
    private val recyclerView by lazy { binding.rvCardioExercises }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupBtnBackClickListener()
        observeCommonInfo()
        setupRecyclerView()
        observeTrainings()
    }

    private fun setupBtnBackClickListener(){
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun observeCommonInfo() {
        lifecycleScope.launch {
            viewModel.trainingTypeCommonInfo.collect() {
                if (it != null) {
                    binding.amountExercises.text =
                        getString(R.string.amount_exercises, it.amountExercises)
                    binding.totalDuration.text = it.totalTimeFormatted
                }
            }
        }
    }

    private fun observeTrainings(){
        lifecycleScope.launch {
            viewModel.cardioTrainings.collect(){
                if (it!=null){
                    rvAdapter.submitList(it)
                }
            }
        }

    }

    private fun setupRvAdapter() {
        rvAdapter.onTrainingItemClickListener = { trainingId ->
            val args = Bundle()
            args.putInt(TRAINING_ID, trainingId)
            findNavController().navigate(R.id.action_cardioFragment_to_trainingFragment, args)
        }
    }

    private fun setupRecyclerView() {
        setupRvAdapter()
        with(recyclerView) {
            adapter = rvAdapter
            layoutManager = LinearLayoutManager(
                context,
                RecyclerView.VERTICAL,
                false
            )
        }
    }

}