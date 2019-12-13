package com.rocky.common.base;

import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.rocky.common.R;
import com.rocky.common.base.adapter.RecyclerAdapter;
import com.rocky.common.base.adapter.holder.BaseViewHolder;
import com.rocky.common.base.adapter.util.OnValueChangeListener;
import com.rocky.common.base.adapter.util.RecyclerConfig;
import com.rocky.common.base.event.LoadingEvent;
import com.rocky.common.base.widget.load_rv.RockyLoadRV;
import com.rocky.common.util.Preconditions;

import java.util.List;
import java.util.Map;

/**
 * @author
 * @date 2019/12/12.
 * description：
 */
public abstract class BaseRecyclerViewFragment extends BaseFragment implements
        SwipeRefreshLayout.OnRefreshListener, BaseViewHolder.OnItemClickListener,
        OnValueChangeListener {
    protected int page = 1;//全局上拉加载页面控制

    protected SwipeRefreshLayout mRefreshLayout;
    protected RockyLoadRV mRecyclerView;
    protected RecyclerAdapter mAdapter;
    private RecyclerConfig mRecyclerConfig;


    @Override
    public void onRefresh() {
        page = 1;
    }

    /**
     * @param position 点击的item位置
     * @param view     item
     */
    @Override
    public void onItemClick(int position, View view) {

    }

    @Override
    protected void init() {
        mRefreshLayout = getView().findViewById(R.id.refresh_layout);
        mRecyclerView = getView().findViewById(R.id.recycler_view);
    }

    @Override
    public void onValueChanged(List<Object> list) {

    }

    protected void buildConfig(RecyclerConfig config) {//基本设置
        mAdapter = new RecyclerAdapter(getContext());
        mAdapter.setOnItemClickListener(this);
        mAdapter.setOnItemClickListener(this);
        Map<Class, Class<? extends BaseViewHolder>> boundViewHolder = config.getBoundViewHolder();
        if (!boundViewHolder.isEmpty()) {
            for (Map.Entry<Class, Class<? extends BaseViewHolder>> classClassEntry : boundViewHolder.entrySet()) {
                mAdapter.bind(classClassEntry.getKey(), classClassEntry.getValue());
            }
        }
        mRecyclerConfig = config;
    }

    protected RecyclerConfig getRecyclerConfig() {
        return mRecyclerConfig;
    }

    public void setRefreshLayout() { //刷新设置
        RecyclerConfig config = getRecyclerConfig();
        Preconditions.checkNotNull(config);

        boolean enabled = config.enableRefresh();
        mRefreshLayout.setEnabled(enabled);
        if (enabled) {
            mRefreshLayout.setOnRefreshListener(this);
        }
    }


    protected void setRecyclerView() {//rv设置
        mRecyclerView.setAdapter(mAdapter);
        RecyclerConfig config = getRecyclerConfig();
        RecyclerView.LayoutManager layoutManager = config.getLayoutManager();
        if (layoutManager != null) {
            mRecyclerView.setLayoutManager(layoutManager);
        } else {
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        }
        RecyclerView.ItemAnimator itemAnimator = config.getItemAnimator();
        if (itemAnimator != null) {
            mRecyclerView.setItemAnimator(itemAnimator);
        }

        RecyclerView.ItemDecoration decoration = config.getDecoration();
        if (decoration != null) {
            mRecyclerView.addItemDecoration(decoration);
        }
        mRecyclerView.setPullLoadEnable(config.enableLoad());
        mRecyclerView.setOnLoadListener(new RockyLoadRV.IonLoadListener() {
            @Override
            public void onLoadMore() {
                onLoad();
            }
        });

    }

    @Override
    public void dealwithLoadingDialog(LoadingEvent event) {
        super.dealwithLoadingDialog(event);
        if (!event.isLoading() && getRecyclerConfig().enableLoad()) {
            mRecyclerView.stopLoadMore();
        }
        if (!event.isLoading() && getRecyclerConfig().enableRefresh()) {
            mRefreshLayout.setRefreshing(false);
        }
    }

    protected void onLoad() {
        ++page;
    }
}
