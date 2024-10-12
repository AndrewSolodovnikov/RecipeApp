package com.sol.recipeapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import com.sol.recipeapp.databinding.ActivityMainBinding
import com.sol.recipeapp.ui.category.CategoriesListFragment
import com.sol.recipeapp.ui.recipes.favorites.FavoritesFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.navigationButtonCategory.setOnClickListener {
            findNavController(R.id.nav_host_fragment_container).navigate(R.id.categoriesListFragment)
        }

        binding.navigationButtonFavorites.setOnClickListener {
            findNavController(R.id.nav_host_fragment_container).navigate(R.id.favoritesFragment)
        }
    }
}