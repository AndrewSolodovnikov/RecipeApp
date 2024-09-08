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
import com.sol.recipeapp.databinding.FragmentRecipeBinding
import java.io.InputStream
import java.math.BigDecimal

class RecipeFragment : Fragment() {
    private val viewModel: RecipeViewModel by activityViewModels()
    private val binding by lazy { FragmentRecipeBinding.inflate(layoutInflater) }
    private val ingredientsAdapter = IngredientsAdapter()
    private val methodAdapter = MethodAdapter()
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
        recipeId?.let { viewModel.loadRecipe(it) }

        initRecycler()
        initUI()
        initSeekBar()
    }

    private fun initUI() {
        binding.btnFavorite.setOnClickListener {
            viewModel.onFavoritesClicked()
        }

        binding.rvIngredients.layoutManager = LinearLayoutManager(context)
        binding.rvMethod.layoutManager = LinearLayoutManager(context)
        binding.rvIngredients.adapter = ingredientsAdapter
        binding.rvMethod.adapter = methodAdapter

        viewModel.recipeState.observe(viewLifecycleOwner) { state ->
            Log.i("!!!info", "seekBar_3, init_4 ViewModel observe recipeState = $state")
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

            state?.recipe?.ingredients?.let { ingredients ->
                Log.i("!!!info", "seekBar_ RecipeFragment ingredients portionCount = ${state.portionCount}")
                val updatedIngredients = ingredients.map { ingredient ->
                    val newQuantity = try {
                        BigDecimal(ingredient.quantity).multiply(BigDecimal(state.portionCount))
                    } catch (e: NumberFormatException) {
                        BigDecimal.ZERO
                    }
                    ingredient.copy(
                        quantity = newQuantity.toString()
                    )

                }
                Log.i("!!!info", "seekBar_ RecipeFragment ingredients state = $state")
                ingredientsAdapter.updateIngredientsList(updatedIngredients)
            }

            try {
                val inputStream: InputStream? = state?.recipe?.imageUrl?.let { context?.assets?.open(it) }
                val drawable = Drawable.createFromStream(inputStream, null)
                binding.ivRecipesHeaderImage.setImageDrawable(drawable)
            } catch (e: Exception) {
                Log.e("MyLogError", "Image ${state?.recipe?.imageUrl} not found")
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
        Log.i("!!!info", "init_3 seekBar init")
        binding.seekbarRecipe.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                recipeId?.let { viewModel.updatePortionCount(it, progress) }
                Log.i("!!!info", "seekBar_5 seekBar progress = $progress")
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }
}
