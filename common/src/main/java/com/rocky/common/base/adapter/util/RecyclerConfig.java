package com.rocky.common.base.adapter.util;

import androidx.recyclerview.widget.RecyclerView;

import com.rocky.common.base.adapter.holder.BaseViewHolder;
import com.rocky.common.base.adapter.holder.ViewHolderFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author
 * @date 2019/12/6.
 * description：
 */
public class RecyclerConfig {
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.ItemDecoration mDecoration;
    private RecyclerView.ItemAnimator mItemAnimator;
    private ViewHolderFactory mViewHolderFactory;
    private Map<Class, Class<? extends BaseViewHolder>> mBoundViewHolder;
    private boolean mRefreshEnabled = true;
    private boolean mLoadEnable = false;

    public RecyclerView.LayoutManager getLayoutManager() {
        return mLayoutManager;
    }

    public RecyclerView.ItemDecoration getDecoration() {
        return mDecoration;
    }

    public RecyclerView.ItemAnimator getItemAnimator() {
        return mItemAnimator;
    }

    public Map<Class, Class<? extends BaseViewHolder>> getBoundViewHolder() {
        return mBoundViewHolder;
    }

    public ViewHolderFactory getViewHolderFactory() {
        return mViewHolderFactory;
    }

    public boolean enableRefresh() {
        return mRefreshEnabled;
    }

    public boolean enableLoad() {
        return mLoadEnable;
    }

    public static class Builder {
        private RecyclerView.LayoutManager mLayoutManager;
        private RecyclerView.ItemDecoration mDecoration;
        private RecyclerView.ItemAnimator mItemAnimator;
        private ViewHolderFactory mViewHolderFactory;
        private Map<Class, Class<? extends BaseViewHolder>> mBoundViewHolder = new HashMap<>();
        private boolean mRefreshEnabled = true;
        private boolean mLoadEnable = false;

        public RecyclerConfig build() {
            RecyclerConfig config = new RecyclerConfig();
            config.mLayoutManager = mLayoutManager;
            config.mDecoration = mDecoration;
            config.mItemAnimator = mItemAnimator;
            config.mBoundViewHolder = mBoundViewHolder;
            config.mRefreshEnabled = mRefreshEnabled;
            config.mLoadEnable = mLoadEnable;
            config.mViewHolderFactory = mViewHolderFactory;
            return config;
        }

        public Builder viewHolderFactory(ViewHolderFactory factory) {
            mViewHolderFactory = factory;
            return this;
        }

        public Builder layoutManager(RecyclerView.LayoutManager layoutManager) {
            mLayoutManager = layoutManager;
            return this;
        }

        public Builder decoration(RecyclerView.ItemDecoration itemDecoration) {
            mDecoration = itemDecoration;
            return this;
        }

        public Builder itemAnimator(RecyclerView.ItemAnimator itemAnimator) {
            mItemAnimator = itemAnimator;
            return this;
        }

        public Builder bind(Class valueClass, Class<? extends BaseViewHolder> viewHolder) {
            mBoundViewHolder.put(valueClass, viewHolder);
            return this;
        }

        public Builder enableRefresh(boolean mRefreshEnabled) {
            this.mRefreshEnabled = mRefreshEnabled;
            return this;
        }

        public Builder enableload(boolean mLoadEnable) {
            this.mLoadEnable = mLoadEnable;
            return this;
        }
    }
}
