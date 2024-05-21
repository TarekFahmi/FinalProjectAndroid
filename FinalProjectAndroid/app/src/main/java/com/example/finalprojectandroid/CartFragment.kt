package com.example.finalprojectandroid

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class CartFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var cartAdapter: CartAdapter
    private lateinit var cartList: MutableList<DishesData>
    private val gson = Gson()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_cart, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerView)
        val payButton: Button = view.findViewById(R.id.payButton)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        cartList = loadCartItems()
        cartAdapter = CartAdapter(cartList,requireContext())
        recyclerView.adapter = cartAdapter

        payButton.setOnClickListener {
            val invoiceDialog = InvoiceDialog(cartList)
            invoiceDialog.show(parentFragmentManager, "InvoiceDialog")
        }
    }

    private fun loadCartItems(): MutableList<DishesData> {
        val sharedPreferences = requireContext().getSharedPreferences("cart", Context.MODE_PRIVATE)
        val type = object : TypeToken<MutableList<DishesData>>() {}.type
        return gson.fromJson(sharedPreferences.getString("cart_list", "[]"), type) ?: mutableListOf()
    }
}
