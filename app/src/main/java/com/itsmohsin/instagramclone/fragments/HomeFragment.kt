package com.itsmohsin.instagramclone.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.itsmohsin.instagramclone.Adapters.FollowAdapter
import com.itsmohsin.instagramclone.Adapters.PostAdapter
import com.itsmohsin.instagramclone.Models.Post
import com.itsmohsin.instagramclone.Models.User
import com.itsmohsin.instagramclone.R
import com.itsmohsin.instagramclone.Utils.FOLLOW
import com.itsmohsin.instagramclone.Utils.POST
import com.itsmohsin.instagramclone.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private var postList=ArrayList<Post>()
    private lateinit var adapter: PostAdapter
    private var followList=ArrayList<User>()
    private lateinit var followAdapter: FollowAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentHomeBinding.inflate(inflater, container, false)
        adapter=PostAdapter(requireContext(),postList)
        binding.rvPost.layoutManager=LinearLayoutManager(requireContext())
        binding.rvPost.adapter=adapter

        followAdapter= FollowAdapter(requireContext(),followList)
        binding.rvfolow.layoutManager=LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL,false)
        binding.rvfolow.adapter=followAdapter
        setHasOptionsMenu(true)
        (requireContext() as AppCompatActivity).setSupportActionBar(binding.materialToolbar2)
        Firebase.firestore.collection(Firebase.auth.currentUser!!.uid+ FOLLOW).get().addOnSuccessListener {
            var tempList = ArrayList<User>()
            followList.clear()
            for (i in it.documents){
                var user: User=i.toObject<User>()!!
                tempList.add(user)

            }
            followList.addAll(tempList)
            followAdapter.notifyDataSetChanged()
        }
        Firebase.firestore.collection(POST).get().addOnSuccessListener {
            var tempList=ArrayList<Post>()
            postList.clear()
            for (i in it.documents){
                var post:Post=i.toObject<Post>()!!
                tempList.add(post)
            }
            postList.addAll(tempList)
            adapter.notifyDataSetChanged()
        }
        return binding.root
    }

    companion object {
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.option_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)

    }
}