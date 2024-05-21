package com.example.finalprojectandroid

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class CartAdapter(private val cartList: MutableList<DishesData>, private val context: Context) :
    RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    private val gson = Gson()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cart, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val dish = cartList[position]
        holder.dishName.text = dish.dishName
        holder.dishPrice.text = "${dish.price}$"
        holder.quantityTextView.text = dish.quantity.toString()

        val storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(dish.photoUrl.toString())
        storageReference.downloadUrl.addOnSuccessListener { uri ->
            Glide.with(holder.itemView.context)
                .load(uri.toString())
                .into(holder.dishImage)
        }.addOnFailureListener {
            // Handle any errors
        }

        holder.removeCartButton.setOnClickListener {
            removeFromCart(dish)
            cartList.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, cartList.size)
        }
    }

    override fun getItemCount(): Int {
        return cartList.size
    }

    private fun saveCart() {

        val sharedPreferences = context.getSharedPreferences("cart", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val json = gson.toJson(cartList)
        editor.putString("cart_list", json)
        editor.apply()
    }

    private fun removeFromCart(dish: DishesData) {

        val sharedPreferences = context.getSharedPreferences("cart", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val type = object : TypeToken<MutableList<DishesData>>() {}.type

        val currentCart: MutableList<DishesData> = gson.fromJson(sharedPreferences.getString("cart_list", "[]"), type) ?: mutableListOf()
        currentCart.remove(dish)

        val json = gson.toJson(currentCart)
        editor.putString("cart_list", json)
        editor.apply()
    }

    class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dishImage: ImageView = itemView.findViewById(R.id.dishImage)
        val dishName: TextView = itemView.findViewById(R.id.dishName)
        val dishPrice: TextView = itemView.findViewById(R.id.dishPrice)
        val quantityTextView: TextView = itemView.findViewById(R.id.quantityTextView)
        val removeCartButton: ImageButton = itemView.findViewById(R.id.removeCartButton)
    }
}
