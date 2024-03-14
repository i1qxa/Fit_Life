package com.fitlife.atfsd.ui.rv_training

import androidx.recyclerview.widget.DiffUtil
import com.fitlife.atfsd.domain.TrainingWithCommonData

class TrainingDiffCallBack:DiffUtil.ItemCallback<TrainingWithCommonData>() {

    override fun areItemsTheSame(
        oldItem: TrainingWithCommonData,
        newItem: TrainingWithCommonData
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: TrainingWithCommonData,
        newItem: TrainingWithCommonData
    ): Boolean {
        return oldItem == newItem
    }
}