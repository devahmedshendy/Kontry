package learn.shendy.kontry.store;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import learn.shendy.kontry.repository.Repository;
import learn.shendy.kontry.store.modules.Internet;
import learn.shendy.kontry.utils.ToastUtil;

public class Store extends AndroidViewModel {
    private static final String TAG = "Store";

    // MARK: Properties

    private final Repository mRepository;

    public final State state;
    public final Actions actions;
    public final Mutations mutations;

    public final Internet internet;

    // MARK: Constructor Methods

    public Store(@NonNull Application application) {
        super(application);

        mRepository = new Repository();

        state = new State();
        mutations = new Mutations(state);
        actions = new Actions(state, mutations, mRepository);

        internet = new Internet(application);
    }

//    @SuppressLint("CheckResult")
//    public void initState() {
//        actions
//                .initState()
//                .observeOn(AndroidSchedulers.mainThread())
////                .retryWhen(throwableFlowable -> throwableFlowable
////                        .flatMap(throwable -> {
////                            if (throwable instanceof UnknownHostException) {
////                                return Flowable.just("");
////                            }
////
////                            return Flowable.error(throwable);
////                        }))
//                .subscribe(
//                        () -> {},
//                        this::onInitStateFailed
//                );
//    }

    private void onInitStateFailed(Throwable t) {
        ToastUtil.showUnhandledError(t);
    }
}
