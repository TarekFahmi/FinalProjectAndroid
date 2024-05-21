package com.example.finalprojectandroid


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.view.View
import android.widget.Toast
import com.example.finalprojectandroid.databinding.ActivityLoginBinding
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.security.MessageDigest

class LoginActivity : AppCompatActivity() {

    private  lateinit var binding: ActivityLoginBinding
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        FirebaseApp.initializeApp(this)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseDatabase=FirebaseDatabase.getInstance("https://finalprojectandroid-1d2d1-default-rtdb.europe-west1.firebasedatabase.app/")
        databaseReference=firebaseDatabase.reference.child("users")

        binding.buttonLogin.setOnClickListener{
            val email = binding.signinEmail.text.toString()
            val password = binding.signinPassword.text.toString()
            binding.progressBar.visibility = View.VISIBLE
            if(email.isNotEmpty() && password.isNotEmpty()){
                loginUser(email,password)
            }else{
                binding.progressBar.visibility = View.INVISIBLE
                Toast.makeText(this@LoginActivity , "All fields are mandatory" ,Toast.LENGTH_SHORT).show()
            }
        }


        binding.SignupRedirect.setOnClickListener {

            startActivity(Intent(this@LoginActivity, SignupActivity::class.java))
            finish()

        }
    }

    private fun hashPassword(password: String): String {
        val bytes = password.toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)
        return Base64.encodeToString(digest, Base64.DEFAULT)
    }
    private fun loginUser(email: String, password: String) {
        val hashedPassword = hashPassword(password)
       databaseReference.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(object :ValueEventListener{
           override fun onDataChange(dataSnapshot: DataSnapshot) {

               if(dataSnapshot.exists()){
                   for(userSnapshot in dataSnapshot.children){
                       val userData = userSnapshot.getValue(UserData::class.java)

                       if(userData!= null && userData.password == hashedPassword){
                           Toast.makeText(this@LoginActivity,"Login Successfully." , Toast.LENGTH_SHORT).show()
                           val intent = Intent(this@LoginActivity, MainActivity::class.java)
                           intent.putExtra("userId",userData.id)
                           startActivity(intent)
                           finish()
                           return
                       }else{
                           Toast.makeText(this@LoginActivity,"Login Failed." , Toast.LENGTH_SHORT).show()
                           binding.progressBar.visibility = View.INVISIBLE


                       }


                   }
               }

           }

           override fun onCancelled(databaseError: DatabaseError) {
               Toast.makeText(this@LoginActivity,"Database Error ${databaseError.message}" , Toast.LENGTH_SHORT).show()

           }
       })
    }
}