package com.rocky.common.base.adapter.holder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.rocky.common.base.adapter.RecyclerAdapter;
import com.rocky.common.base.adapter.util.OnHolderChangeListener;

/**
 * @author
 * @date 2019/12/6.
 * description：
 */
public abstract class BaseViewHolder<V> extends RecyclerView.ViewHolder implements View.OnClickListener {
    private OnItemClickListener mOnItemClickListener;
    protected OnHolderChangeListener mOnHolderChangeListener;


    public BaseViewHolder(Context context, ViewGroup parent, int layoutId) {
        this(LayoutInflater.from(context).inflate(layoutId, parent, false));
    }

    public BaseViewHolder(View itemView) {
        super(itemView);

        bindListener();
    }

    private void bindListener() {
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(getAdapterPosition(), view);
        }
    }


    public void setItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(final int position, View view);
    }


    public void onViewAttachedToWindow() {
    }

    public void onViewDetachedFromWindow() {
    }

    public void setOnHolderChangeListener(OnHolderChangeListener listener) {
        mOnHolderChangeListener = listener;
    }

    public abstract void bindTo(int position, V value, RecyclerAdapter adapter);
}
