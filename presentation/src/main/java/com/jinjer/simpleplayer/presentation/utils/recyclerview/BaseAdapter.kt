package com.jinjer.simpleplayer.presentation.utils.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

abstract class BaseAdapter<P, VH: BaseViewHolder<P>>(
    diff: DiffUtil.ItemCallback<P>, private val itemClick: (P) -> Unit) : ListAdapter<P, VH>(diff) {

    abstract var layoutId: Int

    abstract fun initHolder(view: View): VH

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(layoutId, parent, false)

        val holder = initHolder(view)

        holder.itemView.setOnClickListener {
            val position = holder.adapterPosition
            val data = getItem(position)
            onItemClick(data, position)
        }

        return holder
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val data = getItem(holder.adapterPosition)
        holder.bind(data)
    }

    open fun onItemClick(data: P, position: Int) {
        itemClick(data)
    }
}
