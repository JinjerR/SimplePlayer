package com.jinjer.simpleplayer.presentation.main.albums

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jinjer.simpleplayer.domain.usecases.GetAlbumByIdUseCase
import com.jinjer.simpleplayer.presentation.models.album.AlbumDetails
import com.jinjer.simpleplayer.presentation.models.album.AlbumDetailsMapper
import kotlinx.coroutines.launch

class AlbumDetailsContentViewModel(
    private val getAlbumById: GetAlbumByIdUseCase,
    private val mapper: AlbumDetailsMapper
): ViewModel() {

    private val mAlbumDetails = MutableLiveData<AlbumDetails>()
    val albumDetails: LiveData<AlbumDetails> = mAlbumDetails

    fun getAlbumDetails(albumId: Int) {
        viewModelScope.launch {
            getAlbumById(albumId)?.let {
                mAlbumDetails.value = mapper.from(it)
            }
        }
    }
}