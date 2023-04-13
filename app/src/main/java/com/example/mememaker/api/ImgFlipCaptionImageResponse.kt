package com.example.mememaker.api

import com.google.gson.annotations.SerializedName

class ImgFlipCaptionImageResponse {
    @SerializedName("success")
    var success: Boolean = false

    @SerializedName("data")
    lateinit var data: ImgFlipCaptionImageResponseData

    @SerializedName("error_message")
    lateinit var errorMessage: String
}