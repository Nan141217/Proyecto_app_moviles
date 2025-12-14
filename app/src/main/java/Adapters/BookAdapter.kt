package Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.inicio.R
import Person.Book

class BookAdapter(
    private var bookList: MutableList<Book>,
    private val onEditClick: (Book) -> Unit,
    private val onDeleteClick: (Book) -> Unit
) : RecyclerView.Adapter<BookAdapter.BookViewHolder>() {

    class BookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvBookId: TextView = itemView.findViewById(R.id.tvBookId)
        val tvBookTitle: TextView = itemView.findViewById(R.id.tvBookTitle)
        val tvBookAuthor: TextView = itemView.findViewById(R.id.tvBookAuthor)
        val tvBookAvailability: TextView = itemView.findViewById(R.id.tvBookAvailability)
        val imgBookCover: ImageView = itemView.findViewById(R.id.imgBookCover)
        val btnEditBook: ImageButton = itemView.findViewById(R.id.btnEditBook)
        val btnDeleteBook: ImageButton = itemView.findViewById(R.id.btnDeleteBook)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_book, parent, false)
        return BookViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val book = bookList[position]

        holder.tvBookId.text = "ID: ${book.id}"
        holder.tvBookTitle.text = book.titulo
        holder.tvBookAuthor.text = "Author: ${book.autor?.nombre ?: "Unknown"}"

        if (book.disponible) {
            holder.tvBookAvailability.text = "Available"
            holder.tvBookAvailability.setTextColor(
                holder.itemView.context.getColor(android.R.color.holo_green_dark)
            )
        } else {
            holder.tvBookAvailability.text = "Not Available"
            holder.tvBookAvailability.setTextColor(
                holder.itemView.context.getColor(android.R.color.holo_red_dark)
            )
        }

        // Set book cover image
        if (book.coverImage != null) {
            holder.imgBookCover.setImageBitmap(book.coverImage)
            holder.imgBookCover.visibility = View.VISIBLE
        } else {
            holder.imgBookCover.setImageResource(android.R.drawable.ic_menu_gallery)
            holder.imgBookCover.visibility = View.VISIBLE
        }

        holder.btnEditBook.setOnClickListener { onEditClick(book) }
        holder.btnDeleteBook.setOnClickListener { onDeleteClick(book) }
    }

    override fun getItemCount(): Int = bookList.size

    fun updateList(newList: List<Book>) {
        bookList.clear()
        bookList.addAll(newList)
        notifyDataSetChanged()
    }
}
