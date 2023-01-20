package ru.uspehovmax.shoppinglist.domain

class DeleteShopItemUseCase (private val shopListRepository: ShopListRepository) {
    suspend fun deleteItem(shopItem: ShopItem) {
        shopListRepository.deleteShopItem(shopItem)

    }
}