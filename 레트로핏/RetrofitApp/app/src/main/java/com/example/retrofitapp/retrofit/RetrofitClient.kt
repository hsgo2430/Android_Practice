package com.example.retrofitapp.retrofit

import android.content.ContentValues.TAG
import android.util.Log
import com.example.retrofitapp.utils.API
import com.example.retrofitapp.utils.isJsonArray
import com.example.retrofitapp.utils.isJsonObject
import com.google.gson.JsonObject
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception

// 싱글톤
object RetrofitClient {
    // 레트로핏 클라이언트 선언

    private var retrofitClient: Retrofit? = null
    // private lateinit var retrofitClient: Retrofit 도 가능

    // 레트로핏 클라이언트 가져오기
    fun getClient(baseUri: String):Retrofit?{
        Log.d(TAG, "getClient: getClient() called")

        //okhttp 인스턴스 추가
        val client = OkHttpClient.Builder()

        // 로그를 찍기 위해 로깅 인터셉터 추가
        val loggingInterceptor = HttpLoggingInterceptor(object: HttpLoggingInterceptor.Logger{
            override fun log(message: String) {
                Log.d(TAG, "getClient: log() called / message: $message")

                when {
                    message.isJsonObject() ->
                        Log.d(TAG, JSONObject(message).toString(4))

                    message.isJsonArray() ->
                        Log.d(TAG, JSONObject(message).toString(4))

                    else -> {
                        try {
                            Log.d(TAG, JSONObject(message).toString(4))
                        } catch (e: Exception) {
                            Log.d(TAG, message)
                        }
                    }
                }
            }
        })

        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        //위에서 설정한 로깅 인터셉터를 okhttp 클라이언트에 추가
        client.addInterceptor(loggingInterceptor)

        // 기본 파라미터 추가
        val baseParameterInterceptor : Interceptor = (object : Interceptor{
            override fun intercept(chain: Interceptor.Chain): Response {
                Log.d(TAG, "RetrofitClient - intercept() called")
                // 오리지날 리퀘스트
                val originalRequest = chain.request()

                // ?client_id=asdfadsf
                // 쿼리 파라매터 추가하기
                val addedUrl = originalRequest.url.newBuilder().addQueryParameter("client_id", API.CLIENT_ID).build()

                val finalRequest = originalRequest.newBuilder()
                    .url(addedUrl)
                    .method(originalRequest.method, originalRequest.body)
                    .build()

                return chain.proceed(finalRequest)
            }

        })
        //위에서 설정한 로깅 인터셉터를 okhttp 클라이언트에 추가
        client.addInterceptor(baseParameterInterceptor)


        if(retrofitClient == null){ // 값이 비어있으면

            // 레트로핏 빌더를 통해 인스터스 생성
            retrofitClient = Retrofit.Builder()
                .baseUrl(baseUri) // 베이스 Uri로 getClient의 매개변수 사용
                .addConverterFactory(GsonConverterFactory.create())
                // Gson 형태로 할것이기 떄문에 Gson컨버터 생성 후 사용

                // 위에서 설정한 클라이언트로 레트로핏 클라이언트를 설정한다.
                .client(client.build())
                .build() // 새 레트로핏 객체를 반환
        }
        return retrofitClient
    }
}