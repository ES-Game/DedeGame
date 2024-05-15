package com.dede.dedegame.presentation.login

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import com.dede.dedegame.R
import com.dede.dedegame.extension.checkMinium6Length
import com.dede.dedegame.extension.hasLeastOneLowerCase
import com.dede.dedegame.extension.hasLeastOneSpecialCharacter
import com.dede.dedegame.extension.hasLeastOneUpperCase
import com.dede.dedegame.presentation.widget.InputEditText
import com.quangph.base.mvp.ICommand
import com.quangph.base.mvp.mvpcomponent.view.BaseConstraintView

class LoginView(context: Context?, attrs: AttributeSet?) : BaseConstraintView(context, attrs) {

    private val edtUser by lazy { findViewById<InputEditText>(R.id.edtUser) }
    private val edtPassword by lazy { findViewById<InputEditText>(R.id.edtPassword) }
    private val btnLogin by lazy { findViewById<AppCompatButton>(R.id.btnLogin) }
    private val txtForgetPassword by lazy { findViewById<TextView>(R.id.txtForgetPassword) }
    private val btnRegister by lazy { findViewById<AppCompatButton>(R.id.btnRegister) }
    override fun onInitView() {
        super.onInitView()
        setupToolbar()

        txtForgetPassword.setOnClickListener {
            mPresenter.executeCommand(MoveForgetPasswordCmd())
        }
        btnRegister.setOnClickListener {
            mPresenter.executeCommand(MoveRegisterCmd())
        }
        setupLogin()
        edtUser.setOnEventViewListener(object : InputEditText.OnEventViewListener {
            override fun onTextChanged(s: String?, start: Int, before: Int, count: Int) {
                setupLogin()
            }
        })

        edtPassword.setSecureTextEntry()
        edtPassword.setOnEventViewListener(object : InputEditText.OnEventViewListener {
            override fun onTextChanged(s: String?, start: Int, before: Int, count: Int) {
                s?.let { passw ->
                    if (!checkValidPassword(passw).second) {
                        edtPassword.setErrorText(checkValidPassword(passw).first)
                        edtPassword.showError(true)
                    } else {
                        edtPassword.showError(false)
                        setupLogin()
                    }
                }
            }
        })
    }

    private fun setupToolbar() {
        val containerBack: View = findViewById(R.id.containerBack)
        val txtStartTitle: TextView = findViewById(R.id.txtStartTitle)
        val txtCenterTitle: TextView = findViewById(R.id.txtCenterTitle)
        txtStartTitle.text = "Quay lại"
        containerBack.setOnClickListener {
            mPresenter.executeCommand(OnBackCmd())
        }

        btnLogin.setOnClickListener {
            mPresenter.executeCommand(OnClickLoginCmd(edtUser.inputText, edtPassword.inputText))
        }
    }

    private fun setupLogin() {
        if (canLogin()) {
            btnLogin.background = ContextCompat.getDrawable(
                btnLogin.context,
                R.drawable.bg_corner_orange_button
            )

        } else {
            btnLogin.setBackgroundColor(ContextCompat.getColor(btnLogin.context, R.color.gray_300))
            btnLogin.background = ContextCompat.getDrawable(
                btnLogin.context,
                R.drawable.bg_corner_grey_button
            )
        }
        btnLogin.isEnabled = canLogin()
    }

    private fun canLogin(): Boolean {
        val userNameOrPass = edtUser.inputText?.takeIf { it.isNotEmpty() } ?: return false
        val pass = edtPassword.inputText?.takeIf { it.isNotEmpty() } ?: return false

        if (!checkValidPassword(pass).second) {
            return false
        }

        return true
    }

    private fun checkValidPassword(password: String): Pair<String, Boolean> {
        if (password.isEmpty()) {
            return Pair("Mật khẩu phải không được bỏ trống", false)
        }
        if (!password.checkMinium6Length()) {
            return Pair("Mật khẩu phải có ít nhất 6 ký tự", false)
        }

        if (!password.hasLeastOneUpperCase()) {
            return Pair("Mật khẩu phải có ít nhất 1 ký tự viết hoa", false)
        }

        if (!password.hasLeastOneSpecialCharacter()) {
            return Pair("Mật khẩu phải có ít nhất 1 ký tự đặc biệt", false)
        }

        if (!password.hasLeastOneLowerCase()) {
            return Pair("Mật khẩu phải có ít nhất 1 ký tự thường", false)
        }

        return Pair("", true)
    }

    class OnClickLoginCmd(
        val userNameOrMail: String,
        val password: String,
    ) : ICommand {}

    class MoveRegisterCmd() : ICommand {}
    class MoveForgetPasswordCmd() : ICommand {}
    class OnBackCmd() : ICommand
}


