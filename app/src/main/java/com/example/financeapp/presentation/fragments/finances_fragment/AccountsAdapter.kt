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
import com.example.financeapp.databinding.AccountItemBinding
import dagger.assisted.Assisted
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class AccountsAdapter(
    private val listener: OnAccountClickListener,
    private val c: Context
) : RecyclerView.Adapter<AccountsAdapter.AccountViewHolder>() {

    var lastSelectedPosition = -1
    var selectedPosition = -1

    inner class AccountViewHolder(private val binding: AccountItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.apply {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    setOnClickListener {
                        listener.onAccountItemClick(position)
                    }
                    setOnLongClickListener {
                        listener.onAccountItemLongClick(differ.currentList[position])
                        true
                    }
                }
            }
        }

        fun bind(item: Account) {
            binding.apply {
                accName.text = item.accName
                accMoney.text = c.resources.getString(R.string.money,item.money)
                accImage.setImageDrawable(AppCompatResources.getDrawable(c, item.icon))
            }
        }

        //Создать другой backGround
        fun selected() {
            binding.accImage.background = AppCompatResources.getDrawable(c, R.drawable.gradient)
        }

        fun default() {
            binding.accImage.background = AppCompatResources.getDrawable(c, R.drawable.gradient)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AccountsAdapter.AccountViewHolder {
        return AccountViewHolder(
            AccountItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: AccountsAdapter.AccountViewHolder, position: Int) {
        val item = differ.currentList[position]
        if (position == selectedPosition) {
            if (selectedPosition == lastSelectedPosition) {
                holder.default()
            } else {
                holder.selected()
            }
        }
        holder.bind(item)
    }

    override fun getItemCount() = differ.currentList.size

    private val differCallback = object : DiffUtil.ItemCallback<Account>() {
        override fun areItemsTheSame(oldItem: Account, newItem: Account): Boolean {
            return oldItem.accId == newItem.accId
        }

        override fun areContentsTheSame(oldItem: Account, newItem: Account): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    interface OnAccountClickListener {
        fun onAccountItemClick(position: Int)
        fun onAccountItemLongClick(account: Account)
    }

}