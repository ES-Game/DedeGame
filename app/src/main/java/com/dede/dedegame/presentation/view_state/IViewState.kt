package com.example.demo.viewstate

import android.view.View
import java.util.Stack
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

interface IViewState {
    enum class STATUS {
        ENTER, PENDING, EXIT
    }

    var status: STATUS
    fun enter(view: IViewStateSupportable)
    fun exit(view: IViewStateSupportable)
    fun update(tag: String?)
}

interface IViewStateSupportable {
    var view: View
    var dataState: HashMap<String, Any?>
}

open class BaseViewState<V: View> : IViewState {

    override var status: IViewState.STATUS = IViewState.STATUS.PENDING

    var viewStateSupportable: IViewStateSupportable? = null
    private var dataStoreMap = hashMapOf<String, Any?>()
    private var propertyDelegateList = mutableListOf<IUpdatableDataAllocation<*>>()
    private var pendingSetterMap = linkedMapOf<String, (V) -> Unit>()

    override fun enter(view: IViewStateSupportable) {
        viewStateSupportable = view
        if (status == IViewState.STATUS.PENDING) {
            status = IViewState.STATUS.ENTER
            initState(viewStateSupportable!!)
        } else {
            status = IViewState.STATUS.ENTER
        }
        val itr = pendingSetterMap.keys.iterator()
        while (itr.hasNext()) {
            invokeSetterTrigger(itr.next())
        }
        onEnter(viewStateSupportable!!.view as V)
    }

    override fun exit(view: IViewStateSupportable) {
        if (status != IViewState.STATUS.ENTER) return
        status = IViewState.STATUS.EXIT
        onExit(viewStateSupportable!!.view as V)
    }

    override fun update(tag: String?) {
        if (status == IViewState.STATUS.ENTER) {
            onUpdate(tag)
        }
    }

    open fun onInit(containerView: V){}
    open fun onEnter(containerView: V){}
    open fun onExit(containerView: V){}
    open fun onUpdate(tag: String?) {}

    fun initState(view: IViewStateSupportable) {
        syncDataState(view)
        onInit(view.view as V)
    }

    fun<T> nullableDataState(key: String? = null, initValue: T? = null, setterTrigger: ((V) -> Unit)? = null)
    : ReadWriteProperty<Any, T?> {
        return if (key == null) {
            DefaultNullableDataStatePropertyDelegate(initValue, this, setterTrigger)
        } else {
            if (setterTrigger == null) {
                NullableDataStatePropertyDelegate(initValue, key, dataStoreMap).also {
                    propertyDelegateList.add(it)
                }
            } else {
                VSNullableDataStateDelegate(initValue, key, dataStoreMap, this, setterTrigger).also {
                    propertyDelegateList.add(it)
                }
            }
        }
    }

    fun<T> dataState(key: String? = null, initValue: T, setterTrigger: ((V) -> Unit)? = null)
    : ReadWriteProperty<Any, T> {
        return if (key == null) {
            DefaultDataStatePropertyDelegate(initValue, this, setterTrigger)
        } else {
            if (setterTrigger == null) {
                DataStatePropertyDelegate(initValue, key, dataStoreMap).also {
                    propertyDelegateList.add(it)
                }
            } else {
                VSDataStateDelegate(initValue, key, dataStoreMap, this, setterTrigger).also {
                    propertyDelegateList.add(it)
                }
            }
        }
    }

    internal fun setPendingSetterTrigger(key: String, trigger: (V)-> Unit) {
        pendingSetterMap[key] = trigger
    }

    internal fun hasSetterTrigger(key: String): Boolean {
        return pendingSetterMap.contains(key)
    }

    private fun syncDataState(view: IViewStateSupportable) {
        val keys = dataStoreMap.keys.iterator()
        while (keys.hasNext()) {
            val key = keys.next()
            if (!view.dataState.contains(key)) {
                view.dataState[key] = dataStoreMap[key]
            }
        }
        this.dataStoreMap = view.dataState
        for (delegate in propertyDelegateList) {
            delegate.updateAllocation(view.dataState)
        }
    }

    private fun invokeSetterTrigger(key: String) {
        if (status == IViewState.STATUS.ENTER) {
            pendingSetterMap[key]?.invoke(viewStateSupportable?.view!! as V)
        }
    }
}


class CompoundViewState: IViewState {

    override var status: IViewState.STATUS = IViewState.STATUS.PENDING

    private val stateStack: Stack<IViewState> = Stack()
    private var viewStateSupportable: IViewStateSupportable? = null

    override fun enter(view: IViewStateSupportable) {
        status = IViewState.STATUS.ENTER
        viewStateSupportable = view
        if (stateStack.isEmpty()) {
            return
        }
        for (i in 0 until stateStack.size) {
            stateStack[i].enter(viewStateSupportable!!)
        }
    }

