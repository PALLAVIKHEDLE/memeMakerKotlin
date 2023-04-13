package com.example.mememaker

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mememaker.api.ImgFlipCaptionImageResponseData
import com.example.mememaker.api.ImgFlipExecutor
import com.example.mememaker.api.MemeTemplateItem

private const val TAG = "MemerViewModel"

class MemerViewModel: ViewModel() {
    private var templateIndex: Int = 0

    val memeTemplatesLiveData: LiveData<List<MemeTemplateItem>> = ImgFlipExecutor().fetchTemplates()

    fun getTemplateIndex(): Int {
        return this.templateIndex
    }
    fun decreaseTemplateIndex() {
        if (memeTemplatesLiveData.value != null) {
            templateIndex -= 1
            if (templateIndex < 0) {
                templateIndex = memeTemplatesLiveData.value!!.size - 1
            }
        }
    }
    fun increaseTemplateIndex() {
        if (memeTemplatesLiveData.value != null) {
            templateIndex += 1
            if (templateIndex >= memeTemplatesLiveData.value!!.size) {
                templateIndex = 0
            }
        }
    }

    fun getCurrentMemeTemplate(): MemeTemplateItem? {
        if (this.memeTemplatesLiveData.value != null) {
            if (this.templateIndex >= 0 && this.templateIndex <= this.memeTemplatesLiveData.value!!.size) {
                return this.memeTemplatesLiveData.value!![this.templateIndex]
            }
        }
        return null
    }

    private var captionedMemeLiveData: LiveData<ImgFlipCaptionImageResponseData> = MutableLiveData()

    fun captionMeme(templateId: String, caption: String): LiveData<ImgFlipCaptionImageResponseData> {
        Log.d(TAG, "Received request to caption meme # $templateId with $caption")
        this.captionedMemeLiveData = ImgFlipExecutor().captionImage(
            templateId,
	        caption
        )
        return this.captionedMemeLiveData
    }
}