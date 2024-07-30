package com.sol.recipeapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.recyclerview.widget.GridLayoutManager
import com.sol.recipeapp.databinding.FragmentListCategoriesBinding

class CategoriesListFragment : Fragment() {
    private val binding by lazy { FragmentListCategoriesBinding.inflate(layoutInflater) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initRecycler()
    }

    private fun initRecycler() {
        val customAdapter = CategoriesListAdapter(STUB.getCategories())

        binding.rvCategory.layoutManager = GridLayoutManager(context, 2)
        binding.rvCategory.adapter = customAdapter

        customAdapter.setOnItemClickListener(object : CategoriesListAdapter.OnItemClickListener {
            override fun onItemClick(categoryId: Int) {
                openRecipesByCategoryId(categoryId)
            }
        })
    }

    fun openRecipesByCategoryId(categoryId: Int) {
        val category = STUB.getCategories().find { it.id == categoryId }
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