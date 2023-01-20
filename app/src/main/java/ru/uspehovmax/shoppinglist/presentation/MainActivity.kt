package ru.uspehovmax.shoppinglist.presentation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import ru.uspehovmax.shoppinglist.R
import ru.uspehovmax.shoppinglist.databinding.ActivityMainBinding

/**
 * 1. Объявить viewModel, ShopListAdapter, binding
 * 2. В  onCreate добавить viewModel и сделать observe LiveData (TextView)
 * 3. В  onCreate добавить listener-ы нажатий и свайпа
 */
class MainActivity : AppCompatActivity(), ShopItemFragment.OnEditingFinishedListener  {

    private lateinit var viewModel: MainViewModel // by viewModel()

    //    private lateinit var rvShopList: RecyclerView
    private lateinit var shopListAdapter: ShopListAdapter
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // связываем разметку и переменную и ставим слушателей
        setupRecyclerView()

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        viewModel.shopListViewModel.observe(this) {
//            shopListAdapter.shopList = it     //поменяли реализацию после ListAdapter<...>
            shopListAdapter.submitList(it)
        }

//        val buttonAddItem = findViewById<FloatingActionButton>(R.id.button_add_shop_item)
        binding.buttonAddShopItem.setOnClickListener {
            if (isOnePaneMode()) {
                val intent = ShopItemActivity.newIntentAddItem(this)
                startActivity(intent)
            } else {
                launchFragment(ShopItemFragment.newInstanceAddItem())
            }
        }

    }

    override fun onEditingFinished() {
        Toast.makeText(this@MainActivity, "Success", Toast.LENGTH_SHORT).show()
        supportFragmentManager.popBackStack()
    }

    private fun isOnePaneMode(): Boolean {
        return binding.shopItemContainer == null
    }

    private fun launchFragment(fragment: Fragment) {
        supportFragmentManager.popBackStack()
        supportFragmentManager.beginTransaction()
            .replace(R.id.shop_item_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun setupRecyclerView() {
        // связываем разметку и переменную
/*        val rvShopList = findViewById<RecyclerView>(R.id.rv_shop_list)*/
        with(binding.rvShopList) {
            // можно with (rvShopList) - оставил для наглядности, где rvShopList
            shopListAdapter = ShopListAdapter()
            adapter = shopListAdapter
            // Установка кол-ва элементов в пуле для каждой карточки(cardView) rv - MAX_POOL_SIZE
            recycledViewPool.setMaxRecycledViews(
                ShopListAdapter.VIEW_TYPE_ENABLED,
                ShopListAdapter.MAX_POOL_SIZE
            )
            recycledViewPool.setMaxRecycledViews(
                ShopListAdapter.VIEW_TYPE_DISABLED,
                ShopListAdapter.MAX_POOL_SIZE
            )
        }
        // ставим слушателей
        setupLongClickListener()
        setupClickListener()
        setupSwipeListener(binding.rvShopList)
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
            Log.d("MainActivity", it.toString())
            if (isOnePaneMode()) {
                val intent = ShopItemActivity.newIntentEditItem(this, it.id)
                startActivity(intent)
            } else {
                launchFragment(ShopItemFragment.newInstanceEditItem(it.id))
            }
        }
    }

    private fun setupLongClickListener() {
        shopListAdapter.onShopItemLongClickListener = {
            viewModel.changeEnableState(it)
        }
    }


}
