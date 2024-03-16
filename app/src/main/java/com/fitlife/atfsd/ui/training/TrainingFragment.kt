package com.fitlife.atfsd.ui.training

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavArgs
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import com.fitlife.atfsd.R
import com.fitlife.atfsd.data.local.exercise_items.ExerciseItems
import com.fitlife.atfsd.databinding.FragmentTrainingBinding
import com.fitlife.atfsd.domain.MILS_IN_SECOND
import com.fitlife.atfsd.domain.TRAINING_ID
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TrainingFragment : Fragment() {

    private val binding by lazy { FragmentTrainingBinding.inflate(layoutInflater) }

    private val viewModel: TrainingViewModel by viewModels()

    private val trainingId by lazy { arguments?.getInt(TRAINING_ID) }

    private var meditationLogo: Drawable? = null

    private lateinit var exerciseList: List<ExerciseItems>

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
        observeExerciseList()
        setupBtnStartClickListener()
        setupBtnBackClickListener()
    }

    private fun observeExerciseList() {
        lifecycleScope.launch {
            viewModel.exerciseList.collect { exerciseItemsList ->
                if (exerciseItemsList != null) {
                    exerciseList = exerciseItemsList
                    if (exerciseList[0].typeId == 3) {
                        val imgPosition = exerciseList[0].logo.toInt()
                        meditationLogo = when (imgPosition) {
                            0 -> requireContext().getDrawable(R.drawable.mountains)
                            1 -> requireContext().getDrawable(R.drawable.river)
                            2 -> requireContext().getDrawable(R.drawable.grass)
                            3 -> requireContext().getDrawable(R.drawable.waterfall)
                            4 -> requireContext().getDrawable(R.drawable.ocean)
                            else -> requireContext().getDrawable(R.drawable.forest)
                        }
                        binding.ivExerciseImg.setImageDrawable(meditationLogo)
                    } else {
                        binding.ivExerciseImg.load(exerciseList[0].logo)
                    }
                    binding.tvExerciseName.text = exerciseList[0].name
                    binding.tvExerciseDescription.text = exerciseList[0].description
                    binding.tvRepeatRemaining.text = exerciseList[0].repeat.toString()
                }
            }
        }
    }

    private fun startTraining() {
        lifecycleScope.launch {

            exerciseList.forEach { exerciseItem ->
                if (meditationLogo != null) {
                    binding.ivExerciseImg.setImageDrawable(meditationLogo)
                } else {
                    binding.ivExerciseImg.load(exerciseItem.logo)
                }
                binding.tvExerciseName.text = exerciseItem.name
                binding.tvExerciseDescription.text = exerciseItem.description
                var amountOfRepeat = exerciseItem.repeat
                while (amountOfRepeat > 0) {
                    binding.ivExerciseImg.visibility = View.VISIBLE
                    binding.tvRest.visibility = View.GONE
                    binding.tvRepeatRemaining.text = amountOfRepeat.toString()
                    var timer = exerciseItem.duration
                    updateTimer(timer)
                    binding.layoutTimer.timerProgress.max = timer
                    binding.layoutTimer.timerProgress.setProgress(timer, true)
                    while (timer > 0) {
                        delay(MILS_IN_SECOND)
                        timer -= 1
                        updateTimer(timer)
                        binding.layoutTimer.timerProgress.setProgress(timer, true)
                        if (timer == 0) {
                            var timeForRest = 30
                            binding.ivExerciseImg.visibility = View.GONE
                            binding.tvRest.visibility = View.VISIBLE
                            binding.layoutTimer.timerProgress.max = timeForRest
                            binding.layoutTimer.timerProgress.setProgress(timeForRest, true)
                            updateTimer(timeForRest)
                            while (timeForRest > 0) {
                                delay(MILS_IN_SECOND)
                                timeForRest -= 1
                                updateTimer(timeForRest)
                                binding.layoutTimer.timerProgress.setProgress(timeForRest, true)
                            }
                        }
                    }
                    amountOfRepeat -= 1
                }
            }
            binding.btnStart.visibility = View.VISIBLE
        }
    }

    private fun updateTimer(time: Int) {
        binding.layoutTimer.tvTimer.text = getTimeFormatted(time)
    }

    private fun getTimeFormatted(time: Int): String {
        val mils = (time * MILS_IN_SECOND).toLong()
        val minutes = (mils % (MILS_IN_SECOND * 60 * 60)) / (MILS_IN_SECOND * 60)
        val seconds = time - minutes * 60
        return String.format("%02d:%02d", minutes, seconds)
    }

    private fun setupBtnStartClickListener() {
        binding.btnStart.setOnClickListener {
            startTraining()
            binding.btnStart.visibility = View.GONE
        }
    }

    private fun setupBtnBackClickListener(){
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

}