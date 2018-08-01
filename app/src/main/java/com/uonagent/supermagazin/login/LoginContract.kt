package com.uonagent.supermagazin.login

import com.uonagent.supermagazin.utils.Contract
import com.uonagent.supermagazin.utils.enums.LoginFields

interface LoginContract : Contract {

    interface View : Contract.View {
        fun setLoadingLayout()
        fun setIdleLayout()
        fun startListActivity()
        fun makeVisible()
        fun getAuthErrorMessage(): String
        fun getSomethingWentWrongMessage(): String
        fun setEmptyFieldError(type: LoginFields)
    }

    interface Presenter : Contract.Presenter {
        fun onActivityStarted()
        fun onDestroy()
        fun onEnterClicked(email: String, password: String)
        fun onRegistrationClicked()
    }
}