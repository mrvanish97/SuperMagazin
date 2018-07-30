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

    private var listItemArray: MutableList<ListItemModel>? = null

    private var selectedItemUid: String? = null

    override fun getViewType(): UserViewType? {
        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_holder)
        val currentFragmentTag = currentFragment.tag
        return when (currentFragmentTag) {
            SELECTED_ITEM_FRAGMENT_TAG -> UserViewType.LIST
            ITEM_LIST_FRAGMENT_TAG -> UserViewType.LIST
            else -> null
        }
    }

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

    override fun setAdminItemPermissions() {
        makeAllMenuItemsInvisible()
        mMenu.findItem(R.id.item_menu_accept).isVisible = true
    }

    override fun setAdminListPermissions() {
        makeAllMenuItemsInvisible()
        mMenu.findItem(R.id.item_menu_accept).isVisible = true
    }

    override fun setUserItemPermissions() {
        makeAllMenuItemsInvisible()
        mMenu.findItem(R.id.item_menu_buy).isVisible = true
    }

    override fun setUserListPermissions() {
        makeAllMenuItemsInvisible()
    }

    private fun makeAllMenuItemsInvisible() {
        val array = arrayOf(
                R.id.item_menu_accept,
                R.id.item_menu_add,
                R.id.item_menu_buy)
        for (id in array) {
            mMenu.findItem(id).isVisible = false
        }
    }

    override fun onItemClick(uid: String) {
        selectedItemUid = uid
        bottomNavigation.menu.findItem(R.id.user_menu_product).isEnabled = true
        //bottomNavigation.findViewById<View>(R.id.user_menu_product).performClick()
        mPresenter.onItemClick()
    }

    override fun getItemUid() = selectedItemUid

    override fun onItemLongClick(uid: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onFragmentInteraction(uri: Uri) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onFragmentViewCreated(list: MutableList<ListItemModel>) {
        listItemArray = list
        mPresenter.sendFullListUpdateRequest()
    }

    override fun getListItemArray(): MutableList<ListItemModel>? {
        return listItemArray
    }

    override fun setListItemArray(listItemArray: MutableList<ListItemModel>) {
        Log.d(TAG, listItemArray.size.toString())
        this.listItemArray?.clear()
        this.listItemArray?.addAll(listItemArray)
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
                        setFragment(SELECTED_ITEM_FRAGMENT_TAG)
                        return@OnNavigationItemSelectedListener true
                    }
                    R.id.user_menu_list -> {
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

        toolBarPrepare()

        bottomNavigationPrepare()

        setFragment(ITEM_LIST_FRAGMENT_TAG)
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
            ft.add(R.id.fragment_holder, fragment, tag)
                    .addToBackStack(null)
                    .commit()
        }
        //mPresenter.setAccessPermissions()
    }

    override fun getItemFromRepo(item: ListItemModel?) {
        startSelectedItemFragmentWithItem(item)
    }

    private fun startSelectedItemFragmentWithItem(item: ListItemModel?) {
        selectedItemFragment = SelectedItemFragment.newInstance(item)
        supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_holder, selectedItemFragment, SELECTED_ITEM_FRAGMENT_TAG)
                .addToBackStack(null)
                .commit()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.item_menu_buy -> {
                return true
            }
            else -> {
                return super.onOptionsItemSelected(item)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)

        itemListFragment
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_selected_item, menu)

        mMenu = menu!!

        return true
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        } else {
            moveTaskToBack(true)
        }
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