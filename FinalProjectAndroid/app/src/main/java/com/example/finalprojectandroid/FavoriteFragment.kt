package com.example.finalprojectandroid

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class FavoriteFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var favoriteAdapter: FavoriteAdapter
    private lateinit var favoriteList: MutableList<DishesData>
    private val gson = Gson()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_favorite, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        favoriteList = loadFavorites()
        favoriteAdapter = FavoriteAdapter(requireContext(), favoriteList) { dish ->
            removeFromFavorites(dish)
        }
        recyclerView.adapter = favoriteAdapter
    }

    private fun loadFavorites(): MutableList<DishesData> {

        val sharedPreferences = requireContext().getSharedPreferences("favorites", Context.MODE_PRIVATE)
        val type = object : TypeToken<MutableList<DishesData>>() {}.type
        return gson.fromJson(sharedPreferences.getString("favorites_list", "[]"), type) ?: mutableListOf()
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
}
