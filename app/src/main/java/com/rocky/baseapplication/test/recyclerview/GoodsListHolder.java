package com.rocky.baseapplication.test.recyclerview;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.rocky.baseapplication.R;
import com.rocky.baseapplication.test.RockyStoreModel;
import com.rocky.common.base.adapter.RecyclerAdapter;
import com.rocky.common.base.adapter.holder.BaseViewHolder;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author
 * @date 2019/12/13.
 * description：
 */
public class GoodsListHolder extends BaseViewHolder<RockyStoreModel.GoodsListBean> {
    private Context context;
    @BindView(R.id.tv_goods_brand)
    AppCompatTextView tvGoodsBrand;
    @BindView(R.id.iv_goods)
    AppCompatImageView ivGoods;
    @BindView(R.id.tv_goods_price)
    AppCompatTextView tvGoodsPrice;
    @BindView(R.id.tv_goods_name)
    AppCompatTextView tvGoodsName;
    @BindView(R.id.txt_delete)
    ImageView txtDelete;
    @BindView(R.id.iv_like)
    ImageView ivLike;
    @BindView(R.id.rv_color)
    RecyclerView rvColor;
    @BindView(R.id.tv_goods_old_price)
    AppCompatTextView tvGoodsOldPrice;
    @BindView(R.id.iv_shouqing)
    ImageView iv_shouqing;
    @BindView(R.id.iv_hd)
    ImageView iv_hd;
    @BindView(R.id.rv_size)
    RecyclerView rv_size;

    @BindView(R.id.iv_discount)
    ImageView iv_discount;
    @BindView(R.id.tv_place1)
    TextView tv_place1;
    @BindView(R.id.tv_place2)
    TextView tv_place2;
    @BindView(R.id.tv_discount_bg)
    TextView tv_discount_bg;

    public GoodsListHolder(Context context, ViewGroup parent) {
        super(LayoutInflater.from(context).inflate(R.layout.item_goods_list, parent, false));
        ButterKnife.bind(this, itemView);
        this.context = context;

    }

    @Override
    public void bindTo(int position, RockyStoreModel.GoodsListBean value, RecyclerAdapter adapter) {
        String url = "http://test.jingjing.shop/" + value.getDefault_image();
        Glide.with(context).load(url)
//                .error(R.mipmap.ic_launcher)
//                .placeholder(R.mipmap.ic_launcher)
//                .error(R.drawable.ic_default_goods).placeholder(R.drawable.ic_default_goods)
                .into(ivGoods);

        tvGoodsName.setText(value.getGoods_name().replace(value.getBrand(), ""));
        tvGoodsBrand.setText(value.getBrand());
        if (value.getFirst_price().equals(value.getEnd_price())) {
            tvGoodsPrice.setText("￥" + getFirst_price(value.getFirst_price()));

        } else {
            tvGoodsPrice.setText("￥" + getFirst_price(value.getFirst_price()) + "-" + getFirst_price(value.getEnd_price()));

        }
        if (value.getFirst_price().equals("0.00")) {
            tvGoodsPrice.setText("￥" + getFirst_price(value.getEnd_price()));

        }


        if (TextUtils.isEmpty(value.getIco())) {
            iv_hd.setVisibility(View.GONE);
            if (!TextUtils.isEmpty(value.getTimes_label())) {
                iv_hd.setVisibility(View.VISIBLE);
                iv_hd.setImageResource(R.mipmap.ic_launcher);

            } else if (!TextUtils.isEmpty(value.getLabel())) {
                iv_hd.setVisibility(View.VISIBLE);
                switch (value.getLabel()) {
                    case "新品":
                        iv_hd.setImageResource(R.mipmap.ic_launcher);
                        break;
                    case "经典":
                        iv_hd.setImageResource(R.mipmap.ic_launcher);
                        break;
                    case "爆款":
                        iv_hd.setImageResource(R.mipmap.ic_launcher);
                        break;
                    default:
                        iv_hd.setVisibility(View.GONE);
                }

            }

        } else {
            iv_hd.setVisibility(View.VISIBLE);
            Glide.with(context).load("http://test.jingjing.shop/" + value.getIco())
//                    .error(R.mipmap.ic_launcher)
                    .into(iv_hd);
        }
        if (TextUtils.isEmpty(value.getDiscount_label())) {
            iv_discount.setVisibility(View.GONE);
        } else {
            iv_discount.setVisibility(View.VISIBLE);
            switch (value.getDiscount_label()) {
                case "折扣":
                    iv_discount.setImageResource(R.mipmap.ic_launcher);
                    break;
                case "清仓":
                    iv_discount.setImageResource(R.mipmap.ic_launcher);
                    break;
                default:
                    iv_discount.setVisibility(View.GONE);
            }
        }
        tv_place1.setText(value.getStock_names().get(0));
        if (value.getStock_names().size() >= 2) {
            tv_place2.setVisibility(View.VISIBLE);
            tv_place2.setText(value.getStock_names().get(1));
        } else {
            tv_place2.setVisibility(View.GONE);

        }

        if (Float.valueOf(tvGoodsPrice.getText().toString().split("￥")[1]) / Float.valueOf(value.getMarket_price()) >= 1) {
            tv_discount_bg.setVisibility(View.GONE);
        } else {
            if (Float.valueOf(tvGoodsPrice.getText().toString().split("￥")[1]) / Float.valueOf(value.getMarket_price()) <= 0) {
                tv_discount_bg.setVisibility(View.GONE);

            } else {
                tv_discount_bg.setVisibility(View.VISIBLE);
//                tv_discount_bg.setText(String.format("%.1f", Float.valueOf(tvGoodsPrice.getText().toString().split("￥")[1]) * 10 / Float.valueOf(value.getMarket_price())) + "折");
//                tv_discount_bg.setText(MathUtil.getInstance().getHeatNum(Float.valueOf(tvGoodsPrice.getText().toString().split("￥")[1]) * 10 / Float.valueOf(value.getMarket_price()))+ "折");
            }
        }


        tvGoodsOldPrice.setText("￥" + getFirst_price(value.getMarket_price()));

    }


    public String getFirst_price(String first_price) {
        if (first_price.isEmpty()) {
            return "0";
        }
        if (first_price.indexOf(".") > 0) {
            first_price = first_price.replaceAll("0+?$", "");//去掉多余的0
            first_price = first_price.replaceAll("[.]$", "");//如最后一位是.则去掉
        }
        return first_price;
    }
}
