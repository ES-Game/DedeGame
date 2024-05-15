package com.dede.dedegame.presentation.register

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

class RegisterView(context: Context?, attrs: AttributeSet?) : BaseConstraintView(context, attrs) {

    private val edtEmail by lazy { findViewById<InputEditText>(R.id.edtEmail) }
    private val edtName by lazy { findViewById<InputEditText>(R.id.edtName) }
    private val edtPassword by lazy { findViewById<InputEditText>(R.id.edtPassword) }
    private val edtReInputPassword by lazy { findViewById<InputEditText>(R.id.edtReInputPassword) }
    private val btnRegister by lazy { findViewById<AppCompatButton>(R.id.btnRegister) }
    private val txtLogin by lazy { findViewById<TextView>(R.id.txtLogin) }
    private val containerBack by lazy { findViewById<View>(R.id.containerBack) }
    private val txtStartTitle by lazy { findViewById<TextView>(R.id.txtStartTitle) }
    private val txtCenterTitle by lazy { findViewById<TextView>(R.id.txtCenterTitle) }
    override fun onInitView() {
        super.onInitView()
        setupToolbar()
        txtLogin.setOnClickListener {
//            mPresenter.executeCommand(MoveLoginCmd())
        }
    }

    private fun setupToolbar() {
        txtStartTitle.text = "Quay lại"
        containerBack.setOnClickListener {
            mPresenter.executeCommand(OnBackCmd())
        }

        btnRegister.setOnClickListener {
            mPresenter.executeCommand(
                OnClickRegisterCmd(
                    edtEmail.inputText,
                    edtName.inputText,
                    edtPassword.inputText,
                    edtReInputPassword.inputText
                )
            )
        }

        edtEmail.setOnEventViewListener(object : InputEditText.OnEventViewListener {
            override fun onTextChanged(s: String?, start: Int, before: Int, count: Int) {
                s?.let {
                    edtEmail.showError(!isValidEmail(it))
                    edtEmail.setErrorText("Mật khẩu không đúng định dạng")
                }
            }

        })

        setupLogin()
        edtName.setOnEventViewListener(object : InputEditText.OnEventViewListener {
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

        edtReInputPassword.setSecureTextEntry()
        edtReInputPassword.setOnEventViewListener(object : InputEditText.OnEventViewListener {
            override fun onTextChanged(s: String?, start: Int, before: Int, count: Int) {
                s?.let { passw ->
                    if (!checkValidPassword(passw).second) {
                        edtReInputPassword.setErrorText(checkValidPassword(passw).first)
                        edtReInputPassword.showError(true)
                        edtReInputPassword.setErrorText("Mật khẩu nhập lại không trùng khớp với mật khẩu vừa nhập")
                    } else {
                        edtReInputPassword.showError(false)
                        setupLogin()
                    }
                }
            }
        })
    }

    private fun setupLogin() {
        if (canLogin()) {
            btnRegister.background = ContextCompat.getDrawable(
                btnRegister.context,
                R.drawable.bg_corner_orange_button
            )

        } else {
            btnRegister.setBackgroundColor(
                ContextCompat.getColor(
                    btnRegister.context,
                    R.color.gray_300
                )
            )
            btnRegister.background = ContextCompat.getDrawable(
                btnRegister.context,
                R.drawable.bg_corner_grey_button
            )
        }
        btnRegister.isEnabled = canLogin()
    }

    private fun canLogin(): Boolean {
        val userEmail = edtEmail.inputText?.takeIf { it.isNotEmpty() } ?: return false
        val userName = edtName.inputText?.takeIf { it.isNotEmpty() } ?: return false
        val pass = edtPassword.inputText?.takeIf { it.isNotEmpty() } ?: return false
        val rePass = edtReInputPassword.inputText?.takeIf { it.isNotEmpty() } ?: return false

        if (!isValidEmail(userEmail)) {
            return false
        }

        if (!checkValidPassword(pass).second) {
            return false
        }

        if (!checkValidPassword(rePass).second) {
            return false
        }

        if (!rePass.equals(pass)) {
            return false
        }

        return true
    }

    fun isValidEmail(email: String): Boolean {
        val emailRegex = Regex("^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})")
        return emailRegex.matches(email)
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

    class OnClickRegisterCmd(
        val email: String,
        val name: String,
        val password: String,
        val rePassword: String,
    ) : ICommand {}

    class MoveLoginCmd() : ICommand {}
    class OnBackCmd() : ICommand
}


