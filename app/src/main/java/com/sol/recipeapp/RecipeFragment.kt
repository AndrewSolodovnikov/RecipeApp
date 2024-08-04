package com.sol.recipeapp

import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.sol.recipeapp.databinding.FragmentRecipeBinding
import java.io.InputStream

class RecipeFragment : Fragment() {
    private val binding by lazy { FragmentRecipeBinding.inflate(layoutInflater) }
    private var recipe: Recipe? = null
    private lateinit var ingredientsAdapter: IngredientsAdapter
    private var isFavorite = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recipe = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable(ARG_RECIPE, Recipe::class.java)
        } else {
            arguments?.getParcelable(ARG_RECIPE)
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
            isFavorite = !isFavorite
            val favoriteImage = if (isFavorite) {
                R.drawable.ic_heart
            } else {
                R.drawable.ic_heart_empty
            }
            binding.btnFavorite.setImageResource(favoriteImage)
        }

        binding.tvRecipesHeaderTitle.text = recipe?.title
    }

    private fun initRecycler() {
        recipe?.let {
            ingredientsAdapter = IngredientsAdapter(it.ingredients)
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
            binding.rvMethod.adapter = MethodAdapter(it.method)
            binding.rvMethod.layoutManager = LinearLayoutManager(context)
        }
    }

    private fun initSeekBar() {
        binding.seekbarRecipe.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                binding.tvNumberOfServings.text = progress.toString()
                ingredientsAdapter.updateIngredients(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }
}
