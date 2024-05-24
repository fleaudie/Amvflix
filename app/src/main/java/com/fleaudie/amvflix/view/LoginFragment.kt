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
import com.fleaudie.amvflix.databinding.FragmentLoginBinding
import com.fleaudie.amvflix.util.utilNavigate
import com.fleaudie.amvflix.viewmodel.LoginViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint

class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private val viewModel: LoginViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLogin.setOnClickListener {
            val email = binding.loginMail.text.toString()
            val password = binding.loginPassword.text.toString()

            viewModel.login(email, password) { isSuccess, _ ->
                if (isSuccess) {
                    val user = FirebaseAuth.getInstance().currentUser
                    if (user != null && user.isEmailVerified) {
                        viewModel.checkUsername { usernameExists ->
                            if (usernameExists) {
                                Navigation.utilNavigate(view, R.id.action_loginFragment_to_feedFragment)
                            }
                            else {
                                Navigation.utilNavigate(view, R.id.action_loginFragment_to_usernameFragment)
                            }
                        }
                    } else {
                        Snackbar.make(view, "Please verify your email address!", Snackbar.LENGTH_SHORT).show()
                    }
                } else {
                    Snackbar.make(view, "Email or password wrong!", Snackbar.LENGTH_SHORT).show()
                }
            }
        }
        binding.btnLoginSignup.setOnClickListener {
            Navigation.utilNavigate(it, R.id.action_loginFragment_to_signUpFragment)
        }
        observeData()
    }

    private fun observeData() {
        viewModel.apply {
            binding.apply {
                emailError.observe(viewLifecycleOwner) { loginMail.error = it }
                passwordError.observe(viewLifecycleOwner) { loginPassword.error = it }
            }
        }
    }
}