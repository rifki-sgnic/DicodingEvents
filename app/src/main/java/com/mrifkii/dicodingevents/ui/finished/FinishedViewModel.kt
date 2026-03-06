package com.mrifkii.dicodingevents.ui.finished

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mrifkii.dicodingevents.data.response.EventResponse
import com.mrifkii.dicodingevents.data.response.ListEventsItem
import com.mrifkii.dicodingevents.data.retrofit.ApiConfig
import com.mrifkii.dicodingevents.ui.home.HomeViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FinishedViewModel : ViewModel() {
    private val _finishedEvents = MutableLiveData<List<ListEventsItem>>()
    val finishedEvents: LiveData<List<ListEventsItem>> = _finishedEvents

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getFinishedEvents() {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getEvent(FINISHED_EVENT_PARAM)
        client.enqueue(object : Callback<EventResponse> {
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _finishedEvents.value = responseBody.listEvents?.filterNotNull()
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    companion object {
        private const val TAG = "FinishedViewModel"
        private const val FINISHED_EVENT_PARAM = "0"
    }
}