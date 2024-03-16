package com.fitlife.atfsd.ui.rv_training

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import coil.load
import coil.transform.RoundedCornersTransformation
import com.fitlife.atfsd.R
import com.fitlife.atfsd.domain.TrainingWithCommonData

class TrainingRVAdapter :
    ListAdapter<TrainingWithCommonData, TrainingViewHolder>(TrainingDiffCallBack()) {

    var onTrainingItemClickListener: ((Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrainingViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return TrainingViewHolder(
            layoutInflater.inflate(
                R.layout.training_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: TrainingViewHolder, position: Int) {
        val item = getItem(position)
        with(holder) {
            trainingName.text = item.name
            if (item.logo.length<3){
                val imgId = item.logo.toInt()
                trainingLogo.setImageDrawable(getMeditationLogo(holder.itemView.context, imgId))
            }else{
                trainingLogo.load(item.logo) {
                    transformations(RoundedCornersTransformation(20.0f))
                }
            }
            amountExercises.text =
                holder.itemView.context.getString(R.string.amount_exercises, item.amountExercises)
            totalDuration.text = item.totalTimeFormatted
            itemView.setOnClickListener {
                onTrainingItemClickListener?.invoke(item.id)
            }
        }
    }

    private fun getMeditationLogo(context:Context, logoId:Int): Drawable? {
        return when (logoId) {
            0 -> context.getDrawable(R.drawable.mountains)
            1 -> context.getDrawable(R.drawable.river)
            2 -> context.getDrawable(R.drawable.grass)
            3 -> context.getDrawable(R.drawable.waterfall)
            4 -> context.getDrawable(R.drawable.ocean)
            else -> context.getDrawable(R.drawable.forest)
        }
    }

}