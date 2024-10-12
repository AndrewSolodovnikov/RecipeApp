package com.sol.recipeapp.ui.recipes.recipeslist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.sol.recipeapp.ARG_CATEGORY_ID
import com.sol.recipeapp.ARG_RECIPE
import com.sol.recipeapp.R
import com.sol.recipeapp.databinding.FragmentRecipesListBinding

class RecipesListFragment : Fragment() {
    private val binding by lazy { FragmentRecipesListBinding.inflate(layoutInflater) }
    private val viewModel: RecipeListViewModel by viewModels()

    private var categoryId: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        arguments.let {
            categoryId = it?.getInt(ARG_CATEGORY_ID)
        }

        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {
        categoryId?.let { id ->
            viewModel.loadRecipes(id)
        }

        val customAdapter = RecipesListAdapter(emptyList())
        binding.rvCategory.adapter = customAdapter

        customAdapter.setOnItemClickListener(object : RecipesListAdapter.OnItemClickListener {
            override fun onItemClick(recipeId: Int) {
                openRecipesByRecipeId(recipeId)
            }
        })

        viewModel.recipeListState.observe(viewLifecycleOwner) { state ->
            binding.ivRecipesListHeaderImage.setImageDrawable(state.categoryImageUrl)
            binding.tvRecipesListHeaderTitle.text = state.categoryTitle

            customAdapter.updateRecipes(state.dataSet)
        }
    }

    fun openRecipesByRecipeId(recipeId: Int) {
        findNavController().navigate(
            RecipesListFragmentDirections.actionRecipesListFragmentToRecipeFragment(recipeId)
        )
    }
}