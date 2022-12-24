package ru.uspehovmax.shoppinglist.domain

class DeleteShopItemUseCase (private val shopListRepository: ShopListRepository) {
    fun deleteItem(shopItem: ShopItem) {
        shopListRepository.deleteShopItem(shopItem)

    }
}