package au.edu.swin.sdmd.w06_myfirstintent

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var bookAdapter: BookAdapter
    private var bookList = mutableListOf<Book>()
    private val TAG = "MainActivity"

    // Create a launcher for AddBookActivity to get the result
    private val addBookLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val data = result.data
            data?.let {
                val title = it.getStringExtra("title")
                val author = it.getStringExtra("author")
                val pages = it.getIntExtra("pages", 0)
                val rating = it.getFloatExtra("rating", 0f) // Retrieve the rating
                val newBook = Book(title ?: "", author ?: "", pages, rating, R.drawable.default_cover) // Include rating

                bookList.add(newBook)
                bookAdapter.notifyItemInserted(bookList.size - 1)

                Log.d(TAG, "Book added: $newBook with rating: $rating") // Log book addition
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize the default book list
        bookList = mutableListOf(
            Book("1984", "George Orwell", 328, 4.5f, R.drawable.cover_1984),
            Book("To Kill a Mockingbird", "Harper Lee", 281, 4.8f, R.drawable.cover_to_kill_a_mockingbird),
            Book("The Great Gatsby", "F. Scott Fitzgerald", 180, 4.2f, R.drawable.cover_great_gatsby),
            Book("Moby Dick", "Herman Melville", 585, 4.1f, R.drawable.cover_moby_dick),
            Book("The Catcher in the Rye", "J.D. Salinger", 214, 3.9f, R.drawable.cover_catcher_in_the_rye)
        )

        recyclerView = findViewById(R.id.recycler_view_books)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Pass a lambda to handle item removal and item clicks
        bookAdapter = BookAdapter(bookList) { position ->
            removeBookAtPosition(position)
        }
        recyclerView.adapter = bookAdapter

        // Add a FloatingActionButton to trigger AddBookActivity
        val addBookFab: FloatingActionButton = findViewById(R.id.fab_add_book)
        addBookFab.setOnClickListener {
            val intent = Intent(this, AddBookActivity::class.java)
            addBookLauncher.launch(intent)  // Use the launcher to start AddBookActivity
        }
    }

    // Function to remove a book from the list
    private fun removeBookAtPosition(position: Int) {
        if (position >= 0 && position < bookList.size) {
            val removedBook = bookList[position] // Get the book being removed
            bookList.removeAt(position)  // Remove the book from the list
            bookAdapter.notifyItemRemoved(position)  // Notify the adapter of the item removal

            Log.d(TAG, "Book removed: $removedBook") // Log book removal
        }
    }
}
