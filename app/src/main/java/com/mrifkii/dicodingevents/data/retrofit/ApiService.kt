package com.mrifkii.dicodingevents.data.retrofit

import com.mrifkii.dicodingevents.data.response.EventDetailResponse
import com.mrifkii.dicodingevents.data.response.EventResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("/events")
    fun getEvent(
        @Query("active") active: String = "1",
        @Query("q") q: String? = null
    ): Call<EventResponse>

    @GET("/events/{id}")
    fun getDetailEvent(
        @Path("id") id: String
    ): Call<EventDetailResponse>
}