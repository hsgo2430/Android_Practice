package com.example.retrofitapp.retrofit

import android.content.ContentValues
import android.util.Log
import com.example.retrofitapp.utils.API
import com.example.retrofitapp.utils.RESPONSE_STATE
import com.google.gson.JsonElement
import retrofit2.Call
import retrofit2.Response

class RetrofitManager {
    companion object{
        val instance = RetrofitManager()
    }
    // 레트로핏 인터페이스 가져옴
    private val iRetrofit: IRetrofit? = RetrofitClient.getClient(API.BASE_URL)?.create(IRetrofit::class.java)

    // 사진 검색 API 호출
    fun searchPhotos(searchTerm: String?, completion: (RESPONSE_STATE, String) -> Unit){

        val term = searchTerm.let{
            it
        }?: ""

        val call = iRetrofit?.searchPhotos(searchTerm = term) ?: return

        call.enqueue(object : retrofit2.Callback<JsonElement>{

            //응답 성공시
            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                Log.d(ContentValues.TAG, "응답 성공 / response : ${response.raw()}")
                completion(RESPONSE_STATE.OKAY,response.raw().toString())
            }

            //응답 실패시
            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                Log.d(ContentValues.TAG, "응답 실패 /t : $t")
                completion(RESPONSE_STATE.FALSE, t.toString())
            }

        })
    }
}