package com.uonagent.supermagazin.user

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.uonagent.supermagazin.R
import com.uonagent.supermagazin.login.LoginActivity
import com.uonagent.supermagazin.order.OrderActivity
import com.uonagent.supermagazin.utils.ItemModel
import com.uonagent.supermagazin.utils.UserViewType
import com.uonagent.supermagazin.utils.removeShiftMode
import kotlinx.android.synthetic.main.activity_user.*

private const val ITEM_LIST_FRAGMENT_TAG = "list"
private const val SELECTED_ITEM_FRAGMENT_TAG = "selected"

private const val MODEL = "model"

private const val TAG = "UserActivity"

class UserActivity : AppCompatActivity(), UserContract.View,
        ItemListFragment.OnFragmentInteractionListener,
        SelectedItemFragment.OnFragmentInteractionListener {

    private var itemListFragment: ItemListFragment? = null
    private var selectedItemFragment: SelectedItemFragment? = null

    private lateinit var bottomNavigation: BottomNavigationView

    private lateinit var mMenu: Menu

    private var itemArray: MutableList<ItemModel>? = null

    private var selectedItemUid: String? = null

    private var viewType: UserViewType? = null

    override fun getViewType(): UserViewType? = viewType

    override fun makeFieldsClickable() {
        val f: (View?) -> Unit = {
            if (it != null) {
                it.isClickable = true
            }
        }
        makeFields(f)
    }

    override fun makeFieldsUnclickable() {
        val f: (View?) -> Unit = {
            if (it != null) {
                it.isClickable = false
            }
        }
        makeFields(f)
    }

    private fun makeFields(f: (View?) -> Unit) {
        (supportFragmentManager.findFragmentByTag(SELECTED_ITEM_FRAGMENT_TAG) as SelectedItemFragment?)
                ?.changeClickability(f)
    }

    private var isAddMenuItemVisible = false
    private var isAcceptMenuItemVisible = false
    private var isBuyMenuItemVisible = false

    override fun setAdminItemPermissions() {
        isAddMenuItemVisible = false
        isAcceptMenuItemVisible = true
        isBuyMenuItemVisible = false
        invalidateOptionsMenu()
        setBottomNavigationInAdminMode()
    }

    override fun setAdminListPermissions() {
        isAddMenuItemVisible = true
        isAcceptMenuItemVisible = false
        isBuyMenuItemVisible = false
        invalidateOptionsMenu()
        setBottomNavigationInAdminMode()
    }

    override fun setUserItemPermissions() {
        isAddMenuItemVisible = false
        isAcceptMenuItemVisible = false
        isBuyMenuItemVisible = true
        invalidateOptionsMenu()
        setBottomNavigationInUserMode()
    }

    override fun setUserListPermissions() {
        isAddMenuItemVisible = false
        isAcceptMenuItemVisible = false
        isBuyMenuItemVisible = false
        invalidateOptionsMenu()
        setBottomNavigationInUserMode()
    }


    override fun onItemClick(uid: String) {
        selectedItemUid = uid
        bottomNavigation.menu.findItem(R.id.user_menu_product).isEnabled = true
        bottomNavigation.menu.findItem(R.id.user_menu_product).isChecked = true

        mPresenter.onItemClick()
    }

    private fun setBottomNavigationInAdminMode() {
        bottomNavigation.menu.findItem(R.id.user_menu_user).isEnabled = false
        bottomNavigation.menu.findItem(R.id.user_menu_inbox).isEnabled = true
    }

    private fun setBottomNavigationInUserMode() {
        bottomNavigation.menu.findItem(R.id.user_menu_user).isEnabled = true
        bottomNavigation.menu.findItem(R.id.user_menu_inbox).isEnabled = false
    }

    override fun getItemUid() = selectedItemUid

    override fun onItemLongClick(uid: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onFragmentInteraction(uri: Uri) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onFragmentViewCreated(list: MutableList<ItemModel>) {
        itemArray = list
        mPresenter.sendFullListUpdateRequest()
    }

    override fun getListItemArray(): MutableList<ItemModel>? {
        return itemArray
    }

    override fun setListItemArray(itemArray: MutableList<ItemModel>) {
        Log.d(TAG, itemArray.size.toString())
        this.itemArray?.clear()
        this.itemArray?.addAll(itemArray)
        notifyFragmentAboutListChange()
    }

    private fun notifyFragmentAboutListChange() {
        (supportFragmentManager.findFragmentByTag(ITEM_LIST_FRAGMENT_TAG) as ItemListFragment?)
                ?.notifyDatasetChanged()
    }

    override fun showErrorMessage(message: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setLoadingLayout() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setIdleLayout() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private lateinit var mPresenter: UserContract.Presenter

    private val mOnNavigationItemSelectedListener =
            BottomNavigationView.OnNavigationItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.user_menu_product -> {
                        viewType = UserViewType.ITEM
                        mPresenter.setAccessPermissions()
                        setFragment(SELECTED_ITEM_FRAGMENT_TAG)
                        return@OnNavigationItemSelectedListener true
                    }
                    R.id.user_menu_list -> {
                        viewType = UserViewType.LIST
                        mPresenter.setAccessPermissions()
                        setFragment(ITEM_LIST_FRAGMENT_TAG)
                        return@OnNavigationItemSelectedListener true
                    }
                    R.id.user_menu_user -> {
                        return@OnNavigationItemSelectedListener true
                    }
                    R.id.user_menu_exit -> {
                        mPresenter.onLogOutPressed()
                        val intent = Intent(this, LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                        return@OnNavigationItemSelectedListener true
                    }
                }
                false
            }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        mPresenter = UserPresenter(this)
        bottomNavigationPrepare()
        viewType = UserViewType.LIST
        mPresenter.setAccessPermissions()

        toolBarPrepare()

        setFragment(ITEM_LIST_FRAGMENT_TAG)

    }

    override fun onResume() {
        super.onResume()

        invalidateOptionsMenu()
    }

    private fun bottomNavigationPrepare() {
        bottomNavigation = navigation
        bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        bottomNavigation.removeShiftMode()
        bottomNavigation.findViewById<View>(R.id.user_menu_list).performClick()
        bottomNavigation.menu.findItem(R.id.user_menu_product).isEnabled = false
    }

    private fun toolBarPrepare() {
        setSupportActionBar(toolbar)
    }

    private fun setFragment(tag: String) {
        val fm = supportFragmentManager
        val ft = fm.beginTransaction()
        var fragment = fm.findFragmentByTag(tag)
        if (fragment == null) {
            fragment = FragmentFactory.getFragmentByTag(tag)
        }
        if (!fragment.isAdded) {
            ft.replace(R.id.fragment_holder, fragment, tag)
                    .addToBackStack(null)
                    .commit()
        }
    }

    override fun getItemFromRepo(item: ItemModel?) {
        startSelectedItemFragmentWithItem(item)
    }

    private fun startSelectedItemFragmentWithItem(item: ItemModel?) {
        viewType = UserViewType.ITEM
        mPresenter.setAccessPermissions()
        selectedItemFragment = SelectedItemFragment.newInstance(item)
        supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_holder, selectedItemFragment, SELECTED_ITEM_FRAGMENT_TAG)
                .addToBackStack(null)
                .commit()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.item_menu_add -> {
                true
            }
            R.id.item_menu_accept -> {
                true
            }
            R.id.item_menu_buy -> {
                mPresenter.makeOrder()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    override fun getSelectedItemForOrder(): ItemModel? {
        selectedItemFragment = supportFragmentManager
                .findFragmentByTag(SELECTED_ITEM_FRAGMENT_TAG) as SelectedItemFragment?
        return selectedItemFragment?.getModel()
    }

    override fun replaceWithOrderView(item: ItemModel?) {
        val intent = Intent(this, OrderActivity::class.java)
        intent.putExtra(MODEL, item)
        startActivity(intent)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)

        itemListFragment
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_selected_item, menu)

        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        setVisibilityForMenuItems(menu)

        return true
    }

    private fun setVisibilityForMenuItems(menu: Menu?) {
        val idArr = arrayOf(
                R.id.item_menu_accept,
                R.id.item_menu_add,
                R.id.item_menu_buy
        )
        val visibleArr = arrayOf(
                isAcceptMenuItemVisible,
                isAddMenuItemVisible,
                isBuyMenuItemVisible
        )
        if (menu != null) {
            for (i in idArr.indices) {
                menu.findItem(idArr[i]).isVisible = visibleArr[i]
            }
        }
    }

    override fun onBackPressed() {
        moveTaskToBack(false)
    }

    override fun onStart() {
        super.onStart()
        mPresenter.onActivityStarted()
    }

    override fun closeAndBackToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}

object FragmentFactory {
    fun getFragmentByTag(tag: String) =
            when (tag) {
                ITEM_LIST_FRAGMENT_TAG -> ItemListFragment()
                SELECTED_ITEM_FRAGMENT_TAG -> SelectedItemFragment()
                else -> throw IllegalArgumentException()
            }
}