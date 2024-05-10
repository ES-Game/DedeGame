package com.example.demo.viewstate

import android.view.View
import java.util.Stack

class ViewStateHelper(private val parentView: View) {

    private val viewStateSupportable: IViewStateSupportable = parentView as IViewStateSupportable
    private val stackState = Stack<IViewState>()

    fun updateState(state: IViewState) {
        if (stackState.isEmpty()) {
            state.enter(viewStateSupportable)
            stackState.push(state)
        } else {
            val prevState = stackState.peek()
            if (prevState.equals(state)) {
                return
            }

            prevState.exit(viewStateSupportable)
            state.enter(viewStateSupportable)
            stackState.push(state)
        }
    }

    fun appendState(state: IViewState) {
        if (stackState.isEmpty()) {
            state.enter(viewStateSupportable)
            stackState.push(state)
        } else {
            val prev = stackState.peek()
            if (prev is CompoundViewState) {
                prev.addState(state)
            } else {
                val compound = CompoundViewState()
                compound.addState(prev)
                compound.addState(state)
                compound.enter(viewStateSupportable)
                stackState.push(compound)
            }
        }
    }

    fun removeState(state: IViewState) {
        if (stackState.contains(state)) {
            state.exit(viewStateSupportable)
            stackState.remove(state)
        } else {
            var count = stackState.size
            while (count > 0) {
                val top = stackState[count - 1]
                if (top is CompoundViewState) {
                    if(top.removeState(state)) {
                        if (top.size() == 0) {
                            stackState.removeAt(count - 1)
                        }
                        if (top.size() == 1) {
                            val latest = top.stateAt(0)
                            stackState.removeAt(count - 1)
                            stackState.add(count - 1, latest)
                        }
                    }
                }
                count--
            }
        }
    }

    fun back() {
        if (stackState.isEmpty()) return
        val top = stackState.peek()
        if (top is CompoundViewState) {
            top.back()
            if (top.size() == 0) {
                stackState.pop()
            }
        } else {
            top.exit(viewStateSupportable)
            stackState.pop()
        }
    }

    fun backTo(state: IViewState?) {
        if (state == null) {
            reset()
        }

        while (stackState.isNotEmpty()) {
            val topState = stackState.peek()
            if (topState == state) {
                break
            }

            if (topState is CompoundViewState) {
                topState.backTo(state)
                if (topState.size() > 0) {
                    break
                } else {
                    stackState.pop()
                }
            } else {
                stackState.pop()
                topState.exit(viewStateSupportable)
            }
        }
    }

    fun reset() {
        if (stackState.isEmpty()) {
            return
        }

        while (stackState.isNotEmpty()) {
            val topState = stackState.pop()
            topState.exit(viewStateSupportable)
        }
    }
}