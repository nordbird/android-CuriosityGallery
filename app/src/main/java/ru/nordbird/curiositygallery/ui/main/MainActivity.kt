package ru.nordbird.curiositygallery.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.nordbird.curiositygallery.databinding.ActivityMainBinding
import ru.nordbird.curiositygallery.ui.gallery.GalleryFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(binding.container.id, GalleryFragment.newInstance())
                .commitNow()
        }
    }
}