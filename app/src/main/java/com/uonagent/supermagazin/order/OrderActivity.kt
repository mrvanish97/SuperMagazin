package com.uonagent.supermagazin.order

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.uonagent.supermagazin.R
import com.uonagent.supermagazin.user.UserRepository
import com.uonagent.supermagazin.utils.CurrencyFormatter
import com.uonagent.supermagazin.utils.models.ItemModel
import com.uonagent.supermagazin.utils.models.OrderModel
import kotlinx.android.synthetic.main.activity_order.*
import kotlinx.android.synthetic.main.list_item.*

private const val MODEL_INTENT_TAG = "model"

class OrderActivity : AppCompatActivity(), OrderContract.View {

    private lateinit var item: ItemModel

    private lateinit var itemPhoto: ImageView
    private lateinit var itemTitle: TextView
    private lateinit var itemCost: TextView

    private lateinit var nameField: EditText
    private lateinit var emailField: EditText
    private lateinit var detailsField: EditText

    private lateinit var mPresenter: OrderPresenter

    override fun showInfoMessage(message: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order)

        when {
            savedInstanceState != null -> item = savedInstanceState.getParcelable(MODEL_INTENT_TAG)
            intent.hasExtra(MODEL_INTENT_TAG) -> item = intent.getParcelableExtra(MODEL_INTENT_TAG)
            else -> finish()
        }

        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

        mPresenter = OrderPresenter(this)

        initializeViews()

        draw()
    }

    private fun initializeViews() {
        itemPhoto = item_photo
        itemTitle = item_title
        itemCost = item_cost

        nameField = order_name
        emailField = order_email
        detailsField = order_details
    }

    private fun draw() {
        UserRepository.loadItemPhotoFromStorage(item.photo, itemPhoto, this)
        itemCost.text = CurrencyFormatter.doubleToString(item.cost)
        itemTitle.text = item.title
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)

        outState?.putParcelable(MODEL_INTENT_TAG, item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_order, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.order_menu_send -> {
                mPresenter.sendOrder()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun getOrder(): OrderModel {
        val name = nameField.text.toString()
        val email = emailField.text.toString()
        val details = detailsField.text.toString()
        return OrderModel(item, name, email, details, "")
    }

    override fun showEmptyNameError() {
        order_name_layout.error = getString(R.string.order_empty_name)
    }

    override fun showEmptyEmailError() {
        order_email_layout.error = getString(R.string.order_empty_email)
    }

    override fun showErrorMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    override fun onSuccess() {
        Toast.makeText(this, "Sent", Toast.LENGTH_LONG).show()
        finish()
    }

}
