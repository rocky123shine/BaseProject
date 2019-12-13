package com.rocky.baseapplication.test.recyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.rocky.baseapplication.R;
import com.rocky.common.base.adapter.RecyclerAdapter;
import com.rocky.common.base.adapter.holder.BaseViewHolder;

/**
 * @author
 * @date 2019/12/13.
 * description：
 */
public class DesRvHolder extends BaseViewHolder<String> {
    public DesRvHolder(Context context, ViewGroup parent) {
        super(LayoutInflater.from(context).inflate(R.layout.item_rv_des, parent, false));
    }

    @Override
    public void bindTo(int position, String value, RecyclerAdapter adapter) {

    }
}
