package ru.uspehovmax.shoppinglist.presentation

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import ru.uspehovmax.shoppinglist.data.ShopListRepositoryImpl
import ru.uspehovmax.shoppinglist.domain.*
/**
 * 1. Создать реализацию интерфейса "repository" - ShopListRepositoryImpl
 * 2. В создать переменные на UseCase-ы и добавить в них параметр - repository
 * 3. Создать методы реализующие функции из UseCase-ов
 */

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = ShopListRepositoryImpl(application)
    private val getShopListUseCase = GetShopListUseCase(repository)
    private val editShopItemUseCase = EditShopItemUseCase(repository)
    private val deleteShopItemUseCase = DeleteShopItemUseCase(repository)
/*
    private val addShopItemUseCase = AddShopItemUseCase(repository)
*/

    /*
Во ViewModel нужно работать в гавном потоке и завершать scope в onCleared()
Одно из правил корутин - они должны быть запущены внутри scope с опред.ЖЦ
Чтобы использовать корутины внутри ViewModel - испо-ем scope у которого ЖЦ = ЖЦ ViewModel

 */
    // конструктор CoroutineScope и передать параметром контекст корутины - Dispatchers
    // Dispatchers определяет на каком потоке выполняется корутина
    // Dispatchers.IO - выполенени операций чтения/записи до 64 потоков, Main - на главном потоке,
    // Dispatchers.default - столько потоков сколько ядер проц-а
    // конструктор CoroutineScope
//    private val scope = CoroutineScope(Dispatchers.Main)
    //  Поэтому нужно использовать viewModelScope !

    val shopListViewModel = getShopListUseCase.getShopList()

    fun deleteShopItemUseCase(shopItem: ShopItem) {
        viewModelScope.launch{
            deleteShopItemUseCase.deleteItem(shopItem)
        }
    }

    fun changeEnableState(shopItem: ShopItem) {
        viewModelScope.launch {
            val newItem = shopItem.copy(enabled = !shopItem.enabled)
            editShopItemUseCase.editShopItem(newItem)
        }
    }

    // при использовании viewModelScope не нужна - все автоматом
    // корутина отмена
//    override fun onCleared() {
//        super.onCleared()
//        scope.cancel()
//    }

    //    fun addShopItem(shopItem: ShopItem) {
//        addShopItemUseCase.addShopItem(shopItem)
//        Log.d("msg", "addShopItem")
//    }
}
