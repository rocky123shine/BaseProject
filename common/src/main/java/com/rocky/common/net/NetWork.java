package com.rocky.common.net;

import android.accounts.NetworkErrorException;
import android.util.Log;

import com.rocky.common.BuildConfig;
import com.rocky.common.base.BaseApplication;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static okhttp3.internal.Util.UTF_8;

/**
 * @author
 * @date 2019/12/6.
 * description：
 */
public class NetWork {
    private Retrofit mRetrofit;
    private String mBaseUrl;
    private OkHttpClient mOkhttpClient;
    private static LiveNetworkMonitor  networkMonitor = new LiveNetworkMonitor(BaseApplication.getContext());

    public static NetWork getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private NetWork() {
    }

    private static class SingletonHolder {
        private static final NetWork INSTANCE = new NetWork();
    }

    public Retrofit retrofit() {
        if (mRetrofit == null) {
            mRetrofit = new Retrofit.Builder()
                    .baseUrl(mBaseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(mOkhttpClient)
                    .build();
        }
        return mRetrofit;
    }

    public static class Builder {
        public Builder() {

        }

        private String mBaseUrl;
        private OkHttpClient mOkHttpClient;

        public NetWork build() {

            ensureSaneDefaults();

            NetWork network = getInstance();
            network.mBaseUrl = mBaseUrl;
            network.mOkhttpClient = mOkHttpClient;

            return network;
        }

        public Builder baseUrl(String baseUrl) {
            this.mBaseUrl = baseUrl;
            return this;
        }

        //可以外部传入参数 默认 内部已经实现
        public Builder client(OkHttpClient client) {
            mOkHttpClient = client;
            return this;
        }


        private void ensureSaneDefaults() {
            if (mOkHttpClient == null) {
                OkHttpClient.Builder builder = new OkHttpClient.Builder();
                builder.connectTimeout(10, TimeUnit.SECONDS)
                        .readTimeout(10, TimeUnit.SECONDS)
                        .writeTimeout(10, TimeUnit.SECONDS)
                        .retryOnConnectionFailure(true)             //错误重连
                        .addInterceptor(new Interceptor() {
                            @Override
                            public Response intercept(Chain chain) throws IOException {
                                //拦截器
                                if (networkMonitor.isConnected()) {
                                    //可以统一加参数 例如 token等
                                    Request request = chain.request();

//                                    String method = request.method();
//                                    if ("POST".equals(method)) {
//                                        if (!StringUtils.isEmpty(SPUtils.getString(ECApplication.getContext(), SPParam.USER_TOKEN))) {
//                                            request.url().newBuilder().setEncodedQueryParameter("token", SPUtils.getString(ECApplication.getContext(), SPParam.USER_TOKEN));
//
//                                        }
//                                    }
                                    Request.Builder requestBuilder = request.newBuilder();
                                    requestBuilder.addHeader("Content-Type", "application/json;charset=UTF-8")
                                            .addHeader("versionname", BuildConfig.VERSION_NAME)
                                            .addHeader("versioncode", BuildConfig.VERSION_CODE + "");
                                    Request newRequest = requestBuilder.build();

                                    return chain.proceed(newRequest);
                                } else {
                                    Observable.error(new NetworkErrorException("网路链异常！！！"));
                                }


                                return null;
                            }
                        }).
                        connectTimeout(10, TimeUnit.SECONDS);
                if (BuildConfig.DEBUG) {
                    //打印log
                    builder.addInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            Response response = chain.proceed(chain.request());
                            //获得返回的body，注意此处不要使用responseBody.string()获取返回数据，原因在于这个方法会消耗返回结果的数据(buffer)
                            ResponseBody responseBody = response.body();
                            //为了不消耗buffer，我们这里使用source先获得buffer对象，然后clone()后使用
                            BufferedSource source = responseBody.source();
                            source.request(Long.MAX_VALUE); // Buffer the entire body.
                            //获得返回的数据
                            Buffer buffer = source.buffer();
                            //使用前clone()下，避免直接消耗
                            Request request = chain.request();
                            Log.d("Net_Response", "chain.request().url():" + request.url().toString());
//                            String method = request.method();
//                            if ("POST".equals(method)) {
//                                printParams(request.method(), request.body());
//                            }
                            myLog("Net_Response", "response:" + buffer.clone().readString(Charset.forName("UTF-8")));
                            return response;
                        }
                    });

                }
                mOkHttpClient = builder.build();
            }
        }

        private void myLog(String tag, String msg) {
            //信息太长,分段打印
            //因为String的length是字符数量不是字节数量所以为了防止中文字符过多，
            //  把4*1024的MAX字节打印长度改为2001字符数
            int max_str_length = 2001 - tag.length();
            //大于4000时
            while (msg.length() > max_str_length) {
                Log.i(tag, msg.substring(0, max_str_length));
                Log.i(tag, "\n\n");
                msg = msg.substring(max_str_length);
            }
            //剩余部分
            Log.i(tag, msg);
        }

        private void printParams(String method, RequestBody body) {
            Buffer buffer = new Buffer();
            try {
                body.writeTo(buffer);
                Charset charset = Charset.forName("UTF-8");
                MediaType contentType = body.contentType();
                if (contentType != null) {
                    charset = contentType.charset(UTF_8);
                }
                String params = buffer.readString(charset);
                Log.d("Net_Response", "请求方式:" + method + "  请求参数:" + params);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
