package ru.uspehovmax.shoppinglist.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import ru.uspehovmax.shoppinglist.R
import ru.uspehovmax.shoppinglist.domain.ShopItem

class ShopListAdapter : ListAdapter<ShopItem, ShopItemViewHolder>(ShopItemDiffCallback())  {
        // изменили с : RecyclerView.Adapter<ShopListAdapter.ShopItemViewHolder>() {

//    var count = 0
//      поменяли реализацию после ListAdapter<...>
// удалили реализацию - теперь все делает ListAdapter
//    var shopList = listOf<ShopItem>()
//        set(value) {
//            // callback - создаем экз.класса и вызываем для него метод DiffUtil.calculateDiff
//            // результат вычесления diffResult передаётся в адаптер методом diffResult.dispatchUpdatesTo
//            val callback = ShopListDiffCallback(shopList, value)
//            val diffResult = DiffUtil.calculateDiff(callback)
//            diffResult.dispatchUpdatesTo(this)
//            field = value

//            notifyDataSetChanged() // плохое решение - идет перерисовка всего списка
//        }

    var onShopItemLongClickListener: ((ShopItem) -> Unit)? = null
    var onShopItemClickListener: ((ShopItem) -> Unit)? = null

    companion object {
        const val VIEW_TYPE_ENABLED = 100
        const val VIEW_TYPE_DISABLED = 101
        const val MAX_POOL_SIZE = 15
    }

//    interface OnShopItemLongClickListener {
//        fun onShopItemLongClick(shopItem: ShopItem)
//    }

//      поменяли реализацию после ListAdapter<...>
//    override fun getItemCount(): Int {
//        return shopList.size
//    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopItemViewHolder {
//        Log.d("msg", "onCreateViewHolder ${count++}")
        val layout = when (viewType) {
            VIEW_TYPE_ENABLED -> R.layout.item_shop_enabled
            VIEW_TYPE_DISABLED -> R.layout.item_shop_disabled
            else -> throw RuntimeException("Unknown view Type: $viewType")
        }
        val view = LayoutInflater.from(parent.context).inflate(layout, parent, false)
        return ShopItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ShopItemViewHolder, position: Int) {
//        Log.d("msg", "onBindViewHolder ${count++}")
//        val shopItem = shopList[position]  //      поменяли реализацию после ListAdapter<...>
        val shopItem = getItem(position)

        holder.tvName.text = "${shopItem.name} "
        holder.tvCount.text = shopItem.count.toString()
        holder.view.setOnLongClickListener {
            onShopItemLongClickListener?.invoke(shopItem)
            true
        }
        holder.view.setOnClickListener {
            onShopItemClickListener?.invoke(shopItem)
            true
        }
    }

    override fun getItemViewType(position: Int): Int {
//        val item = shopList[position] // поменяли реализацию после ListAdapter<...>
        val item = getItem(position)
        return if (item.enabled) {
            VIEW_TYPE_ENABLED
        } else {
            VIEW_TYPE_DISABLED
        }
    }

//       поменяли реализацию после ListAdapter<...>
//    override fun onViewRecycled(holder: ShopItemViewHolder) {
//        super.onViewRecycled(holder)
//        holder.tvName.text = ""
//        holder.tvCount.text = ""
//        holder.tvName.setTextColor(
//            ContextCompat.getColor(
//                holder.view.context,
//                android.R.color.white
//            )
//        )
//    }


}