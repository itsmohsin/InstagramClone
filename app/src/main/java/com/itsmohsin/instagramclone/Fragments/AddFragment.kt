package com.itsmohsin.instagramclone.Fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.itsmohsin.instagramclone.Post.PostActivity
import com.itsmohsin.instagramclone.Post.ReelsActivity
import com.itsmohsin.instagramclone.databinding.FragmentAddBinding

class AddFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentAddBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentAddBinding.inflate(inflater, container, false)
        binding.llPost.setOnClickListener {
            activity?.startActivity(Intent(requireContext(), PostActivity::class.java))
            activity?.finish()
        }
        binding.llReels.setOnClickListener {
            activity?.startActivity(Intent(requireContext(), ReelsActivity::class.java))

        }
        return binding.root
    }

    companion object {

    }
}