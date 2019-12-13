package com.rocky.common.base.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rocky.common.base.adapter.holder.BaseViewHolder;
import com.rocky.common.base.adapter.holder.ViewHolderFactory;
import com.rocky.common.base.adapter.util.OnHolderChangeListener;
import com.rocky.common.base.adapter.util.OnValueChangeListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author
 * @date 2019/12/6.
 * description：
 */
public class RecyclerAdapter extends RecyclerView.Adapter<BaseViewHolder> implements OnHolderChangeListener {
    private ViewHolderFactory mFactory;
    private List<Object> dataList = new ArrayList<>();
    private BaseViewHolder.OnItemClickListener mOnItemClickListener;
    private Map<String, Object> mapField = new HashMap<>();
    private OnValueChangeListener mOnValueChangeListener;
    private BaseViewHolder holder;

    public RecyclerAdapter(Context context) {
        this(new ViewHolderFactory(context));
    }

    public void putField(String key, Object object) {
        mapField.put(key, object);
    }

    public RecyclerAdapter(ViewHolderFactory factory) {
        mFactory = factory;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        BaseViewHolder viewHolder = mFactory.create(viewType, parent);
        bindListener(viewHolder);
        bindChangeListener(viewHolder);
        holder = viewHolder;
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        holder.bindTo(position, dataList.get(position), this);
    }

    @Override
    public int getItemViewType(int position) {
        return mFactory.itemViewType(dataList.get(position));
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    @Override
    public void onHolderChange(BaseViewHolder viewHolder) {
        if (mOnValueChangeListener != null) {
            mOnValueChangeListener.onValueChanged(dataList);
        }
    }

    public void bind(Class valueClass, Class<? extends BaseViewHolder> viewHolder) {
        mFactory.bind(valueClass, viewHolder);
    }

    @Override
    public void onViewAttachedToWindow(@NonNull BaseViewHolder holder) {
        holder.onViewAttachedToWindow();
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull BaseViewHolder holder) {
        holder.onViewDetachedFromWindow();
    }

    public List<Object> getItems() {
        return dataList;
    }

    public void putFiled(String key, Object value) {
        mapField.put(key, value);
    }

    public <T> T getField(String key) {
        return (T) mapField.get(key);
    }

    private void bindChangeListener(BaseViewHolder viewHolder) {
        if (viewHolder != null) {
            viewHolder.setOnHolderChangeListener(this);
        }
    }

    private void bindListener(BaseViewHolder viewHolder) {
        if (viewHolder != null) {
            viewHolder.setItemClickListener(mOnItemClickListener);
        }
    }

    public void setOnItemClickListener(BaseViewHolder.OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public void setmOnValueChangeListener(OnValueChangeListener mOnValueChangeListener) {
        this.mOnValueChangeListener = mOnValueChangeListener;
    }

    public void add(Object object) {
        dataList.add(object);
        notifyItemInserted(dataList.size());
    }

    public void add(int pos, Object object) {
        dataList.add(pos, object);
        notifyItemInserted(dataList.size());
    }

    public void addAll(List<?> list) {
        dataList.clear();
        if (list != null && list.size() > 0) {
            dataList.addAll(list);
        }

        notifyDataSetChanged();
    }

    public void remove(int position) {
        dataList.remove(position);
        notifyItemRemoved(position);
    }

    public void clear() {
        dataList.clear();
        notifyDataSetChanged();
    }

    public void appendAll(List<?> list) {
        if (list == null || list.size() == 0) {
            if (holder != null) {
                Toast.makeText(holder.itemView.getContext(), "没有更多", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        int wantSize = list.size();
        int preSize = dataList.size();
        List<Object> data = new ArrayList<>(preSize + wantSize);
        data.addAll(dataList);
        data.addAll(list);
        dataList = data;
        notifyItemRangeInserted(preSize, wantSize);
    }

    public Object getItem(int position) {
        if (position < dataList.size()) {
            return dataList.get(position);
        } else {
            return dataList.get(position - 1);
        }
    }
}
