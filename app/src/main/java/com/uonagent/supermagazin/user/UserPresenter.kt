package com.uonagent.supermagazin.user

import com.uonagent.supermagazin.utils.FirebaseListListener

class UserPresenter(private val mView: UserContract.View) : UserContract.Presenter {
    override fun sendFullListUpdateRequest() {
        val updatedList = arrayListOf<ListItemModel>()
        UserRepository.initialize(object : FirebaseListListener {
            override fun onStart() {

            }

            override fun onAdd(item: ListItemModel) {
                updatedList.add(ListItemModel())
            }

        })
    }

    override fun onDestroy() {
    }

    override fun onActivityStarted() {
        if (!UserRepository.currentUserExists()) {
            mView.closeAndBackToLogin()
        }
    }



}