package com.sol.recipeapp.ui.recipes.recipeslist

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.sol.recipeapp.R
import com.sol.recipeapp.data.Recipe
import com.sol.recipeapp.databinding.FragmentRecipesListBinding

class RecipesListFragment : Fragment() {
    private val binding by lazy { FragmentRecipesListBinding.inflate(layoutInflater) }
    private val viewModel: RecipeListViewModel by viewModels()
    private val args: RecipesListFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {
        val categoryId = args.category.id
        viewModel.loadRecipes(categoryId)

        val customAdapter = RecipesListAdapter(emptyList())
        binding.rvCategory.adapter = customAdapter

        customAdapter.setOnItemClickListener(object : RecipesListAdapter.OnItemClickListener {
            override fun onItemClick(recipe: Recipe) {
                openRecipesByRecipeId(recipe)
            }
        })

        viewModel.recipeListState.observe(viewLifecycleOwner) { state ->
            Log.i("!!!info", "State $state")
            binding.ivRecipesListHeaderImage.setImageDrawable(state.categoryImageUrl)
            binding.tvRecipesListHeaderTitle.text = state.categoryTitle

            if (state.dataSet != null) {
                customAdapter.updateRecipes(state.dataSet)
            } else {
                val errorData = getString(R.string.error_retrofit_data)
                Toast.makeText(requireContext(), errorData, Toast.LENGTH_LONG).show()
            }
        }
    }

    fun openRecipesByRecipeId(recipe: Recipe) {
        findNavController().navigate(
            RecipesListFragmentDirections.actionRecipesListFragmentToRecipeFragment(recipe)
        )
    }
}