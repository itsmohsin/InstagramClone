package com.itsmohsin.instagramclone

import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.itsmohsin.instagramclone.Models.User
import com.itsmohsin.instagramclone.Utils.USER_NODE
import com.itsmohsin.instagramclone.Utils.USER_PROFILE_FOLDER
import com.itsmohsin.instagramclone.Utils.uploadImage
import com.itsmohsin.instagramclone.databinding.ActivitySignUpBinding
import com.squareup.picasso.Picasso


class SignUpActivity : AppCompatActivity() {
    val binding by lazy {
        ActivitySignUpBinding.inflate(layoutInflater)
    }

    lateinit var user: User
    private val launcher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            uploadImage(uri, USER_PROFILE_FOLDER) {
                if (it == null) {

                } else {
                    user.image = it
                    binding.profileImage.setImageURI(uri)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val text =
            "<font color=#FF000000>Have an account? </font> <font color=#1E88E5>Log in</font>"
        binding.tvLogin.setText(Html.fromHtml(text))
        user = User()
        if (intent.hasExtra("MODE")) {
            if (intent.getIntExtra("MODE", -1) == 1) {
                binding.btnSignUp.text = "Update Profile"
                Firebase.firestore.collection(USER_NODE).document(Firebase.auth.currentUser!!.uid)
                    .get()
                    .addOnSuccessListener {
                        user = it.toObject<User>()!!
                        if (!user.image.isNullOrEmpty()) {
                            Picasso.get().load(user.image).into(binding.profileImage)
                        }
                        binding.tfName.editText?.setText(user.name)
                        binding.tfEmail.editText?.setText(user.email)
                        binding.tfPassword.editText?.setText(user.password)
                    }
            }
        }

        binding.btnSignUp.setOnClickListener {
            if (intent.hasExtra("MODE")) {
                if (intent.getIntExtra("MODE", -1) == 1) {
                    Firebase.firestore.collection(USER_NODE)
                        .document(Firebase.auth.currentUser!!.uid).set(user)
                        .addOnSuccessListener {
                            startActivity(
                                Intent(
                                    this@SignUpActivity,
                                    HomeActivity::class.java
                                )
                            )
                            finish()
                        }
                }
            } else {
                if (binding.tfName.editText?.text.toString().equals("") or
                    binding.tfEmail.editText?.text.toString().equals("") or
                    binding.tfPassword.editText?.text.toString().equals("")
                ) {
                    Toast.makeText(this, "Please fill required information", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                        binding.tfEmail.editText?.text.toString(),
                        binding.tfName.editText?.text.toString(),
                    ).addOnCompleteListener { result ->

                        if (result.isSuccessful) {
                            user.name = binding.tfName.editText?.text.toString()
                            user.email = binding.tfEmail.editText?.text.toString()
                            user.password = binding.tfPassword.editText?.text.toString()
                            Firebase.firestore.collection(USER_NODE)
                                .document(Firebase.auth.currentUser!!.uid).set(user)
                                .addOnSuccessListener {
                                    startActivity(
                                        Intent(
                                            this@SignUpActivity,
                                            HomeActivity::class.java
                                        )
                                    )
                                    finish()
                                }
                        } else {
                            Toast.makeText(
                                this@SignUpActivity,
                                result.exception?.localizedMessage,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }
        binding.addImage.setOnClickListener {
            launcher.launch("image/*")
        }
        binding.tvLogin.setOnClickListener {
            startActivity(Intent(this@SignUpActivity, LoginActivity::class.java))
            finish()
        }
    }
}