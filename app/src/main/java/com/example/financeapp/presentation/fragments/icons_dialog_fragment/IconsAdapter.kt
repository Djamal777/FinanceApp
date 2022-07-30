package com.example.financeapp.presentation.fragments.icons_dialog_fragment

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.example.financeapp.databinding.IconItemBinding
import com.example.financeapp.presentation.fragments.finances_fragment.AccountsAdapter
import com.example.financeapp.util.getAllDrawable

class IconsAdapter(
    private val listener: OnIconClickListener,
    private val c: Context
) : RecyclerView.Adapter<IconsAdapter.IconViewHolder>() {

    inner class IconViewHolder(private val binding: IconItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener.onIconItemClick(icons[position])
                }
            }
        }

        fun bind(item: String) {
            binding.root.setImageDrawable(
                AppCompatResources.getDrawable(
                    c,
                    c.resources.getIdentifier(item, "drawable", c.packageName)
                )
            )
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): IconsAdapter.IconViewHolder {
        return IconViewHolder(
            IconItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    private val icons= getAllDrawable(c)

    override fun onBindViewHolder(holder: IconsAdapter.IconViewHolder, position: Int) {
        val item = icons[position]
        holder.bind(item)
    }

    override fun getItemCount() = icons.size

    interface OnIconClickListener {
        fun onIconItemClick(tag:String)
    }
}