package com.jinjer.simpleplayer.presentation.main.search.base

import androidx.lifecycle.*
import com.jinjer.simpleplayer.domain.models.TrackDomain
import com.jinjer.simpleplayer.domain.usecases.GetTracksUseCase
import com.jinjer.simpleplayer.domain.utils.Mapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

abstract class SearchViewModelBase<P>(
    private val getTracks: GetTracksUseCase,
    private val mapper: Mapper<TrackDomain, P>): ViewModel() {

    private var dataSource: List<P> = emptyList()
    protected var query = ""

    private val mSearchResult = MutableLiveData<List<P>>()
    val searchResult: LiveData<List<P>> = mSearchResult

    fun onTracksLoaded() {
        viewModelScope.launch {
            dataSource = withContext(Dispatchers.IO) {
                mapper.fromList(getTracks())
            }
        }
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