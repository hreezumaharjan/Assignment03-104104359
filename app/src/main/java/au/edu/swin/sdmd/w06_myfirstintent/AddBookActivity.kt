package au.edu.swin.sdmd.w06_myfirstintent

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.RatingBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AddBookActivity : AppCompatActivity() {

    private lateinit var titleEditText: EditText
    private lateinit var authorEditText: EditText
    private lateinit var pagesEditText: EditText
    private lateinit var ratingBar: RatingBar
    private lateinit var addButton: Button
    private val TAG = "AddBookActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_book)

        titleEditText = findViewById(R.id.edit_text_title)
        authorEditText = findViewById(R.id.edit_text_author)
        pagesEditText = findViewById(R.id.edit_text_pages)
        ratingBar = findViewById(R.id.rating_bar)
        addButton = findViewById(R.id.button_add_book)

        addButton.setOnClickListener {
            val title = titleEditText.text.toString()
            val author = authorEditText.text.toString()
            val pages = pagesEditText.text.toString().toIntOrNull()
            val rating = ratingBar.rating // Get the rating from the RatingBar

            Log.d(TAG, "Title: $title, Author: $author, Pages: $pages, Rating: $rating")

            if (title.isNotEmpty() && author.isNotEmpty() && pages != null) {
                Log.d(TAG, "Valid input. Adding book.")
                val resultIntent = Intent()
                resultIntent.putExtra("title", title)
                resultIntent.putExtra("author", author)
                resultIntent.putExtra("pages", pages)
                resultIntent.putExtra("rating", rating) // Pass the rating to the result
                resultIntent.putExtra("coverImageResId", R.drawable.default_cover) // Default cover image
                setResult(Activity.RESULT_OK, resultIntent)
                finish()

                // Show success toast
                Toast.makeText(this, "Book added successfully!", Toast.LENGTH_SHORT).show()
            } else {
                Log.e(TAG, "Invalid input. Please fill in all fields correctly.")
                // Show error toast if any field is empty
                Toast.makeText(this, "Please fill in all fields correctly.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
