package com.quangph.base.common;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.StatFs;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.quangph.base.common.bus.AppBus;
import com.quangph.base.common.bus.IAppEvent;
import com.quangph.base.common.bus.IAppEventHandler;
import com.quangph.base.viewbinder.ViewBinder;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Date;
import java.util.Locale;
import java.util.Map;


public abstract class BaseActivity extends AppCompatActivity implements IAppEventHandler {
    private boolean isInDebugMode = true;

    private boolean isKeyboardShown;
    private KeyboardTouchManager mKeyboardTouchManager;
    private ScreenOrientationHelper mScreenOrientationHelper;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private ActivityNavi<Intent, ActivityResult> mActivityLauncher =
            ActivityNavi.registerActivityForResult(this);
    private ActivityNavi<String, Boolean> mPermissionLauncher =
            ActivityNavi.registerActivityForResult(this,
            new ActivityResultContracts.RequestPermission());
    private ActivityNavi<String[], Map<String, Boolean>> mMultiPermissionLauncher =
            ActivityNavi.registerActivityForResult(this,
                    new ActivityResultContracts.RequestMultiplePermissions());

    protected void enableDebugMode(boolean isDebug) {
        isInDebugMode = isDebug;
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Thread.setDefaultUncaughtExceptionHandler(getUncaughtExceptionHandler());
        onViewWillCreate(savedInstanceState);
        int layout = onGetLayoutId();
        setContentView(layout);
        mScreenOrientationHelper = new ScreenOrientationHelper(this);
        mScreenOrientationHelper.onCreate(savedInstanceState);

        if (onInit()) {
            onBindView();
        }

        try {
            onViewDidCreated(savedInstanceState);
            preventFocusOnEditText();
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    try {
                        onPostInit(savedInstanceState);
                    } catch (Throwable t) {
                        t.printStackTrace();
                    }
                }
            });

            AppBus.getInstance().register(this);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    /**
     * Hide keyboard when touch or scroll outside keyboard. By default, the keyboard will hide when
     * touch outside.
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        /*switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastTouchX = ev.getX();
                mLastTouchY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (isScrollGesture(ev, mLastTouchX, mLastTouchY)) {
                    isPrevTouchEventMove = true;
                }
                break;
            case MotionEvent.ACTION_UP:
                if (isPrevTouchEventMove) {
                    isPrevTouchEventMove = false;
                    if (hideKeyboardWhenScrollingOutside()) {
                        boolean consumed = hideKeyBoardWhenTouchOutside(ev);
                        if (consumed) return true;
                    }
                } else {
                    if (hideKeyboardWhenScrollingOutside()) {
                        boolean consumed = hideKeyBoardWhenTouchOutside(ev);
                        if (consumed) return true;
                    }
                }
                break;
        }*/
        if (mKeyboardTouchManager.dispatchTouchEvent(ev)) {
            return true;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
        AppBus.getInstance().unregister(this);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mScreenOrientationHelper.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mScreenOrientationHelper.onRestoreInstanceState(savedInstanceState);
        mScreenOrientationHelper.checkOrientationChanged();
    }

    @Override
    public void handle(IAppEvent event) {

    }

    protected void onBindView() {
        ViewBinder.bind(this);
    }

    protected String[] emailForErrorReport() {
        return null;
    }

    protected Thread.UncaughtExceptionHandler getUncaughtExceptionHandler() {
        String[] emails = emailForErrorReport();
        return new UnCaughtException(this, emails);
    }

    protected void onViewWillCreate(Bundle savedInstanceState) {
    }

    protected int onGetLayoutId() {
        return ViewBinder.getViewLayout(this);
    }

    protected boolean onInit() {
        return true;
    }

    protected void onViewDidCreated(@Nullable Bundle savedInstanceState) {
        mKeyboardTouchManager = new KeyboardTouchManager(this);
    }

    protected void onPostInit(@Nullable Bundle savedInstanceState) {
    }

    protected void onHideKeyboard(){}

    protected void onShowKeyboard() {}

    public void setKeyboardAction(KeyboardTouchManager.KEYBOARD_CONFIG action) {
        mKeyboardTouchManager.setAction(action);
    }

    public Handler getHandler() {
        return mHandler;
    }

    /**
     * Check the touch region to dismiss keyboard
     * In some case, we need to a view show above keyboard,
     * so we need to check the touch point outside of that view to dismiss keyboard
     * @param visibleBound
     * @param x
     * @param y
     * @return
     */
    public boolean acceptToDismissKeyboard(Rect visibleBound, float x, float y) {
        return true;
    }

    /**
     * Check device orientation after destroy or just destroy by finish function. This func is used
     * in {@link #onDestroy()} to handle two cases: destroy before being recreated (orientation), or
     * destroyed permanently
     *
     * @return true if device orientation, mean the activity is destroyed and will recreate by orientation,
     * false when the activity is destroyed and will not recreate
     */
    public boolean checkOrientation() {
        return mScreenOrientationHelper.isOrientationChanged();
    }

    public void showToast(int stringId) {
        Toast.makeText(this, stringId, Toast.LENGTH_SHORT).show();
    }

    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void hideKeyBoard() {
        InputMethodManager imm = (InputMethodManager) this
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            View focusView = this.getCurrentFocus();
            IBinder token = focusView == null
                    ? getWindow().getDecorView().getApplicationWindowToken() : focusView.getWindowToken();
            if (focusView != null) {
                focusView.clearFocus();
            }
            imm.hideSoftInputFromWindow(token,
                    InputMethodManager.HIDE_NOT_ALWAYS);
            isKeyboardShown = false;
            onHideKeyboard();
        }
    }

    public void showKeyBoard(View v) {
        if (isKeyboardShown) return;
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(v, 0);
            isKeyboardShown = true;
            onShowKeyboard();
        }
    }

    public boolean isKeyboardShown() {
        return isKeyboardShown;
    }

    public void launch(Intent itn, @Nullable ActivityNavi.OnActivityResult<ActivityResult> onResult) {
        if (onResult == null) {
            startActivity(itn);
        } else {
            mActivityLauncher.launch(itn, onResult);
        }
    }

    public void permission(ActivityNavi.OnActivityResult<Boolean> onResult, String perm) {
        mPermissionLauncher.launch(perm, onResult);
    }

    public void permissions(ActivityNavi.OnActivityResult<Map<String, Boolean>> onResult, String... perms) {
        mMultiPermissionLauncher.launch(perms, onResult);
    }

    protected void logger(String log) {
        if (isInDebugMode) {
            Log.e(getClass().getSimpleName(), log);
        }
    }

    private void preventFocusOnEditText() {
        ViewGroup rootView = getWindow().getDecorView().findViewById(android.R.id.content);
        View contentView = rootView.getChildAt(0);
        contentView.setFocusable(true);
        contentView.setFocusableInTouchMode(true);
    }


    /**********************************************************************************************/
    public static class UnCaughtException implements Thread.UncaughtExceptionHandler {
        private Thread.UncaughtExceptionHandler previousHandler;
        private Context context;
        private String[] mEmailForErrorReports;

        public UnCaughtException(Context ctx, String[] email) {
            context = ctx;
            mEmailForErrorReports = email;
        }

        private StatFs getStatFs() {
            File path = Environment.getDataDirectory();
            return new StatFs(path.getPath());
        }

        private long getAvailableInternalMemorySize(StatFs stat) {
            long blockSize = stat.getBlockSize();
            long availableBlocks = stat.getAvailableBlocks();
            return availableBlocks * blockSize;
        }

        private long getTotalInternalMemorySize(StatFs stat) {
            long blockSize = stat.getBlockSize();
            long totalBlocks = stat.getBlockCount();
            return totalBlocks * blockSize;
        }

        private void addInformation(StringBuilder message) {
            message.append("Locale: ").append(Locale.getDefault()).append('\n');
            try {
                PackageManager pm = context.getPackageManager();
                PackageInfo pi;
                pi = pm.getPackageInfo(context.getPackageName(), 0);
                message.append("Version: ").append(pi.versionName).append('\n');
                message.append("Package: ").append(pi.packageName).append('\n');
            } catch (Exception e) {
                Log.e("CustomExceptionHandler", "Error", e);
                message.append("Could not create Version information for ").append(
                        context.getPackageName());
            }
            message.append("Phone Model: ").append(android.os.Build.MODEL).append(
                    '\n');
            message.append("Android Version: ").append(
                    android.os.Build.VERSION.RELEASE).append('\n');
            message.append("Board: ").append(android.os.Build.BOARD).append('\n');
            message.append("Brand: ").append(android.os.Build.BRAND).append('\n');
            message.append("Device: ").append(android.os.Build.DEVICE).append('\n');
            message.append("Host: ").append(android.os.Build.HOST).append('\n');
            message.append("ID: ").append(android.os.Build.ID).append('\n');
            message.append("Model: ").append(android.os.Build.MODEL).append('\n');
            message.append("Product: ").append(android.os.Build.PRODUCT).append(
                    '\n');
            message.append("Type: ").append(android.os.Build.TYPE).append('\n');
            StatFs stat = getStatFs();
            message.append("Total Internal memory: ").append(
                    getTotalInternalMemorySize(stat)).append('\n');
            message.append("Available Internal memory: ").append(
                    getAvailableInternalMemorySize(stat)).append('\n');
        }

        @Override
        public void uncaughtException(Thread t, Throwable e) {
            try {
                StringBuilder report = new StringBuilder();
                Date curDate = new Date();
                report.append("Error Report collected on : ").append(curDate.toString()).append('\n').append('\n');
                report.append("Informations :").append('\n');
                addInformation(report);
                report.append('\n').append('\n');
                report.append("Stack:\n");
                final Writer result = new StringWriter();
                final PrintWriter printWriter = new PrintWriter(result);
                e.printStackTrace(printWriter);
                report.append(result.toString());
                printWriter.close();
                report.append('\n');
                report.append("****  End of current Report ***");
                Log.e(UnCaughtException.class.getName(),
                        "Error while sendErrorMail" + report);

                if (mEmailForErrorReports != null && mEmailForErrorReports.length > 0) {
                    sendErrorMail(report, mEmailForErrorReports);
                }
            } catch (Throwable ignore) {
                Log.e(UnCaughtException.class.getName(),
                        "Error while sending error e-mail", ignore);
            }
        }

        private void sendErrorMail(final StringBuilder errorContent, String[] emails) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(context);
            new Thread() {
                @Override
                public void run() {
                    Looper.prepare();
                    builder.setTitle("Sorry...!");
                    builder.create();
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            System.exit(0);
                        }
                    });
                    builder.setPositiveButton("Report", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent sendIntent = new Intent(Intent.ACTION_SEND);
                            String subject = "Your App crashed! Fix it!";
                            StringBuilder body = new StringBuilder("Yoddle");
                            body.append('\n').append('\n');
                            body.append(errorContent).append('\n').append('\n');
                            sendIntent.setType("message/rfc822");
                            sendIntent.putExtra(Intent.EXTRA_EMAIL, mEmailForErrorReports);
                            sendIntent.putExtra(Intent.EXTRA_TEXT, body.toString());
                            sendIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
                            sendIntent.setType("message/rfc822");
                            context.startActivity(sendIntent);
                            System.exit(0);
                        }
                    });
                    builder.setMessage("Unfortunately,This application has stopped");
                    builder.show();
                    Looper.loop();
                }
            }.start();
        }
    }
}
