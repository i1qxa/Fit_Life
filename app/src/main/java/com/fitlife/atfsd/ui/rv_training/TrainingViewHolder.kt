package com.fitlife.atfsd.ui.rv_training

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fitlife.atfsd.R

class TrainingViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    val trainingName = itemView.findViewById<TextView>(R.id.tvTrainingName)
    val trainingLogo = itemView.findViewById<ImageView>(R.id.ivTrainingLogo)
    val amountExercises = itemView.findViewById<TextView>(R.id.amountExercisesTraining)
    val totalDuration = itemView.findViewById<TextView>(R.id.totalDurationTraining)

}