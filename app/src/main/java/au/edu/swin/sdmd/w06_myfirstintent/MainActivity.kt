package au.edu.swin.sdmd.w06_myfirstintent

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var rentalItems: List<RentalItem>
    private var currentIndex = 0

    // SharedPreferences keys
    private val PREFS_NAME = "RentalAppPrefs"
    private val CREDIT_KEY = "user_credit"
    private val BORROWED_ITEMS_KEY = "borrowed_items"
    private val TAG = "MainActivity"

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)

        // Reset data only if the activity is created fresh (not recreated)
        if (savedInstanceState == null) {
            resetData()
        }

        // Initialize rental items
        rentalItems = listOf(
            RentalItem(
                name = "Guitar",
                rating = 4.5f,
                attribute = "Acoustic",
                pricePerMonth = "$50",
                description = "A high-quality acoustic guitar suitable for beginners and professionals.",
                imageResId = R.drawable.guitar
            ),
            RentalItem(
                name = "Drum Kit",
                rating = 4.0f,
                attribute = "Electronic",
                pricePerMonth = "$80",
                description = "An electronic drum kit with multiple sound options and connectivity features.",
                imageResId = R.drawable.drum_kit
            ),
            RentalItem(
                name = "Keyboard",
                rating = 3.5f,
                attribute = "Digital",
                pricePerMonth = "$60",
                description = "A versatile digital keyboard with various instrument sounds and recording capabilities.",
                imageResId = R.drawable.keyboard
            )
        )

        val imageView: ImageView = findViewById(R.id.itemImage)
        val nameTextView: TextView = findViewById(R.id.itemName)
        val ratingTextView: TextView = findViewById(R.id.itemRating)
        val priceTextView: TextView = findViewById(R.id.itemPrice)
        val descriptionTextView: TextView = findViewById(R.id.itemDescription)
        val nextButton: Button = findViewById(R.id.nextButton)
        val borrowButton: Button = findViewById(R.id.borrowButton)

        fun updateUI() {
            val item = rentalItems[currentIndex]
            nameTextView.text = item.name
            ratingTextView.text = "Rating: ${item.rating}"
            priceTextView.text = "Price: ${item.pricePerMonth}"
            descriptionTextView.text = item.description
            imageView.setImageResource(item.imageResId)

            // Check if the item is borrowed
            val borrowedItems = sharedPreferences.getStringSet(BORROWED_ITEMS_KEY, mutableSetOf())
            val isBorrowed = borrowedItems?.contains(item.name) ?: false

            if (isBorrowed) {
                borrowButton.text = "Borrowed"
                borrowButton.isEnabled = false
            } else {
                borrowButton.text = "Borrow"
                borrowButton.isEnabled = true
            }

            Log.d(TAG, "Updated UI for item: ${item.name}, Borrowed: $isBorrowed")
        }

        updateUI()

        nextButton.setOnClickListener {
            currentIndex = (currentIndex + 1) % rentalItems.size
            Log.d(TAG, "Current index updated to: $currentIndex")
            updateUI()
        }

        borrowButton.setOnClickListener {
            val intent = Intent(this, DetailActivity::class.java).apply {
                putExtra("rentalItem", rentalItems[currentIndex])
            }
            Log.d(TAG, "Borrow button clicked for item: ${rentalItems[currentIndex].name}")
            startActivity(intent)
        }
    }

    private fun resetData() {
        // Reset user credit to $120
        sharedPreferences.edit().putFloat("user_credit", 120f).apply()
        // Clear borrowed items
        sharedPreferences.edit().putStringSet("borrowed_items", mutableSetOf<String>()).apply()
        Log.d(TAG, "Data reset: User credit set to $120, borrowed items cleared.")
    }
}
