package com.jinjer.simpleplayer.presentation.main.search.base

import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// TODO: first need to take the result from the data layer,
//  and only then we run it through the mapper into our search result

abstract class SearchViewModelBase<P>: ViewModel() {

    protected val mSearchResult = MutableLiveData<List<P>>()
    val searchResult: LiveData<List<P>> = mSearchResult

    var query = ""

    fun search(query: String) {
        this.query = query

        if(query.isEmpty()) {
            mSearchResult.value = emptyList()
            return
        }

        viewModelScope.launch {
            prepareSearch(query)
        }
    }

    abstract suspend fun prepareSearch(query: String)
}