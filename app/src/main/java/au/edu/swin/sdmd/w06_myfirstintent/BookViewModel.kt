
package au.edu.swin.sdmd.w06_myfirstintent

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class BookViewModel : ViewModel() {
    private val _bookList = MutableLiveData<MutableList<Book>>(mutableListOf())
    val bookList: LiveData<MutableList<Book>> get() = _bookList

    fun addBook(book: Book) {
        _bookList.value?.add(book)
        _bookList.value = _bookList.value // Trigger LiveData update
    }

    fun updateBook(position: Int, book: Book) {
        _bookList.value?.set(position, book)
        _bookList.value = _bookList.value // Trigger LiveData update
    }

    fun removeBook(position: Int) {
        _bookList.value?.removeAt(position)
        _bookList.value = _bookList.value // Trigger LiveData update
    }
}
