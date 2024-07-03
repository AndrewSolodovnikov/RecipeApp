package com.sol.recipeapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.sol.recipeapp.databinding.FragmentListCategoriesBinding

class CategoriesListFragment : Fragment() {
    private var _binding: FragmentListCategoriesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentListCategoriesBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val dataset = listOf(
            Category(0, "Бургеры", "Рецепты всех популярных видов бургеров", "burger.png"),
            Category(1, "Десерты", "Самые вкусные рецепты десертов специально для вас", "dessert.png"),
            Category(2, "Пицца", "Пицца на любой вкус и цвет. Лучшая подборка для тебя", "pizza.png"),
            Category(3, "Рыба", "Печеная, жареная, сушеная, любая рыба на твой вкус", "fish.png"),
            Category(4, "Супы", "От классики до экзотики: мир в одной тарелке", "soup.png"),
            Category(5, "Салаты", "Хрустящий калейдоскоп под соусом вдохновения", "salad.png"),
        )
        val customAdapter = CategoriesListAdapter(dataset)

        binding.rvCategory.layoutManager = GridLayoutManager(context, 2)
        binding.rvCategory.adapter = customAdapter
    }

}