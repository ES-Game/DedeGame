package com.quangph.base.lifecycle

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.annotation.NonNull

/**
 * Created by Pham Hai QUANG on 9/5/2018.
 */

interface ILifeCycle

interface IDestroy: ILifeCycle

interface OnActivityCreated: ILifeCycle {
    fun onActivityCreated(savedInstanceState: Bundle)
}

interface OnActivityResult: ILifeCycle {
    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent): Boolean
}

interface OnConfigurationChanged: ILifeCycle {
    fun onConfigurationChanged(newConfig: Configuration): Boolean
}

interface OnAttach: ILifeCycle {
    fun onAttach(context: Context)
}

interface OnDestroy: IDestroy {
    fun onDestroy()
}

interface OnDestroyView: IDestroy {
    fun onDestroyView()
}

interface OnDetach: ILifeCycle {
    fun onDetach()
}

interface OnHiddenChanged: ILifeCycle {
    fun onHiddenChanged(hidden: Boolean)
}

interface OnNewIntent: ILifeCycle {
    fun onNewIntent(intent: Intent)
}

interface OnPause: ILifeCycle {
    fun onPause()
}

interface OnPostCreate: ILifeCycle {
    fun onPostCreate(savedInstanceState: Bundle)
}

interface OnRestart: ILifeCycle {
    fun onRestart()
}

interface OnRestoreInstanceState: ILifeCycle {
    fun onRestoreInstanceState(savedInstance: Bundle): Boolean
}

interface OnResume: ILifeCycle {
    fun onResume()
}

interface OnSaveInstanceState: ILifeCycle {
    fun onSaveInstanceState(outState: Bundle): Boolean
}

interface OnStart: ILifeCycle {
    fun onStart()
}

interface OnStop: ILifeCycle {
    fun onStop()
}

interface OnViewCreated: ILifeCycle {
    fun onViewCreated(view: View, savedInstanceState: Bundle)
}

interface OnVisible: ILifeCycle {
    fun onVisible(isVisible: Boolean)
}

interface OnRequestPermissionsResult: ILifeCycle {
    fun onRequestPermissionsResult(requestCode: Int, @NonNull permissions: Array<String>,
                                   @NonNull grantResults: Array<Int>)
}

interface OnBackPressed: ILifeCycle {
    fun onBackPressed(): Boolean
}