package com.sol.recipeapp.ui.recipes.favorites

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.recyclerview.widget.LinearLayoutManager
import com.sol.recipeapp.ARG_FAVORITES_SHARED_PREF
import com.sol.recipeapp.ARG_RECIPE
import com.sol.recipeapp.R
import com.sol.recipeapp.STUB
import com.sol.recipeapp.databinding.FragmentFavoritesBinding
import com.sol.recipeapp.ui.recipes.recipe.RecipeFragment
import com.sol.recipeapp.ui.recipes.recipeslist.RecipesListAdapter

class FavoritesFragment : Fragment() {
    private val binding by lazy { FragmentFavoritesBinding.inflate(layoutInflater) }
    private val sharedPref by lazy {
        requireActivity().getSharedPreferences(
            ARG_FAVORITES_SHARED_PREF,
            Context.MODE_PRIVATE
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        initFavorites()
        return binding.root
    }

    private fun initRecycler() {
        val favoriteIds = getFavorites()?.mapNotNull { it.toIntOrNull() }?.toSet()
        val favoriteRecipes = favoriteIds?.let { STUB.getRecipesByIds(it) }
        val recipeListAdapter = favoriteRecipes?.let { RecipesListAdapter(it) }

        binding.rvFavorites.layoutManager = LinearLayoutManager(context)
        binding.rvFavorites.adapter = recipeListAdapter

        recipeListAdapter?.setOnItemClickListener(object : RecipesListAdapter.OnItemClickListener {
            override fun onItemClick(recipeId: Int) {
                openRecipesByRecipeId(recipeId)
            }
        })
    }

    private fun initFavorites() {
        if (getFavorites()?.isEmpty() == true) {
            binding.tvFavoriteEmpty.visibility = View.VISIBLE
            binding.rvFavorites.visibility = View.GONE
        } else {
            binding.tvFavoriteEmpty.visibility = View.GONE
            binding.rvFavorites.visibility = View.VISIBLE
            initRecycler()
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

    private fun getFavorites(): MutableSet<String>? {
        return sharedPref.getStringSet(ARG_FAVORITES_SHARED_PREF, HashSet())?.toMutableSet()
    }
}