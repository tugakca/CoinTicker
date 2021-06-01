package com.android.cointicker.view

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.android.cointicker.App
import com.android.cointicker.R
import com.android.cointicker.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth


class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mAuth = FirebaseAuth.getInstance()

        if (App.userId.isNullOrEmpty()) {
            binding.loginButton.text = "Login"
            binding.username.visibility = View.GONE
            binding.divider.visibility = View.GONE
        } else {
            binding.username.visibility = View.VISIBLE
            binding.divider.visibility = View.VISIBLE
            binding.loginButton.text = "Logout"
            binding.username.text = mAuth.currentUser!!.providerData.get(0).email
        }
        binding.loginButton.setOnClickListener {
            if (binding.loginButton.text == "Login") {
                val intent = Intent(requireContext(), LoginActivity::class.java)
                startActivity(intent)
            } else {
                mAuth.signOut()
                App.userId=null
                binding.username.visibility = View.GONE
                binding.divider.visibility = View.GONE
                binding.loginButton.text = "Login"
            }
        }
    }
}