package com.jinjer.simpleplayer.presentation.main.singers

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.jinjer.simpleplayer.presentation.base.IOnBackPressed
import com.jinjer.simpleplayer.presentation.main.albums.AlbumDetailsContentFragment
import com.jinjer.simpleplayer.presentation.main.albums.AlbumDetailsFragment
import com.jinjer.simpleplayer.presentation.main.player.PlayerFragmentWithContent

interface IOnAlbumClickedListener {
    fun onAlbumClicked(albumId: Int)
}

class SingerDetailsFragment: PlayerFragmentWithContent(), IOnAlbumClickedListener {
    private val tagAlbumDetails = "tag_album_details"

    private val albumDetailsFragment: AlbumDetailsContentFragment?
    get() = childFragmentManager.findFragmentByTag(tagAlbumDetails) as? AlbumDetailsContentFragment

    override fun getContentFragment(): Fragment {
        val singerId = arguments?.getInt(keySingerId, -1) ?: -1
        return SingerDetailsContentFragment.newInstance(singerId)
    }

    override fun onAlbumClicked(albumId: Int) {
        showNewContent(AlbumDetailsContentFragment.newInstance(albumId), tagAlbumDetails, true)
    }

    override fun backProcessed(): Boolean {
        val isParentProcessedBack =  super.backProcessed()
        if (isParentProcessedBack) {
            return true
        }

        albumDetailsFragment?.let {
            childFragmentManager.popBackStack()
            return true
        }

        return false
    }

    companion object {
        const val keySingerId = "key_singer_id"

        fun newInstance(singerId: Int) = SingerDetailsFragment().apply {
            arguments = Bundle().apply {
                putInt(keySingerId, singerId)
            }
        }
    }
}