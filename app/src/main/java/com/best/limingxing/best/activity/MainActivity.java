package com.best.limingxing.best.activity;

import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;

import com.best.limingxing.best.R;
import com.best.limingxing.best.bean.GankPerson;
import com.best.limingxing.best.bean.Person;
import com.best.limingxing.best.fragment.FaXianFragment;
import com.best.limingxing.best.fragment.MeFragment;
import com.best.limingxing.best.fragment.TongXunLuFragment;
import com.best.limingxing.best.fragment.WeiXinFragment;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.OkHttpClient;

public class MainActivity extends BaseActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    String url = "http://gank.io/api/data/Android/10/1";

    @BindView(R.id.weixin)
    RadioButton weixin;
    @BindView(R.id.tongxunlu)
    RadioButton tongxunlu;
    @BindView(R.id.faxian)
    RadioButton faxian;
    @BindView(R.id.me)
    RadioButton me;

    int index;
    int currentIndex = 0;
    RadioButton[] rbs;
    Fragment[] mFragments;
    WeiXinFragment mWeiXinFragment;
    TongXunLuFragment mTongXunLuFragment;
    FaXianFragment mFaXianFragment;
    MeFragment mMeFragment;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Log.i(TAG, "onCreate");
        super.onCreate(savedInstanceState);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L,TimeUnit.MILLISECONDS)
                .build();
        OkHttpUtils.initClient(okHttpClient);


        OkHttpUtils.get().url(url).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                GankPerson gankPerson = null;
                Log.d(TAG,response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean error = jsonObject.getBoolean("error");
                    JSONArray jsonArray = jsonObject.getJSONArray("results");
                    List<Person> list = new ArrayList<Person>();
                    List<String> imagesList = new ArrayList<String>();
                    for (int i = 0;i < jsonArray.length();i++) {
                        JSONObject jsonResults = (JSONObject) jsonArray.get(i);
                        String _id = jsonResults.getString("_id");
                        String createdAt = jsonResults.getString("createdAt");
                        String desc = jsonResults.getString("desc");

                        if (jsonResults.has("images")) {
                            JSONArray imagesArray = jsonResults.getJSONArray("images");
                            for (int j = 0; j < imagesArray.length(); j++) {
                                String images = (String) imagesArray.get(j);
                                Log.d(TAG, "onResponse: " + images);
                                imagesList.add(images);
                            }
                        }

                        String publishedAt = jsonResults.getString("publishedAt");
                        String source = jsonResults.getString("source");
                        String type = jsonResults.getString("type");
                        String url = jsonResults.getString("url");
                        Boolean used = jsonResults.getBoolean("used");
                        String who = jsonResults.getString("who");
                        Person persons = new Person(_id,createdAt,desc, imagesList ,publishedAt,source,type,url,used,who);
                        list.add(persons);
                    }
                    Log.d(TAG,list.toString());
                    gankPerson = new GankPerson();
                    gankPerson.setError(error);
                    gankPerson.setResults(list);
                    Log.d(TAG, "onResponse: " + gankPerson.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }


    private void initFragment() {
        mFragments = new Fragment[4];

        mWeiXinFragment = new WeiXinFragment();
        mTongXunLuFragment = new TongXunLuFragment();
        mFaXianFragment = new FaXianFragment();
        mMeFragment = new MeFragment();
        mFragments[0] = mWeiXinFragment;
        mFragments[1] = mTongXunLuFragment;
        mFragments[2] = mFaXianFragment;
        mFragments[3] = mMeFragment;
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_container, mWeiXinFragment)
                .add(R.id.fragment_container, mTongXunLuFragment)
                .add(R.id.fragment_container, mFaXianFragment)
                .add(R.id.fragment_container, mMeFragment)
                .hide(mMeFragment)
                .hide(mFaXianFragment)
                .hide(mTongXunLuFragment)
                .show(mWeiXinFragment)
                .commit();
    }

    @Override
    protected void initView() {
        rbs = new RadioButton[4];
        rbs[0] = weixin;
        rbs[1] = tongxunlu;
        rbs[2] = faxian;
        rbs[3] = me;
    }

    @Override
    protected void initData() {
        initFragment();

    }

    @Override
    protected void setListener() {

    }

    @OnClick({R.id.weixin, R.id.tongxunlu, R.id.faxian, R.id.me})
    public void onCheckedChange(View view) {
        switch (view.getId()) {
            case R.id.weixin:
                index = 0;
                break;
            case R.id.tongxunlu:
                index = 1;
                break;
            case R.id.faxian:
                index = 2;
                break;
            case R.id.me:
                index = 3;
                break;
        }
        setFragments();
    }
    public void setFragments(){
        if (index!=currentIndex){
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.hide(mFragments[currentIndex]);
            if (!mFragments[index].isAdded()){
                ft.add(R.id.fragment_container,mFragments[index]);
            }
            ft.show(mFragments[index]).commit();
        }
        setRadioButtonStatus();
        currentIndex = index;
    }
    private void setRadioButtonStatus(){
        for (int i = 0;i < rbs.length;i++){
            if (i == index){
                rbs[i].setChecked(true);
            }else {
                rbs[i].setChecked(false);
            }
        }
    }
}
