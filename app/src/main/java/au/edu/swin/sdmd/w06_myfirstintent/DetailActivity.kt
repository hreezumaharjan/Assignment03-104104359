package au.edu.swin.sdmd.w06_myfirstintent

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class DetailActivity : AppCompatActivity() {

    private var duration: Int = 1 // Default duration
    private lateinit var rentalItem: RentalItem

    // SharedPreferences keys
    private val PREFS_NAME = "RentalAppPrefs"
    private val CREDIT_KEY = "user_credit"
    private val BORROWED_ITEMS_KEY = "borrowed_items"
    private val TAG = "DetailActivity"

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)

        // Ensure "rentalItem" key is used in the intent
        rentalItem = intent.getParcelableExtra("rentalItem")!!

        // Initialize views
        val imageView: ImageView = findViewById(R.id.detailImage)
        val nameTextView: TextView = findViewById(R.id.detailName)
        val ratingTextView: TextView = findViewById(R.id.detailRating)
        val priceTextView: TextView = findViewById(R.id.detailPrice)
        val descriptionTextView: TextView = findViewById(R.id.detailDescription)
        val durationSeekBar: SeekBar = findViewById(R.id.durationSeekBar)
        val selectedDurationTextView: TextView = findViewById(R.id.selectedDurationTextView)
        val saveButton: Button = findViewById(R.id.saveButton)
        val cancelButton: Button = findViewById(R.id.cancelButton)
        val userCreditTextView: TextView = findViewById(R.id.userCreditTextView)

        // Set item details
        nameTextView.text = rentalItem.name
        ratingTextView.text = "Rating: ${rentalItem.rating}"
        priceTextView.text = "Price: ${rentalItem.pricePerMonth}"
        descriptionTextView.text = rentalItem.description
        imageView.setImageResource(rentalItem.imageResId)

        // Display user credit
        var currentCredit = sharedPreferences.getFloat(CREDIT_KEY, 120f).toDouble()
        userCreditTextView.text = "Your Credit: $$currentCredit"

        // Initialize SeekBar
        durationSeekBar.progress = duration
        selectedDurationTextView.text = "Duration: $duration month"

        durationSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                duration = if (progress < 1) 1 else progress // Ensure minimum duration is 1
                selectedDurationTextView.text = "Duration: $duration month${if (duration > 1) "s" else ""}"
                Log.d(TAG, "SeekBar progress changed: $duration")
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                // No action needed
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                // No action needed
            }
        })

        saveButton.setOnClickListener {
            val cost = calculateCost(duration)
            if (currentCredit >= cost) {
                // Update borrowed items
                val borrowedItems = sharedPreferences.getStringSet(BORROWED_ITEMS_KEY, mutableSetOf()) ?: mutableSetOf()
                borrowedItems.add(rentalItem.name)
                sharedPreferences.edit().putStringSet(BORROWED_ITEMS_KEY, borrowedItems).apply()


                Toast.makeText(this, "Item borrowed for $duration month(s)", Toast.LENGTH_SHORT).show()
                Log.d(TAG, "Item borrowed: ${rentalItem.name}, Duration: $duration, Cost: $$cost")
                finish() // Close DetailActivity
            } else {
                Toast.makeText(this, "Not enough credit to borrow this item.", Toast.LENGTH_SHORT).show()
                Log.w(TAG, "Insufficient credit: $$currentCredit for cost: $$cost")
            }
        }

        cancelButton.setOnClickListener {
            Log.d(TAG, "Cancel button clicked, returning to main.")
            finish() // Close DetailActivity
        }
    }

    private fun calculateCost(months: Int): Double {
        val pricePerMonth = rentalItem.pricePerMonth.replace("$", "").toDouble()
        return pricePerMonth * months
    }
}
