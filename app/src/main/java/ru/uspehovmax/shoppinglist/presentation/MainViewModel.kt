package ru.uspehovmax.shoppinglist.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import ru.uspehovmax.shoppinglist.data.ShopListRepositoryImpl
import ru.uspehovmax.shoppinglist.domain.*
/**
 * 1. Создать реализацию интерфейса "repository" - ShopListRepositoryImpl
 * 2. В создать переменные на UseCase-ы и добавить в них параметр - repository
 * 3. Создать методы реализующие функции из UseCase-ов
 */

class MainViewModel : ViewModel() {

    private val repository = ShopListRepositoryImpl
    private val getShopListUseCase = GetShopListUseCase(repository)
    private val editShopItemUseCase = EditShopItemUseCase(repository)
    private val deleteShopItemUseCase = DeleteShopItemUseCase(repository)
    private val addShopItemUseCase = AddShopItemUseCase(repository)

    val shopListViewModel = getShopListUseCase.getShopList()

    fun deleteShopItemUseCase(shopItem: ShopItem) {
        deleteShopItemUseCase.deleteItem(shopItem)
    }

    fun changeEnableState(shopItem: ShopItem) {
        val newItem = shopItem.copy(enabled = !shopItem.enabled)
        editShopItemUseCase.editShopItem(newItem)
    }

//    fun addShopItem(shopItem: ShopItem) {
//        addShopItemUseCase.addShopItem(shopItem)
//        Log.d("msg", "addShopItem")
//    }
}
/*
1.
 */