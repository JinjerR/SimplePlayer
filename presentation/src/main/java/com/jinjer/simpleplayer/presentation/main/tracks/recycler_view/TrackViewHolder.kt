package com.jinjer.simpleplayer.presentation.main.tracks.recycler_view

import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import com.jinjer.simpleplayer.presentation.R
import com.jinjer.simpleplayer.presentation.models.track.Track
import com.jinjer.simpleplayer.presentation.utils.recyclerview.BaseViewHolder

class TrackViewHolder(view: View): BaseViewHolder<Track>(view) {
    private val bandName: AppCompatTextView = view.findViewById(R.id.band_name)
    private val trackName: AppCompatTextView = view.findViewById(R.id.track_name)
    private val imgPlay: AppCompatImageView = view.findViewById(R.id.img_play)

    override fun bind(data: Track) {
        val resId = if (data.isPlaying)
            R.drawable.icon_pause
        else
            R.drawable.icon_play

        val bgColor = if (data.isSelected) {
            ContextCompat.getColor(itemView.context, R.color.colorPrimary)
        } else {
            ContextCompat.getColor(itemView.context, android.R.color.transparent)
        }

        imgPlay.setImageResource(resId)
        bandName.text = data.bandName
        trackName.text = data.trackName

        itemView.setBackgroundColor(bgColor)
    }
}