package com.rocky.baseapplication.test;

import android.text.TextUtils;

import java.util.List;

public class RockyStoreModel {

    private String site_url;
    private List<GoodsListBean> goods_list;
    private List<String> cate;
    private List<SecondBean> second;

    public void setSecond(List<SecondBean> second) {
        this.second = second;
    }

    public List<SecondBean> getSecond() {
        return second;
    }


    public void setCate(List<String> cate) {
        this.cate = cate;
    }

    public List<String> getCate() {
        return cate;
    }

    public String getSite_url() {
        return site_url;
    }

    public void setSite_url(String site_url) {
        this.site_url = site_url;
    }

    public List<GoodsListBean> getGoods_list() {
        return goods_list;
    }


    public void setGoods_list(List<GoodsListBean> goods_list) {
        this.goods_list = goods_list;
    }


    public static class GoodsListBean {
        /**
         * brand : boys
         * goods_id : 223
         * store_id : 2
         * goods_name : boys衣服
         * default_image : data/files/store_2/goods_3/small_201905211733236470.jpg
         * price : 1000.00
         * sales : 0
         * comments : 2
         * add_time : 1558431399
         */

        private String brand;
        private String goods_id;
        private String store_id;
        private String goods_name;
        private String default_image;
        private String price;
        private String sales;
        private String comments;
        private String add_time;
        private String market_price = "0";
        private boolean collect;
        private boolean is_exist;
        private String end_price = "";
        private String first_price = "";
        private String ico = "";
        private String label="";//新品 经典 爆款
        private String times_label = "";//特卖
        private String discount_label="";//清仓 折扣 标签
        private List<String> stock_names;//发货地

        public void setDiscount_label(String discount_label) {
            this.discount_label = discount_label;
        }

        public void setStock_names(List<String> stock_names) {
            this.stock_names = stock_names;
        }

        public void setTimes_label(String times_label) {
            this.times_label = times_label;
        }

        public List<String> getStock_names() {
            return stock_names;
        }

        public String getDiscount_label() {
            return discount_label;
        }

        public String getTimes_label() {
            return times_label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public String getLabel() {
            return label;
        }

        public String getIco() {
            if (TextUtils.isEmpty(ico)) {
                return "";
            }
            return ico;
        }

        public void setIco(String ico) {
            this.ico = ico;
        }

        public void setEnd_price(String end_price) {
            this.end_price = end_price;
        }

        public void setFirst_price(String first_price) {
            this.first_price = first_price;
        }

        public String getEnd_price() {
            if (TextUtils.isEmpty(end_price) || "null".equals(end_price)) {
                return "";
            }
            return end_price;
        }

        public String getFirst_price() {
            if (TextUtils.isEmpty(first_price) || "null".equals(first_price)) {
                return "";
            }
            return first_price;
        }

        public void setIs_exist(boolean is_exist) {
            this.is_exist = is_exist;
        }

        public boolean isIs_exist() {
            return is_exist;
        }

        public boolean isCollect() {
            return collect;
        }

        public void setCollect(boolean collect) {
            this.collect = collect;
        }

        private List<String> spec;
        private List<String> spec_2s;

        public void setSpec_2s(List<String> spec_2s) {
            this.spec_2s = spec_2s;
        }

        public List<String> getSpec_2s() {
            return spec_2s;
        }

        public void setSpec(List<String> spec) {
            this.spec = spec;
        }

        public List<String> getSpec() {
            return spec;
        }

        public void setMarket_price(String market_price) {
            this.market_price = market_price;
        }

        public String getMarket_price() {
            return market_price;
        }

        public String getBrand() {
            return brand;
        }

        public void setBrand(String brand) {
            this.brand = brand;
        }

        public String getGoods_id() {
            return goods_id;
        }

        public void setGoods_id(String goods_id) {
            this.goods_id = goods_id;
        }

        public String getStore_id() {
            return store_id;
        }

        public void setStore_id(String store_id) {
            this.store_id = store_id;
        }

        public String getGoods_name() {
            return goods_name;
        }

        public void setGoods_name(String goods_name) {
            this.goods_name = goods_name;
        }

        public String getDefault_image() {
            return default_image;
        }

        public void setDefault_image(String default_image) {
            this.default_image = default_image;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getSales() {
            return sales;
        }

        public void setSales(String sales) {
            this.sales = sales;
        }

        public String getComments() {
            return comments;
        }

        public void setComments(String comments) {
            this.comments = comments;
        }

        public String getAdd_time() {
            return add_time;
        }

        public void setAdd_time(String add_time) {
            this.add_time = add_time;
        }
    }

}
