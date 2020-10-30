package com.jinjer.simpleplayer.presentation.main.albums

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jinjer.simpleplayer.domain.usecases.GetAlbumDetailsByIdUseCase
import com.jinjer.simpleplayer.presentation.models.AlbumDetails
import com.jinjer.simpleplayer.presentation.models.mappers.AlbumDetailsMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AlbumDetailsContentViewModel(
    private val getAlbumDetailsById: GetAlbumDetailsByIdUseCase,
    private val mapper: AlbumDetailsMapper): ViewModel() {

    private val mAlbumDetails = MutableLiveData<AlbumDetails>()
    val albumDetails: LiveData<AlbumDetails> = mAlbumDetails

    fun getAlbumDetails(albumId: Int) {
        viewModelScope.launch {
            mAlbumDetails.value = withContext(Dispatchers.IO) {
                mapper.from(getAlbumDetailsById(albumId))
            }
        }
    }
}