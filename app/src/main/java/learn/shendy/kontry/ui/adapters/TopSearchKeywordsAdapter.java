package learn.shendy.kontry.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import java.util.Collections;
import java.util.List;

import learn.shendy.kontry.R;
import learn.shendy.kontry.databinding.HolderTopSearchKeywordBinding;
import learn.shendy.kontry.databinding.HolderTopSearchKeywordsHeaderBinding;
import learn.shendy.kontry.repository.local.search_keywords.SearchKeyword;
import learn.shendy.kontry.ui.fragments.BaseFragment;

public class TopSearchKeywordsAdapter extends RecyclerView.Adapter<ViewHolder> {

    public interface TopSearchKeywordsHandler {
        void onTopSearchKeywordSelect(SearchKeyword keyword);
        void onTopSearchKeywordDelete(SearchKeyword keyword);
    }

    private final int TYPE_ITEM = R.layout.holder_top_search_keyword;
    private final int TYPE_HEADER = R.layout.holder_top_search_keywords_header;

    private LayoutInflater mLayoutInflater;
    private TopSearchKeywordsHandler mTopSearchKeywordsHandler;

    private List<SearchKeyword> mKeywords = Collections.emptyList();

    public TopSearchKeywordsAdapter(Context context, BaseFragment fragment) {
        mLayoutInflater = LayoutInflater.from(context);
        mTopSearchKeywordsHandler = (TopSearchKeywordsHandler) fragment;
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? TYPE_HEADER : TYPE_ITEM;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewHolder holder;
        if (viewType == TYPE_HEADER) {
            HolderTopSearchKeywordsHeaderBinding binding = HolderTopSearchKeywordsHeaderBinding
                    .inflate(mLayoutInflater, parent, false);

            holder = new TopSearchKeywordsHeaderHolder(binding);
        } else {
            HolderTopSearchKeywordBinding binding = HolderTopSearchKeywordBinding
                    .inflate(mLayoutInflater, parent, false);

            holder = new TopSearchKeywordHolder(binding);
        }

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        if (position > 0) {
            TopSearchKeywordHolder holder = (TopSearchKeywordHolder) viewHolder;

            SearchKeyword keyword = mKeywords.get(position - 1);

            holder.mBinding.topSearchKeywordTv.setText(keyword.getName());

            holder.mBinding.topSearchKeywordTv.setOnClickListener(__ ->
                    mTopSearchKeywordsHandler.onTopSearchKeywordSelect(keyword));

            holder.mBinding.topSearchKeywordDeleteBtn.setOnClickListener(__ ->
                    mTopSearchKeywordsHandler.onTopSearchKeywordDelete(keyword));
        }
    }

    @Override
    public int getItemCount() {
        return mKeywords.size() + 1;
    }

    public void update(List<SearchKeyword> list) {
        mKeywords = list;
        notifyDataSetChanged();
    }

    static class TopSearchKeywordHolder extends ViewHolder {

        HolderTopSearchKeywordBinding mBinding;

        TopSearchKeywordHolder(@NonNull HolderTopSearchKeywordBinding binding) {
            super(binding.getRoot());

            mBinding = binding;
        }
    }

    static class TopSearchKeywordsHeaderHolder extends ViewHolder {

        HolderTopSearchKeywordsHeaderBinding mBinding;


        TopSearchKeywordsHeaderHolder(@NonNull HolderTopSearchKeywordsHeaderBinding binding) {
            super(binding.getRoot());

            mBinding = binding;
        }
    }
}
