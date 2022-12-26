package ru.uspehovmax.shoppinglist.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.uspehovmax.shoppinglist.data.ShopListRepositoryImpl
import ru.uspehovmax.shoppinglist.domain.AddShopItemUseCase
import ru.uspehovmax.shoppinglist.domain.EditShopItemUseCase
import ru.uspehovmax.shoppinglist.domain.GetShopItemUseCase
import ru.uspehovmax.shoppinglist.domain.ShopItem

/**
 * Данная ViewModel будет работать с окном ввода/редактирования (ShopItemActivity)
 *  Данные с полей ввода должны быть сохранены в виде shopItem
 *  MutableLiveData для работы внутри ViewModel,  LiveData для работы снаружи, в Activity
 *  Методы parseName, parseCount, validateInput для проверки корректроности введенных данных
 *  _errorInputName/_errorInputCount наличие ошибки в данных ввода
 *  public fun resetErrorName() для сброса ошибки из Activity
 *  finishWork() и _shouldCloseScreen.value = true для сигнал-и о возможности закрыть окно
 */
class ShopItemViewModel : ViewModel() {

    private val repository2 = ShopListRepositoryImpl
    private val getShopItemUseCase = GetShopItemUseCase(repository2)
    private val addShopItemUseCase = AddShopItemUseCase(repository2)
    private val editShopItemUseCase = EditShopItemUseCase(repository2)

    // MutableLiveData для ShopItemViewModel,  LiveData для ShopItemActivity
    private val _errorInputName = MutableLiveData<Boolean>()
    val errorInputName: LiveData<Boolean>
        get() = _errorInputName

    private val _errorInputCount = MutableLiveData<Boolean>()
    val errorInputCount: LiveData<Boolean>
        get() = _errorInputCount

    private val _shopItem = MutableLiveData<ShopItem>()
    val shopItem: LiveData<ShopItem>
        get() = _shopItem

    private val _shouldCloseScreen = MutableLiveData<Unit>()
    val shouldCloseScreen: LiveData<Unit>
        get() = _shouldCloseScreen

    fun getShopItem(shopItemId: Int) {
        val item = getShopItemUseCase.getShopItem(shopItemId)
        _shopItem.value = item
    }

    /**
     * В полях ввода получаем данные строкового типа
     * для inputName применяем метод parseName
     * в нем - удаление лишних пробелов и если не введено ничего inputName=""
     * inputCount тоже типа String? будет преобразован к типу Int
     * */
    fun addShopItem(inputName: String?, inputCount: String?) {
        val name = parseName(inputName)
        val count = parseCount(inputCount)
        val fieldsValid = validateInput(name, count)
        if (fieldsValid) {
            val shopItem = ShopItem(name, count, enabled = true)
            addShopItemUseCase.addShopItem(shopItem)
            finishWork()
        }
    }

    fun editShopItem(inputName: String?, inputCount: String?) {
        val name = parseName(inputName)
        val count = parseCount(inputCount)
        val fieldsValid = validateInput(name, count)
        if (fieldsValid) {
            _shopItem.value?.let {
                val item = it.copy(name = name, count = count)
                editShopItemUseCase.editShopItem(item)
                finishWork()
            }
        }
    }

    private fun parseName(inputName: String?): String {
        return inputName?.trim() ?: ""
    }

    private fun parseCount(inputCount: String?): Int {
        return try {
            inputCount?.trim()?.toInt() ?: 0
        } catch (e: Exception) {
            0
        }
    }

    private fun validateInput(name: String, count: Int): Boolean {
        var result = true
        if (name.isBlank()) {
            _errorInputName.value = true
            result = false
        }
        if (count <= 0) {
            _errorInputCount.value = true
            result = false
        }
        return result
    }

    public fun resetErrorName() {
        _errorInputName.value = false
    }

    public fun resetErrorCount() {
        _errorInputCount.value = false
    }

    private fun finishWork() {
        _shouldCloseScreen.value = Unit
    }

}