package com.example.financeapp.presentation.fragments.finances_fragment

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.financeapp.R
import com.example.financeapp.data.local.entities.Account
import com.example.financeapp.databinding.AccountItemBinding

class AccountsAdapter(
    private val listener: OnAccountClickListener,
    private val c: Context
) : RecyclerView.Adapter<AccountsAdapter.AccountViewHolder>() {

    inner class AccountViewHolder(private val binding: AccountItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.apply {
                setOnClickListener {
                    val position = bindingAdapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onAccountItemClick(position)
                    }
                }
                setOnLongClickListener {
                    val position = bindingAdapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onAccountItemLongClick(differ.currentList[position])
                    }
                    true
                }
            }
        }

        fun bind(item: Account) {
            binding.apply {
                accName.text = item.accName
                accMoney.text = c.resources.getString(R.string.money,"", item.money)
                accImage.setImageDrawable(
                    AppCompatResources.getDrawable(
                        c,
                        c.resources.getIdentifier(item.icon, "drawable", c.packageName)
                    )
                )
            }
        }

        //Создать другой backGround
        fun selected() {
            binding.accImage.background =
                AppCompatResources.getDrawable(c, R.drawable.gradient_clicked)
            Log.d("ViewHolder", "item $bindingAdapterPosition selected: yes")
        }

        fun default() {
            binding.accImage.background = AppCompatResources.getDrawable(c, R.drawable.gradient)
            Log.d("ViewHolder", "item $bindingAdapterPosition selected: no")
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AccountViewHolder {
        return AccountViewHolder(
            AccountItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: AccountViewHolder, position: Int) {
        val item = differ.currentList[position]
        if (item.selected) {
            holder.selected()
        } else {
            holder.default()
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