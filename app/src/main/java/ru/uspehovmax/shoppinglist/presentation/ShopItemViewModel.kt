package ru.uspehovmax.shoppinglist.presentation

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
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
class ShopItemViewModel(application: Application) : AndroidViewModel(application) {

    /*
    Во ViewModel нужно работать в гавном потоке и завершать scope в onCleared()
    Одно из правил корутин - они должны быть запущены внутри scope с опред.ЖЦ
    Чтобы использовать корутины внутри ViewModel - испо-ем scope у которого ЖЦ = ЖЦ ViewModel

     */

    // конструктор CoroutineScope и передать параметром контекст корутины - Dispatchers
    // Dispatchers определяет на каком потоке выполняется корутина
    // Dispatchers.IO - выполенени операций чтения/записи до 64 потоков, Main - на главном потоке,
    // Dispatchers.default - столько потоков сколько ядер проц-а
//    private val scope = CoroutineScope(Dispatchers.Main)
    //  Поэтому нужно использовать viewModelScope !


    private val repository = ShopListRepositoryImpl(application)
    private val getShopItemUseCase = GetShopItemUseCase(repository)
    private val addShopItemUseCase = AddShopItemUseCase(repository)
    private val editShopItemUseCase = EditShopItemUseCase(repository)

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
        viewModelScope.launch {
            val item = getShopItemUseCase.getShopItem(shopItemId)
            _shopItem.value = item
        }
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
            viewModelScope.launch {
                val shopItem = ShopItem(name, count, enabled = true)
                addShopItemUseCase.addShopItem(shopItem)
                finishWork()
            }
        }
    }

    fun editShopItem(inputName: String?, inputCount: String?) {
        val name = parseName(inputName)
        val count = parseCount(inputCount)
        val fieldsValid = validateInput(name, count)
        if (fieldsValid) {
            _shopItem.value?.let {
                viewModelScope.launch {
                    val item = it.copy(name = name, count = count)
                    editShopItemUseCase.editShopItem(item)
                    finishWork()
                }
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

    fun resetErrorInputName() {
        _errorInputName.value = false
    }

    fun resetErrorInputCount() {
        _errorInputCount.value = false
    }

    private fun finishWork() {
        _shouldCloseScreen.value = Unit
    }

    // при использовании viewModelScope не нужна - все автоматом
    // корутина отмена
//    override fun onCleared() {
//        super.onCleared()
//        scope.cancel()
//    }
}