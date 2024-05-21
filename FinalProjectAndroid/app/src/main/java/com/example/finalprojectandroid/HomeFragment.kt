package com.example.finalprojectandroid

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class HomeFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var foodList: ArrayList<DishesData>
    private lateinit var adapter: MyAdapter
    private val db = FirebaseFirestore.getInstance()
    private lateinit var categoryButtons: List<Button>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.foodList)
        recyclerView.layoutManager = GridLayoutManager(context, 2)
        recyclerView.setHasFixedSize(true)

        foodList = arrayListOf()
        adapter = MyAdapter(foodList)
        recyclerView.adapter = adapter

        categoryButtons = listOf(
            view.findViewById(R.id.newButton),
            view.findViewById(R.id.freshButton),
            view.findViewById(R.id.meatButton),
            view.findViewById(R.id.seafoodButton),
            view.findViewById(R.id.vegetableButton),
            view.findViewById(R.id.bakeryButton)
        )

        for (button in categoryButtons) {
            button.setOnClickListener {
                val category = button.text.toString()
                filterByCategory(category)
            }
        }

        fetchFoodData()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun fetchFoodData() {
        db.collection("Dishes")
            .get()
            .addOnSuccessListener { result ->
                foodList.clear()
                for (document in result) {
                    val dish = document.toObject(DishesData::class.java)
                    foodList.add(dish)
                }
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener { e ->
                Log.e("HomeFragment", "Error loading dishes", e)
            }
    }

    private fun filterByCategory(category: String) {
        val filteredList = foodList.filter { it.categoryName == category }
        adapter.updateData(ArrayList(filteredList))
    }
}