package com.itsmohsin.instagramclone.Post

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.itsmohsin.instagramclone.HomeActivity
import com.itsmohsin.instagramclone.Models.Post
import com.itsmohsin.instagramclone.Utils.POST
import com.itsmohsin.instagramclone.Utils.POST_FOLDER
import com.itsmohsin.instagramclone.Utils.uploadImage
import com.itsmohsin.instagramclone.databinding.ActivityPostBinding


class PostActivity : AppCompatActivity() {
    val binding by lazy {
        ActivityPostBinding.inflate(layoutInflater)
    }
    var imageUrl: String? = null
    private val launcher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            uploadImage(uri, POST_FOLDER) {
                url ->
                if (url != null) {
                    binding.ivSelectImage.setImageURI(uri)
                    imageUrl = url
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setSupportActionBar(binding.materialToolbar)
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar()?.setDisplayShowHomeEnabled(true);
        binding.materialToolbar.setNavigationOnClickListener {
            finish()
        }

        binding.ivSelectImage.setOnClickListener {
            launcher.launch("image/*")
        }

        binding.btnPost.setOnClickListener {
            val post: Post = Post(imageUrl!!, binding.tfCaption.editText?.text.toString())

            Firebase.firestore.collection(POST).document().set(post).addOnSuccessListener {
                Firebase.firestore.collection(Firebase.auth.currentUser!!.uid).document().set(post).addOnSuccessListener {
                    startActivity(Intent(this@PostActivity, HomeActivity::class.java))
                    finish()
                }
            }
        }
    }
}