    override fun exit(view: IViewStateSupportable) {
        if (status != IViewState.STATUS.ENTER) return
        status = IViewState.STATUS.EXIT
        while (stateStack.isNotEmpty()) {
            val topState = stateStack.pop()
            topState.exit(view)
        }
    }

    override fun update(tag: String?) {

    }

    fun addState(state: IViewState): Boolean {
        var isAdded = false
        if (stateStack.isEmpty()) {
            isAdded = true
            stateStack.push(state)
        } else {
            if (stateStack.peek() != state) {
                isAdded = true
                stateStack.push(state)
            }
        }

        if (isAdded && (status == IViewState.STATUS.ENTER)) {
            state.enter(viewStateSupportable!!)
        }
        return isAdded
    }

    fun removeState(state: IViewState): Boolean {
        if (status != IViewState.STATUS.ENTER) return false
        if (stateStack.contains(state)) {
            state.exit(viewStateSupportable!!)
            stateStack.remove(state)
            return true
        }
        return false
    }

    fun back() {
        if (status != IViewState.STATUS.ENTER) return
        if (stateStack.isNotEmpty()) return
        val top = stateStack.pop()
        top.exit(viewStateSupportable!!)
    }

    fun backTo(state: IViewState?) {
        if (status != IViewState.STATUS.ENTER) return

        if (state == null) {
            reset()
        }
        while (stateStack.isNotEmpty()) {
            val topState = stateStack.peek()
            if (topState == state) {
                break
            }
            stateStack.pop()
            topState.exit(viewStateSupportable!!)
        }
    }

    fun reset() {
        if (status != IViewState.STATUS.ENTER) return

        if (stateStack.isEmpty()) {
            return
        }

        while (stateStack.isNotEmpty()) {
            val topState = stateStack.pop()
            topState.exit(viewStateSupportable!!)
        }
    }

    fun size(): Int {
        return stateStack.size
    }

    fun stateAt(index: Int): IViewState {
        return stateStack[index]
    }
}


class VSDataStateDelegate<V: View, T>(initValue: T, key: String, dataStoreMap: HashMap<String, Any?>,
                                      private val vs: BaseViewState<V>,
                                      private val setterTrigger: ((V)-> Unit)?)
    : DataStatePropertyDelegate<T>(initValue, key, dataStoreMap) {

    override fun setValue(thisRef: Any, property: KProperty<*>, value: T) {
        super.setValue(thisRef, property, value)
        setterTrigger?.let {
            if (!vs.hasSetterTrigger(property.name)) {
                vs.setPendingSetterTrigger(property.name, it)
            }
        }
        if (vs.status == IViewState.STATUS.ENTER) {
            setterTrigger?.invoke(vs.viewStateSupportable!!.view as V)
        }
    }
}


class VSNullableDataStateDelegate<V: View, T>(initValue: T?, key: String, dataStoreMap: HashMap<String, Any?>,
                                      private val vs: BaseViewState<V>,
                                      private val setterTrigger: ((V)-> Unit)?) :
    NullableDataStatePropertyDelegate<T>(initValue, key, dataStoreMap) {

    override fun setValue(thisRef: Any, property: KProperty<*>, value: T?) {
        super.setValue(thisRef, property, value)
        setterTrigger?.let {
            if (!vs.hasSetterTrigger(property.name)) {
                vs.setPendingSetterTrigger(property.name, it)
            }
        }
        if (vs.status == IViewState.STATUS.ENTER) {
            setterTrigger?.invoke(vs.viewStateSupportable!!.view as V)
        }
    }
}


class DefaultDataStatePropertyDelegate<V: View, T>(private val initValue: T,
                                                   private val vs: BaseViewState<V>,
                                                   private val setterTrigger: ((V)-> Unit)?)
    : ReadWriteProperty<Any?, T> {

    private var realValue: T = initValue

    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return realValue
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        this.realValue = value
        setterTrigger?.let {
            if (!vs.hasSetterTrigger(property.name)) {
                vs.setPendingSetterTrigger(property.name, it)
            }
        }
        if (vs.status == IViewState.STATUS.ENTER) {
            setterTrigger?.invoke(vs.viewStateSupportable!!.view as V)
        }
    }
}


class DefaultNullableDataStatePropertyDelegate<V: View, T>(private val initValue: T? = null,
                                                   private val vs: BaseViewState<V>,
                                                   private val setterTrigger: ((V)-> Unit)?)
    : ReadWriteProperty<Any?, T?> {

    private var realValue: T? = initValue

    override fun getValue(thisRef: Any?, property: KProperty<*>): T? {
        return realValue
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T?) {
        this.realValue = value
        setterTrigger?.let {
            if (!vs.hasSetterTrigger(property.name)) {
                vs.setPendingSetterTrigger(property.name, it)
            }
        }
        if (vs.status == IViewState.STATUS.ENTER) {
            setterTrigger?.invoke(vs.viewStateSupportable!!.view as V)
        }
    }
}