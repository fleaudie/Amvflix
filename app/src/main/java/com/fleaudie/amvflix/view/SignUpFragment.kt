package com.fleaudie.amvflix.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.fleaudie.amvflix.R
import com.fleaudie.amvflix.util.utilNavigate
import com.fleaudie.amvflix.databinding.FragmentSignUpBinding
import com.fleaudie.amvflix.viewmodel.SignUpViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpFragment : Fragment() {
    private lateinit var binding: FragmentSignUpBinding
    private val viewModel: SignUpViewModel by viewModels()
    private var user = FirebaseAuth.getInstance().currentUser?.uid

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sign_up, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnSignup.setOnClickListener {
            val email = binding.signupMail.text.toString()
            val password = binding.signupPassword.text.toString()
            val name = binding.signupName.text.toString()
            val surname = binding.signupSurname.text.toString()
            val rePassword = binding.signupPasswordControl.text.toString()

            viewModel.register(it, email, password, name, surname, rePassword)
        }
        binding.btnSignupLogin.setOnClickListener {
            Navigation.utilNavigate(it, R.id.action_signUpFragment_to_loginFragment)
        }
        observeData()
    }

    private fun observeData() {
        viewModel.apply {
            binding.apply {
                nameError.observe(viewLifecycleOwner) { signUpNameLayout.error = it }
                surnameError.observe(viewLifecycleOwner) { signUpSurnameLayout.error = it }
                emailError.observe(viewLifecycleOwner) { signUpMailLayout.error = it }
                passwordError.observe(viewLifecycleOwner) { signUpPasswordLayout.error = it }
                passwordControlError.observe(viewLifecycleOwner) { signUpRePasswordLayout.error = it }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if (user != null){
            view?.let { Navigation.utilNavigate(it, R.id.action_signUpFragment_to_feedFragment) }
        }
    }
}