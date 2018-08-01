package com.uonagent.supermagazin.order

import com.uonagent.supermagazin.utils.Contract
import com.uonagent.supermagazin.utils.models.OrderModel

interface OrderContract : Contract {

    interface View : Contract.View{
        fun getOrder(): OrderModel
        fun showEmptyNameError()
        fun showEmptyEmailError()
        fun onSuccess()
    }

    interface Presenter : Contract.Presenter {
        fun sendOrder()
    }
}