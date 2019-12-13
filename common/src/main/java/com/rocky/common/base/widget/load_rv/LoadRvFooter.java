package com.rocky.common.base.widget.load_rv;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.rocky.common.R;


/**
 * @author rocky
 * @date 2019/6/17.
 * description： 自定义 rv的脚布局 即 rv 加载时候的样式
 */
public class LoadRvFooter extends LinearLayout {


    /**
     * 正常状态
     */
    public final static int STATE_NORMAL = 0;
    /**
     * 准备状态
     */
    public final static int STATE_READY = 1;
    /**
     * 加载状态
     */
    public final static int STATE_LOADING = 2;

    private View mContentView;
    private View mProgressBarLayout;
    private TextView mHintView;
    private Context mContext;

    public LoadRvFooter(Context context) {
        super(context);
        initView(context);
    }

    public LoadRvFooter(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        mContext = context;

        //添加上拉加载布局文件
        RelativeLayout moreView = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.rocky_rv_foot, this, false);
        addView(moreView);

        moreView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));


        //实例化组件
        mContentView = moreView.findViewById(R.id.rocky_load_footer_content);
        mProgressBarLayout = moreView.findViewById(R.id.rocky_load_footer_progressbar_layout);
        mHintView = (TextView) moreView.findViewById(R.id.rocky_load_footer_hint_textview);


    }


    /**
     * 当禁用上拉加载功能的时候隐藏底部区域
     */
    public void hide() {
        LayoutParams lp = (LayoutParams) mContentView.getLayoutParams();
        lp.height = 0;//这里设为0，那么虽然是显示，但是看不到
        mContentView.setLayoutParams(lp);
    }

    /**
     * 显示底部上拉加载区域
     */
    public void show() {
        LayoutParams lp = (LayoutParams) mContentView.getLayoutParams();
        lp.height = LayoutParams.WRAP_CONTENT;
        mContentView.setLayoutParams(lp);
    }

    /**
     * 设置布局的底外边距【暂时没有用到】
     */
    public void setBottomMargin(int height) {
        if (height < 0) return;
        LayoutParams lp = (LayoutParams) mContentView.getLayoutParams();
        lp.bottomMargin = height;
        mContentView.setLayoutParams(lp);
    }

    /**
     * 获取布局的底外边距【暂时没有用到】
     */
    public int getBottomMargin() {
        LayoutParams lp = (LayoutParams) mContentView.getLayoutParams();
        return lp.bottomMargin;
    }


    /**
     * 对外提供更改 footer状态的方法 目的是配合 rv的状态 做相应改变
     *
     * @param state - STATE_NORMAL(0),STATE_READY(1),STATE_LOADING(2)
     */


    public void setState(int state) {
        //隐藏 提示文字和进度条
        mHintView.setVisibility(GONE);
        mProgressBarLayout.setVisibility(GONE);

        //判断状态 做出显示隐藏动作

        if (state == STATE_READY) {
            //上拉 但是 为松手的状态  我们成为准备状态
            mHintView.setVisibility(VISIBLE);
            Drawable drawable = ContextCompat.getDrawable(mContext, R.mipmap.rocky_load_icon_pull);

            //setCompoundDrawables 画的drawable的宽高是按drawable.setBound()设置的宽高
            //而setCompoundDrawablesWithIntrinsicBounds是画的drawable的宽高是按drawable固定的宽高，
            // 即通过getIntrinsicWidth()与getIntrinsicHeight()自动获得
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            mHintView.setCompoundDrawables(null, drawable, null, null);
            mHintView.setText("上拉加载更多");

        } else if (state == STATE_LOADING) {
            //加载状态
            mProgressBarLayout.setVisibility(View.VISIBLE);
        } else {
            //正常状态
            mHintView.setVisibility(View.VISIBLE);
            mHintView.setCompoundDrawables(null, null, null, null);
//            mHintView.setText("查看更多");
            mHintView.setText("亲，到底了哦");
        }
    }


}
