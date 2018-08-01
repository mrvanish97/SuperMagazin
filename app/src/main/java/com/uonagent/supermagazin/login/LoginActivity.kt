package com.uonagent.supermagazin.login

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TextInputLayout
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Toast
import com.uonagent.supermagazin.R
import com.uonagent.supermagazin.user.UserActivity
import com.uonagent.supermagazin.utils.enums.LoginFields
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity(), LoginContract.View {


    companion object {
        private const val TAG = "LoginActivity"
        private val FIELD_ERRORS = arrayListOf(
                R.string.login_empty_email,
                R.string.login_empty_password
        )
    }


    private lateinit var mPresenter: LoginContract.Presenter

    override fun showInfoMessage(message: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getAuthErrorMessage() =
            resources.getString(R.string.login_auth_error)!!

    override fun getSomethingWentWrongMessage() =
            resources.getString(R.string.login_something_went_wrong)!!

    override fun showErrorMessage(message: String) {
        Log.d(TAG, "showErrorMessage $message")
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    override fun setLoadingLayout() {
        progressbar_login.visibility = View.VISIBLE
        textview_login_registration.visibility = View.GONE
        button_login_enter.visibility = View.GONE
    }

    override fun setIdleLayout() {
        progressbar_login.visibility = View.GONE
        textview_login_registration.visibility = View.VISIBLE
        button_login_enter.visibility = View.VISIBLE
    }

    override fun startListActivity() {
        Log.d(TAG, "startListActivity()")
        val intent = Intent(this, UserActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun setEmptyFieldError(type: LoginFields) {
        val array = arrayListOf<TextInputLayout>(
                textinputlayout_login_email,
                textinputlayout_login_password
        )
        val activatedError = array.find { !it.error.isNullOrBlank() }
        if (activatedError != null) {
            activatedError.error = null
        }
        val index = type.value
        if (index != -1) {
            val changingError = array[index]
            changingError.error = resources.getString(FIELD_ERRORS[index])
        }
    }

    override fun makeVisible() {
        login_constraint.visibility = View.VISIBLE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        login_constraint.visibility = View.VISIBLE

        mPresenter = LoginPresenter(this)
    }

    fun onEnterClick(view: View) {
        val email = edittext_login_email.text.toString()
        val password = edittext_login_password.text.toString()
        mPresenter.onEnterClicked(email, password)
    }

    override fun onStart() {
        super.onStart()
        mPresenter.onActivityStarted()
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.onDestroy()
    }
}
