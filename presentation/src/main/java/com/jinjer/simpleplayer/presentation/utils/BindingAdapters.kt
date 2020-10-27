package com.jinjer.simpleplayer.presentation.utils

import android.net.Uri
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.jinjer.simpleplayer.presentation.R

object BindingAdapters {
    @BindingAdapter("app:track_name")
    @JvmStatic fun trackName(txt: TextView, name: String) {
        txt.text = name
    }

    @BindingAdapter("app:band_name")
    @JvmStatic fun bandName(txt: TextView, name: String) {
        txt.text = name
    }

    @BindingAdapter("app:track_duration")
    @JvmStatic fun trackDuration(txt: TextView, duration: Long) {
        val formattedDuration = TimeUtils.getFormattedSongDurationFromMillis(duration)
        txt.text = formattedDuration
    }

    @BindingAdapter("app:track_max_duration")
    @JvmStatic fun trackMaxDuration(seekBar: SeekBar, duration: Long) {
        seekBar.max = (duration / 1000).toInt()
    }

    @BindingAdapter("app:album_image")
    @JvmStatic fun albumImage(imageView: ImageView, uri: Uri?) {
        uri ?: return

        Glide.with(imageView)
            .load(uri)
            .error(R.drawable.icon_empty_album_art)
            .placeholder(R.drawable.icon_empty_album_art)
            .into(imageView)
    }

    @BindingAdapter("app:play_icon")
    @JvmStatic fun playIcon(imageView: ImageView, resId: Int) {
        imageView.setImageResource(resId)
    }
}