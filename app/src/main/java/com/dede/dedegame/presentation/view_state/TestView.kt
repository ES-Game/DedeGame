package com.example.demo.viewstate

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import com.dede.dedegame.R


class TestView(context: Context?, attrs: AttributeSet?) : LinearLayout(context, attrs), IViewStateSupportable {

    //private var btn1: Button = findViewById(R.id.btn1)
    private lateinit var desV: View
    private lateinit var desV2: View

    private val stateHelper = ViewStateHelper(this)

    override fun onFinishInflate() {
        super.onFinishInflate()
        desV = findViewById(R.id.desView)
        desV2 = findViewById(R.id.desView2)
    }

    fun updateState(state: IViewState) {
        stateHelper.updateState(state)
    }

    fun appendState(state: IViewState) {
        stateHelper.appendState(state)
    }

    fun remove(state: IViewState) {
        stateHelper.removeState(state)
    }

    fun reset() {
        stateHelper.reset()
    }

    class State1: BaseViewState<TestView>() {

        var listName: MutableList<String> by dataState("list", mutableListOf())
        var name: String? by nullableDataState("name", null) {
            it.run {
                desV.setBackgroundColor(Color.RED)
            }
        }

        init {
            listName.add("state 1 Name 1")
            listName.add("state 1 Name 2")
            name = "state 1"
        }

        override fun onEnter(containerView: TestView) {
            super.onEnter(containerView)
            for (i in 0 until listName.size) {
                Log.e("State1", listName[i])
            }
            Log.e("State1", name!!)
        }

        override fun onExit(containerView: TestView) {
            super.onExit(containerView)
            containerView.desV.setBackgroundColor(Color.BLACK)
            //listName.removeAt(1)
        }
    }

    class State2: BaseViewState<TestView>() {

        var listName: MutableList<String> by dataState("list", mutableListOf())
        var name: String? by nullableDataState {

        }

        override fun onEnter(containerView: TestView) {
            super.onEnter(containerView)
            containerView.desV2.setBackgroundColor(Color.BLUE)
            listName.add("state 2 name 1")
            listName.add("state 2 name 2")
            name = "state 2"
        }

        override fun onExit(containerView: TestView) {
            super.onExit(containerView)
            containerView.desV2.setBackgroundColor(Color.BLACK)
        }
    }

    override var view: View = this
    override var dataState: HashMap<String, Any?> = hashMapOf()
}