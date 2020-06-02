package learn.shendy.kontry.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import learn.shendy.kontry.App;
import learn.shendy.kontry.R;

public class ToastUtil {
    private static final String TAG = "ToastUtil";

    private static Context getApplicationContext() {
        return App.INSTANCE.getApplicationContext();
    }

    public static void showLongInfo(int messageResId) {
        showInfo(messageResId, Toast.LENGTH_LONG);
    }

    public static void showShortInfo(int messageResId) {
        showInfo(messageResId, Toast.LENGTH_SHORT);
    }

    private static void showInfo(int messageResId, int duration) {
        Toast.makeText(
                getApplicationContext(),
                messageResId,
                duration
        ).show();
    }

    public static void showLongError(int messageResId, Throwable t) {
        showError(messageResId, t, Toast.LENGTH_LONG);
    }

    public static void showShortError(int resId, Throwable t) {
        showError(resId, t, Toast.LENGTH_SHORT);
    }

    public static void showUnhandledError(Throwable t) {
        showLongError(R.string.unhandled_error, t);
    }

    private static void showError(int messageResId, Throwable t, int duration) {
        Log.e(TAG, "Toast Error: " + t.getMessage());
        t.printStackTrace();

        Toast.makeText(
                getApplicationContext(),
                messageResId,
                duration
        ).show();
    }
}
