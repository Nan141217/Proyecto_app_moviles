package Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.inicio.R
import Person.Author

class AuthorAdapter(
    private var authorList: MutableList<Author>,
    private val onEditClick: (Author) -> Unit,
    private val onDeleteClick: (Author) -> Unit
) : RecyclerView.Adapter<AuthorAdapter.AuthorViewHolder>() {

    class AuthorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvAuthorId: TextView = itemView.findViewById(R.id.tvAuthorId)
        val tvAuthorName: TextView = itemView.findViewById(R.id.tvAuthorName)
        val btnEditAuthor: ImageButton = itemView.findViewById(R.id.btnEditAuthor)
        val btnDeleteAuthor: ImageButton = itemView.findViewById(R.id.btnDeleteAuthor)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AuthorViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_author, parent, false)
        return AuthorViewHolder(view)
    }

    override fun onBindViewHolder(holder: AuthorViewHolder, position: Int) {
        val author = authorList[position]

        holder.tvAuthorId.text = "ID: ${author.id}"
        holder.tvAuthorName.text = author.nombre

        holder.btnEditAuthor.setOnClickListener { onEditClick(author) }
        holder.btnDeleteAuthor.setOnClickListener { onDeleteClick(author) }
    }

    override fun getItemCount(): Int = authorList.size

    fun updateList(newList: List<Author>) {
        authorList.clear()
        authorList.addAll(newList)
        notifyDataSetChanged()
    }
}
