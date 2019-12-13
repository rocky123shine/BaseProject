package com.rocky.baseapplication.test;

import com.rocky.common.net.ResultModel;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface StoreService {



    /**
     * @param map
     * @return
     * @"goods_id":_goodsId,
     * @"keyword":_keyword,
     * @"cate_id":_cateId,
     * @"page":@(_page),
     * @"sort":@"sales" sort:  'sales', 'price', 'add_time', 'comments'
     * order: asc desc
     */
    @FormUrlEncoded
    @POST("index.php?m=home&c=Index&a=get_list")
    Observable<ResultModel<RockyStoreModel>> rockyStore(@FieldMap Map<String, Object> map);


}
