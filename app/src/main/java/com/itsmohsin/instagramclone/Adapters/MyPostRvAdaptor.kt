package com.itsmohsin.instagramclone.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.itsmohsin.instagramclone.Models.Post
import com.itsmohsin.instagramclone.databinding.MyPostRvDesignBinding
import com.squareup.picasso.Picasso

class MyPostRvAdaptor(var context: Context, var postList: ArrayList<Post>) :
    RecyclerView.Adapter<MyPostRvAdaptor.ViewHolder>() {
    inner class ViewHolder(var binding: MyPostRvDesignBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var binding = MyPostRvDesignBinding.inflate(LayoutInflater.from(context), parent,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return postList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Picasso.get().load(postList.get(position).postUrl).into(holder.binding.ivPostImage)
    }
}