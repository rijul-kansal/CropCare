package com.learning.cropcare.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.learning.cropcare.databinding.FragmentHistoryBinding


class History : Fragment() {

    lateinit var binding:FragmentHistoryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding= FragmentHistoryBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return binding.root
    }

}