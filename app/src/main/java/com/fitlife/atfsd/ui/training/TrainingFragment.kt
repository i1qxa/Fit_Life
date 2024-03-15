package com.fitlife.atfsd.ui.training

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavArgs
import androidx.navigation.fragment.navArgs
import coil.load
import com.fitlife.atfsd.databinding.FragmentTrainingBinding
import com.fitlife.atfsd.domain.TRAINING_ID
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TrainingFragment : Fragment() {

    private val binding by lazy { FragmentTrainingBinding.inflate(layoutInflater) }

    private val viewModel: TrainingViewModel by viewModels()

    private val trainingId by lazy { arguments?.getInt(TRAINING_ID) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        trainingId?.let { viewModel.setupTrainingId(it) }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupBtnStartClickListener()
    }

    private fun observeExerciseList(){
        lifecycleScope.launch {
            viewModel.exerciseList.collect{ exerciseItemsList ->
                if (exerciseItemsList!=null){
                    exerciseItemsList.forEach { exerciseItem ->
                        binding.ivExerciseImg.load(exerciseItem.logo)
                        binding.tvExerciseName.text = exerciseItem.name
                        binding.tvExerciseDescription.text = exerciseItem.description
                        var amountOfRepeat = exerciseItem.repeat
                        while (amountOfRepeat>0){
                            var timer = exerciseItem.duration
                            updateTimer(timer)
                            binding.timerProgress.max = timer
                            binding.timerProgress.setProgress(timer, true)
                                while (timer>0){
                                delay(1000)
                                timer-=1
                                updateTimer(timer)
                                    binding.timerProgress.setProgress(timer, true)
                                if (timer ==0){
                                    var timeForRest = 30
                                    binding.timerProgress.max = timeForRest
                                    binding.timerProgress.setProgress(timeForRest, true)
                                    updateTimer(timeForRest)
                                    while (timeForRest>0){
                                        delay(1000)
                                        timeForRest-=1
                                        updateTimer(timeForRest)
                                        binding.timerProgress.setProgress(timeForRest, true)
                                    }
                                }
                            }
                            amountOfRepeat-=1
                        }
                    }
                }
            }
        }
    }

    private fun updateTimer(time:Int){
        binding.tvTimer.text = time.toString()
    }

    private fun setupBtnStartClickListener(){
        binding.btnStart.setOnClickListener {
            observeExerciseList()
        }
    }

}