package ru.uspehovmax.shoppinglist.data

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import ru.uspehovmax.shoppinglist.domain.ShopItem
import ru.uspehovmax.shoppinglist.domain.ShopItem.Companion.UNDEFINED_ID
import ru.uspehovmax.shoppinglist.domain.ShopListRepository
import kotlin.random.Random

class ShopListRepositoryImpl(
    application: Application
) : ShopListRepository {
    /*
    Нам нужно преобразовать сущности ShopItem в ShopItemDbModel и обратно
    Для преобразования одних объектов в другие применяются т.н. мапперы Mapper
     */
    private val shopListDao = AppDataBase.getInstance(application).shopListDao()
    private val mapper = ShopListMapper()

    private val db = AppDataBase.getInstance(application)

/*  // изменили реализацию - БД
    private var autoIncrementId = 0
    private val shopListLD = MutableLiveData<List<ShopItem>>()
    private val shopList = sortedSetOf<ShopItem>({ o1, o2 -> o1.id.compareTo(o2.id) })

    init {
        for (i in 0 until 500) {
            val item = ShopItem("Name $i",i, Random.nextBoolean())
            addShopItem(item)
        }
    }*/

    override suspend fun addShopItem(shopItem: ShopItem) {
/*      // изменили реализацию - БД
        if (shopItem.id == UNDEFINED_ID) {
            shopItem.id = autoIncrementId++
        }
        shopList.add(shopItem)
        updateList()*/
        shopListDao.addShopItem(mapper.mapEntityToDbModel(shopItem))
    }

    override suspend fun deleteShopItem(shopItem: ShopItem) {
/*        // изменили реализацию - БД
        shopList.remove(shopItem)
        updateList()*/
        shopListDao.deleteShopItem(shopItem.id)
    }

    override suspend fun editShopItem(shopItem: ShopItem) {
/*        // изменили реализацию - БД
        val oldItem = getShopItem(shopItem.id)
        shopList.remove(oldItem)
        addShopItem(shopItem)*/
        shopListDao.addShopItem(mapper.mapEntityToDbModel(shopItem))
    }

    override suspend fun getShopItem(shopItemId: Int): ShopItem {
/*        // изменили реализацию - БД
        return shopList.find {
            it.id == shopItemId
        } ?: throw RuntimeException("Item with id $shopItemId not found")*/
        val dbModel = shopListDao.getShopItem(shopItemId)
        return mapper.mapDbModelToEntity(dbModel)
    }

    // лучше использовать Transformations.map вместо MediatorLiveData (под капотом тот же Mediator)
    //  для преобразования списка из БД в LiveData<List<ShopItem>>
    override fun getShopList(): LiveData<List<ShopItem>> = Transformations.map(
        shopListDao.getShopList()
    ) {
        mapper.mapListDbModelToListEntity(it)
    }

/*    // используем MediatorLiveData для преобразования списка из БД в LiveData<List<ShopItem>>
    override fun getShopList(): LiveData<List<ShopItem>> = MediatorLiveData<List<ShopItem>>()
        .apply {
            addSource(shopListDao.getShopList()) {
                value = mapper.mapListDbModelToListEntity(it)
            }
        }

        // изменили реализацию - БД
        return shopListLD*/

/*     // изменили реализацию - БД
    private fun updateList() {
        shopListLD.value = shopList.toList()
}*/

}