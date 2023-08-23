package com.itsmohsin.instagramclone.Post

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.itsmohsin.instagramclone.HomeActivity
import com.itsmohsin.instagramclone.Models.Reel
import com.itsmohsin.instagramclone.Utils.REEL
import com.itsmohsin.instagramclone.Utils.REEL_FOLDER
import com.itsmohsin.instagramclone.Utils.uploadVideo
import com.itsmohsin.instagramclone.databinding.ActivityReelsBinding

class ReelActivity : AppCompatActivity() {
    val binding by lazy {
        ActivityReelsBinding.inflate(layoutInflater)
    }
    private lateinit var videoUrl: String
    lateinit var progressDialog: ProgressDialog
    private val launcher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            uploadVideo(uri, REEL_FOLDER, progressDialog) { url ->
                if (url != null) {
                    videoUrl = url
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        progressDialog = ProgressDialog(this)

        binding.ivSelectReel.setOnClickListener {
            launcher.launch("video/*")
        }

        binding.btnCancel.setOnClickListener {
            startActivity(Intent(this@ReelActivity, HomeActivity::class.java))
            finish()
        }

        binding.btnPost.setOnClickListener {
            val reel: Reel = Reel(videoUrl!!, binding.tfCaption.editText?.text.toString())

            Firebase.firestore.collection(REEL).document().set(reel).addOnSuccessListener {
                Firebase.firestore.collection(Firebase.auth.currentUser!!.uid + REEL).document()
                    .set(reel).addOnSuccessListener {
                        startActivity(Intent(this@ReelActivity, HomeActivity::class.java))
                        finish()
                    }
            }
        }
    }
}