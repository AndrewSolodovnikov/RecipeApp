package com.sol.recipeapp.ui.recipes.recipe

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.sol.recipeapp.ARG_RECIPE
import com.sol.recipeapp.R
import com.sol.recipeapp.data.Recipe
import com.sol.recipeapp.databinding.FragmentRecipeBinding
import java.io.InputStream

class RecipeFragment : Fragment() {
    private val viewModel: RecipeViewModel by activityViewModels()
    private val binding by lazy { FragmentRecipeBinding.inflate(layoutInflater) }
    private val ingredientsAdapter = IngredientsAdapter()
    private val methodAdapter = MethodAdapter()
    private val recipe: Recipe? = null
    private var recipeId: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recipeId = arguments?.getInt(ARG_RECIPE)
        if (recipeId != null) {
            viewModel.loadRecipe(recipeId!!)
        }

        initRecycler()
        initUI()
        initSeekBar()
    }

    private fun initUI() {
        try {
            val inputStream: InputStream? = recipe?.imageUrl?.let { context?.assets?.open(it) }
            val drawable = Drawable.createFromStream(inputStream, null)
            binding.ivRecipesHeaderImage.setImageDrawable(drawable)
        } catch (e: Exception) {
            Log.e("MyLogError", "Image ${recipe?.imageUrl} not found")
        }

        binding.btnFavorite.setOnClickListener {
            viewModel.onFavoritesClicked()
        }

        binding.rvIngredients.layoutManager = LinearLayoutManager(context)
        binding.rvMethod.layoutManager = LinearLayoutManager(context)
        binding.rvIngredients.adapter = ingredientsAdapter
        binding.rvMethod.adapter = methodAdapter

        viewModel.recipeState.observe(viewLifecycleOwner) { state ->
            binding.tvRecipesHeaderTitle.text = state?.recipe?.title
            binding.tvNumberOfServings.text = state?.portionCount.toString()
            binding.seekbarRecipe.progress = state?.portionCount ?: 1

            if (state?.isFavorite == true) {
                binding.btnFavorite.setImageResource(R.drawable.ic_heart)
            } else {
                binding.btnFavorite.setImageResource(R.drawable.ic_heart_empty)
            }

            state?.recipe?.ingredients?.let { ingredients ->
                ingredientsAdapter.updateIngredientsList(ingredients)
            }

            state?.recipe?.method?.let { method ->
                methodAdapter.updateMethodList(method)
            }
        }
    }

    private fun initRecycler() {
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

    private fun initSeekBar() {
        binding.seekbarRecipe.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                recipeId?.let { viewModel.updatePortionCount(it, progress) }
                }


            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }
}
