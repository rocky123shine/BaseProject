package com.rocky.baseapplication.test.net;

import android.widget.TextView;

import com.rocky.baseapplication.R;
import com.rocky.baseapplication.test.RockyStoreModel;
import com.rocky.baseapplication.test.StoreService;
import com.rocky.common.base.BaseActivity;
import com.rocky.common.net.CommonObserver;
import com.rocky.common.net.NetWork;
import com.rocky.common.net.RequestUtitl;
import com.rocky.common.net.ResultModel;

import java.util.HashMap;
import java.util.Map;

/**
 * @author
 * @date 2019/12/12.
 * description：
 */
public class NetActivity extends BaseActivity {

    private TextView tv_test;

    @Override
    protected int setContentLayout() {
        return R.layout.activity_net;
    }

    @Override
    protected void init() {
        super.init();
        tv_test = findViewById(R.id.tv_test);


        request();
    }
    private void request() {
        Map<String, Object> objectObjectHashMap = new HashMap<>();
        objectObjectHashMap.put("user_id", "1514");
        objectObjectHashMap.put("goods_id", "54910,54882,54862,54848,54836,54823,54819,54817,54815,54795,54793,54783,54776,54767,54757,54756,54734,54731,54710,54709,54702,54695,54687,54686,54684,56001,55998,55964,55962,55944,55894,55847,55846,55843,55753,55699,55356,55277,55271,55146,55100,55086,55073,55055,55042,55039,55038,54919,54359,54339,54337,54318,54314,54313,54312,54288,54269,54262,54253,54247,54246,53477,53438,33630,2531,2524,2169,1813,1811,1753,1752,1739,54685,54683,54682,54681,54679,54656,54654,54649,54647,54619,54611,54610,54556,54543,54541,54536,54522,54519,54515,54513,54441,54413,54408,54391,54378,1743,1749");
        objectObjectHashMap.put("page", "1");
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
                        tv_test.setText(rockyStoreModel.getGoods_list().toString());
//                        Log.d("MainActivityccaaa", rockyStoreModel.getGoods_list().toString());

                    }
                }, RockyStoreModel.class);
    }
}
