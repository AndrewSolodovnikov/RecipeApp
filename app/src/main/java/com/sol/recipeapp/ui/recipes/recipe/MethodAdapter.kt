package com.sol.recipeapp.ui.recipes.recipe

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sol.recipeapp.databinding.ItemMethodBinding

class MethodAdapter(private var dataSet: List<String> = emptyList()) :
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
                binding.methodStep.text = "${position + 1}. " + this
            }
            binding.dividerMethod.visibility = if (position == dataSet.size - 1) View.GONE else View.VISIBLE
        }
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    fun updateMethodList(data: List<String>) {
        dataSet = data
        notifyDataSetChanged()
    }
}