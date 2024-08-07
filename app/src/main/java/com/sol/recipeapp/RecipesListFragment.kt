package com.sol.recipeapp

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.recyclerview.widget.LinearLayoutManager
import com.sol.recipeapp.databinding.FragmentRecipesListBinding
import java.io.InputStream

class RecipesListFragment : Fragment() {
    private val binding by lazy { FragmentRecipesListBinding.inflate(layoutInflater) }

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
        initRecycler()
        initUI()
    }

    private fun initUI() {
        try {
            val inputStream: InputStream? = categoryImageUrl?.let { context?.assets?.open(it) }
            val drawable = Drawable.createFromStream(inputStream, null)
            binding.ivRecipesListHeaderImage.setImageDrawable(drawable)
        } catch (e: Exception) {
            Log.e("MyLogError", "Image $categoryImageUrl not found")
        }

        binding.tvRecipesListHeaderTitle.text = categoryName
    }

    private fun initRecycler() {
        val customAdapter = categoryId?.let { STUB.getRecipesByCategoryId(it) }
            ?.let { RecipesListAdapter(it) }

        binding.rvCategory.layoutManager = LinearLayoutManager(context)
        binding.rvCategory.adapter = customAdapter

        customAdapter?.setOnItemClickListener(object : RecipesListAdapter.OnItemClickListener {
            override fun onItemClick(recipeId: Int) {
                openRecipesByRecipeId(recipeId)
            }
        })
    }

    fun openRecipesByRecipeId(recipeId: Int) {
        val recipe = STUB.getRecipeById(recipeId)
        val bundle = bundleOf(ARG_RECIPE to recipe)

        parentFragmentManager.commit {
            replace<RecipeFragment>(R.id.mainContainer, args = bundle)
            setReorderingAllowed(true)
            addToBackStack("recipes_list_fragment")
        }
    }
}