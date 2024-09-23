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

    // List of rental items available for borrowing
    private lateinit var rentalItems: List<RentalItem>
    private var currentIndex = 0 // Tracks the currently displayed rental item

    // SharedPreferences keys for storing user data
    private val PREFS_NAME = "RentalAppPrefs"
    private val CREDIT_KEY = "user_credit" // Key to store user's credit balance
    private val BORROWED_ITEMS_KEY = "borrowed_items" // Key to store names of borrowed items
    private val TAG = "MainActivity" // Tag for logging purposes

    // Reference to SharedPreferences for persistent storage
    private lateinit var sharedPreferences: SharedPreferences

    // UI Elements: TextViews, ImageView, and Buttons
    private lateinit var userCreditMainTextView: TextView
    private lateinit var imageView: ImageView
    private lateinit var nameTextView: TextView
    private lateinit var ratingTextView: TextView
    private lateinit var priceTextView: TextView
    private lateinit var descriptionTextView: TextView
    private lateinit var nextButton: Button
    private lateinit var borrowButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main) // Sets the layout for this activity

        // Initialize SharedPreferences for storing and retrieving user data
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)

        // Reset user data only if the activity is created for the first time
        if (savedInstanceState == null) {
            resetData()
        }

        // Initialize the list of rental items with predefined data
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

        // Initialize UI components by linking them with their corresponding views in the layout
        imageView = findViewById(R.id.itemImage)
        nameTextView = findViewById(R.id.itemName)
        ratingTextView = findViewById(R.id.itemRating)
        priceTextView = findViewById(R.id.itemPrice)
        descriptionTextView = findViewById(R.id.itemDescription)
        nextButton = findViewById(R.id.nextButton)
        borrowButton = findViewById(R.id.borrowButton)
        userCreditMainTextView = findViewById(R.id.userCreditMainTextView)

        // Update the UI to display the first rental item
        updateUI()

        // Set up listener for the "Next" button to cycle through rental items
        nextButton.setOnClickListener {
            // Increment the current index and wrap around if it exceeds the list size
            currentIndex = (currentIndex + 1) % rentalItems.size
            Log.d(TAG, "Current index updated to: $currentIndex")
            updateUI() // Refresh the UI with the new rental item
        }

        // Set up listener for the "Borrow" button to initiate borrowing process
        borrowButton.setOnClickListener {
            // Create an Intent to navigate to DetailActivity, passing the selected rental item
            val intent = Intent(this, DetailActivity::class.java).apply {
                putExtra("rentalItem", rentalItems[currentIndex])
            }
            Log.d(TAG, "Borrow button clicked for item: ${rentalItems[currentIndex].name}")
            startActivity(intent) // Start DetailActivity
        }
    }

    override fun onResume() {
        super.onResume()
        displayUserCredit() // Update the displayed user credit when the activity resumes
        updateUI() // Refresh the UI in case any changes occurred
    }

    /**
     * Updates the UI elements to display information about the current rental item.
     * Also checks if the item has already been borrowed and updates the borrow button accordingly.
     */
    private fun updateUI() {
        val item = rentalItems[currentIndex] // Get the current rental item
        nameTextView.text = item.name
        ratingTextView.text = "Rating: ${item.rating}"
        priceTextView.text = "Price: ${item.pricePerMonth}"
        descriptionTextView.text = item.description
        imageView.setImageResource(item.imageResId) // Set the image for the rental item

        // Retrieve the set of borrowed items from SharedPreferences
        val borrowedItems = sharedPreferences.getStringSet(BORROWED_ITEMS_KEY, mutableSetOf())
        // Check if the current item is already borrowed
        val isBorrowed = borrowedItems?.contains(item.name) ?: false

        if (isBorrowed) {
            borrowButton.text = "Borrowed" // Update button text to indicate it's already borrowed
            borrowButton.isEnabled = false // Disable the button to prevent re-borrowing
        } else {
            borrowButton.text = "Borrow" // Set button text to "Borrow"
            borrowButton.isEnabled = true // Enable the button to allow borrowing
        }

        Log.d(TAG, "Updated UI for item: ${item.name}, Borrowed: $isBorrowed")
    }

    /**
     * Displays the user's current credit balance on the main screen.
     */
    private fun displayUserCredit() {
        val currentCredit = sharedPreferences.getFloat(CREDIT_KEY, 120f) // Retrieve credit, default is $120
        userCreditMainTextView.text = "Your Credit: $$currentCredit"
        Log.d(TAG, "Displayed user credit: $$currentCredit")
    }

    /**
     * Resets user data by setting the credit to $120 and clearing any borrowed items.
     * This method is called only when the activity is created fresh (not recreated).
     */
    private fun resetData() {
        // Reset user credit to $120
        sharedPreferences.edit().putFloat(CREDIT_KEY, 120f).apply()
        // Clear the set of borrowed items
        sharedPreferences.edit().putStringSet(BORROWED_ITEMS_KEY, mutableSetOf<String>()).apply()
        Log.d(TAG, "Data reset: User credit set to \$120, borrowed items cleared.")
    }
}
