package com.sol.recipeapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.sol.recipeapp.databinding.ActivityMainBinding

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