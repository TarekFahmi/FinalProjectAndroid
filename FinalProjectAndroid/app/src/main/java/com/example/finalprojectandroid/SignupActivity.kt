package com.example.finalprojectandroid

import java.security.MessageDigest
import android.util.Base64
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.finalprojectandroid.databinding.ActivitySignupBinding
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.initialize

class SignupActivity : AppCompatActivity() {

    private  lateinit var binding: ActivitySignupBinding
    private lateinit var firebaseDatabase:FirebaseDatabase
    private lateinit var databaseReference:DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseDatabase=FirebaseDatabase.getInstance("https://finalprojectandroid-1d2d1-default-rtdb.europe-west1.firebasedatabase.app/")
        databaseReference=firebaseDatabase.reference.child("users")
        binding.SignUpButton.setOnClickListener{
            val signupFirstname = binding.signupFirstname.text.toString()
            val signupLastname = binding.signupLastname.text.toString()
            val signupEmail = binding.signupEmail.text.toString()
            val signupPassword= binding.signupPassword.text.toString()


            if(signupFirstname.isNotEmpty() && signupLastname.isNotEmpty()&& signupEmail.isNotEmpty() && signupPassword.isNotEmpty()){
                signupUser(signupFirstname,signupLastname,signupEmail,signupPassword)
            }else{

                Toast.makeText(this@SignupActivity,"All fields are mandatory" , Toast.LENGTH_SHORT).show()
            }
        }
        binding.loginRedirect.setOnClickListener{
            startActivity(Intent(this@SignupActivity , LoginActivity::class.java))
            finish()
        }
    }
    private fun hashPassword(password: String): String {
        val bytes = password.toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)
        return Base64.encodeToString(digest, Base64.DEFAULT)
    }

    private  fun signupUser(firstname:String , lastname:String , email:String , password:String){
        val hashedPassword = hashPassword(password)

        databaseReference.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                if(!dataSnapshot.exists()){
                    val id = databaseReference.push().key
                    val userData = UserData(id,firstname,lastname,email,hashedPassword)
                    databaseReference.child(id!!).setValue(userData)
                    Toast.makeText(this@SignupActivity,"Signup Successfully.",Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@SignupActivity,LoginActivity::class.java))
                    finish()

                }else{
                    Toast.makeText(this@SignupActivity,"User Already Exists" , Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(this@SignupActivity,"Database Error ${databaseError.message}" , Toast.LENGTH_SHORT).show()

            }
        })

    }
}