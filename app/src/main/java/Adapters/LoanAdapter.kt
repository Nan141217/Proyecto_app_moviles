package Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.inicio.R
import Person.Loan
import java.text.SimpleDateFormat
import java.util.Locale

class LoanAdapter(
    private var loanList: MutableList<Loan>,
    private val onEditClick: (Loan) -> Unit,
    private val onDeleteClick: (Loan) -> Unit
) : RecyclerView.Adapter<LoanAdapter.LoanViewHolder>() {

    class LoanViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvLoanId: TextView = itemView.findViewById(R.id.tvLoanId)
        val tvLoanBook: TextView = itemView.findViewById(R.id.tvLoanBook)
        val tvLoanBorrower: TextView = itemView.findViewById(R.id.tvLoanBorrower)
        val tvLoanDate: TextView = itemView.findViewById(R.id.tvLoanDate)
        val tvLoanStatus: TextView = itemView.findViewById(R.id.tvLoanStatus)
        val btnEditLoan: ImageButton = itemView.findViewById(R.id.btnEditLoan)
        val btnDeleteLoan: ImageButton = itemView.findViewById(R.id.btnDeleteLoan)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LoanViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_loan, parent, false)
        return LoanViewHolder(view)
    }

    override fun onBindViewHolder(holder: LoanViewHolder, position: Int) {
        val loan = loanList[position]
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

        holder.tvLoanId.text = "ID: ${loan.id}"
        holder.tvLoanBook.text = "Libro: ${loan.libro?.titulo ?: "Sin título"}"
        holder.tvLoanBorrower.text = "Prestatario: ${loan.prestatario}"
        holder.tvLoanDate.text = "Fecha: ${dateFormat.format(loan.fecha)}"

        if (loan.devuelto) {
            holder.tvLoanStatus.text = "Devuelto"
            holder.tvLoanStatus.setTextColor(
                holder.itemView.context.getColor(android.R.color.holo_green_dark)
            )
        } else {
            holder.tvLoanStatus.text = "Pendiente"
            holder.tvLoanStatus.setTextColor(
                holder.itemView.context.getColor(android.R.color.holo_orange_dark)
            )
        }

        holder.btnEditLoan.setOnClickListener { onEditClick(loan) }
        holder.btnDeleteLoan.setOnClickListener { onDeleteClick(loan) }
    }

    override fun getItemCount(): Int = loanList.size

    fun updateList(newList: List<Loan>) {
        loanList.clear()
        loanList.addAll(newList)
        notifyDataSetChanged()
    }
}
