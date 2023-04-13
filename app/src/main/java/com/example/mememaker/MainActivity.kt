package com.example.mememaker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val memeTemplatesFragment = this.supportFragmentManager.findFragmentById(R.id.meme_template_fragment_container)
        if (memeTemplatesFragment == null) {
            val fragment = MemeTemplatesFragment()
            this.supportFragmentManager
                .beginTransaction()
                .add(R.id.meme_template_fragment_container, fragment)
                .commit()
        }

        val renderedMemeFragment = this.supportFragmentManager.findFragmentById(R.id.rendered_meme_container)
        if (renderedMemeFragment == null) {
            val fragment = RenderedMemeFragment()
            this.supportFragmentManager
                .beginTransaction()
                .add(R.id.rendered_meme_container, fragment)
                .commit()
        }

    }
}