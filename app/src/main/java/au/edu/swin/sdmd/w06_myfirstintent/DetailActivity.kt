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

    // Default duration for rental is 1 month
    private var duration: Int = 1
    private lateinit var rentalItem: RentalItem // RentalItem object, passed from the previous activity

    // SharedPreferences keys to store/retrieve user data
    private val PREFS_NAME = "RentalAppPrefs"
    private val CREDIT_KEY = "user_credit"  // Key to store user's credit balance
    private val BORROWED_ITEMS_KEY = "borrowed_items" // Key to store borrowed items
    private val TAG = "DetailActivity" // Tag for logging

    // Reference to SharedPreferences
    private lateinit var sharedPreferences: SharedPreferences

    // UI elements (Image, TextViews, Buttons, SeekBar)
    private lateinit var imageView: ImageView
    private lateinit var nameTextView: TextView
    private lateinit var ratingTextView: TextView
    private lateinit var priceTextView: TextView
    private lateinit var descriptionTextView: TextView
    private lateinit var durationSeekBar: SeekBar
    private lateinit var selectedDurationTextView: TextView
    private lateinit var saveButton: Button
    private lateinit var cancelButton: Button
    private lateinit var userCreditTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        // Initialize SharedPreferences to store user data
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)

        // Retrieve the RentalItem object passed via intent
        rentalItem = intent.getParcelableExtra("rentalItem")!!

        // Initialize UI components by finding views in the layout
        imageView = findViewById(R.id.detailImage)
        nameTextView = findViewById(R.id.detailName)
        ratingTextView = findViewById(R.id.detailRating)
        priceTextView = findViewById(R.id.detailPrice)
        descriptionTextView = findViewById(R.id.detailDescription)
        durationSeekBar = findViewById(R.id.durationSeekBar)
        selectedDurationTextView = findViewById(R.id.selectedDurationTextView)
        saveButton = findViewById(R.id.saveButton)
        cancelButton = findViewById(R.id.cancelButton)
        userCreditTextView = findViewById(R.id.userCreditTextView)

        // Set the rental item details in the UI
        nameTextView.text = rentalItem.name
        ratingTextView.text = "Rating: ${rentalItem.rating}"
        priceTextView.text = "Price: ${rentalItem.pricePerMonth}"
        descriptionTextView.text = rentalItem.description
        imageView.setImageResource(rentalItem.imageResId) // Load image resource

        // Display the current user credit balance
        displayUserCredit()

        // Initialize SeekBar for setting rental duration, starting from 1 month
        durationSeekBar.progress = duration
        selectedDurationTextView.text = "Duration: $duration month"

        // Handle SeekBar progress change
        durationSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                // Ensure the duration is at least 1 month
                duration = if (progress < 1) 1 else progress
                selectedDurationTextView.text = "Duration: $duration month${if (duration > 1) "s" else ""}"
                Log.d(TAG, "SeekBar progress changed: $duration")
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                // No action needed when SeekBar is first touched
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                // No action needed when SeekBar interaction stops
            }
        })

        // Handle the Save button click
        saveButton.setOnClickListener {
            // Calculate the cost for the selected rental duration
            val cost = calculateCost(duration)
            // Get the user's current credit from SharedPreferences (default is 120)
            val currentCredit = sharedPreferences.getFloat(CREDIT_KEY, 120f).toDouble()

            // Check if the user has enough credit to rent the item
            if (currentCredit >= cost) {
                // Deduct the cost from the user's credit balance
                val newCredit = currentCredit - cost
                sharedPreferences.edit().putFloat(CREDIT_KEY, newCredit.toFloat()).apply()

                // Add the rented item to the user's borrowed items set
                val borrowedItems = sharedPreferences.getStringSet(BORROWED_ITEMS_KEY, mutableSetOf())?.toMutableSet() ?: mutableSetOf()
                borrowedItems.add(rentalItem.name)
                sharedPreferences.edit().putStringSet(BORROWED_ITEMS_KEY, borrowedItems).apply()

                // Update the displayed credit balance
                userCreditTextView.text = "Your Credit: $$newCredit"

                // Show a toast message indicating success
                Toast.makeText(this, "Item borrowed for $duration month(s)", Toast.LENGTH_SHORT).show()
                Log.d(TAG, "Item borrowed: ${rentalItem.name}, Duration: $duration, Cost: $$cost, New Credit: $$newCredit")

                // Close the activity and return to the previous screen
                finish()
            } else {
                // Not enough credit to rent the item, show a warning
                Toast.makeText(this, "Not enough credit to borrow this item.", Toast.LENGTH_SHORT).show()
                Log.w(TAG, "Insufficient credit: $$currentCredit for cost: $$cost")
            }
        }

        // Handle the Cancel button click
        cancelButton.setOnClickListener {
            Log.d(TAG, "Cancel button clicked, returning to main.")
            finish() // Close the activity and return to the previous screen
        }
    }

    // Update user credit when returning to this activity
    override fun onResume() {
        super.onResume()
        displayUserCredit()
    }

    // Display the user's current credit balance
    private fun displayUserCredit() {
        val currentCredit = sharedPreferences.getFloat(CREDIT_KEY, 120f).toDouble()
        userCreditTextView.text = "Your Credit: $$currentCredit"
        Log.d(TAG, "Displayed user credit: $$currentCredit")
    }

    // Calculate the total rental cost based on the number of months
    private fun calculateCost(months: Int): Double {
        // Remove the "$" symbol from price and convert it to a double
        val pricePerMonth = rentalItem.pricePerMonth.replace("$", "").toDouble()
        return pricePerMonth * months
    }
}
