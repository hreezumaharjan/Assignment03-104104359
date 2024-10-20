package au.edu.swin.sdmd.w06_myfirstintent


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import android.util.Log


import androidx.appcompat.app.AppCompatActivity

class AddBookActivity : AppCompatActivity() {

    private lateinit var titleEditText: EditText
    private lateinit var authorEditText: EditText
    private lateinit var pagesEditText: EditText
    private lateinit var addButton: Button
    private val TAG = "AddBookActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_book)

        titleEditText = findViewById(R.id.edit_text_title)
        authorEditText = findViewById(R.id.edit_text_author)
        pagesEditText = findViewById(R.id.edit_text_pages)
        addButton = findViewById(R.id.button_add_book)

        addButton.setOnClickListener {
            val title = titleEditText.text.toString()
            val author = authorEditText.text.toString()
            val pages = pagesEditText.text.toString().toIntOrNull()

            Log.d(TAG, "Title: $title, Author: $author, Pages: $pages")

            if (title.isNotEmpty() && author.isNotEmpty() && pages != null) {
                Log.d(TAG, "Valid input. Adding book.")
                val resultIntent = Intent()
                resultIntent.putExtra("title", title)
                resultIntent.putExtra("author", author)
                resultIntent.putExtra("pages", pages)
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

