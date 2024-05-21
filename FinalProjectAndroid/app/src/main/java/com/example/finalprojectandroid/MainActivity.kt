package com.example.finalprojectandroid

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.finalprojectandroid.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
class MainActivity : AppCompatActivity() {
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseDatabase =
            FirebaseDatabase.getInstance("https://finalprojectandroid-1d2d1-default-rtdb.europe-west1.firebasedatabase.app/")
        databaseReference = firebaseDatabase.reference.child("users")

        val user = intent.getStringExtra("userId")

        GetUserFirstName(user)
        bottomNavigationView = findViewById(R.id.bottom_navigation)

        bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.bottom_home -> {
                    replaceFragment(HomeFragment())
                    true
                }

                R.id.bottom_list -> {

                    replaceFragment(MapsFragment())
                    true
                }

                R.id.bottom_favorite -> {
                    replaceFragment(FavoriteFragment())
                    true
                }

                R.id.bottom_cart -> {
                    replaceFragment(CartFragment())
                    true
                }

                else -> false
            }
        }
        replaceFragment(HomeFragment())
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit()
    }

    private fun GetUserFirstName(userID: String?) {
        val userRef = databaseReference.child(userID!!)
        userRef.addValueEventListener(object : ValueEventListener {
            @SuppressLint("SetTextI18n")
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val userData = dataSnapshot.getValue(UserData::class.java)
                    if (userData != null) {
                        binding.Username.text =
                            "Welcome Back " + userData.firstname + " " + userData.lastname + " !"
                    } else {
                        Log.d("MainActivity", "User data not found for ID: $userID")
                    }
                } else {
                    Log.d("MainActivity", "No user data found in database")
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(
                    this@MainActivity,
                    "Database Error ${databaseError.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}
/*AIzaSyBwB0AUf7EptvspIfqbJHH3mabNzyW78zQ  : API KEY*/




