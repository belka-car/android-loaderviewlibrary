package com.elyeproj.sampleloaderview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val _data: MutableLiveData<Data> = MutableLiveData()
    val data: LiveData<Data> = _data

    init {
        loadData()
    }

    fun loadData() = viewModelScope.launch(Dispatchers.IO) {
        delay(5000L)
        _data.postValue(
            Data(
                name = "Mr. Donald Trump",
                title = "President of United State (2017 - now)",
                phone = "+001 2345 6789",
                email = "donald.trump@donaldtrump.com",
                image = R.drawable.trump,
            )
        )
    }

}