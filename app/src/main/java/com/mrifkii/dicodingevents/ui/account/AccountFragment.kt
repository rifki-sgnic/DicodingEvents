package com.mrifkii.dicodingevents.ui.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.mrifkii.dicodingevents.R
import com.mrifkii.dicodingevents.databinding.FragmentAccountBinding

class AccountFragment : Fragment() {

    private var _binding: FragmentAccountBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupDummyProfile()
    }

    private fun setupDummyProfile() {
        binding.apply {
            tvName.text = getString(R.string.dummy_name)
            tvEmail.text = getString(R.string.dummy_email)

            Glide.with(this@AccountFragment)
                .load("https://ui-avatars.com/api/?name=Dicoding+Student&background=0D8ABC&color=fff&size=256")
                .circleCrop()
                .into(ivProfilePicture)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}