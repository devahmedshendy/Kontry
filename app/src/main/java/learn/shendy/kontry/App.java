package learn.shendy.kontry;

import android.content.res.TypedArray;

public class App extends android.app.Application {
    public static String PACKAGE_NAME;
    public static float ACTION_BAR_SIZE;
    public static App INSTANCE;

    @Override
    public void onCreate() {
        super.onCreate();

        INSTANCE = this;
        PACKAGE_NAME = getApplicationContext().getPackageName();
        ACTION_BAR_SIZE = getActionBarSize();
    }

    /*
        reference http://blog.moagrius.com/android/android-get-system-defined-actionbar-size-height/
     */
    public static float getActionBarSize() {
        TypedArray styledAttributes = INSTANCE.getTheme().obtainStyledAttributes(new int[] { android.R.attr.actionBarSize });
        float actionBarSize = styledAttributes.getDimension(0, 0);
        styledAttributes.recycle();
        return actionBarSize;
    }
}
