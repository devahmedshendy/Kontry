package learn.shendy.kontry.store.modules;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.ConnectivityManager.NetworkCallback;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;
import learn.shendy.kontry.enums.NETWORK_STATUS;

import static learn.shendy.kontry.enums.NETWORK_STATUS.OFFLINE;
import static learn.shendy.kontry.enums.NETWORK_STATUS.ONLINE;
import static learn.shendy.kontry.enums.NETWORK_STATUS.UNAVAILABLE;

public class Internet {
    private static final String TAG = "Internet";

    private final ConnectivityManager mConnectivityManager;
    private final Application mApplication;
    private final NetworkRequest mNetworkRequest;

    // MARK: Constructor Methods

    public Internet(Application application) {
        mApplication = application;

        mConnectivityManager = (ConnectivityManager) mApplication.getSystemService(Context.CONNECTIVITY_SERVICE);

        mNetworkRequest = new NetworkRequest
                .Builder()
                .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
                .build();

        EMIT_INITIAL_NETWORK_STATUS();
        registerNetworkCallback();
    }

    // MARK: Setup Methods

    public void registerNetworkCallback() {
        Log.d(TAG, "registerNetworkCallback: starts");
        mConnectivityManager.registerNetworkCallback(mNetworkRequest, mNetworkCallback);
        Log.d(TAG, "registerNetworkCallback: ends");
    }

    public void unregisterNetworkCallback() {
        Log.d(TAG, "unregisterNetworkCallback: starts");
        mConnectivityManager.unregisterNetworkCallback(mNetworkCallback);
        Log.d(TAG, "unregisterNetworkCallback: ends");
    }

    private NetworkCallback mNetworkCallback = new NetworkCallback() {
        private List<Network> activeNetworks = new ArrayList<>();

        private void removeNetwork(Network network) {
            for (Iterator<Network> iterator = activeNetworks.iterator(); iterator.hasNext();) {
                Network net = iterator.next();

                if (net.equals(network)) {
                    activeNetworks.remove(net);
                }
            }
        }

        private boolean isNetworkSaved(Network network) {
            for (Network net : activeNetworks) {
                if (net.equals(network)) {
                    return false;
                }
            }

            return true;
        }

        @Override
        public void onAvailable(@NonNull Network network) {
            super.onAvailable(network);

            if (isNetworkSaved(network)) {
                activeNetworks.add(network);
            }

            EMIT_NETWORK_STATUS(activeNetworks.isEmpty() ? OFFLINE : ONLINE);
        }

        @Override
        public void onLost(@NonNull Network network) {
            super.onLost(network);

            removeNetwork(network);

            EMIT_NETWORK_STATUS(activeNetworks.isEmpty() ? OFFLINE : ONLINE);
        }

        @Override
        public void onUnavailable() {
            super.onUnavailable();

            EMIT_NETWORK_STATUS(UNAVAILABLE);
        }
    };

    // MARK: State Properties

    private NETWORK_STATUS mCurrentNetworkStatus;
    private BehaviorSubject<NETWORK_STATUS> mNetworkStatusObservable = BehaviorSubject.create();

    // MARK: Mutation Methods

    private void EMIT_INITIAL_NETWORK_STATUS() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Network activeNetwork = mConnectivityManager.getActiveNetwork();
            NetworkCapabilities capabilities = mConnectivityManager.getNetworkCapabilities(activeNetwork);

            mCurrentNetworkStatus = (
                    capabilities != null &&
                    capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                    capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
            ) ? ONLINE : OFFLINE;

        } else {
            NetworkInfo activeNetworkInfo = mConnectivityManager.getActiveNetworkInfo(); // Deprecated in 29
            mCurrentNetworkStatus = (
                    activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting()
            ) ? ONLINE : OFFLINE; // Deprecated in 28
        }

        Log.d(TAG, "EMIT_INITIAL_NETWORK_STATUS: " + mCurrentNetworkStatus);
        mNetworkStatusObservable.onNext(mCurrentNetworkStatus);
    }

    private void EMIT_NETWORK_STATUS(NETWORK_STATUS status) {
        mCurrentNetworkStatus = status;
        mNetworkStatusObservable.onNext(mCurrentNetworkStatus);
    }

    // MARK: Action Methods

    public Observable<NETWORK_STATUS> networkStatusObservable() {
        return mNetworkStatusObservable
                .subscribeOn(Schedulers.computation());
    }

    public Observable<NETWORK_STATUS> networkOnlineStatusObservable() {
        return mNetworkStatusObservable
                .filter(status -> status == ONLINE)
                .subscribeOn(Schedulers.computation());
    }

    public Observable<NETWORK_STATUS> networkOfflineStatusObservable() {
        return mNetworkStatusObservable
                .filter(status -> status == OFFLINE || status == UNAVAILABLE)
                .subscribeOn(Schedulers.computation());
    }

    public boolean isOnline() {
        Log.d(TAG, "isOnline: mCurrentNetworkStatus " + ONLINE);
        return mCurrentNetworkStatus == ONLINE;
    }

    public boolean isOffline() {
        return mCurrentNetworkStatus == OFFLINE;
    }

//    public NETWORK_STATUS getNetworkStatus() {
//
//        if (mConnectivityManager == null) {
//            Throwable t = new NullPointerException("ConnectivityManager is not available");
//            ToastUtil.showUnhandledError(t);
//            return UNAVAILABLE;
//        }
//
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            Network activeNetwork = mConnectivityManager.getActiveNetwork();
//            if (activeNetwork == null) return UNAVAILABLE;
//
//            NetworkCapabilities networkCapabilities = mConnectivityManager.getNetworkCapabilities(activeNetwork);
//            if (networkCapabilities == null) return UNAVAILABLE;
//
//            Log.d(TAG, "getNetworkStatus: " + networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN));
//
//            if (
//                    networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
//                    || networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
//                    || networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
//                    || networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN)
//            ) {
//                return ONLINE;
//            } else {
//                return OFFLINE;
//            }
//        } else {
//            NetworkInfo activeNetwork = mConnectivityManager.getActiveNetworkInfo();
//            if (activeNetwork == null) return UNAVAILABLE;
//
//            return activeNetwork.isConnectedOrConnecting() ? ONLINE : OFFLINE;
//        }
//    }
}
