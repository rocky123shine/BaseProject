package com.rocky.baseapplication.test.recyclerview;

import android.view.View;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;

import com.rocky.baseapplication.R;
import com.rocky.baseapplication.test.RockyStoreModel;
import com.rocky.baseapplication.test.StoreService;
import com.rocky.common.base.BaseRecyclerViewActivity;
import com.rocky.common.base.adapter.util.RecyclerConfig;
import com.rocky.common.net.CommonObserver;
import com.rocky.common.net.NetWork;
import com.rocky.common.net.RequestUtitl;
import com.rocky.common.net.ResultModel;

import java.util.HashMap;
import java.util.Map;

/**
 * @author
 * @date 2019/12/13.
 * description：
 */
public class RvActivity extends BaseRecyclerViewActivity {
    @Override
    protected int setContentLayout() {
        return R.layout.activity_rv;
    }

    @Override
    protected void init() {
        super.init();
        //配置 rv参数
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                //如果有加载更多  需要占整行
                if (position == mAdapter.getItemCount()) {
                    return gridLayoutManager.getSpanCount();
                }
                //其次在判断数据类型 显示不同布局
                Object object = mAdapter.getItem(position);
                if (object instanceof RockyStoreModel.GoodsListBean) {//正常数据 占一个位置
                    return 1;
                } else {
                    return 2;
                }
            }
        });

        RecyclerConfig.Builder builder = new RecyclerConfig.Builder();
        builder.bind(String.class,DesRvHolder.class)
                .bind(RockyStoreModel.GoodsListBean.class, GoodsListHolder.class)
                .layoutManager(gridLayoutManager)
                .enableRefresh(true)
                .enableload(true);

        buildConfig(builder.build());
        setRefreshLayout();
        setRecyclerView();
        request(false);

    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        request(false);
    }

    @Override
    protected void onLoad() {
        super.onLoad();
        request(true);
    }

    private void request(final boolean load) {
        Map<String, Object> objectObjectHashMap = new HashMap<>();
        objectObjectHashMap.put("user_id", "1514");
        objectObjectHashMap.put("goods_id", "54910,54882,54862,54848,54836,54823,54819,54817,54815,54795,54793,54783,54776,54767,54757,54756,54734,54731,54710,54709,54702,54695,54687,54686,54684,56001,55998,55964,55962,55944,55894,55847,55846,55843,55753,55699,55356,55277,55271,55146,55100,55086,55073,55055,55042,55039,55038,54919,54359,54339,54337,54318,54314,54313,54312,54288,54269,54262,54253,54247,54246,53477,53438,33630,2531,2524,2169,1813,1811,1753,1752,1739,54685,54683,54682,54681,54679,54656,54654,54649,54647,54619,54611,54610,54556,54543,54541,54536,54522,54519,54515,54513,54441,54413,54408,54391,54378,1743,1749");
        objectObjectHashMap.put("page", page);
        objectObjectHashMap.put("sort", "add_time");
        objectObjectHashMap.put("pagenum", "10");
        objectObjectHashMap.put("token", "v47d0uumubeh140ic95vgrfd14");
        RequestUtitl.getInstance().request(
                NetWork.getInstance().retrofit().create(StoreService.class).rockyStore(objectObjectHashMap),
                this.<ResultModel<RockyStoreModel>>bindToLifecycle(),
                new CommonObserver<RockyStoreModel>() {
                    @Override
                    public void onNext(RockyStoreModel rockyStoreModel) {
                        super.onNext(rockyStoreModel);

                        if (load) {
                            mAdapter.appendAll(rockyStoreModel.getGoods_list());

                        } else {
                            mAdapter.putField("rockyStoreModel", rockyStoreModel);
//                            initBanner(rockyStoreModel);
                            mAdapter.clear();
                            if (null == rockyStoreModel.getGoods_list() || rockyStoreModel.getGoods_list().size() <= 0) {
                                //  showShortToast(R.string.no_data);

                            } else {
                                mAdapter.add("des");
                                mAdapter.appendAll(rockyStoreModel.getGoods_list());

                            }


                        }


                    }
                }, RockyStoreModel.class);
    }

    @Override
    public void onItemClick(int position, View view) {
        super.onItemClick(position, view);
        Toast.makeText(activity, "点击了pos:" + position, Toast.LENGTH_SHORT).show();
    }
}
