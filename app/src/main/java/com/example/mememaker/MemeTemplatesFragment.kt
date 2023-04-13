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

private const val TAG = "MemeTemplatesFragment"

class MemeTemplatesFragment: Fragment() {
    private lateinit var memerViewModel: MemerViewModel

    private lateinit var memeTemplateImage: ImageView
    private lateinit var prevButton: Button
    private lateinit var nextButton: Button
    private lateinit var memeTemplateIndexLabel: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.memerViewModel = ViewModelProvider(this.requireActivity())[MemerViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_meme_template, container, false)
        this.memeTemplateImage = view.findViewById(R.id.meme_template_image)
        this.prevButton = view.findViewById(R.id.prev_button)
        this.nextButton = view.findViewById(R.id.next_button)
        this.memeTemplateIndexLabel = view.findViewById(R.id.meme_template_index_label)
        this.prevButton.setOnClickListener {
            memerViewModel.decreaseTemplateIndex()
            updateToCurrentMemeTemplate()
        }
        this.nextButton.setOnClickListener {
            memerViewModel.increaseTemplateIndex()
            updateToCurrentMemeTemplate()
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.memerViewModel.memeTemplatesLiveData.observe(
            this.viewLifecycleOwner
        ) { memeTemplates ->
            Log.d(TAG, "ViewModel has noticed new meme templates $memeTemplates")
            this.updateToCurrentMemeTemplate()
        }
    }

    private fun updateToCurrentMemeTemplate() {
        this.memeTemplateIndexLabel.text = this.memerViewModel.getTemplateIndex().toString()

        val meme = this.memerViewModel.getCurrentMemeTemplate()
        if (meme != null) {
            Log.v(TAG, "Meme selected: $meme")

            Picasso.get()
                .load(meme.url)
                .into(this.memeTemplateImage)
        }
    }
}

