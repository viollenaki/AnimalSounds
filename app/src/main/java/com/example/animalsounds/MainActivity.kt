package com.example.animalsounds

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.GridView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.appbar.MaterialToolbar

class MainActivity : AppCompatActivity() {
    lateinit var gridView: GridView
    private lateinit var toolbar: MaterialToolbar
    private var mediaPlayer: MediaPlayer? = null
    private val animalSoundMap = mapOf(
        "Cow" to "cow",
        "Elephant" to "elephant",
        "Lion" to "lion",
        "Owl" to "owl",
        "Bird" to "bird",
        "Horse" to "hourse",
        "Chicken" to "chicken",
        "Eagle" to "eagle",
        "Cat" to "cat",
        "Dog" to "dog",
        "Duck" to "duck",
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom)
            insets
        }

        gridView = findViewById(R.id.gridView)
        toolbar = findViewById(R.id.materialToolbar)

        // Устанавливаем иконку меню и обработчик
        toolbar.setNavigationIcon(R.drawable.ic_menu)
        toolbar.setNavigationOnClickListener { view ->
            showCategoryMenu(view)
        }

        val animalNames = listOf(
            "Cow",
            "Horse",
            "Cat",
            "Dog",
            "Chicken",
            "Duck",
            "Lion",
            "Elephant",
            "Bird",
            "Eagle",
            "Owl",
        )

        val animalImages = listOf(
            R.drawable.cow,
            R.drawable.horse,
            R.drawable.cat,
            R.drawable.dog,
            R.drawable.chicken,
            R.drawable.duck,
            R.drawable.lion,
            R.drawable.elephant,
            R.drawable.bird,
            R.drawable.eagle,
            R.drawable.owl,
        )


        gridView.adapter = AnimalAdapter(
            animalNames = animalNames,
            animalImages = animalImages,
            context = this,
        )

        gridView.setOnItemClickListener { _, _, position, _ ->
            playAnimalSound(animalNames[position])
        }
    }

    private fun showCategoryMenu(anchor: android.view.View) {
        val popup = PopupMenu(this, anchor)
        popup.menuInflater.inflate(R.menu.main_menu, popup.menu)
        popup.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_farm -> {
                    openCategory(CategoryActivity.CATEGORY_FARM)
                    true
                }
                R.id.menu_wild -> {
                    openCategory(CategoryActivity.CATEGORY_WILD)
                    true
                }
                R.id.menu_birds -> {
                    openCategory(CategoryActivity.CATEGORY_BIRDS)
                    true
                }
                else -> false
            }
        }
        popup.show()
    }

    private fun openCategory(category: String) {
        val intent = Intent(this, CategoryActivity::class.java)
        intent.putExtra(CategoryActivity.EXTRA_CATEGORY, category)
        startActivity(intent)
    }

    private fun playAnimalSound(animalName: String) {
        val soundFileName = animalSoundMap[animalName] ?: return
        val soundRes = resources.getIdentifier(soundFileName, "raw", packageName)
        if (soundRes == 0) return
        releasePlayer()
        mediaPlayer = MediaPlayer.create(this, soundRes).apply {
            setOnCompletionListener { releasePlayer() }
            start()
        }
    }

    private fun releasePlayer() {
        mediaPlayer?.let {
            if (it.isPlaying) it.stop()
            it.release()
        }
        mediaPlayer = null
    }

    override fun onDestroy() {
        releasePlayer()
        super.onDestroy()
    }
}