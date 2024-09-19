package au.edu.swin.sdmd.w06_myfirstintent

// DetailActivity.kt
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.lang.NumberFormatException

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        // Ensure "rentalItem" key is used in the intent
        val item: RentalItem? = intent.getParcelableExtra("rentalItem")

        if (item == null) {
            Toast.makeText(this, "Error: No rental item data found", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        val imageView: ImageView = findViewById(R.id.detailImage)
        val nameTextView: TextView = findViewById(R.id.detailName)
        val ratingTextView: TextView = findViewById(R.id.detailRating)
        val priceTextView: TextView = findViewById(R.id.detailPrice)
        val durationEditText: EditText = findViewById(R.id.durationEditText)
        val saveButton: Button = findViewById(R.id.saveButton)
        val cancelButton: Button = findViewById(R.id.cancelButton)

        nameTextView.text = item.name
        ratingTextView.text = item.rating.toString()
        priceTextView.text = item.pricePerMonth

        saveButton.setOnClickListener {
            val durationString = durationEditText.text.toString()
            try {
                val duration = durationString.toInt()
                val price = item.pricePerMonth.substring(1).toDouble() // Handle decimal prices
                val totalCost = duration * price

                if (totalCost > 100) { // Assuming 100 is the credit limit
                    Toast.makeText(this, "Credit limit exceeded", Toast.LENGTH_SHORT).show()
                } else {
                    // Handle saving logic here
                    Toast.makeText(this, "Booking confirmed for $duration months", Toast.LENGTH_SHORT).show()
                    finish() // Return to the main activity
                }
            } catch (e: NumberFormatException) {
                Toast.makeText(this, "Invalid duration input", Toast.LENGTH_SHORT).show()
            }
        }

        cancelButton.setOnClickListener {
            finish() // Return to the main activity
        }
    }
}
