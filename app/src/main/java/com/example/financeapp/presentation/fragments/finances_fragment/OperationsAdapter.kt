package com.example.financeapp.presentation.fragments.finances_fragment

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.financeapp.R
import com.example.financeapp.data.local.CategoryType
import com.example.financeapp.databinding.OperationItemBinding
import com.example.financeapp.databinding.OperationsHeaderBinding
import com.example.financeapp.domain.model.OperationAndCategoryAndAccount
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter
import java.util.*

class OperationsAdapter(
    private val c: Context,
    private val listener: OnOperationClickListener?
) : RecyclerView.Adapter<OperationsAdapter.OperationViewHolder>(),
    StickyRecyclerHeadersAdapter<OperationsAdapter.HeaderViewHolder> {

    inner class HeaderViewHolder(private val binding: OperationsHeaderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(date: String) {
            binding.date.text = date
        }
    }

    inner class OperationViewHolder(private val binding: OperationItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.apply {
                setOnClickListener {
                    val position = bindingAdapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        listener?.onOperationClickListener(differ.currentList[position])
                    }
                }
            }
        }

        fun bind(item: OperationAndCategoryAndAccount) {
            binding.apply {
                accImage.setImageDrawable(
                    AppCompatResources.getDrawable(
                        c,
                        c.resources.getIdentifier(item.icon, "drawable", c.packageName)
                    )
                )
                accImage.background = AppCompatResources.getDrawable(c, R.drawable.gradient)
                accName.text = item.accName
                categoryName.text = item.categoryName
                if (item.categoryType == CategoryType.INCOME) {
                    money.text = c.resources.getString(R.string.money, "+", item.money)
                    money.setTextColor(Color.GREEN)
                } else {
                    money.text = c.resources.getString(R.string.money, "-", item.money)
                    money.setTextColor(Color.RED)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OperationViewHolder {
        return OperationViewHolder(
            OperationItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: OperationViewHolder, position: Int) {
        val item = differ.currentList[holder.bindingAdapterPosition]
        holder.bind(item)
    }

    override fun getItemCount() = differ.currentList.size

    private val differCallback = object : DiffUtil.ItemCallback<OperationAndCategoryAndAccount>() {
        override fun areItemsTheSame(
            oldItem: OperationAndCategoryAndAccount,
            newItem: OperationAndCategoryAndAccount
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: OperationAndCategoryAndAccount,
            newItem: OperationAndCategoryAndAccount
        ): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    interface OnOperationClickListener {
        fun onOperationClickListener(operation: OperationAndCategoryAndAccount)
    }

    override fun getHeaderId(position: Int): Long {
        return differ.currentList[position].date
    }

    override fun onCreateHeaderViewHolder(parent: ViewGroup?): OperationsAdapter.HeaderViewHolder {
        return HeaderViewHolder(
            OperationsHeaderBinding.inflate(LayoutInflater.from(parent?.context), parent, false)
        )
    }

    override fun onBindHeaderViewHolder(headerViewHolder: HeaderViewHolder?, i: Int) {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = differ.currentList[i].date
        val date = "${calendar.get(Calendar.DATE)} ${
            calendar.getDisplayName(
                Calendar.MONTH,
                Calendar.LONG,
                Locale.getDefault()
            )
        } ${calendar.get(Calendar.YEAR)}"
        headerViewHolder?.bind(date)
    }
}