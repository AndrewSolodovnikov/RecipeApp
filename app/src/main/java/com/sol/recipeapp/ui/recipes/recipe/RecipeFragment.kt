package com.sol.recipeapp.ui.recipes.recipe

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.sol.recipeapp.ARG_RECIPE
import com.sol.recipeapp.R
import com.sol.recipeapp.databinding.FragmentRecipeBinding
import java.math.BigDecimal

class RecipeFragment : Fragment() {
    private val viewModel: RecipeViewModel by viewModels()
    private val binding by lazy { FragmentRecipeBinding.inflate(layoutInflater) }
    private val ingredientsAdapter = IngredientsAdapter()
    private val methodAdapter = MethodAdapter()
    private var recipeId: Int? = null
    private val args: RecipeFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recipeId = args.recipeId
        recipeId?.let { viewModel.loadRecipe(it) }

        initUI()
        initSeekBar()
    }

    private fun initUI() {
        binding.btnFavorite.setOnClickListener {
            viewModel.onFavoritesClicked()
        }

        binding.rvIngredients.adapter = ingredientsAdapter
        binding.rvMethod.adapter = methodAdapter

        viewModel.recipeState.observe(viewLifecycleOwner) { state ->
            Log.i("!!!info", "seekBar_3, init_4 ViewModel observe recipeState = $state")
            binding.tvRecipesHeaderTitle.text = state?.recipe?.title
            binding.tvNumberOfServings.text = state?.portionCount.toString()
            binding.seekbarRecipe.progress = state?.portionCount ?: 1
            binding.ivRecipesHeaderImage.setImageDrawable(state?.recipeImage)

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
                Log.i(
                    "!!!info",
                    "seekBar_ RecipeFragment ingredients portionCount = ${state.portionCount}"
                )
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

        }
    }

    private fun initSeekBar() {
        Log.i("!!!info", "init_3 seekBar init")
        binding.seekbarRecipe.setOnSeekBarChangeListener(
            PortionSeekBarListener { progress ->
                viewModel.updatePortionCount(progress)
            }
        )
    }

    class PortionSeekBarListener(val onChangeIngredients: (Int) -> Unit) :
        SeekBar.OnSeekBarChangeListener {
        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            onChangeIngredients(progress)
        }

        override fun onStartTrackingTouch(seekBar: SeekBar?) {}
        override fun onStopTrackingTouch(seekBar: SeekBar?) {}

    }
}