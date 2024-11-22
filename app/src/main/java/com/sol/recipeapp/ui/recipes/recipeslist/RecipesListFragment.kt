package com.sol.recipeapp.ui.recipes.recipeslist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.sol.recipeapp.BASE_URL
import com.sol.recipeapp.IMAGE_CATEGORY_URL
import com.sol.recipeapp.R
import com.sol.recipeapp.data.Recipe
import com.sol.recipeapp.databinding.FragmentRecipesListBinding
import kotlinx.coroutines.launch

class RecipesListFragment : Fragment() {
    private val binding by lazy { FragmentRecipesListBinding.inflate(layoutInflater) }
    private val viewModel: RecipeListViewModel by viewModels()
    private val args: RecipesListFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {
        val categoryId = args.category.id

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.loadRecipes(categoryId)
        }

        val customAdapter = RecipesListAdapter(emptyList())
        binding.rvCategory.adapter = customAdapter

        customAdapter.setOnItemClickListener(object : RecipesListAdapter.OnItemClickListener {
            override fun onItemClick(recipe: Recipe) {
                openRecipesByRecipeId(recipe)
            }
        })

        viewModel.recipeListState.observe(viewLifecycleOwner) { state ->
            binding.tvRecipesListHeaderTitle.text = state.category?.title

            val imageUrl = "$BASE_URL$IMAGE_CATEGORY_URL${state.category?.imageUrl}"
            val imageView: ImageView = binding.ivRecipesListHeaderImage
            Glide.with(this).load(imageUrl).into(imageView)

            if (state.recipeList != null) {
                if (state.recipeList.isNotEmpty()) {
                    customAdapter.updateRecipes(state.recipeList)
                }
            } else if (state.isError) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.error_retrofit_data),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    fun openRecipesByRecipeId(recipe: Recipe) {
        findNavController().navigate(
            RecipesListFragmentDirections.actionRecipesListFragmentToRecipeFragment(recipe)
        )
    }
}