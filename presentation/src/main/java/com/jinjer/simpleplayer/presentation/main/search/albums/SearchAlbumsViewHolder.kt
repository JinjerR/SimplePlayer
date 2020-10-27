package com.jinjer.simpleplayer.presentation.main.search.albums

import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.jinjer.simpleplayer.presentation.R
import com.jinjer.simpleplayer.presentation.utils.recyclerview.BaseViewHolder

class SearchAlbumsViewHolder(view: View): BaseViewHolder<SearchAlbumPresenter>(view) {
    private val imgAlbum: AppCompatImageView = view.findViewById(R.id.img_album)
    private val albumName: AppCompatTextView = view.findViewById(R.id.name)

    override fun bind(data: SearchAlbumPresenter) {
        val requestOptions = RequestOptions()
            .error(R.drawable.icon_empty_album_art_round)
            .placeholder(R.drawable.icon_empty_album_art_round)
            .circleCrop()

        Glide.with(imgAlbum)
            .load(data.albumUri)
            .apply(requestOptions)
            .into(imgAlbum)

        albumName.text = data.albumName
    }
}