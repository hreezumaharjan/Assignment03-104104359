package au.edu.swin.sdmd.w06_myfirstintent

// MainActivity.kt
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var rentalItems: List<RentalItem>
    private var currentIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize rental items
        rentalItems = listOf(
            RentalItem("Guitar", 4.5f, "Acoustic", "$50"),
            RentalItem("Drum Kit", 4.0f, "Electronic", "$80"),
            RentalItem("Keyboard", 3.5f, "Digital", "$60")
        )

        val imageView: ImageView = findViewById(R.id.itemImage)
        val nameTextView: TextView = findViewById(R.id.itemName)
        val ratingTextView: TextView = findViewById(R.id.itemRating)
        val priceTextView: TextView = findViewById(R.id.itemPrice)
        val nextButton: Button = findViewById(R.id.nextButton)
        val borrowButton: Button = findViewById(R.id.borrowButton)

        fun updateUI() {
            val item = rentalItems[currentIndex]
            nameTextView.text = item.name
            ratingTextView.text = item.rating.toString()
            priceTextView.text = item.pricePerMonth
            // Set the image resource based on the item (example: imageView.setImageResource(R.drawable.item_image))
        }

        updateUI()

        nextButton.setOnClickListener {
            currentIndex = (currentIndex + 1) % rentalItems.size
            updateUI()
        }

        borrowButton.setOnClickListener {
            val intent = Intent(this, DetailActivity::class.java).apply {
                putExtra("rentalItem", rentalItems[currentIndex])
            }
            startActivity(intent)
        }
    }
}
