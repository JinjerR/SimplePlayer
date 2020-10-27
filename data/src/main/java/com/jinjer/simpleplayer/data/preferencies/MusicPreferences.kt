package com.jinjer.simpleplayer.data.preferencies

import android.content.Context
import android.content.SharedPreferences
import com.jinjer.simpleplayer.data.R
import com.jinjer.simpleplayer.domain.data_sources.IMusicPreferences

class MusicPreferences(private val appContext: Context): IMusicPreferences {

    private val prefName = "shared_preferences"

    override fun setCurrentTrackId(trackId: Int) {
        getPreferences()
            .edit()
            .putInt(appContext.getString(R.string.pref_current_song_id), trackId)
            .apply()
    }

    override fun getCurrentTrackId(): Int {
        return getPreferences()
            .getInt(appContext.getString(R.string.pref_current_song_id), -1)
    }

    private fun getPreferences(): SharedPreferences {
        return appContext.getSharedPreferences(prefName, Context.MODE_PRIVATE)
    }
}