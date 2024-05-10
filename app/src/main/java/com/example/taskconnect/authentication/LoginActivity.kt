package com.example.taskconnect.authentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.taskconnect.MainActivity
import com.example.taskconnect.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private val binding:ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }
    private lateinit var auth:FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.goToSignup.setOnClickListener {
            startActivity(Intent(this@LoginActivity,SignupActivity::class.java))
        }

        auth = FirebaseAuth.getInstance()

        binding.btnSignIn.setOnClickListener {

            val email = binding.email.text.toString()
            val password = binding.password.text.toString()

            if (email.isEmpty() || password.isEmpty()){
                Toast.makeText(this, "Please fill the required fields!", Toast.LENGTH_SHORT).show()
            }
            else{
                auth.signInWithEmailAndPassword(email,password)
                    .addOnCompleteListener { task->
                        if (task.isSuccessful){
                            Toast.makeText(this, "You Logged in Successfully\n${auth.currentUser?.email}", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this@LoginActivity,MainActivity::class.java))
                            finish()
                        }
                        else{
                            Toast.makeText(this, "Something went wrong!!", Toast.LENGTH_SHORT).show()
                        }
                    }
            }

        }









    }
}