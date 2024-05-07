package com.semenchenko.foodfriend.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.semenchenko.foodfriend.R
import com.semenchenko.foodfriend.databinding.FragmentAccountBinding

class AccountFragment : Fragment(R.layout.fragment_account) {

    private lateinit var binding: FragmentAccountBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAccountBinding.bind(view)

    }
}