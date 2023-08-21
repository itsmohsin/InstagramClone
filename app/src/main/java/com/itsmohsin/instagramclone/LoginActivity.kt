package com.itsmohsin.instagramclone

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.itsmohsin.instagramclone.Models.User
import com.itsmohsin.instagramclone.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.btnLogin.setOnClickListener {
            if (binding.tfEmail.editText?.text.toString().equals("") or
                binding.tfPassword.editText?.text.toString().equals("")
            ) {
                Toast.makeText(
                    this@LoginActivity,
                    "Please fill all the details",
                    Toast.LENGTH_SHORT
                ).show()

            }else{
                var user = User(
                    binding.tfEmail.editText?.text.toString(),
                    binding.tfPassword.editText?.text.toString()
                )
                Firebase.auth.signInWithEmailAndPassword(user.email!!, user.password!!)
                    .addOnCompleteListener {
                        if(it.isSuccessful){
                            startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
                            finish()
                        }else {
                            Toast.makeText(this@LoginActivity, it.exception?.localizedMessage, Toast.LENGTH_SHORT).show()
                        }
                    }

            }
        }
    }
}