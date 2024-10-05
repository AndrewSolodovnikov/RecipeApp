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
import com.sol.recipeapp.ARG_CATEGORY_ID
import com.sol.recipeapp.R
import com.sol.recipeapp.data.Category
import com.sol.recipeapp.databinding.FragmentListCategoriesBinding
import com.sol.recipeapp.ui.recipes.recipeslist.RecipesListFragment

class CategoriesListFragment : Fragment() {
    private val binding by lazy { FragmentListCategoriesBinding.inflate(layoutInflater) }
    private val viewModel: CategoriesListViewModel by viewModels()
    private var currentCategoryList: List<Category> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {
        val customAdapter = CategoriesListAdapter(emptyList())
        binding.rvCategory.adapter = customAdapter

        customAdapter.setOnItemClickListener(object : CategoriesListAdapter.OnItemClickListener {
            override fun onItemClick(categoryId: Int) {
                openRecipesByCategoryId(categoryId)
            }
        })

        viewModel.categoriesListState.observe(viewLifecycleOwner) { state ->
            customAdapter.updateData(state.dataSet)
            currentCategoryList = state.dataSet
        }
    }

    fun openRecipesByCategoryId(categoryId: Int) {
        val category = currentCategoryList.find { it.id == categoryId }
        Log.i("!!!info", "category = $category")

        val bundle = bundleOf(
            ARG_CATEGORY_ID to categoryId,
            )

        parentFragmentManager.commit {
            replace<RecipesListFragment>(R.id.mainContainer, args = bundle)
            setReorderingAllowed(true)
            addToBackStack("categories_list_fragment")
        }

    }
}