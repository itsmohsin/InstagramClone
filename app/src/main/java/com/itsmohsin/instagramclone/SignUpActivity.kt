package com.itsmohsin.instagramclone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.itsmohsin.instagramclone.Models.User
import com.itsmohsin.instagramclone.Utils.USER_NODE
import com.itsmohsin.instagramclone.Utils.USER_PROFILE_FOLDER
import com.itsmohsin.instagramclone.Utils.uploadImage
import com.itsmohsin.instagramclone.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {
    val binding by lazy {
        ActivitySignUpBinding.inflate(layoutInflater)
    }

    lateinit var user: User
    private val launcher = registerForActivityResult(ActivityResultContracts.GetContent()){
        uri->
        uri?.let {
            uploadImage(uri, USER_PROFILE_FOLDER){
                if(it==null){

                }else{
                    user.image=it
                    binding.profileImage.setImageURI(uri)
                }
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        user = User()

        binding.btnSignUp.setOnClickListener {
            if (binding.tfName.editText?.text.toString().equals("") or
                binding.tfEmail.editText?.text.toString().equals("") or
                binding.tfPassword.editText?.text.toString().equals("")
            ) {
                Toast.makeText(this, "Please fill required information", Toast.LENGTH_SHORT).show()
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
                                startActivity(Intent(this@SignUpActivity, HomeActivity::class.java))
                                finish()
//                                Toast.makeText(
//                                    this@SignUpActivity,
//                                    "Login Successfully",
//                                    Toast.LENGTH_SHORT
//                                ).show()
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
        binding.addImage.setOnClickListener {
            launcher.launch("image/*")
        }
        binding.tvLogin.setOnClickListener {
            startActivity(Intent(this@SignUpActivity,LoginActivity::class.java))
            finish()
        }
    }
}