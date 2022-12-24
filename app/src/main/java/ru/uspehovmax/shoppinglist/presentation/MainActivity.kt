package ru.uspehovmax.shoppinglist.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import ru.uspehovmax.shoppinglist.R

/**
 * 1. Объявить viewModel и ShopListAdapter
 * 2. В  onCreate добавить viewModel и сделать observe LiveData (TextView)
 * 3. В  onCreate добавить listener-ы нажатий и свайпа
 */
class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel // by viewModel()
    //    private lateinit var rvShopList: RecyclerView
    private lateinit var shopListAdapter: ShopListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupRecyclerView()

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        viewModel.shopListViewModel.observe(this) {
//            shopListAdapter.shopList = it     //поменяли реализацию после ListAdapter<...>
            shopListAdapter.submitList(it)
        }

    }

    private fun setupRecyclerView() {
        // связываем разметку и переменную
        val rvShopList = findViewById<RecyclerView>(R.id.rv_shop_list)

        // можно with (rvShopList) - оставил для наглядности, где rvShopList
            shopListAdapter = ShopListAdapter()
            rvShopList.adapter = shopListAdapter
            // Установка кол-ва элементов в пуле для каждой карточки(cardView) rv - MAX_POOL_SIZE
            rvShopList.recycledViewPool.setMaxRecycledViews(
                ShopListAdapter.VIEW_TYPE_ENABLED,
                ShopListAdapter.MAX_POOL_SIZE
            )
            rvShopList.recycledViewPool.setMaxRecycledViews(
                ShopListAdapter.VIEW_TYPE_DISABLED,
                ShopListAdapter.MAX_POOL_SIZE
            )

        // ставим слушателей
        setupLongClickListener()
        setupClickListener()
        setupSwipeListener(rvShopList)
    }

    private fun setupSwipeListener(rvShopList: RecyclerView) {
        val callback = object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
//                val item = shopListAdapter.shopList...
                val item = shopListAdapter.currentList[viewHolder.adapterPosition]
                viewModel.deleteShopItemUseCase(item)
            }
        }
        val itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(rvShopList)
    }

    private fun setupClickListener() {
        shopListAdapter.onShopItemClickListener = {
            Log.d("msg", it.toString())
    //            viewModel.editShopItem(it)
        }
    }

    private fun setupLongClickListener() {
        shopListAdapter.onShopItemLongClickListener = {
            viewModel.changeEnableState(it)
        }
    }



}
