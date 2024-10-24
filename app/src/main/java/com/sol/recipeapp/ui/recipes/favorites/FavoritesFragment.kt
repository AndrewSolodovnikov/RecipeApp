package com.sol.recipeapp.ui.recipes.favorites

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.sol.recipeapp.data.Recipe
import com.sol.recipeapp.databinding.FragmentFavoritesBinding
import com.sol.recipeapp.ui.recipes.recipeslist.RecipesListAdapter

class FavoritesFragment : Fragment() {
    private val binding by lazy { FragmentFavoritesBinding.inflate(layoutInflater) }
    private val viewModel: FavoritesViewModel by viewModels()
    private val recipeListAdapter by lazy { RecipesListAdapter(emptyList()) }

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
        binding.rvFavorites.adapter = recipeListAdapter
        viewModel.loadFavoritesRecipes()

        recipeListAdapter.setOnItemClickListener(object : RecipesListAdapter.OnItemClickListener {
            override fun onItemClick(recipe: Recipe) {
                openRecipesByRecipeId(recipe)
            }
        })

        viewModel.favoritesState.observe(viewLifecycleOwner) { state ->
            Log.i("!!!info", "DataSet = ${state.dataSet}")
            if (state.dataSet?.isEmpty() == true) {
                binding.tvFavoriteEmpty.visibility = View.VISIBLE
                binding.rvFavorites.visibility = View.GONE
            } else {
                binding.tvFavoriteEmpty.visibility = View.GONE
                binding.rvFavorites.visibility = View.VISIBLE
                state.dataSet?.let { recipeListAdapter.updateRecipes(it) }
            }
        }
    }

    fun openRecipesByRecipeId(recipe: Recipe) {
        findNavController().navigate(
            FavoritesFragmentDirections.actionFavoritesFragmentToRecipeFragment(recipe)
        )
    }

}