package com.example.finalprojectandroid

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class DishDetailsDialog : DialogFragment() {

    private lateinit var dish: DishesData
    private var quantity = 1
    private var isFavorite = false
    private val gson = Gson()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_dish_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dishImage: ImageView = view.findViewById(R.id.dishImage)
        val dishName: TextView = view.findViewById(R.id.dishName)
        val dishPrice: TextView = view.findViewById(R.id.dishPrice)
        val quantityTextView: TextView = view.findViewById(R.id.quantityTextView)
        val minusButton: Button = view.findViewById(R.id.minusButton)
        val plusButton: Button = view.findViewById(R.id.plusButton)
        val favoriteButton: ImageButton = view.findViewById(R.id.favoriteButton)
        val addToCartButton: Button = view.findViewById(R.id.addToCartButton)

        dishName.text = dish.dishName
        dishPrice.text = "${dish.price}$"

        val storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(dish.photoUrl.toString())
        storageReference.downloadUrl.addOnSuccessListener(OnSuccessListener<Uri> { uri ->
            Glide.with(requireContext())
                .load(uri.toString())
                .into(dishImage)
        }).addOnFailureListener(OnFailureListener {
        })

        minusButton.setOnClickListener {
            if (quantity > 1) {
                quantity--
                quantityTextView.text = quantity.toString()
            }
        }

        plusButton.setOnClickListener {
            quantity++
            quantityTextView.text = quantity.toString()
        }

        favoriteButton.setOnClickListener {
            isFavorite = !isFavorite
            if (isFavorite) {
                favoriteButton.setImageResource(R.drawable.heart_filled)
                addToFavorites(dish)
            } else {
                favoriteButton.setImageResource(R.drawable.heart_outline)
                removeFromFavorites(dish)
            }
        }

        addToCartButton.setOnClickListener {
            dish.quantity = quantity
            addToCart(dish)
        }

        checkIfFavorite(dish)
    }

    private fun checkIfFavorite(dish: DishesData) {

        val sharedPreferences = requireContext().getSharedPreferences("favorites", Context.MODE_PRIVATE)
        val type = object : TypeToken<MutableList<DishesData>>() {}.type
        val currentFavorites: MutableList<DishesData> = gson.fromJson(sharedPreferences.getString("favorites_list", "[]"), type) ?: mutableListOf()
        isFavorite = currentFavorites.contains(dish)
        val favoriteButton: ImageButton = view?.findViewById(R.id.favoriteButton) ?: return
        if (isFavorite) {
            favoriteButton.setImageResource(R.drawable.heart_filled)
        } else {
            favoriteButton.setImageResource(R.drawable.heart_outline)
        }
    }

    private fun addToFavorites(dish: DishesData) {

        val sharedPreferences = requireContext().getSharedPreferences("favorites", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val type = object : TypeToken<MutableList<DishesData>>() {}.type

        val currentFavorites: MutableList<DishesData> = gson.fromJson(sharedPreferences.getString("favorites_list", "[]"), type) ?: mutableListOf()
        if (!currentFavorites.contains(dish)) {
            currentFavorites.add(dish)
        }

        val json = gson.toJson(currentFavorites)
        editor.putString("favorites_list", json)
        editor.apply()
    }

    private fun removeFromFavorites(dish: DishesData) {

        val sharedPreferences = requireContext().getSharedPreferences("favorites", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val type = object : TypeToken<MutableList<DishesData>>() {}.type

        val currentFavorites: MutableList<DishesData> = gson.fromJson(sharedPreferences.getString("favorites_list", "[]"), type) ?: mutableListOf()
        currentFavorites.remove(dish)

        val json = gson.toJson(currentFavorites)
        editor.putString("favorites_list", json)
        editor.apply()
    }

    private fun addToCart(dish: DishesData) {

        val sharedPreferences = requireContext().getSharedPreferences("cart", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val type = object : TypeToken<MutableList<DishesData>>() {}.type

        val currentCart: MutableList<DishesData> = gson.fromJson(sharedPreferences.getString("cart_list", "[]"), type) ?: mutableListOf()
        val existingDish = currentCart.find { it.dishName == dish.dishName }
        if (existingDish != null) {
            existingDish.quantity += dish.quantity
        } else {
            currentCart.add(dish)
        }
        val json = gson.toJson(currentCart)
        editor.putString("cart_list", json)
        editor.apply()
        Toast.makeText(requireContext(), "Added to cart successfully!", Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val TAG = "DishDetailsDialogFragment"

        fun newInstance(dish: DishesData): DishDetailsDialog {
            val fragment = DishDetailsDialog()
            fragment.dish = dish
            return fragment
        }
    }
}
