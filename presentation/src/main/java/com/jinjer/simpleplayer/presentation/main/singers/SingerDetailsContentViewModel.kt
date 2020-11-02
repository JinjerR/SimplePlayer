package com.jinjer.simpleplayer.presentation.main.singers

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jinjer.simpleplayer.domain.usecases.GetSingerByIdUseCase
import com.jinjer.simpleplayer.presentation.models.singer.SingerDetails
import com.jinjer.simpleplayer.presentation.models.singer.SingerDetailsMapper
import kotlinx.coroutines.launch

class SingerDetailsContentViewModel(
    private val getSingerById: GetSingerByIdUseCase,
    private val singerMapper: SingerDetailsMapper
): ViewModel() {

    private val mSingerDetails = MutableLiveData<SingerDetails>()
    val singerDetails: LiveData<SingerDetails> = mSingerDetails

    fun getSingerDetails(singerId: Int) {
        viewModelScope.launch {
            getSingerById(singerId)?.let {
                mSingerDetails.value = singerMapper.from(it)
            }
        }
    }
}