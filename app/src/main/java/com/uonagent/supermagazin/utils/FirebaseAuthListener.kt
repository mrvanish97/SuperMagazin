package com.uonagent.supermagazin.utils

interface FirebaseAuthListener {
    fun onStart()
    fun onSuccess()
    fun onFailure()
}