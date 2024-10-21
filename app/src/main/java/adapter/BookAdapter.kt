package au.edu.swin.sdmd.w06_myfirstintent

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.content.Intent



class BookAdapter(
    private var bookList: MutableList<Book>,
    private val onItemRemove: (Int) -> Unit
) : RecyclerView.Adapter<BookAdapter.BookViewHolder>() {

    class BookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val coverImageView: ImageView = itemView.findViewById(R.id.image_view_cover)
        val titleTextView: TextView = itemView.findViewById(R.id.text_view_title)
        val authorTextView: TextView = itemView.findViewById(R.id.text_view_author)
        val pagesTextView: TextView = itemView.findViewById(R.id.text_view_pages)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_book, parent, false)
        return BookViewHolder(view)
    }
    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val book = bookList[position]
        holder.titleTextView.text = book.title
        holder.authorTextView.text = book.author
        holder.pagesTextView.text = book.pages.toString()
        holder.coverImageView.setImageResource(book.coverImageResId)

        // Set a long click listener to remove the item
        holder.itemView.setOnLongClickListener {
            onItemRemove(position)
            true
        }

        // Set a click listener to open the BookDetailsActivity
        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, BookDetailsActivity::class.java).apply {
                putExtra("title", book.title)
                putExtra("author", book.author)
                putExtra("pages", book.pages)
                putExtra("coverImageResId", book.coverImageResId)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount() = bookList.size

    fun updateBooks(newBooks: MutableList<Book>) {
        bookList = newBooks
        notifyDataSetChanged()
    }
}
