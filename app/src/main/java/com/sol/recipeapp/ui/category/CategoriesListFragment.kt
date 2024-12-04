package com.sol.recipeapp.ui.category

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.sol.recipeapp.R
import com.sol.recipeapp.RecipesApplication
import com.sol.recipeapp.databinding.FragmentListCategoriesBinding
import com.sol.recipeapp.di.AppContainer

class CategoriesListFragment : Fragment() {
    private val binding by lazy { FragmentListCategoriesBinding.inflate(layoutInflater) }
    private lateinit var categoriesListViewModel: CategoriesListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val appContainer: AppContainer =
            (requireActivity().application as RecipesApplication).appContainer
        categoriesListViewModel = appContainer.categoryListViewModelFactory.create()
    }
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
        val customAdapter = CategoriesListAdapter(emptyList())
        binding.rvCategory.adapter = customAdapter
        categoriesListViewModel.loadCategory()

        customAdapter.setOnItemClickListener(object : CategoriesListAdapter.OnItemClickListener {
            override fun onItemClick(categoryId: Int) {
                categoriesListViewModel.openRecipesByCategoryId(categoryId)
            }
        })

        categoriesListViewModel.categoriesListState.observe(viewLifecycleOwner) { state ->
            if (state.dataSet != null) {
                customAdapter.updateData(state.dataSet)
            } else {
                val errorData = getString(R.string.error_retrofit_data)
                Toast.makeText(requireContext(), errorData, Toast.LENGTH_LONG).show()
            }

            if (state.navigateToCategory == null) {
                customAdapter.setOnItemClickListener(object :
                    CategoriesListAdapter.OnItemClickListener {
                    override fun onItemClick(categoryId: Int) {
                        categoriesListViewModel.openRecipesByCategoryId(categoryId)
                    }
                })
            } else {
                customAdapter.setOnItemClickListener(null)
                findNavController().navigate(
                    CategoriesListFragmentDirections
                        .actionCategoriesListFragmentToRecipesListFragment(state.navigateToCategory)
                )
                categoriesListViewModel.clearNavigation()
            }
        }
    }

}