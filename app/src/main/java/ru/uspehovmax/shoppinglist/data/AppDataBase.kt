package ru.uspehovmax.shoppinglist.data

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ShopItemDbModel::class, ], version = 1, exportSchema = false)
abstract class AppDataBase: RoomDatabase() {

    abstract fun shopListDao(): ShopListDao

    companion object{
        private var INSTANCE : AppDataBase? = null
        private val LOCK = Any()
        private const val DB_NAME = "shop_item.db"

        fun getInstance(application: Application):AppDataBase {
            // методика дабл чек - двойная проверка
            INSTANCE?.let {
                return it
            }
            // критичиский блок, чтобы два потока не смогли одновременно создать две БД
            // добавлена вторая проверка  INSTANCE?.let
            synchronized(LOCK) {
                INSTANCE?.let {
                    return it
                }
                val db = Room.databaseBuilder(
                    application,
                    AppDataBase::class.java,
                    DB_NAME
                )
                    //.allowMainThreadQueries() // убрать после!!!
                    .build()
                INSTANCE = db
                return db
            }
        }
    }
}