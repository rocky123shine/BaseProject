package com.rocky.common.base.adapter.holder;

import android.content.Context;
import android.view.ViewGroup;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author
 * @date 2019/12/6.
 * description：
 */
public class ViewHolderFactory {
    protected Context mContext;
    private Map<Class, Class<? extends BaseViewHolder>> boundViewHolder = new HashMap<>();
    private List<Class> valueClassType = new ArrayList<>();

    public ViewHolderFactory(Context context) {
        this.mContext = context;
    }

    public BaseViewHolder create(int viewType, ViewGroup parent) {
        Class valueClass = valueClassType.get(viewType);

        Class<? extends BaseViewHolder> viewHolderClass = boundViewHolder.get(valueClass);
        try {
            Constructor<? extends BaseViewHolder> constructor = viewHolderClass.getDeclaredConstructor(Context.class, ViewGroup.class);
            return constructor.newInstance(mContext, parent);
        } catch (Throwable e) {
            throw new RuntimeException("Unable to create ViewHolder for "
                    + valueClass
                    + "." + e.getMessage()
                    , e);
        }
    }

    public void bind(Class valueClass, Class<? extends BaseViewHolder> viewHolder) {
        valueClassType.add(valueClass);
        boundViewHolder.put(valueClass, viewHolder);
    }

    public int itemViewType(Object object) {
        return valueClassType.indexOf(object.getClass());
    }

    public List<Class> getValueClassType() {
        return valueClassType;
    }

    public Map<Class, Class<? extends BaseViewHolder>> getBoundViewHolder() {
        return boundViewHolder;
    }
}
