package com.jinjer.simpleplayer.presentation.main.albums

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.jinjer.simpleplayer.presentation.main.player.PlayerFragmentWithContent

class AlbumDetailsFragment: PlayerFragmentWithContent() {
    companion object {
        const val keyAlbumId = "key_album_id"

        fun newInstance(albumId: Int): AlbumDetailsFragment = AlbumDetailsFragment().apply {
            arguments = Bundle().apply {
                putInt(keyAlbumId, albumId)
            }
        }
    }

    override fun getContentFragment(): Fragment {
        val albumId = arguments?.getInt(keyAlbumId, -1) ?: -1
        return AlbumDetailsContentFragment.newInstance(albumId)
    }
}