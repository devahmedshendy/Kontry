package learn.shendy.kontry.ui.fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import net.yslibrary.android.keyboardvisibilityevent.util.UIUtil;

import java.net.UnknownHostException;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import learn.shendy.kontry.R;
import learn.shendy.kontry.store.Store;

public abstract class BaseFragment extends Fragment {
    private static final String TAG = "BaseFragment";

    protected CompositeDisposable mFragmentDisposables = new CompositeDisposable();

    protected Store mStore;
    private ActionBar mSupportActionBar;
    private FragmentManager mSupportFragmentManager;

    // MARK: Lifecycle Methods

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mStore = new ViewModelProvider(requireActivity()).get(Store.class);
        mSupportActionBar = ((AppCompatActivity) requireActivity()).getSupportActionBar();
        mSupportFragmentManager = requireActivity().getSupportFragmentManager();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        mFragmentDisposables.clear();
    }

    // MARK: Common Fragment Methods

    public boolean reactWhenNoInternetThrowable(Throwable t) {
        if (t instanceof UnknownHostException) {
            hideKeyboard();

            Navigation
                    .findNavController(requireActivity(), R.id.main_nav_host)
                    .navigate(R.id.global_action_to_noInternetFragment);
            return true;
        }
        return false;
    }

    public boolean reactWhenFailedToReachInternet() {
        if (mStore.internet.isOffline()) {
            Navigation
                    .findNavController(requireActivity(), R.id.main_nav_host)
                    .navigate(R.id.global_action_to_noInternetFragment);
            return true;
        }

        return false;
    }

    public void hideActionBar() {
        mSupportActionBar.hide();
    }

    public void setCustomActionBarView(View actionBar) {
        if (mSupportActionBar != null) {
//            LayoutParams layoutParams = new LayoutParams(MATCH_PARENT, MATCH_PARENT);
//            View customView = LayoutInflater.from(requireContext()).inflate(resId, null, false);

            mSupportActionBar.show();
            mSupportActionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            mSupportActionBar.setDisplayShowHomeEnabled(false);
            mSupportActionBar.setDisplayShowTitleEnabled(false);
            mSupportActionBar.setDisplayShowCustomEnabled(true);
            mSupportActionBar.setCustomView(actionBar);
            mSupportActionBar.setElevation(0);

        }
    }

    void openFragment(int container, Fragment fragment, String tag, boolean addToBackStack) {
        FragmentTransaction transaction = mSupportFragmentManager
                .beginTransaction()
                .add(container, fragment, tag);

        if (addToBackStack) {
            transaction.addToBackStack(tag);
        }

        transaction.commit();
    }

    void closeFragment(String tag) {
        hideKeyboard();

        Fragment fragment = mSupportFragmentManager.findFragmentByTag(tag);

        if (fragment != null) {
            mSupportFragmentManager
                    .beginTransaction()
                    .remove(fragment)
                    .commit();
        }
    }

    void addOnBackPressedCallback(OnBackPressedCallback callback) {
        requireActivity()
                .getOnBackPressedDispatcher()
                .addCallback(callback);
    }

    protected void navigateBack() {
        hideKeyboard();
        requireActivity().onBackPressed();
    }

    protected void hideKeyboard() {
        UIUtil.hideKeyboard(requireActivity());
    }

    protected void openKeyboardFor(EditText target) {
        target.requestFocus();
        UIUtil.showKeyboard(requireActivity(), target);
    }

    protected void saveDisposableOnSubscribe(Disposable d) {
        mFragmentDisposables.add(d);
    }
}
