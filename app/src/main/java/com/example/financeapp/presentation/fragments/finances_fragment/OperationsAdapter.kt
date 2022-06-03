package com.example.financeapp.presentation.fragments.finances_fragment

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.financeapp.R
import com.example.financeapp.data.local.entities.Account
import com.example.financeapp.data.local.entities.Operation
import com.example.financeapp.databinding.OperationItemBinding
import com.example.financeapp.domain.model.OperationAndCategoryAndAccount

class OperationsAdapter(
    private val c: Context
):RecyclerView.Adapter<OperationsAdapter.OperationViewHolder>() {

    inner class OperationViewHolder(private val binding: OperationItemBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(item: OperationAndCategoryAndAccount){
            binding.apply {
                accImage.setImageDrawable(AppCompatResources.getDrawable(c, item.icon))
                accImage.background=AppCompatResources.getDrawable(c, R.drawable.gradient)
                accName.text=item.accName
                categoryName.text=item.categoryName
                money.text=c.resources.getString(R.string.money,item.money)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OperationViewHolder {
        return OperationViewHolder(
            OperationItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: OperationViewHolder, position: Int) {
        val item=differ.currentList[holder.bindingAdapterPosition]
        holder.bind(item)
    }

    override fun getItemCount() = differ.currentList.size

    private val differCallback = object : DiffUtil.ItemCallback<OperationAndCategoryAndAccount>() {
        override fun areItemsTheSame(oldItem: OperationAndCategoryAndAccount, newItem: OperationAndCategoryAndAccount): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: OperationAndCategoryAndAccount, newItem: OperationAndCategoryAndAccount): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)
}