package com.example.finalprojectandroid

import android.annotation.SuppressLint
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.storage.FirebaseStorage

class MyAdapter(private var foodList: ArrayList<DishesData>) : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.food_list, parent, false)
        return MyViewHolder(itemView)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = foodList[position]
        holder.foodTV.text = currentItem.dishName
        holder.priceTV.text = "${currentItem.price}$"

        val storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(currentItem.photoUrl.toString())
        storageReference.downloadUrl.addOnSuccessListener(OnSuccessListener<Uri> { uri ->
            Glide.with(holder.itemView.context)
                .load(uri.toString())
                .into(holder.image)
        }).addOnFailureListener(OnFailureListener {
            // Handle any errors
        })

        // Apply animation
        val animation = AnimationUtils.loadAnimation(holder.itemView.context, R.anim.animation)
        holder.itemView.startAnimation(animation)

        // Show the dialog when an item is clicked
        holder.itemView.setOnClickListener {
            val activity = it.context as FragmentActivity
            val dialogFragment = DishDetailsDialog.newInstance(currentItem)
            dialogFragment.show(activity.supportFragmentManager, DishDetailsDialog.TAG)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newList: ArrayList<DishesData>) {
        foodList = newList
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return foodList.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val foodTV: TextView = itemView.findViewById(R.id.foodTV)
        val priceTV: TextView = itemView.findViewById(R.id.priceTV)
        val image: ImageView = itemView.findViewById(R.id.imageV)
    }
}

