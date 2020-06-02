package learn.shendy.kontry;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import learn.shendy.kontry.store.Store;
import learn.shendy.kontry.ui.fragments.components.BottomNavFragment;
import learn.shendy.kontry.ui.fragments.components.ContentLoaderFragment;
import learn.shendy.kontry.ui.fragments.components.PleaseWaitFragment;
import learn.shendy.kontry.ui.fragments.components.WaitingForTheNetworkFragment;
import learn.shendy.kontry.ui.fragments.components.WatermarkFragment;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private Store mStore;

    // MARK: Lifecycle Methods

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupActivity();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy: starts");
        super.onDestroy();
        mStore.internet.unregisterNetworkCallback();
        Log.d(TAG, "onDestroy: ends");
    }

    /*
            Keep it, you might need it later.
         */
//    void hideSystemUI() {
//        getWindow()
//                .getDecorView()
//                .setSystemUiVisibility(
//                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_FULLSCREEN
//                );
//    }

    // MARK: Setup Methods

    private void setupActivity() {
        mStore = new ViewModelProvider(this).get(Store.class);

        openBottomNavFragment();
        openWatermarkFragment();
        openContentLoaderFragment();
        openPleaseWaitFragment();
        openNoInternetFragment();
    }

    private void openWatermarkFragment() {
        openFragment(
                R.id.watermark_frame_container,
                new WatermarkFragment(),
                WatermarkFragment.TAG
        );
    }

    private void openBottomNavFragment() {
        openFragment(
                R.id.bottom_nav_frame_container,
                new BottomNavFragment(),
                BottomNavFragment.TAG
        );
    }

    private void openContentLoaderFragment() {
        openFragment(
                R.id.content_loader_frame_container,
                new ContentLoaderFragment(),
                ContentLoaderFragment.TAG
        );
    }

    private void openPleaseWaitFragment() {
        openFragment(
                R.id.please_wait_frame_container,
                new PleaseWaitFragment(),
                PleaseWaitFragment.TAG
        );
    }

    private void openNoInternetFragment() {
        openFragment(
                R.id.no_internet_connection_frame_container,
                new WaitingForTheNetworkFragment(),
                WaitingForTheNetworkFragment.TAG
        );
    }

    private void openFragment(int container, Fragment fragment, String tag) {
        getSupportFragmentManager()
                .beginTransaction()
                .add(container, fragment, tag)
                .disallowAddToBackStack()
                .commit();
    }
}
