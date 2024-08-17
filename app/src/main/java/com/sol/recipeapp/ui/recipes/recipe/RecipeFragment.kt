package com.sol.recipeapp.ui.recipes.recipe

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.sol.recipeapp.ARG_FAVORITES_SHARED_PREF
import com.sol.recipeapp.ARG_RECIPE
import com.sol.recipeapp.R
import com.sol.recipeapp.data.Recipe
import com.sol.recipeapp.databinding.FragmentRecipeBinding
import java.io.InputStream

class RecipeFragment : Fragment() {
    private val viewModel: RecipeViewModel by activityViewModels()
    private val binding by lazy { FragmentRecipeBinding.inflate(layoutInflater) }
    private var recipe: Recipe? = null
    private val ingredientsAdapter by lazy { recipe?.ingredients?.let { IngredientsAdapter(it) } }
    private val sharedPref by lazy {
        requireActivity().getSharedPreferences(
            ARG_FAVORITES_SHARED_PREF,
            Context.MODE_PRIVATE
        )
    }
    private val methodAdapter by lazy { recipe?.method?.let { MethodAdapter(it) } }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /*
        recipe = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable(ARG_RECIPE, Recipe::class.java)
        } else {
            arguments?.getParcelable(ARG_RECIPE)
        }

         */

        val recipeId = arguments?.getInt(ARG_RECIPE) ?: null
        if (recipeId != null) {
            viewModel.loadRecipe(recipeId)
        }

        initRecycler()
        initUI()
        initSeekBar()
        viewModelObserver()
    }

    private fun initUI() {
        try {
            val inputStream: InputStream? = recipe?.imageUrl?.let { context?.assets?.open(it) }
            val drawable = Drawable.createFromStream(inputStream, null)
            binding.ivRecipesHeaderImage.setImageDrawable(drawable)
        } catch (e: Exception) {
            Log.e("MyLogError", "Image ${recipe?.imageUrl} not found")
        }

        updateFavoriteIcon()

        binding.btnFavorite.setOnClickListener {
            toggleFavorite()
        }

        binding.tvRecipesHeaderTitle.text = recipe?.title
    }

    private fun initRecycler() {
        recipe?.let {
            binding.rvIngredients.adapter = ingredientsAdapter
            binding.rvIngredients.layoutManager = LinearLayoutManager(context)

            val divider = MaterialDividerItemDecoration(
                requireContext(),
                LinearLayoutManager.VERTICAL
            ).apply {
                isLastItemDecorated = false
                dividerInsetStart = resources.getDimensionPixelSize(R.dimen.padding_text_12)
                dividerInsetEnd = resources.getDimensionPixelSize(R.dimen.padding_text_12)
                dividerColor = resources.getColor(R.color.divider_color)
            }
            divider.dividerThickness = 2
            binding.rvIngredients.addItemDecoration(divider)
            binding.rvMethod.addItemDecoration(divider)
        }

        recipe?.let {
            binding.rvMethod.adapter = methodAdapter
            binding.rvMethod.layoutManager = LinearLayoutManager(context)
        }
    }

    private fun initSeekBar() {
        binding.seekbarRecipe.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                binding.tvNumberOfServings.text = progress.toString()
                ingredientsAdapter?.updateIngredients(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    private fun toggleFavorite() {
        val favorites = getFavorites()
        val recipeId = recipe?.id.toString()
        if (favorites.contains(recipeId)) {
            favorites.remove(recipeId)
            binding.btnFavorite.setImageResource(R.drawable.ic_heart_empty)
        } else {
            favorites.add(recipeId)
            binding.btnFavorite.setImageResource(R.drawable.ic_heart)
        }
        saveFavorites(favorites)
    }

    private fun updateFavoriteIcon() {
        val favorites = getFavorites()
        val recipeId = recipe?.id.toString()
        if (favorites.contains(recipeId)) {
            binding.btnFavorite.setImageResource(R.drawable.ic_heart)
        } else {
            binding.btnFavorite.setImageResource(R.drawable.ic_heart_empty)
        }
    }

    private fun saveFavorites(favoriteIds: Set<String>) {
        with(sharedPref.edit()) {
            putStringSet(ARG_FAVORITES_SHARED_PREF, favoriteIds)
            apply()
        }
    }

    /*
    private fun getFavorites(): MutableSet<String> {
        return HashSet(sharedPref
            .getStringSet(ARG_FAVORITES_SHARED_PREF, HashSet()) ?: mutableSetOf())
    }

     */

    private fun viewModelObserver() {
        viewModel.recipeState.observe(
            viewLifecycleOwner,
            { state -> Log.i("!!!", "Favorite ${state.isFavorite}") },
        )
    }
}