package com.itsmohsin.instagramclone.Utils

import android.app.ProgressDialog
import android.net.Uri
import android.widget.ProgressBar
import com.google.firebase.storage.FirebaseStorage
import com.itsmohsin.instagramclone.Post.ReelActivity
import java.util.UUID
import javax.security.auth.callback.Callback

fun uploadImage(uri: Uri, folderName: String, callback: (String?) -> Unit) {
    var imageUrl: String? = null
    FirebaseStorage.getInstance().getReference(folderName).child(UUID.randomUUID().toString())
        .putFile(uri).addOnSuccessListener {
            it.storage.downloadUrl.addOnSuccessListener {
                imageUrl = it.toString()
                callback(imageUrl)
            }
        }
}

fun uploadVideo(
    uri: Uri,
    folderName: String,
    progressDialog: ProgressDialog,
    callback: (String?) -> Unit
) {
    var videoUrl: String? = null
    progressDialog.setTitle("Uploading...")
    progressDialog.show()
    FirebaseStorage.getInstance().getReference(folderName).child(UUID.randomUUID().toString())
        .putFile(uri).addOnSuccessListener {
            it.storage.downloadUrl.addOnSuccessListener {
                videoUrl = it.toString()
                progressDialog.dismiss()
                callback(videoUrl)
            }
        }
        .addOnProgressListener {
            val uploadedValue: Long = it.bytesTransferred / it.totalByteCount
            progressDialog.setMessage("Uploaded $uploadedValue")
        }
}