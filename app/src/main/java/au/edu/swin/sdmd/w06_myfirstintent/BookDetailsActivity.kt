package au.edu.swin.sdmd.w06_myfirstintent

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class BookDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_detail)

        val title = intent.getStringExtra("title")
        val author = intent.getStringExtra("author")
        val pages = intent.getIntExtra("pages", 0)
        val rating = intent.getFloatExtra("rating", 0f)
        val coverImageResId = intent.getIntExtra("coverImageResId", R.drawable.default_cover)
        val description = intent.getStringExtra("description")
        val goBackButton: Button = findViewById(R.id.button_go_back)


        val coverImageView: ImageView = findViewById(R.id.image_view_cover_detail)
        val titleTextView: TextView = findViewById(R.id.text_view_title_detail)
        val authorTextView: TextView = findViewById(R.id.text_view_author_detail)
        val pagesTextView: TextView = findViewById(R.id.text_view_pages_detail)
        val descriptionTextView: TextView = findViewById(R.id.text_view_description_detail)
        val ratingBar: RatingBar = findViewById(R.id.rating_bar_detail)
        goBackButton.setOnClickListener {
            finish() // Finish the current activity and go back to the previous one
        }
        coverImageView.setImageResource(coverImageResId)
        titleTextView.text = title
        authorTextView.text = author
        pagesTextView.text = getString(R.string.book_pages, pages)
        descriptionTextView.text = description // Set the description
        ratingBar.rating = 4.0f
    }
}

