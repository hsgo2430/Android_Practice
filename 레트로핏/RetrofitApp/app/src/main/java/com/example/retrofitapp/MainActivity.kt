package com.example.retrofitapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.example.retrofitapp.databinding.ActivityMainBinding
import com.example.retrofitapp.retrofit.RetrofitManager
import com.example.retrofitapp.utils.Constants.TAG
import com.example.retrofitapp.utils.RESPONSE_STATE
import com.example.retrofitapp.utils.SEARCH_TYPE
import com.example.retrofitapp.utils.onMyTextChanged

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var currentSearchType: SEARCH_TYPE = SEARCH_TYPE.PHOTO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Log.d(TAG, "MainActivity - onCreate() called")
        
        binding

        binding.searchTermRadioGroup.setOnCheckedChangeListener { _, checkedId ->


            // switch 문
            when(checkedId) {
                R.id.photo_search_radio_btn -> {
                    Log.d(TAG, "사진검색 버튼 클릭!")
                    binding.searchTermTextLayout.hint = "사진검색"
                    binding.searchTermTextLayout.startIconDrawable = resources.getDrawable(R.drawable.ic_photo_library_black_24dp, resources.newTheme())
                    this.currentSearchType = SEARCH_TYPE.PHOTO
                }

                R.id.user_search_radio_btn -> {
                    Log.d(TAG, "사용자검색 버튼 클릭!")
                    binding.searchTermTextLayout.hint = "사용자검색"
                    binding.searchTermTextLayout.startIconDrawable = resources.getDrawable(R.drawable.ic_person_black_24dp, resources.newTheme())
                    this.currentSearchType = SEARCH_TYPE.USER
                }
            }
            Log.d(TAG, "MainActivity - OnCheckedChanged() called / currentSearchType : $currentSearchType")
        }


        // 텍스트가 변경이 되었을때
        binding.searchTermEditText.onMyTextChanged {

            // 입력된 글자가 하나라도 있다면
            if(it.toString().count() > 0){
                // 검색 버튼을 보여준다.
                binding.searchBtn.frameSearchBtn.visibility = View.VISIBLE
                binding.searchTermTextLayout.helperText = " "



                // 스크롤뷰를 올린다.
                binding.mainScrollview.scrollTo(0,200)

            } else {
                binding.searchBtn.frameSearchBtn.visibility = View.INVISIBLE
                binding.searchTermTextLayout.helperText = "검색어를 입력해주세요"
            }

            if (it.toString().count() == 12) {
                Log.d(TAG, "MainActivity - 에러 띄우기 ")
                Toast.makeText(this, "검색어는 12자 까지만 입력 가능합니다.", Toast.LENGTH_SHORT).show()
            }

        }

        // 검색 버튼 클릭시
        binding.searchBtn.btnSearch.setOnClickListener {
            Log.d(TAG, "MainActivity - 검색 버튼이 클릭되었다. / currentSearchType : $currentSearchType")

            RetrofitManager.instance.searchPhotos(searchTerm = binding.searchTermEditText.toString(), completion = {
                    responseState, responseBody ->
                when(responseState){
                    RESPONSE_STATE.OKAY ->{
                        Log.d(TAG, "api 호출 성공: $responseBody")
                    }
                    RESPONSE_STATE.FALSE ->{
                        Toast.makeText(this, "api 호출 에러입니다.", Toast.LENGTH_SHORT).show()
                        Log.d(TAG, "api 호출 실패: $responseBody")
                    }
                }
            })
            this.handleSearchButtonUi()
        }

    }

    private fun handleSearchButtonUi(){

        binding.searchBtn.btnProgress.visibility = View.VISIBLE

        binding.searchBtn.btnSearch.text = ""

        Handler().postDelayed({
            binding.searchBtn.btnProgress.visibility = View.INVISIBLE
            binding.searchBtn.btnSearch.text = "검색"
        }, 1500)

    }
}