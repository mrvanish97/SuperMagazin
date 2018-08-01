package com.uonagent.supermagazin.utils.listeners

interface FirebaseAuthListener {
    fun onStart()
    fun onSuccess()
    fun onFailure()
}