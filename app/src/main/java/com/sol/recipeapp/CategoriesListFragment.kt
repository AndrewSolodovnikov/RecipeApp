package com.sol.recipeapp

import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sol.recipeapp.databinding.FragmentListCategoriesBinding
import java.io.IOException

class CategoriesListFragment : Fragment() {
    private var _binding: FragmentListCategoriesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentListCategoriesBinding.inflate(inflater, container, false)
        val view = binding.root

        placeHeaderImage()

        return view
    }

    fun placeHeaderImage() {
        val assetManager = requireContext().assets
        try {
            val inputStream = assetManager.open("bcg_categories.png")
            val bitmap = BitmapFactory.decodeStream(inputStream)
            binding.categoryHeaderImage.setImageBitmap(bitmap)
            inputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}