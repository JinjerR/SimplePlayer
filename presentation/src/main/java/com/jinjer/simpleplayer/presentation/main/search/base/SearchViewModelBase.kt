package com.jinjer.simpleplayer.presentation.main.search.base

import androidx.lifecycle.*
import com.jinjer.simpleplayer.domain.models.TrackDomain
import com.jinjer.simpleplayer.domain.usecases.GetTracksUseCase
import com.jinjer.simpleplayer.domain.utils.Mapper
import com.jinjer.simpleplayer.presentation.controller.main.IPlayerController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

abstract class SearchViewModelBase<P>(
    playerController: IPlayerController,
    private val getTracks: GetTracksUseCase,
    private val mapper: Mapper<TrackDomain, P>): ViewModel() {

    private var dataSource: List<P> = emptyList()
    protected var query = ""

    private val mSearchResult = MutableLiveData<List<P>>()
    val searchResult: LiveData<List<P>> = mSearchResult

    private val trackLoadingObserver: Observer<Boolean> = Observer { isTracksLoaded ->
        if (isTracksLoaded) {
            viewModelScope.launch {
                dataSource = withContext(Dispatchers.IO) {
                    mapper.fromList(getTracks())
                }
            }
        }
    }

    private val controller = playerController.also { controller ->
        controller.isTracksLoaded.observeForever(trackLoadingObserver)
    }

    override fun onCleared() {
        controller.isTracksLoaded.removeObserver(trackLoadingObserver)
    }

    fun search(query: String) {
        this.query = query

        viewModelScope.launch {
            mSearchResult.value = withContext(Dispatchers.IO) {
                dataSource.filter(::filter)
            }
        }
    }

    abstract fun filter(element: P): Boolean
}