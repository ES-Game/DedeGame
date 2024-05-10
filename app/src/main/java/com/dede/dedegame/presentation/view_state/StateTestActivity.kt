package com.example.demo.viewstate

import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity
import com.dede.dedegame.R


class StateTestActivity: ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_uistate_test)
        val parentView = findViewById<TestView>(R.id.parentV)
        val state1 = TestView.State1()
        val state2 = TestView.State2()
        findViewById<Button>(R.id.btn1).setOnClickListener {
            parentView.updateState(state1)
        }

        findViewById<Button>(R.id.btn2).setOnClickListener {
            parentView.updateState(state2)
        }

        findViewById<Button>(R.id.btn3).setOnClickListener {
            parentView.remove(state1)
        }
    }
}