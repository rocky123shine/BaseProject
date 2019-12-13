package com.rocky.common.base.widget.load_rv;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.rocky.common.base.adapter.RecyclerAdapter;
import com.rocky.common.base.adapter.holder.BaseViewHolder;

/**
 * @author rocky
 * @date 2019/6/17.
 * description：自定义 rv内部适配器
 */
public class RockyLoadAdapter extends RecyclerView.Adapter<BaseViewHolder> {


    private final RockyLoadRV rockyLoadRV;
    private RecyclerView.Adapter adapter;
    /**
     * 是否启用上拉加载功能的标记：true-使用；false-禁用
     */
    private boolean mEnablePullLoad;
    /**
     * 上拉加载区域（foot区域）
     */
    private LoadRvFooter mFooterView;

    //下面的ItemViewType是保留值(ReservedItemViewType),如果用户的adapter与它们重复将会强制抛出异常。
    // 不过为了简化,我们检测到重复时对用户的提示是ItemViewType必须小于10000
    private static final int TYPE_FOOTER = 100001;//设置一个很大的数字,尽可能避免和用户的adapter冲突
    private static final int TYPE_ITEM = 100002;


    public RockyLoadAdapter(RockyLoadRV rockyLoadRV, RecyclerView.Adapter adapter, LoadRvFooter mFooterView, boolean mEnablePullLoad) {
        this.adapter = adapter;
        this.mFooterView = mFooterView;
        this.mEnablePullLoad = mEnablePullLoad;
        this.rockyLoadRV = rockyLoadRV;

    }


    /**
     * 获取总的条目数
     */
    @Override
    public int getItemCount() {
        int itemCount = adapter.getItemCount();
        if (mEnablePullLoad) {//如果启用上拉加载区域，那么就需要在原来的列表总数基础上加1
            itemCount = itemCount + 1;
        }
        return itemCount;
    }

    @Override
    public int getItemViewType(int position) {
        if (isFooter(position)) {
            return TYPE_FOOTER;
        } else {
//        	iv_return TYPE_ITEM;//不能return TYPE_ITEM，因为adapter中可能会设置不同的类型
            return adapter.getItemViewType(position);
        }
    }

    /**
     * 判断是否属于上拉加载区域-即最后一行
     */
    public boolean isFooter(int position) {
        if (mEnablePullLoad) {//如果启用上拉加载区域，那么最后一行，就是总数目- 1
            return position == getItemCount() -1;
        } else {
            return false;
        }
    }


    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        if (i == TYPE_FOOTER) {
//            iv_return new BaseViewHolder(mFooterView) {
//
//                @Override
//                public void bindTo(int position, Object value, RecyclerAdapter adapter) {
//                    RecyclerView.LayoutManager layoutManager = rockyLoadRV.getLayoutManager();
//                    if (layoutManager instanceof GridLayoutManager) {
//                        ((GridLayoutManager) layoutManager).setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
//                            @Override
//                            public int getSpanSize(int position) {
//                                iv_return ((GridLayoutManager) layoutManager).getSpanCount();
//                            }
//                        });
//                    }
//
//                    if (layoutManager instanceof StaggeredGridLayoutManager) {
//                        ((StaggeredGridLayoutManager) layoutManager).setSpanCount(((StaggeredGridLayoutManager) layoutManager).getSpanCount());
//                    }
//
//                }
//            };

            return new  BaseViewHolder(mFooterView){
                @Override
                public void bindTo(int position, Object value, RecyclerAdapter adapter) {

                }
            };

        }

        return (BaseViewHolder) adapter.onCreateViewHolder(viewGroup, i);
    }



    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder viewHolder, int i) {

        int itemCount;
        if (adapter != null) {
            itemCount = adapter.getItemCount();
            if (i < itemCount) {
                adapter.onBindViewHolder(viewHolder, i);
                return;
            }
        }
    }


    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        //对于 表格布局 需要特殊处理

        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();

        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager gridLayoutManager = (GridLayoutManager) manager;
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int i) {
                    //如果是底部上拉加载区域，则独占一行
                    return isFooter(i) ? gridLayoutManager.getSpanCount() : 1;
                }
            });
        }

        adapter.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        adapter.onDetachedFromRecyclerView(recyclerView);
    }


    @Override
    public void onViewAttachedToWindow(@NonNull BaseViewHolder holder) {
        super.onViewAttachedToWindow(holder);

        ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();

        if (layoutParams != null && layoutParams instanceof StaggeredGridLayoutManager.LayoutParams && isFooter(holder.getLayoutPosition())) {
            StaggeredGridLayoutManager.LayoutParams params = (StaggeredGridLayoutManager.LayoutParams) layoutParams;
            params.setFullSpan(true);
        }


        adapter.onViewAttachedToWindow(holder);


    }

    @Override
    public void onViewDetachedFromWindow(BaseViewHolder holder) {
        adapter.onViewDetachedFromWindow(holder);
    }


    @Override
    public void onViewRecycled(BaseViewHolder holder) {
        adapter.onViewRecycled(holder);
    }


    @Override
    public void unregisterAdapterDataObserver(RecyclerView.AdapterDataObserver observer) {
        adapter.unregisterAdapterDataObserver(observer);
    }

    @Override
    public void registerAdapterDataObserver(RecyclerView.AdapterDataObserver observer) {
        adapter.registerAdapterDataObserver(observer);
    }

    public boolean ismEnablePullLoad() {
        return mEnablePullLoad;
    }

    /**
     * 解决当第一页显示出来footview的时候刷新崩溃的问题
     */
    public void setmEnablePullLoad(boolean mEnablePullLoad) {
        this.mEnablePullLoad = mEnablePullLoad;
    }
}
