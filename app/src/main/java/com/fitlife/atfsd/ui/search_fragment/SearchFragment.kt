package com.fitlife.atfsd.ui.search_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fitlife.atfsd.R
import com.fitlife.atfsd.databinding.FragmentSearchBinding
import com.fitlife.atfsd.domain.IS_SINGLE_EXERCISE
import com.fitlife.atfsd.domain.TRAINING_ID
import com.fitlife.atfsd.ui.rv_training.TrainingRVAdapter
import kotlinx.coroutines.launch

class SearchFragment : Fragment() {

    private val binding by lazy { FragmentSearchBinding.inflate(layoutInflater) }
    private val viewModel by viewModels<SearchViewModel>()
    private val rvAdapter by lazy { TrainingRVAdapter() }
    private val recyclerView by lazy { binding.rvExercises }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupBtnSearchClickListener()
        setupBtnBackClickListener()
        observeExerciseList()
        setupRecyclerView()
    }

    private fun setupBtnBackClickListener(){
        binding.btnBack.setOnClickListener {
            findNavController().navigate(R.id.action_searchFragment_to_homeFragment)
        }
    }

    private fun observeExerciseList() {
        lifecycleScope.launch {
            viewModel.exerciseListLD.observe(viewLifecycleOwner) {
                if (it != null) {
                    binding.amountExercises.text =
                        getString(R.string.amount_exercises, it.size)
                    rvAdapter.submitList(it)
                }
            }
        }
    }


    private fun setupRvAdapter() {
        rvAdapter.onTrainingItemClickListener = { trainingId ->
            val args = Bundle()
            args.putInt(TRAINING_ID, trainingId)
            args.putBoolean(IS_SINGLE_EXERCISE, true)
            findNavController().navigate(R.id.action_searchFragment_to_trainingFragment, args)
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

    private fun setupBtnSearchClickListener(){

        binding.btnSearch.setOnClickListener {
            viewModel.findExercises(binding.etSearchText.text.toString())
        }

    }

}