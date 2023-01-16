package ru.uspehovmax.shoppinglist.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.TextInputLayout
import ru.uspehovmax.shoppinglist.R
import ru.uspehovmax.shoppinglist.domain.ShopItem

class ShopItemActivity : AppCompatActivity(), ShopItemFragment.OnEditingFinishedListener {

/*    private lateinit var viewModelShopItem: ShopItemViewModel

    private lateinit var tilName: TextInputLayout
    private lateinit var tilCount: TextInputLayout
    private lateinit var etName: EditText
    private lateinit var etCount: EditText
    private lateinit var buttonSave: Button*/

    private var screenMode = MODE_UNKNOWN
    private var shopItemId = ShopItem.UNDEFINED_ID

    companion object {
        private const val EXTRA_SCREEN_MODE = "extra_mode"
        private const val EXTRA_SHOP_ITEM_ID = "extra_shop_item"
        private const val MODE_EDIT = "mode_edit"
        private const val MODE_ADD = "mode_add"
        private const val MODE_UNKNOWN = ""

        fun newIntentAddItem(context: Context): Intent {
            val intent = Intent(context, ShopItemActivity::class.java)
            intent.putExtra(EXTRA_SCREEN_MODE,MODE_ADD)
            return intent
        }

        fun newIntentEditItem(context: Context, shopItemId: Int): Intent {
            val intent = Intent(context, ShopItemActivity::class.java)
            intent.putExtra(EXTRA_SCREEN_MODE,MODE_EDIT)
            intent.putExtra(EXTRA_SHOP_ITEM_ID,shopItemId)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // установка контента
        setContentView(R.layout.activity_shop_item)

        // проверка переданного из интента
        parseIntent()
/*
        // инициализируем viewModel через ViewModelProvider
//        viewModelShopItem = ViewModelProvider(this)[ShopItemViewModel::class.java]

        // инициализируем  view элементы
//        initViews()
        // сброс ошибки
//        addTextChangeListeners() */

        // выбранный режим
        if (savedInstanceState == null) {
            launchRightMode()
        }
//        observeViewModel()
    }

    override fun onEditingFinished() {
        finish()
    }

/*    private fun observeViewModel(){
        // при неправильном вводе Count появится сообщение
        viewModelShopItem.errorInputCount.observe(this) {
            val message = if(it) {
                getString(R.string.error_input_count)
            } else {
                null
            }
            tilCount.error = message
        }

        // при неправильном вводе Name появится сообщение
        viewModelShopItem.errorInputName.observe(this) {
            val message = if(it) {
                getString(R.string.error_input_name)
            } else {
                null
            }
            tilName.error = message
        }

        viewModelShopItem.shouldCloseScreen.observe(this) {
            // при срабатывании LiveData<Unit>
            // Закрытие активити, возврат к MainActivity
            finish()
        }
    }*/

    private fun launchRightMode() {
        val fragment = when (screenMode) {
            MODE_EDIT -> ShopItemFragment.newInstanceEditItem(shopItemId)
            MODE_ADD  -> ShopItemFragment.newInstanceAddItem()
            else      -> throw RuntimeException("Unknown screen mode $screenMode")
        }
        supportFragmentManager.beginTransaction()
            .replace(R.id.shop_item_container, fragment)
            .commit()
    }

/*    private fun addTextChangeListeners() {
        // --
        etName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModelShopItem.resetErrorName()
            }
            override fun afterTextChanged(p0: Editable?) {
            }
        })
        // --
        etCount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModelShopItem.resetErrorCount()
            }
            override fun afterTextChanged(p0: Editable?) {
            }
        })

    }*/

/*    private fun launchAddMode() {
//        viewModelShopItem.getShopItem(shopItemId)
//        viewModelShopItem.shopItem.observe(this){
//            etName.setText(it.name)
//            etCount.setText(it.count.toString())
//        }
        buttonSave.setOnClickListener{
            viewModelShopItem.addShopItem(etName.text?.toString(), etCount.text?.toString())
        }
    }*/

/*    private fun launchEditMode() {
        viewModelShopItem.getShopItem(shopItemId)
        viewModelShopItem.shopItem.observe(this){
            etName.setText(it.name)
            etCount.setText(it.count.toString())
        }
        buttonSave.setOnClickListener{
            viewModelShopItem.editShopItem(etName.text?.toString(), etCount.text?.toString())
        }
    }*/

/*    private fun initViews() {
        // инициализируем переменные, связывая с разметкой
        tilName = findViewById(R.id.til_name)
        tilCount = findViewById(R.id.til_count)
        etName = findViewById(R.id.et_name)
        etCount = findViewById(R.id.et_count)
        buttonSave = findViewById(R.id.save_button)
    }*/

    private fun parseIntent(){
        if (!intent.hasExtra(EXTRA_SCREEN_MODE)) {
            throw RuntimeException("Param screen mode is absent")
        }
        val mode = intent.getStringExtra(EXTRA_SCREEN_MODE)
        if (mode != MODE_EDIT && mode!= MODE_ADD) {
            throw RuntimeException("Unknown screen mode $mode")
        }
        screenMode = mode
        if(screenMode == MODE_EDIT ) {
            if(!intent.hasExtra(EXTRA_SHOP_ITEM_ID)){
                throw RuntimeException("Param shop itemId is absent")
            }
            shopItemId = intent.getIntExtra(EXTRA_SHOP_ITEM_ID,ShopItem.UNDEFINED_ID)
        }
    }


}