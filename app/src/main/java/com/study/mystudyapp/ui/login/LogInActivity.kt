package com.study.mystudyapp.ui.login

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import com.study.mystudyapp.R
import com.study.mystudyapp.databinding.ActivityLogInBinding
import com.study.mystudyapp.toast
import kotlinx.android.synthetic.main.activity_log_in.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class LogInActivity : AppCompatActivity(), AuthListener, KodeinAware {

    override val kodein by kodein()
    private val factory: LogInViewModelFactory by instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        title = getString(R.string.sign_in)

        val binding: ActivityLogInBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_log_in)

        val viewModel = ViewModelProvider(this, factory).get(LogInViewModel::class.java)

        binding.loginViewModel = viewModel
        viewModel.authListener = this


    }

    private fun hideKeyboard() {
        val input = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        input.hideSoftInputFromWindow(login_main_layout.windowToken, 0)
    }

    override fun onStarted() {
        hideKeyboard()
        progress_bar.visibility = View.VISIBLE
    }

    override fun onResult(loginResponse: LiveData<String>) {
        loginResponse.observe(this, {
            // ارجع شل الرساله وفصلها على حسب الخطأ يا كلمة سر او ايميل او او او ... الخ
            if (it != "authenticated") {
                if (it == "network error") {
                    toast(getString(R.string.network_error))
                } else {
                    toast(getString(R.string.can_not_log_in))
                }
                progress_bar.visibility = View.GONE
            } else {
                finish()
            }

        })
    }

    override fun onEmptyFiled(message: String) {
        progress_bar.visibility = View.GONE

        when {
            message.contains("empty pass") -> text_field_password.error =
                getString(R.string.please_write_password)
            message.contains("empty email") -> text_field_email.error =
                getString(R.string.please_write_email)
            else -> toast(message)
        }
    }

}