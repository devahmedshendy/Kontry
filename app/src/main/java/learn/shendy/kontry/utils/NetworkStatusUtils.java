package learn.shendy.kontry.utils;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import learn.shendy.kontry.App;
import learn.shendy.kontry.enums.NETWORK_STATUS;

import static learn.shendy.kontry.enums.NETWORK_STATUS.OFFLINE;
import static learn.shendy.kontry.enums.NETWORK_STATUS.ONLINE;
import static learn.shendy.kontry.enums.NETWORK_STATUS.UNAVAILABLE;

public class NetworkStatusUtils extends LiveData<NETWORK_STATUS> {
    private static final String TAG = "InternetConnectionRecei";

    private static NetworkStatusUtils INSTANCE;

    public static NetworkStatusUtils singleton() {
        if (INSTANCE == null) {
            INSTANCE = new NetworkStatusUtils(App.INSTANCE);
        }

        return INSTANCE;
    }

    private Application mApplication;
    private NetworkRequest mNetworkRequest;

    private NetworkStatusUtils(Application application) {
        mApplication = application;

        mNetworkRequest = new NetworkRequest
                .Builder()
                .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
                .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                .addTransportType(NetworkCapabilities.TRANSPORT_ETHERNET)
                .build();
    }

    @Override
    protected void onActive() {
        super.onActive();
        postNetworkStatus();
    }

    public NETWORK_STATUS getNetworkStatus() {
        ConnectivityManager cm = (ConnectivityManager) mApplication
                .getSystemService(Context.CONNECTIVITY_SERVICE);


        if (cm == null) {
            Throwable t = new NullPointerException("ConnectivityManager is not available");
            ToastUtil.showUnhandledError(t);
            return UNAVAILABLE;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Network activeNetwork = cm.getActiveNetwork();
            if (activeNetwork == null) return UNAVAILABLE;

            NetworkCapabilities networkCapabilities = cm.getNetworkCapabilities(activeNetwork);
            if (networkCapabilities == null) return UNAVAILABLE;

            if (
                    networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                    || networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                    || networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
            ){
                return ONLINE;
            } else {
                return OFFLINE;
            }
        } else {
            NetworkInfo activeNetworkInfo = cm.getActiveNetworkInfo();
            if (activeNetworkInfo == null) return UNAVAILABLE;

            return activeNetworkInfo.isConnected() ? ONLINE : OFFLINE;
        }
    }

    private void postNetworkStatus() {
        ConnectivityManager cm = (ConnectivityManager) mApplication
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        if (cm == null) {
            Throwable t = new NullPointerException("ConnectivityManager is not available");
            ToastUtil.showUnhandledError(t);
            postValue(UNAVAILABLE);
            return;
        }

        cm.registerNetworkCallback(mNetworkRequest, new ConnectivityManager.NetworkCallback() {
            @Override
            public void onAvailable(@NonNull Network network) {
                super.onAvailable(network);

                postValue(ONLINE);
            }

            @Override
            public void onLost(@NonNull Network network) {
                super.onLost(network);
                postValue(OFFLINE);
            }

            @Override
            public void onUnavailable() {
                super.onUnavailable();
                postValue(UNAVAILABLE);
            }
        });
    }
}
