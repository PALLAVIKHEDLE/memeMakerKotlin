package com.example.mememaker.api


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val TAG = "ImgFlipExecutor"

//********************************
// THIS IS ABSOLUTELY TERRIBLE
private const val DO_NOT_DO_THIS_Username = "frankzk"
private const val DO_NOT_DO_THIS_Password = "ABcd12!@"

// instead of this, either use a username password that you make the user enter
// or in some cases use OAuth

//*********************************

class ImgFlipExecutor {
    private val api: ImgFlipApi

    init {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://api.imgflip.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        this.api = retrofit.create(ImgFlipApi::class.java)
    }

    fun fetchTemplates(): LiveData<List<MemeTemplateItem>> {

        val responseLiveData: MutableLiveData<List<MemeTemplateItem>> = MutableLiveData()

        val imgFlipRequest: Call<ImgFlipGetMemesResponse> = this.api.fetchTemplate()

        imgFlipRequest.enqueue(object: Callback<ImgFlipGetMemesResponse> {

            override fun onFailure(call: Call<ImgFlipGetMemesResponse>, t: Throwable) {
                Log.e(TAG, "Response received from ImgFlip fetch failed")
            }

            override fun onResponse(
                call: Call<ImgFlipGetMemesResponse>,
                response: Response<ImgFlipGetMemesResponse>
            ) {
                Log.d(TAG, "Response received from ImgFlip get_memes endpoint")

                val imgFlipGetMemesResponse: ImgFlipGetMemesResponse? = response.body()
                val imgFlipGetMemesResponseData: ImgFlipGetMemesResponseData? = imgFlipGetMemesResponse?.data
		
                var memeTemplates: List<MemeTemplateItem> = imgFlipGetMemesResponseData?.templates ?: mutableListOf()
                memeTemplates = memeTemplates.filterNot {
                    it.url.isBlank()
                }
        	Log.d(TAG, "ImgFlip templates: $memeTemplates")
                responseLiveData.value = memeTemplates
            }
        })

        return responseLiveData
    }

    fun captionImage(templateId: String, caption: String): LiveData<ImgFlipCaptionImageResponseData> {
        val responseLiveData: MutableLiveData<ImgFlipCaptionImageResponseData> = MutableLiveData()
        val imgFlipRequest: Call<ImgFlipCaptionImageResponse> = this.api.captionImage(
            templateId = templateId,
            username = DO_NOT_DO_THIS_Username,  //	HORRIBLE !!!!
            password = DO_NOT_DO_THIS_Password,  //	HORRIBLE !!!!
            caption1 = caption,
            caption2 = caption
        )

        Log.d(TAG, "Enquieuing  a request to caption the template # $templateId with $caption")
        imgFlipRequest.enqueue(object: Callback<ImgFlipCaptionImageResponse> {

            override fun onFailure(call: Call<ImgFlipCaptionImageResponse>, t: Throwable) {
                Log.e(TAG, "Failed to caption meme template!")
            }

            override fun onResponse(
                call: Call<ImgFlipCaptionImageResponse>,
                response: Response<ImgFlipCaptionImageResponse>
            ) {
                Log.d(TAG, "response received from ImgFlip caption_image endpoint")
                val imgFlipCaptionImageResponse: ImgFlipCaptionImageResponse? = response.body()

                if (imgFlipCaptionImageResponse != null && imgFlipCaptionImageResponse.success) {
                    val imgFlipCaptionImageResponseData: ImgFlipCaptionImageResponseData = imgFlipCaptionImageResponse.data
                    responseLiveData.value = imgFlipCaptionImageResponseData
                    Log.d(TAG, "Got new meme url: ${imgFlipCaptionImageResponseData.url}")
                } else {
                    Log.d(TAG, "Request to caption image has failed: ${imgFlipCaptionImageResponse?.errorMessage}")
                }
            }
        })

        return responseLiveData
    }
}