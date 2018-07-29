package com.uonagent.supermagazin.user

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.uonagent.supermagazin.R
import com.uonagent.supermagazin.login.LoginActivity

private const val LIST_FRAGMENT_TAG = "list"

class UserActivity : AppCompatActivity(), UserContract.View,
        ItemListFragment.OnFragmentInteractionListener {

    private var itemListFragment: ItemListFragment? = null
    private var listItemArray: MutableList<ListItemModel>? = null

    override fun onFragmentViewCreated(list: MutableList<ListItemModel>) {
        listItemArray = list
        mPresenter.sendFullListUpdateRequest()
    }

    override fun getListItemArray(): MutableList<ListItemModel>? {
        return listItemArray
    }

    override fun setListItemArray(listItemArray: MutableList<ListItemModel>) {

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


    @Suppress("CAST_NEVER_SUCCEEDS")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        mPresenter = UserPresenter(this)

        val fm = supportFragmentManager
        val ft = fm.beginTransaction()
        itemListFragment = fm.findFragmentByTag(LIST_FRAGMENT_TAG) as ItemListFragment?
        if (itemListFragment == null) {
            itemListFragment = ItemListFragment()
        }
        if (!itemListFragment!!.isAdded) {
            ft.add(R.id.fragment_holder, itemListFragment, LIST_FRAGMENT_TAG)
                    .commit()
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