package com.example.taskconnect.authentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.taskconnect.databinding.ActivitySignupBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.userProfileChangeRequest
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SignupActivity : AppCompatActivity() {
    private val binding:ActivitySignupBinding by lazy {
        ActivitySignupBinding.inflate(layoutInflater)
    }
    private lateinit var auth:FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val db = Firebase.firestore

        binding.goToLogin.setOnClickListener {
            startActivity(Intent(this@SignupActivity,LoginActivity::class.java))
            finish()
        }

        auth = FirebaseAuth.getInstance()


        binding.btnSignup.setOnClickListener {

            val name = binding.name.text.toString()
            val email = binding.email.text.toString()
            val status = binding.status.text.toString()
            val password = binding.password.text.toString()
            val repeatPassword = binding.repeatPassword.text.toString()

            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || repeatPassword.isEmpty()){
                Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show()
            }
            else if (password != repeatPassword){
                Toast.makeText(this, "Passwords are not same", Toast.LENGTH_SHORT).show()
            }
            else{

                val user = hashMapOf(
                    "name" to name,
                    "status" to status
                )

                db.collection("users")
                    .add(user)
                    .addOnSuccessListener {documentReference->
                        Toast.makeText(this, "Added to db collection ${documentReference.id}", Toast.LENGTH_SHORT).show()
                        Log.e("signup activity","Added to db collection ${documentReference.id}")
                    }
                    .addOnFailureListener {e->
                        Toast.makeText(this, "Failed to add in db!!${e.message}", Toast.LENGTH_SHORT).show()
                        Log.e("signup activity","Failed to add in db!!${e.message}")
                    }

                auth.createUserWithEmailAndPassword(email,password)
                    .addOnCompleteListener {task->
                        if (task.isSuccessful){
                            val user = auth.currentUser
                            val userProfile = userProfileChangeRequest {
                                displayName = name
                            }
                            user?.updateProfile(userProfile)?.addOnCompleteListener {
                                if (it.isSuccessful){
                                    Toast.makeText(applicationContext,"Profile updated with Name",Toast.LENGTH_LONG).show()
                                }
                            }
                            Toast.makeText(this, "Account created successfully!", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this@SignupActivity,LoginActivity::class.java))
                            finish()
                        }
                    }
            }
        }

    }
}