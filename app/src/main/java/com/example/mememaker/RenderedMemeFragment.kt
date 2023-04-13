package com.example.mememaker

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.squareup.picasso.Picasso

private const val TAG = "RenderedMemeFragment"

class RenderedMemeFragment: Fragment() {

    private lateinit var memerViewModel: MemerViewModel

    private lateinit var captionInput: TextView
    private lateinit var captionButton: Button
    private lateinit var captionedMemeImage: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.memerViewModel = ViewModelProvider(this.requireActivity())[MemerViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_rendered_meme, container, false)
        this.captionInput = view.findViewById(R.id.caption_input)
        this.captionButton = view.findViewById(R.id.caption_button)
        this.captionedMemeImage = view.findViewById(R.id.captioned_meme)
        captionButton.setOnClickListener {
            Log.v(TAG, "Enter caption button clicked")
            val text = this.captionInput.text
            val memeTemplate = this.memerViewModel.getCurrentMemeTemplate()
            if (text == null) {
                Log.e(TAG, "Cannot caption meme because the text is null")
            } else if (memeTemplate == null) {
                Log.e(TAG, "Cannot caption meme because the selected template is null")
            } else {
                Log.v(TAG, "Asking the ViewModel to set a caption: $text for $memeTemplate")
                this.memerViewModel.captionMeme(
                    templateId = memeTemplate.id,
                    caption = text.toString()
                ).observe(
                    this.viewLifecycleOwner
                ) { captionMemeResponseData ->
                    Log.d(
                        TAG,
                        "Fragment has been notified that a meme has been created: $captionMemeResponseData"
                    )
                    Picasso.get()
                        .load(captionMemeResponseData.url)
                        .into(this.captionedMemeImage)
                }
            }
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.v(TAG, "onViewCreated called")
    }

}