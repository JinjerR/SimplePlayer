package com.jinjer.simpleplayer.presentation.utils.recyclerview

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class BaseViewHolder<P>(view: View): RecyclerView.ViewHolder(view) {
    abstract fun bind(data: P)
}
