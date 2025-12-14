package com.example.animalsounds

import android.media.MediaPlayer
import android.os.Bundle
import android.widget.GridView
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.appbar.MaterialToolbar

class CategoryActivity : AppCompatActivity() {
    private lateinit var gridView: GridView
    private lateinit var toolbar: MaterialToolbar
    private lateinit var backgroundImage: ImageView
    private var mediaPlayer: MediaPlayer? = null
    private var currentCategory: String = CATEGORY_FARM

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

    companion object {
        const val EXTRA_CATEGORY = "category"
        const val CATEGORY_FARM = "farm"
        const val CATEGORY_WILD = "wild"
        const val CATEGORY_BIRDS = "birds"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_category)
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom)
            insets
        }

        gridView = findViewById(R.id.gridView)
        toolbar = findViewById(R.id.materialToolbar)
        backgroundImage = findViewById(R.id.backgroundImage)

        toolbar.setNavigationIcon(R.drawable.ic_menu)
        toolbar.setNavigationOnClickListener { view ->
            showCategoryMenu(view)
        }

        currentCategory = intent.getStringExtra(EXTRA_CATEGORY) ?: CATEGORY_FARM
        setupCategory(currentCategory)
    }

    private fun showCategoryMenu(anchor: android.view.View) {
        val popup = PopupMenu(this, anchor)
        popup.menuInflater.inflate(R.menu.main_menu, popup.menu)
        popup.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_farm -> {
                    if (currentCategory != CATEGORY_FARM) {
                        openCategory(CATEGORY_FARM)
                    }
                    true
                }
                R.id.menu_wild -> {
                    if (currentCategory != CATEGORY_WILD) {
                        openCategory(CATEGORY_WILD)
                    }
                    true
                }
                R.id.menu_birds -> {
                    if (currentCategory != CATEGORY_BIRDS) {
                        openCategory(CATEGORY_BIRDS)
                    }
                    true
                }
                else -> false
            }
        }
        popup.show()
    }

    private fun openCategory(category: String) {
        currentCategory = category
        setupCategory(category)
    }

    private fun setupCategory(category: String) {
        val (title, background, animalNames, animalImages) = when (category) {
            CATEGORY_FARM -> CategoryData(
                title = "Домашние",
                background = R.drawable.farm,
                animalNames = listOf("Cow", "Horse", "Cat", "Dog", "Chicken", "Duck"),
                animalImages = listOf(
                    R.drawable.cow,
                    R.drawable.horse,
                    R.drawable.cat,
                    R.drawable.dog,
                    R.drawable.chicken,
                    R.drawable.duck
                )
            )
            CATEGORY_WILD -> CategoryData(
                title = "Дикие",
                background = R.drawable.wild,
                animalNames = listOf("Lion", "Elephant"),
                animalImages = listOf(
                    R.drawable.lion,
                    R.drawable.elephant
                )
            )
            CATEGORY_BIRDS -> CategoryData(
                title = "Птицы",
                background = R.drawable.birds,
                animalNames = listOf("Bird", "Eagle", "Owl"),
                animalImages = listOf(
                    R.drawable.bird,
                    R.drawable.eagle,
                    R.drawable.owl
                )
            )
            else -> CategoryData(
                title = "Животные",
                background = R.drawable.farm,
                animalNames = emptyList(),
                animalImages = emptyList()
            )
        }

        toolbar.title = title
        backgroundImage.setImageResource(background)

        gridView.adapter = AnimalAdapter(
            animalNames = animalNames,
            animalImages = animalImages,
            context = this
        )

        gridView.setOnItemClickListener { _, _, position, _ ->
            playAnimalSound(animalNames[position])
        }
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

    private data class CategoryData(
        val title: String,
        val background: Int,
        val animalNames: List<String>,
        val animalImages: List<Int>
    )
}
