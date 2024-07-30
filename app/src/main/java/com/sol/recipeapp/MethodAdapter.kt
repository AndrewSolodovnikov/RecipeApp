package com.sol.recipeapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sol.recipeapp.databinding.ItemIngredientBinding
import com.sol.recipeapp.databinding.ItemMethodBinding
import java.lang.reflect.Method

class MethodAdapter(private val dataSet: List<String>) :
    RecyclerView.Adapter<MethodAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: ItemMethodBinding) :
        RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemMethodBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        with(viewHolder) {
            with(dataSet[position]) {
                binding.methodStep.text = this
            }
        }
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }
}