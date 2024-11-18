package com.sol.recipeapp.ui.recipes.recipe

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.sol.recipeapp.IMAGE_CATEGORY_URL
import com.sol.recipeapp.R
import com.sol.recipeapp.BASE_URL
import com.sol.recipeapp.databinding.FragmentRecipeBinding
import kotlinx.coroutines.launch
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

        initUI()
        initSeekBar()
    }

    private fun initUI() {
        binding.btnFavorite.setOnClickListener {
            viewModel.onFavoritesClicked()
        }

        recipeId = args.recipe.id
        viewLifecycleOwner.lifecycleScope.launch {
            recipeId?.let { viewModel.loadRecipe(it) }
        }

        binding.rvIngredients.adapter = ingredientsAdapter
        binding.rvMethod.adapter = methodAdapter

        viewModel.recipeState.observe(viewLifecycleOwner) { state ->
            if (!state.isError) {
                val recipe = state.recipe
                recipe?.let {
                    binding.tvRecipesHeaderTitle.text = recipe.title

                    ingredientsAdapter.updateIngredientsList(recipe.ingredients)
                    methodAdapter.updateMethodList(recipe.method)

                    val updatedIngredients = recipe.ingredients.map { ingredient ->
                        val newQuantity = try {
                            BigDecimal(ingredient.quantity).multiply(BigDecimal(state.portionCount))
                        } catch (e: NumberFormatException) {
                            BigDecimal.ZERO
                        }
                        ingredient.copy(quantity = newQuantity.toString())
                    }
                    ingredientsAdapter.updateIngredientsList(updatedIngredients)
                }
            } else {
                val errorData = getString(R.string.error_retrofit_data)
                Toast.makeText(requireContext(), errorData, Toast.LENGTH_LONG).show()
            }


            binding.tvNumberOfServings.text = state?.portionCount.toString()
            binding.seekbarRecipe.progress = state?.portionCount ?: 1

            val imageView: ImageView = binding.ivRecipesHeaderImage
            val imageUrl = "$BASE_URL$IMAGE_CATEGORY_URL${state.recipe?.imageUrl}"
            Log.i("!!!img", "image url $imageUrl")
            Glide.with(this)
                .load(imageUrl)
                .placeholder(R.drawable.img_placeholder)
                .error(R.drawable.img_error)
                .into(imageView)

            if (state?.isFavorite == true) {
                binding.btnFavorite.setImageResource(R.drawable.ic_heart)
            } else {
                binding.btnFavorite.setImageResource(R.drawable.ic_heart_empty)
            }


        }
    }

    private fun initSeekBar() {
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