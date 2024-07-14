package com.sol.recipeapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.sol.recipeapp.databinding.FragmentRecipesListBinding

class RecipesListFragment : Fragment() {
    private val binding by lazy { FragmentRecipesListBinding.inflate(layoutInflater) }

    private val categoryId: Int? = null
    private val categoryName: String? = null
    private val categoryImageUrl: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = binding.root
        return view

        arguments.let {
            categoryId = it?.getInt(ARG_CATEGORY_ID)
            categoryName = it?.getString(ARG_CATEGORY_NAME)
            categoryImageUrl = it?.getString(ARG_CATEGORY_IMAGE_URL)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initRecycler()
    }

    private fun initRecycler() {
        val customAdapter = CategoriesListAdapter(STUB.getCategories())

        binding.rvCategory.layoutManager = LinearLayoutManager(context)
        binding.rvCategory.adapter = customAdapter
    }

}