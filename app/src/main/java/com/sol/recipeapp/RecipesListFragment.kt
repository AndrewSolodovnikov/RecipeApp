package com.sol.recipeapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.sol.recipeapp.databinding.FragmentRecipesListBinding

class RecipesListFragment : Fragment() {
    private var _binding: FragmentRecipesListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRecipesListBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

}