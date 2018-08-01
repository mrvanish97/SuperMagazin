package com.uonagent.supermagazin.order

import com.uonagent.supermagazin.utils.listeners.FirebaseEditListener
import com.uonagent.supermagazin.utils.models.OrderModel

class OrderPresenter(private val mView: OrderContract.View): OrderContract.Presenter{
    override fun sendOrder() {
        val order = mView.getOrder()

        try {
            checkName(order)
            checkEmail(order)
        } catch (e: IllegalArgumentException) {
            return
        }

        OrderRepository.addOrder(order, object : FirebaseEditListener {
            override fun onSuccess() {
                mView.onSuccess()
            }

            override fun onFailure(message: String) {
                mView.showErrorMessage(message)
            }

        })
    }

    private fun checkName (order: OrderModel) {
        if (order.clientEmail.isBlank()) {
            mView.showEmptyNameError()
            throw IllegalArgumentException()
        }
    }

    private fun checkEmail(order: OrderModel) {
        if (order.clientName.isBlank()) {
            mView.showEmptyEmailError()
            throw IllegalArgumentException()
        }
    }
}