package com.sol.recipeapp

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.sol.recipeapp.databinding.FragmentFavoritesBinding
import com.sol.recipeapp.databinding.FragmentRecipesListBinding

class FavoritesFragment : Fragment() {
    private val binding by lazy { FragmentFavoritesBinding.inflate(layoutInflater) }
    private val recipe: Recipe? = null
    private val categoryId: Int? = null
    //private val recipesListFragment: RecipesListFragment? = null
    private val recipesListFragment by lazy { RecipesListFragment() }
    private val sharedPref by lazy {
        requireActivity().getSharedPreferences(
            ARG_FAVORITES_SHARED_PREF,
            Context.MODE_PRIVATE
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root

        initRecycler()

        binding.tvFavoriteEmpty.text = getFavorites().toString()
    }

    private fun initRecycler() {
        val customAdapter = categoryId?.let { STUB.getRecipesByCategoryId(it) }
            ?.let { RecipesListAdapter(it) }

        binding.rvCategory.layoutManager = LinearLayoutManager(context)
        binding.rvCategory.adapter = customAdapter

        customAdapter?.setOnItemClickListener(object : RecipesListAdapter.OnItemClickListener {
            override fun onItemClick(recipeId: Int) {
                recipesListFragment.openRecipesByRecipeId(recipeId)
            }
        })
    }

    private fun getFavorites(): MutableSet<String>? {
        return sharedPref.getStringSet(ARG_FAVORITES_SHARED_PREF, HashSet())?.toMutableSet()
    }
}