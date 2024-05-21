package com.example.finalprojectandroid

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage

class FavoriteAdapter(
    private val context: Context,
    private val favoriteList: MutableList<DishesData>,
    private val onFavoriteRemoved: (DishesData) -> Unit
) : RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_favorite, parent, false)
        return FavoriteViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        val dish = favoriteList[position]
        holder.dishName.text = dish.dishName
        holder.dishPrice.text = "${dish.price}$"

        val storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(dish.photoUrl.toString())
        storageReference.downloadUrl.addOnSuccessListener { uri ->
            Glide.with(context)
                .load(uri.toString())
                .into(holder.dishImage)
        }.addOnFailureListener {
        }

        holder.removeFavoriteButton.setOnClickListener {
            favoriteList.removeAt(position)
            notifyItemRemoved(position)
            onFavoriteRemoved(dish)
        }

        holder.itemView.setOnClickListener {
            val activity = it.context as FragmentActivity
            val dialogFragment = DishDetailsDialog.newInstance(dish)
            dialogFragment.show(activity.supportFragmentManager, DishDetailsDialog.TAG)
        }
    }

    override fun getItemCount(): Int {
        return favoriteList.size
    }

    class FavoriteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dishImage: ImageView = itemView.findViewById(R.id.dishImage)
        val dishName: TextView = itemView.findViewById(R.id.dishName)
        val dishPrice: TextView = itemView.findViewById(R.id.dishPrice)
        val removeFavoriteButton: ImageButton = itemView.findViewById(R.id.removeFavoriteButton)
    }
}
