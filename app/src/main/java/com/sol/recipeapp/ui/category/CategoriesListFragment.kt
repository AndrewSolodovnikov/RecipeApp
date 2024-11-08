package com.sol.recipeapp.ui.category

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.sol.recipeapp.databinding.FragmentListCategoriesBinding

class CategoriesListFragment : Fragment() {
    private val binding by lazy { FragmentListCategoriesBinding.inflate(layoutInflater) }
    private val viewModel: CategoriesListViewModel by viewModels()

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

        customAdapter.setOnItemClickListener(object : CategoriesListAdapter.OnItemClickListener {
            override fun onItemClick(categoryId: Int) {
                Log.i("!!!info", "categoryId = $categoryId")
                openRecipesByCategoryId(categoryId)
            }
        })

        viewModel.categoriesListState.observe(viewLifecycleOwner) { state ->
            customAdapter.updateData(state.dataSet)
            state.errorMessage?.let {
                Log.i("!!!toast", "value errorMessage UI ${state.errorMessage}")
                Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
            }
        }
    }

    fun openRecipesByCategoryId(categoryId: Int) {
        val category = viewModel.categoriesListState.value?.dataSet?.find { it.id == categoryId }

        category?.let {
            findNavController().navigate(
                CategoriesListFragmentDirections
                    .actionCategoriesListFragmentToRecipesListFragment(it)
            )
        } ?: throw IllegalArgumentException("Category with categoryId = $categoryId not found")
    }
}