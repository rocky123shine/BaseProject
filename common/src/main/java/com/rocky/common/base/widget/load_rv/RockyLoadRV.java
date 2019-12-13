package com.rocky.common.base.widget.load_rv;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MotionEventCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

/**
 * @author rocky
 * @date 2019/6/17.
 * description： 自定义 rv实现上拉加载
 */
public class RockyLoadRV extends RecyclerView {

    private LoadRvFooter mFooterView;
    private int touchSlop;
    private Context mContext;
    private int INVALID_POINTER = -1;
    private int scrollPointerId = INVALID_POINTER;
    private int initialTouchX;
    private int initialTouchY;

    /**
     * 自定义适配器，用于在基础列表数据的基础上添加底部区域
     */
    private RockyLoadAdapter loadAdapter;

    public RockyLoadRV(@NonNull Context context) {
        super(context);
        initView(context);

    }


    public RockyLoadRV(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);

    }

    public RockyLoadRV(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context);

    }

    /**
     * 当前是否处于下拉刷新的状态 :默认为false
     */
    private boolean isPullRefresh = false;

    /**
     * 是否是否处于下拉刷新的状态
     */
    public void setPullRefresh(boolean pullRefresh) {
        isPullRefresh = pullRefresh;
    }

    /**
     * 当前是否处于快速滑动的状态 :默认为false
     */
    private boolean isQuickSlide = false;

    /**
     * 是否启用上拉加载功能的标记：true-启用；false-禁用
     */
    private boolean mEnablePullLoad = true;
    /**
     * 上拉加载区域是否正在显示的标记
     */
    private boolean isShowFooter = false;

    /**
     * 是否处于加载状态的标记：true-加载；false-正常
     */
    private boolean mPullLoading = false;

    /**
     * recyclerView总item数目
     */
    private int mTotalItemCount = 0;

    /**
     * recyclerView最后一个可见的item的下标值
     */
    private int lastVisibleItem = 0;
    /**
     * 快速移动产生惯性的移动距离值（自定义的临界值）
     */
    private final static int FAST_MOVE_DY = 150;

    private void initView(Context context) {
        //处理嵌套 滑动冲突 --------------start--------------
        ViewConfiguration vc = ViewConfiguration.get(context);
        touchSlop = vc.getScaledEdgeSlop();
        mContext = context;
        //处理嵌套 滑动冲突 --------------end--------------

        //初始化 尾布局
        mFooterView = new LoadRvFooter(context);

        //判断是否在刷新 刷新 禁止滑动

        this.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (isPullRefresh) {
                    //消费掉
                    return true;
                } else {
                    return false;
                }
            }
        });

        //添加滚动监听


        this.addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                //判断当前 rv的状态

                switch (newState) {
                    case SCROLL_STATE_DRAGGING://拖拽
                        isQuickSlide = false;//每一次开始滑动之前，设置快速滑动状态值为false
                        if (mEnablePullLoad) {
                            mFooterView.show();//因为快速滑动产生惯性的时候会进行隐藏底部上拉加载区域，所以需要在下次正常滑动之前在这里重新显示FootView
                        }

                        break;
                    case SCROLL_STATE_SETTLING://
                        break;

                    case SCROLL_STATE_IDLE://空闲状态

                        //isQuickSlide = false;//解开这个注释，就是不管快速滑动还是正常滑动，只要显示最后一条列表，就会自动加载数据
                        if (isShowFooter && !mPullLoading && !isQuickSlide) {//也就是手指慢慢滑动出来foot区域的情况
                            startLoadMore(); //停止滑动后，如果上拉加载区域正在显示并且没有处于正在加载状态并且不是快速滑动状态，那么就开始加载
                        } else {
                            mFooterView.setState(LoadRvFooter.STATE_NORMAL);//底部区域显示【查看更多】
                        }
                        break;

                }

            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                //获取当前显示的最后一个子项下标值
                LayoutManager layoutManager = recyclerView.getLayoutManager();
                mTotalItemCount = layoutManager.getItemCount();
                if (layoutManager instanceof GridLayoutManager) {
                    lastVisibleItem = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
                } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                    int[] lastPositions = new int[((StaggeredGridLayoutManager) layoutManager).getSpanCount()];
                    ((StaggeredGridLayoutManager) layoutManager).findLastVisibleItemPositions(lastPositions);
                    int max = lastPositions[0];
                    for (int value : lastPositions) {
                        if (value > max) {
                            max = value;
                        }
                    }
                    lastVisibleItem = max;
                } else {
                    lastVisibleItem = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
                }


                if (mEnablePullLoad) {


                    if (lastVisibleItem >= mTotalItemCount - 1) {
                        if (dy > 0) {
                            isShowFooter = true;

                            /*
                             * 如果移动的距离大于FAST_MOVE_DY，则表明当前处于快速滑动产生惯性的情况，则隐藏底部上拉加载区域，这样就控制了底部上拉加载区域的不正常显示*/
                            if (dy > FAST_MOVE_DY) {
                                mFooterView.hide();
                                isQuickSlide = true;
                            } else {
                                mFooterView.setState(LoadRvFooter.STATE_READY);
                            }
                        } else {

                        }
                    } else {
                        /*如果还没有到最后一个子项，前提条件是启用了上拉加载功能
                         * 则：(1)设置上拉加载区域显示状态值为false
                         * (2)隐藏上拉加载区域*/
                        isShowFooter = false;
                        mFooterView.hide();
                    }


                }

            }
        });
    }

    /**
     * 自定义上拉加载的监听器
     */
    private IonLoadListener mLoadListener;

    /**
     * 开始加载，显示加载状态
     */
    private void startLoadMore() {
        if (mEnablePullLoad) {
            if (mPullLoading) return;
            mPullLoading = true;
            mFooterView.setState(LoadRvFooter.STATE_LOADING);
            if (mLoadListener != null) {
                mLoadListener.onLoadMore();
            }
        }
    }
    /**
     * 停止加载，还原到正常状态
     */
    public void stopLoadMore() {
        if (mEnablePullLoad) {//启用上拉加载功能
            if (mPullLoading ) {//如果处于加载状态
                mPullLoading = false;
                mFooterView.setState(LoadRvFooter.STATE_NORMAL);
                mFooterView.hide();
            }
        }
    }
    /**
     * 自定义下拉刷新和上拉加载的监听器
     */
    public interface IonLoadListener {
        /**
         * 上拉加载
         */
        public void onLoadMore();
    }

    public void setOnLoadListener(IonLoadListener l) {
        mLoadListener = l;
    }


    /**
     * 设置适配器
     *
     * @param adapter
     */
    public void setAdapter(Adapter adapter) {
        loadAdapter = new RockyLoadAdapter(this,adapter, mFooterView, mEnablePullLoad);
        super.setAdapter(loadAdapter);
    }

    /**
     * 是否正在上拉加载
     */
    public boolean ismPullLoading() {
        return mPullLoading;
    }


    /**
     * 设置启用或者禁用上拉加载功能
     *
     * @param enable
     */
    public void setPullLoadEnable(boolean enable) {

        mEnablePullLoad = enable;
        if (!mEnablePullLoad) {
            //禁止加载
            mFooterView.hide();
            mFooterView.setOnClickListener(null);
        } else {
            mPullLoading = false;
            mFooterView.show();
            mFooterView.setState(LoadRvFooter.STATE_NORMAL);
            mFooterView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startLoadMore();
                }
            });
        }
        if (loadAdapter != null) {
            loadAdapter.setmEnablePullLoad(mEnablePullLoad);
            loadAdapter.notifyDataSetChanged();
        }

    }
    //处理嵌套 滑动冲突 --------------start--------------
    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        if (e == null) {
            return false;
        }
        int action = MotionEventCompat.getActionMasked(e);
        int actionIndex = MotionEventCompat.getActionIndex(e);
        switch (action) {
            case MotionEvent.ACTION_DOWN :
                scrollPointerId = MotionEventCompat.getPointerId(e, 0);
                initialTouchX = Math.round(e.getX() + 0.5f);
                initialTouchY = Math.round(e.getY() + 0.5f);
                return super.onInterceptTouchEvent(e);
            case MotionEvent.ACTION_POINTER_DOWN:
                scrollPointerId = MotionEventCompat.getPointerId(e, actionIndex);
                initialTouchX = Math.round(MotionEventCompat.getX(e, actionIndex) + 0.5f);
                initialTouchY = Math.round(MotionEventCompat.getY(e, actionIndex) + 0.5f);
                return super.onInterceptTouchEvent(e);
            case MotionEvent.ACTION_MOVE:
                int index = MotionEventCompat.findPointerIndex(e, scrollPointerId);
                if (index < 0) {
                    return false;
                }
                int x = Math.round(MotionEventCompat.getX(e, index) + 0.5f);
                int y = Math.round(MotionEventCompat.getY(e, index) + 0.5f);
                if (getScrollState() != SCROLL_STATE_DRAGGING ) {
                    int dx = x - initialTouchX;
                    int dy = y - initialTouchY;
                    boolean startScroll = false;


                    if(getLayoutManager().canScrollHorizontally() && Math.abs(dy) > touchSlop &&
                            ( Math.abs(dx) > Math.abs(dy))) {
                        startScroll = true;
                    }
                    if(getLayoutManager().canScrollVertically() && Math.abs(dy) > touchSlop &&
                            ( Math.abs(dy) > Math.abs(dx))) {
                        startScroll = true;
                    }


                    //如下条件，结合成一个条件， 前者条件已经是判断未纵向移动了，那么后面补上横向移动就行
                    if(getLayoutManager().canScrollVertically() && Math.abs(dy) > touchSlop &&
                            (getLayoutManager().canScrollHorizontally() || Math.abs(dy) > Math.abs(dx))) {
                        startScroll = true;
                    }
                    return startScroll && super.onInterceptTouchEvent(e);
                }
                return super.onInterceptTouchEvent(e);
            default:
                return super.onInterceptTouchEvent(e);
        }
    }
    //处理嵌套 滑动冲突 --------------end--------------
}
