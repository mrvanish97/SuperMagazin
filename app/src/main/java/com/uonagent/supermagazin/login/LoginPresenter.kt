package com.uonagent.supermagazin.login

import com.uonagent.supermagazin.utils.FirebaseAuthListener
import com.uonagent.supermagazin.utils.LoginFields

class LoginPresenter(private val mView: LoginContract.View) : LoginContract.Presenter {

    override fun onActivityStarted() {
        if (LoginRepository.currentUserExists()) {
            mView.startListActivity()
        } else {
            mView.makeVisible()
        }
    }

    override fun onEnterClicked(email: String, password: String) {
        if (isAnonymousAuth(email, password)) {
            LoginRepository.signInAnonymously(object : FirebaseAuthListener {
                override fun onStart() {
                    mView.setLoadingLayout()
                }

                override fun onSuccess() {
                    mView.startListActivity()
                }

                override fun onFailure() {
                    mView.setIdleLayout()
                    val message = mView.getSomethingWentWrongMessage()
                    mView.showErrorMessage(message)
                    mView.setEmptyFieldError(LoginFields.NULL)
                }
            })
        } else if (isEmailAuth(email, password)) {
            LoginRepository.signInEmail(email, password, object : FirebaseAuthListener {
                override fun onStart() {
                    mView.setLoadingLayout()
                }

                override fun onSuccess() {
                    mView.startListActivity()
                }

                override fun onFailure() {
                    mView.setIdleLayout()
                    val message = mView.getAuthErrorMessage()
                    mView.showErrorMessage(message)
                    mView.setEmptyFieldError(LoginFields.NULL)

                }

            })
        } else {
            val type = if (email.isBlank()) {
                LoginFields.EMAIL
            } else {
                LoginFields.PASSWORD
            }
            mView.setEmptyFieldError(type)
        }
    }

    override fun onRegistrationClicked() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onDestroy() {
    }

    private fun isAnonymousAuth(email: String, password: String) = email.isBlank() && password.isEmpty()

    private fun isEmailAuth(email: String, password: String) = !email.isBlank() && !password.isEmpty()

}