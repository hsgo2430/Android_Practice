package com.example.retrofitapp.retrofit

import com.example.retrofitapp.utils.API
import com.google.gson.JsonElement
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface IRetrofit {

    @GET(API.SEARCH_PHOTO) // 어노테이션으로 BaseUri뒤에 있을 추가 uri를 설정
    fun searchPhotos(@Query("query") searchTerm: String) :Call<JsonElement>
    // => http://www.unsplash.com/search/photos/?query="searchTerm"

    @GET(API.SEARCH_USERS)
    fun searchUsers(@Query("query") searchTerm: String) :Call<JsonElement>
    // => http://www.unsplash.com/search/users/?query="searchTerm"
}