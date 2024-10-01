package com.sol.recipeapp.ui.recipes.favorites

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.sol.recipeapp.ARG_RECIPE
import com.sol.recipeapp.R
import com.sol.recipeapp.databinding.FragmentFavoritesBinding
import com.sol.recipeapp.ui.recipes.recipe.RecipeFragment
import com.sol.recipeapp.ui.recipes.recipeslist.RecipesListAdapter

class FavoritesFragment : Fragment() {
    private val binding by lazy { FragmentFavoritesBinding.inflate(layoutInflater) }
    private val viewModel: FavoritesViewModel by viewModels()
    private val recipeListAdapter by lazy { RecipesListAdapter(emptyList()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        initUI()
        return binding.root
    }

    /*
    private fun initRecycler(favoriteRecipes: List<Recipe>) {
        //val recipeListAdapter = RecipesListAdapter(favoriteRecipes)

        binding.rvFavorites.layoutManager = LinearLayoutManager(context)
        //binding.rvFavorites.adapter = recipeListAdapter

        recipeListAdapter.setOnItemClickListener(object : RecipesListAdapter.OnItemClickListener {
            override fun onItemClick(recipeId: Int) {
                openRecipesByRecipeId(recipeId)
            }
        })
    }

     */

    private fun initUI() {
        binding.rvFavorites.layoutManager = LinearLayoutManager(context)
        binding.rvFavorites.adapter = recipeListAdapter

        viewModel.loadFavoritesRecipes()

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

            recipeListAdapter.setOnItemClickListener(object : RecipesListAdapter.OnItemClickListener {
                override fun onItemClick(recipeId: Int) {
                    openRecipesByRecipeId(recipeId)
                }
            })

        }
    }

    fun openRecipesByRecipeId(recipeId: Int) {
        val bundle = bundleOf(ARG_RECIPE to recipeId)

        parentFragmentManager.commit {
            replace<RecipeFragment>(R.id.mainContainer, args = bundle)
            setReorderingAllowed(true)
            addToBackStack("recipes_list_fragment")
        }
    }

}