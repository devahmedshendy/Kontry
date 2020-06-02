package learn.shendy.kontry.ui.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import androidx.transition.AutoTransition;
import androidx.transition.Transition;
import androidx.transition.TransitionManager;

import java.util.Collections;
import java.util.List;

import learn.shendy.kontry.R;
import learn.shendy.kontry.animators.CountryCellAnimator;
import learn.shendy.kontry.databinding.HolderCountryCellBinding;
import learn.shendy.kontry.repository.model.country.CountryCell;
import learn.shendy.kontry.ui.fragments.BaseFragment;
import learn.shendy.kontry.utils.SVGUtil;

public class CountryCellsAdapter extends Adapter<CountryCellsAdapter.CountryCellHolder> {
    private static final String TAG = "CountryCellsAdapter";

    public interface CountryNameClickHandler {
        void onCountryNameClick(String name);
    }

    private RecyclerView mRecyclerView;

    private final CountryNameClickHandler mCountryNameClickHandler;
    private final LayoutInflater mLayoutInflater;

    private int mCurrentExpandedPosition = -1;
    private List<CountryCell> mList = Collections.emptyList();

    public CountryCellsAdapter(Context context, BaseFragment fragment) {
        mLayoutInflater = LayoutInflater.from(context);
        mCountryNameClickHandler = (CountryNameClickHandler) fragment;
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mRecyclerView = recyclerView;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @NonNull
    @Override
    public CountryCellHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        HolderCountryCellBinding binding = HolderCountryCellBinding
                .inflate(mLayoutInflater, parent, false);
        return new CountryCellHolder(binding, parent);
    }

    @SuppressLint({"ClickableViewAccessibility", "CheckResult"})
    @Override
    public void onBindViewHolder(@NonNull CountryCellHolder holder, int position) {
        CountryCell countryCell = mList.get(position);

        holder.resetIfExpanded();

        holder.bind(countryCell);

        holder.mBinding.ccCountryNameTv.setOnClickListener(btn ->
                mCountryNameClickHandler.onCountryNameClick(countryCell.getName()));

//        holder.mBinding.ccCountryNameTv.setOnClickListener(btn -> {
//            mStore.actions
//                    .findCountryByFullName(countryCell.getName())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(
//                            country -> {
//                                NavDirections action = CountriesFragmentDirections
//                                        .actionCountriesFragmentToCountryDetailsFragment(country);
//
//                                Navigation.findNavController(btn).navigate(action);
//                            },
//                            ToastUtil::showUnhandledError
//                    );
//        });

        holder.mBinding.ccExpandCellBtn.setOnClickListener(__ -> {
            countryCell.flipExpanded();

            if (countryCell.isExpanded()) {
                if (mCurrentExpandedPosition != -1 && mCurrentExpandedPosition != position) {
                    CountryCellHolder expandedHolder = (CountryCellHolder) mRecyclerView
                            .findViewHolderForAdapterPosition(mCurrentExpandedPosition);

                    if (expandedHolder != null) {
                        expandedHolder.mBinding.ccExpandCellBtn.performClick();
                    }
                }
                mCurrentExpandedPosition = position;

            } else {
                mCurrentExpandedPosition = -1;
            }

            holder.bind(countryCell);
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull CountryCellHolder holder) {
        super.onViewDetachedFromWindow(holder);
    }

    public void updateList(List<CountryCell> newList) {
        mList = newList;
        notifyDataSetChanged();
    }

    static class CountryCellHolder extends ViewHolder {

        private boolean mExpanded = false;

        private RecyclerView mParent;
        private final ConstraintLayout mMainLayout;
        private final ConstraintSet mCollapsedConstraints = new ConstraintSet();
        private final ConstraintSet mExpandedConstraints = new ConstraintSet();

        private CountryCellAnimator mCellAnimator;

        CountryCell mDetails;

        final HolderCountryCellBinding mBinding;

        CountryCellHolder(HolderCountryCellBinding binding, ViewGroup parent) {
            super(binding.getRoot());

            mBinding = binding;
            mMainLayout = (ConstraintLayout) itemView;
            mParent = (RecyclerView) parent;

            mCellAnimator = new CountryCellAnimator(mBinding.ccCountryNameTv, mBinding.ccExpandCellBtn, mBinding.ccCellWrapper);

            mCollapsedConstraints.clone(mMainLayout);
            mExpandedConstraints.clone(mMainLayout.getContext(), R.layout.holder_country_cell_expand);

            // I may need this approach one time, so I kept it
            /*
                ViewHolder has not been added to the recyclerView yet,
                so we need to listen to add it later
             */
//            itemView
//                    .getViewTreeObserver()
//                    .addOnGlobalLayoutListener(() -> mParent = (RecyclerView) itemView.getParent());
        }

        void bind(CountryCell details) {
            mDetails = details;

            setExpanded(mDetails.isExpanded());

            mBinding.ccCountryNameTv.setText(mDetails.getName());
        }

        @SuppressLint("ClickableViewAccessibility")
        void setExpanded(boolean expand) {
            if (mExpanded == expand) return;

            mExpanded = expand;

            Transition autoTransition = new AutoTransition();
            TransitionManager.beginDelayedTransition(mParent == null ? mMainLayout : mParent, autoTransition);

            if (mExpanded) {
                mBinding.ccCountryCapitalTv.setText(mDetails.getCapital());
                mBinding.ccCountryPopulationTv.setText(String.valueOf(mDetails.getPopulation()));

                mExpandedConstraints.applyTo(mMainLayout);
                mCellAnimator.forward();

                SVGUtil.fetchInto(mMainLayout.getContext(), mDetails.getFlagURL(), mBinding.ccCountryFlagIv);

            } else {
                mCellAnimator.backward();
                mCollapsedConstraints.applyTo(mMainLayout);
            }
        }

        void resetIfExpanded() {
            if (mExpanded) {
                mBinding.ccExpandCellBtn.performClick();
            }
        }
    }
}
