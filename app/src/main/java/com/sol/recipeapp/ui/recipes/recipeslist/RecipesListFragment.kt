package com.sol.recipeapp.ui.recipes.recipeslist

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.sol.recipeapp.BASE_URL
import com.sol.recipeapp.IMAGE_CATEGORY_URL
import com.sol.recipeapp.R
import com.sol.recipeapp.data.Recipe
import com.sol.recipeapp.databinding.FragmentRecipesListBinding

class RecipesListFragment : Fragment() {
    private val binding by lazy { FragmentRecipesListBinding.inflate(layoutInflater) }
    private val viewModel: RecipeListViewModel by viewModels()
    private val args: RecipesListFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {
        val category =args.category
        val categoryId = args.category.id
        viewModel.loadRecipes(categoryId)

        val customAdapter = RecipesListAdapter(emptyList())
        binding.rvCategory.adapter = customAdapter

        customAdapter.setOnItemClickListener(object : RecipesListAdapter.OnItemClickListener {
            override fun onItemClick(recipe: Recipe) {
                openRecipesByRecipeId(recipe)
            }
        })

        viewModel.recipeListState.observe(viewLifecycleOwner) { state ->
            binding.tvRecipesListHeaderTitle.text = state.category?.title
            Log.i("!!!cat", "title ${state.category}")

            val imageUrl = "$BASE_URL$IMAGE_CATEGORY_URL${state.category?.imageUrl}"
            val imageView: ImageView = binding.ivRecipesListHeaderImage
            Glide.with(this)
                .load(imageUrl)
                .into(imageView)

            if (state.recipeList != null) {
                customAdapter.updateRecipes(state.recipeList)
            } else {
                val errorData = getString(R.string.error_retrofit_data)
                Toast.makeText(requireContext(), errorData, Toast.LENGTH_LONG).show()
            }
        }
    }

    fun openRecipesByRecipeId(recipe: Recipe) {
        findNavController().navigate(
            RecipesListFragmentDirections.actionRecipesListFragmentToRecipeFragment(recipe)
        )
    }
}