package com.jinjer.simpleplayer.presentation.main.singers

import androidx.fragment.app.Fragment
import com.jinjer.simpleplayer.presentation.main.player.PlayerFragmentWithContent

class SingerDetailsFragment: PlayerFragmentWithContent() {

    override fun getContentFragment(): Fragment {
        val singerId = arguments?.getInt(keySingerId, -1) ?: -1
        return SingerDetailsContentFragment.newInstance(singerId)
    }

    companion object {
        const val keySingerId = "key_singer_id"
    }
}