package ru.uspehovmax.shoppinglist.presentation

import android.view.View
import android.widget.TextView
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import ru.uspehovmax.shoppinglist.R
import ru.uspehovmax.shoppinglist.databinding.ItemShopDisabledBinding

class ShopItemViewHolder (
    val binding: ViewDataBinding
    ) : RecyclerView.ViewHolder(binding.root)
//{
//    val tvName = view.findViewById<TextView>(R.id.tv_name)
//    val tvCount = view.findViewById<TextView>(R.id.tv_count)
//}