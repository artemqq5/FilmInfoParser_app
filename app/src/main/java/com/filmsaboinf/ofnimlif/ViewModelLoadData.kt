package com.filmsaboinf.ofnimlif

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.filmsaboinf.ofnimlif.repository.DefaultRepository
import com.filmsaboinf.ofnimlif.repository.model.FilmPreview
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ViewModelLoadData : ViewModel() {

    private val repository by lazy {
        DefaultRepository()
    }

    private val mutableLoadData = MutableLiveData<List<FilmPreview>>()
    val loadData: LiveData<List<FilmPreview>>
        get() = mutableLoadData

    private val mutableLoadDataDetail = MutableLiveData<FilmPreview>()
    val loadDataDetail: LiveData<FilmPreview>
        get() = mutableLoadDataDetail

    fun loadFilmsData(page: Int) {

        val coroutineException = CoroutineExceptionHandler { _, e ->
            logcat("coroutine exception handler: $e")
        }

        viewModelScope.launch(Dispatchers.IO + coroutineException) {
            val listOfFilms = repository.getFilmsFromPage(page)
            mutableLoadData.postValue(listOfFilms)
        }
    }

    fun loadDetailToFilm(detail: FilmPreview) {
        val coroutineException = CoroutineExceptionHandler { _, e ->
            logcat("coroutine exception detail handler: $e")
        }

        viewModelScope.launch(Dispatchers.IO + coroutineException) {
            val finalFilmObject = repository.loadFilmPage(detail)
            mutableLoadDataDetail.postValue(finalFilmObject)
        }
    }

}