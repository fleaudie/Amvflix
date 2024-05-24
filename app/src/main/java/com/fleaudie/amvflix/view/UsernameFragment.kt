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
import com.fleaudie.amvflix.databinding.FragmentUsernameBinding
import com.fleaudie.amvflix.util.utilNavigate
import com.fleaudie.amvflix.viewmodel.UsernameViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UsernameFragment : Fragment() {
    private lateinit var binding: FragmentUsernameBinding
    private val viewModel: UsernameViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_username, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnUsernameContinue.setOnClickListener{
            val username = binding.editTextUsername.text.toString()
            viewModel.createUsername(username){ isSuccess, _ ->
                if (isSuccess){
                    Navigation.utilNavigate(view, R.id.action_usernameFragment_to_feedFragment)
                }
            }
        }
        observeData()
    }

    private fun observeData() {
        viewModel.apply {
            binding.apply {
                usernameError.observe(viewLifecycleOwner) { editTextUsername.error = it }
            }
        }
    }

}