package com.sol.recipeapp.ui.recipes.recipe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.sol.recipeapp.BASE_URL
import com.sol.recipeapp.IMAGE_CATEGORY_URL
import com.sol.recipeapp.R
import com.sol.recipeapp.databinding.FragmentRecipeBinding

class RecipeFragment : Fragment() {
    private val viewModel: RecipeViewModel by viewModels()
    private val binding by lazy { FragmentRecipeBinding.inflate(layoutInflater) }
    private val ingredientsAdapter = IngredientsAdapter()
    private val methodAdapter = MethodAdapter()
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
            viewModel.onFavoriteClicked()
        }

        val recipeArgs = args.recipe
        viewModel.loadRecipe(recipeArgs)

        binding.rvIngredients.adapter = ingredientsAdapter
        binding.rvMethod.adapter = methodAdapter

        viewModel.recipeState.observe(viewLifecycleOwner) { state ->
            val recipe = state.recipe
            recipe?.let {
                binding.tvRecipesHeaderTitle.text = recipe.title

                ingredientsAdapter.updateIngredientsList(recipe.ingredients)
                methodAdapter.updateMethodList(recipe.method)
                ingredientsAdapter.updatePortionCount(state.portionCount)
            }

            binding.tvNumberOfServings.text = state?.portionCount.toString()
            binding.seekbarRecipe.progress = state?.portionCount ?: 1

            val imageView: ImageView = binding.ivRecipesHeaderImage
            val imageUrl = "$BASE_URL$IMAGE_CATEGORY_URL${state.recipe?.imageUrl}"
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