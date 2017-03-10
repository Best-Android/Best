package com.best.limingxing.best.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;

import com.best.limingxing.best.R;
import com.best.limingxing.best.fragment.FaXianFragment;
import com.best.limingxing.best.fragment.MeFragment;
import com.best.limingxing.best.fragment.TongXunLuFragment;
import com.best.limingxing.best.fragment.WeiXinFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

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
        Log.i("MainActivity", "onCreate");
        super.onCreate(savedInstanceState);
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
