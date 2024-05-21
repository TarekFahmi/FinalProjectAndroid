package com.example.finalprojectandroid

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class InvoiceDialog(private val cartList: List<DishesData>) : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_invoice, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
        val totalAmountTextView: TextView = view.findViewById(R.id.totalAmountTextView)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = InvoiceAdapter(cartList)

        val totalAmount = cartList.sumOf { it.price!! * it.quantity }
        totalAmountTextView.text = "Total Amount: $$totalAmount"
    }
}

class InvoiceAdapter(private val cartList: List<DishesData>) :
    RecyclerView.Adapter<InvoiceAdapter.InvoiceViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InvoiceViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_invoice, parent, false)
        return InvoiceViewHolder(view)
    }

    override fun onBindViewHolder(holder: InvoiceViewHolder, position: Int) {
        val dish = cartList[position]
        holder.dishName.text = dish.dishName
        holder.dishQuantity.text = "Quantity: ${dish.quantity}"
        holder.dishPrice.text = "Price: $${dish.price!! * dish.quantity}"
    }

    override fun getItemCount(): Int {
        return cartList.size
    }

    class InvoiceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dishName: TextView = itemView.findViewById(R.id.dishName)
        val dishQuantity: TextView = itemView.findViewById(R.id.dishQuantity)
        val dishPrice: TextView = itemView.findViewById(R.id.dishPrice)
    }
}
