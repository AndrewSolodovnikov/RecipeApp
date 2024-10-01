package com.sol.recipeapp.ui.recipes.recipeslist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.sol.recipeapp.ARG_CATEGORY_ID
import com.sol.recipeapp.ARG_CATEGORY_IMAGE_URL
import com.sol.recipeapp.ARG_CATEGORY_NAME
import com.sol.recipeapp.ARG_RECIPE
import com.sol.recipeapp.R
import com.sol.recipeapp.databinding.FragmentRecipesListBinding
import com.sol.recipeapp.ui.recipes.recipe.RecipeFragment

class RecipesListFragment : Fragment() {
    private val binding by lazy { FragmentRecipesListBinding.inflate(layoutInflater) }
    private val viewModel: RecipeListViewModel by viewModels()

    private var categoryId: Int? = null
    private var categoryName: String? = null
    private var categoryImageUrl: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        arguments.let {
            categoryId = it?.getInt(ARG_CATEGORY_ID)
            categoryName = it?.getString(ARG_CATEGORY_NAME)
            categoryImageUrl = it?.getString(ARG_CATEGORY_IMAGE_URL)
        }

        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initUI()
    }

    private fun initUI() {
        categoryId?.let { id ->
            viewModel.loadRecipes(id)
        }

        categoryImageUrl?.let { url ->
            viewModel.categoryImage(url)
        }

        binding.rvCategory.layoutManager = LinearLayoutManager(context)

        viewModel.recipeListState.observe(viewLifecycleOwner) { state ->
            binding.ivRecipesListHeaderImage.setImageDrawable(state.categoryImageUrl)
            binding.tvRecipesListHeaderTitle.text = categoryName

            val customAdapter = RecipesListAdapter(state.dataSet)
            binding.rvCategory.adapter = customAdapter

            customAdapter.setOnItemClickListener(object : RecipesListAdapter.OnItemClickListener {
                override fun onItemClick(recipeId: Int) {
                    openRecipesByRecipeId(recipeId)
                }
            })

        }
    }

    fun openRecipesByRecipeId(recipeId: Int) {
        val bundle = bundleOf(ARG_RECIPE to recipeId)

        parentFragmentManager.commit {
            replace<RecipeFragment>(R.id.mainContainer, args = bundle)
            setReorderingAllowed(true)
            addToBackStack("recipes_list_fragment")
        }
    }
}