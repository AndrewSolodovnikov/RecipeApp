package com.sol.recipeapp.ui.category

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.sol.recipeapp.ARG_CATEGORY_ID
import com.sol.recipeapp.ARG_CATEGORY_IMAGE_URL
import com.sol.recipeapp.ARG_CATEGORY_NAME
import com.sol.recipeapp.R
import com.sol.recipeapp.databinding.FragmentListCategoriesBinding
import com.sol.recipeapp.ui.recipes.recipeslist.RecipesListFragment

class CategoriesListFragment : Fragment() {
    private val binding by lazy { FragmentListCategoriesBinding.inflate(layoutInflater) }
    private val viewModel: CategoriesListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initUI()
    }

    private fun initUI() {
        binding.rvCategory.layoutManager = GridLayoutManager(context, 2)

        viewModel.categoriesListState.observe(viewLifecycleOwner) { state ->
            val customAdapter = CategoriesListAdapter(state.dataSet)
            binding.rvCategory.adapter = customAdapter

            customAdapter.setOnItemClickListener(object : CategoriesListAdapter.OnItemClickListener {
                override fun onItemClick(categoryId: Int) {
                    openRecipesByCategoryId(categoryId)
                }
            })
        }
    }

    fun openRecipesByCategoryId(categoryId: Int) {
        val category = viewModel.categoriesListState.value?.dataSet?.find { it.id == categoryId }
        Log.i("!!!info", "category = $category")
        val categoryName = category?.title
        val categoryImageUrl = category?.imageUrl

        val bundle = bundleOf(
            ARG_CATEGORY_ID to categoryId,
            ARG_CATEGORY_NAME to categoryName,
            ARG_CATEGORY_IMAGE_URL to categoryImageUrl)

        parentFragmentManager.commit {
            replace<RecipesListFragment>(R.id.mainContainer, args = bundle)
            setReorderingAllowed(true)
            addToBackStack("categories_list_fragment")
        }

    }
}