package com.sol.recipeapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import com.sol.recipeapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val navOptions = NavOptions.Builder()
        .setLaunchSingleTop(true)
        .setEnterAnim(R.anim.slide_in_right)
        .setExitAnim(R.anim.slide_out_left)
        .setPopEnterAnim(R.anim.slide_in_left)
        .setPopExitAnim(R.anim.slide_out_right)
        .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.navigationButtonCategory.setOnClickListener {
            findNavController(R.id.nav_host_fragment_container)
                .navigate(R.id.categoriesListFragment, null, navOptions)
        }

        binding.navigationButtonFavorites.setOnClickListener {
            findNavController(R.id.nav_host_fragment_container)
                .navigate(R.id.favoritesFragment, null, navOptions)
        }
    }
}