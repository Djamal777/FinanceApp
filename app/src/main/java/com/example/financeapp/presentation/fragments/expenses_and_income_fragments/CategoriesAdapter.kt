package com.example.financeapp.presentation.fragments.expenses_and_income_fragments

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.financeapp.R
import com.example.financeapp.databinding.CategoryItemBinding
import com.example.financeapp.domain.model.CategoryAndMoney

class CategoriesAdapter(
    private val c: Context,
    private val listener: OnCategoryClickListener
) : RecyclerView.Adapter<CategoriesAdapter.CategoryViewHolder>() {

    inner class CategoryViewHolder(private val binding: CategoryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.apply {
                setOnClickListener {
                    val position = bindingAdapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onCategoryItemClick(differ.currentList[position])
                    }
                }
                setOnLongClickListener {
                    val position = bindingAdapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onCategoryItemLongClick(differ.currentList[position])
                    }
                    true
                }
            }
        }

        fun bind(item: CategoryAndMoney) {
            binding.apply {
                expCategoryName.text = item.categoryName
                expCategoryImage.setImageDrawable(
                    AppCompatResources.getDrawable(
                        c,
                        c.resources.getIdentifier(item.icon, "drawable", c.packageName)
                    )
                )
                item.plan?.let {
                    expPlanMoney.text = c.resources.getString(R.string.money_plan, it)
                    if (item.moneyAmount > it) {
                        expPlanMoney.setTextColor(Color.RED)
                    } else {
                        expPlanMoney.setTextColor(Color.GREEN)
                    }
                }
                expCategoryMoney.text = c.resources.getString(R.string.money,"", item.moneyAmount)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder(
            CategoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val item = differ.currentList[holder.bindingAdapterPosition]
        holder.bind(item)
    }

    override fun getItemCount() = differ.currentList.size

    private val differCallback = object : DiffUtil.ItemCallback<CategoryAndMoney>() {
        override fun areItemsTheSame(
            oldItem: CategoryAndMoney,
            newItem: CategoryAndMoney
        ): Boolean {
            return oldItem.categoryId == newItem.categoryId
        }

        override fun areContentsTheSame(
            oldItem: CategoryAndMoney,
            newItem: CategoryAndMoney
        ): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    interface OnCategoryClickListener {
        fun onCategoryItemClick(category: CategoryAndMoney)
        fun onCategoryItemLongClick(category: CategoryAndMoney)
    }
}