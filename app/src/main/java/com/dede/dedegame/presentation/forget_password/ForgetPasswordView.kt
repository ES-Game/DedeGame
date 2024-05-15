package com.dede.dedegame.presentation.forget_password

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import com.dede.dedegame.R
import com.dede.dedegame.presentation.widget.InputEditText
import com.quangph.base.mvp.ICommand
import com.quangph.base.mvp.mvpcomponent.view.BaseConstraintView

class ForgetPasswordView(context: Context?, attrs: AttributeSet?) : BaseConstraintView(context, attrs) {

    private val edtEmail by lazy { findViewById<InputEditText>(R.id.edtEmail) }
    private val btnGetPassword by lazy { findViewById<AppCompatButton>(R.id.btnGetPassword) }
    private val containerBack by lazy { findViewById<View>(R.id.containerBack) }
    private val txtStartTitle by lazy { findViewById<TextView>(R.id.txtStartTitle) }
    private val txtCenterTitle by lazy { findViewById<TextView>(R.id.txtCenterTitle) }
    override fun onInitView() {
        super.onInitView()
        setupToolbar()
        btnGetPassword.setOnClickListener {

        }
    }

    private fun setupToolbar() {
        txtStartTitle.text = "Quay lại"
        containerBack.setOnClickListener {
            mPresenter.executeCommand(OnBackCmd())
        }
    }

    class OnBackCmd() : ICommand
}


