package com.tangchaoke.electrombilechargingpile;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.mylhyl.acp.Acp;
import com.mylhyl.acp.AcpListener;
import com.mylhyl.acp.AcpOptions;
import com.tangchaoke.electrombilechargingpile.activity.LoginActivity;
import com.tangchaoke.electrombilechargingpile.base.BaseActivity;
import com.tangchaoke.electrombilechargingpile.fragment.FindBFragment;
import com.tangchaoke.electrombilechargingpile.fragment.IndexFragment;
import com.tangchaoke.electrombilechargingpile.fragment.MeFragment;
import com.tangchaoke.electrombilechargingpile.fragment.MessageFragment;
import com.tangchaoke.electrombilechargingpile.thread.MUIToast;

import java.util.List;

import cn.jpush.android.api.JPushMessage;

public class MainActivity extends BaseActivity {
    private RadioGroup radioGroup;
    private RadioButton rb_index;
    private long mExitTime;//退出时的时间
    private Fragment indexFragment, chargingFragment, findFragment, meFragment;
    RadioButton rb_find;
    RadioButton rb_me;
    RadioButton rb_charging;
    private int position=0;


    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);

        radioGroup = findView(R.id.radioGroup);
        rb_index = findView(R.id.rb_index);
        rb_find = findView(R.id.rb_find);
        rb_me = findView(R.id.rb_me);
        rb_me.setChecked(false);
        rb_charging = findViewById(R.id.rb_charging);

        Log.i("11111111111", "gotResult: " + new JPushMessage().toString());

        ////////////////////////////  动态获取权限  /////////////////////////////////////////////
        Acp.getInstance(this).request(new AcpOptions.Builder()
                .setPermissions(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CAMERA)
                .build(), new AcpListener() {
            @Override
            public void onGranted() {
                rb_index.setChecked(true);
                setSelect(0);
            }

            @Override
            public void onDenied(List<String> permissions) {
                MUIToast.show(MainActivity.this, permissions.toString() + "权限拒绝");
            }
        });
    }

    @Override
    protected void initListener() {
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.rb_index:
                        setSelect(0);
                        break;
                    case R.id.rb_charging:
                        setSelect(1);
                        break;
                    case R.id.rb_find:
                        setSelect(2);
                        break;
                    case R.id.rb_me:
                        setSelect(3);
                        break;
                }
            }
        });
    }

    @Override
    protected void initData() {
        rb_index.setChecked(true);
        setSelect(0);
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        if (fragment instanceof IndexFragment){
            indexFragment = fragment;
        }
        if (fragment instanceof MessageFragment){
            chargingFragment = fragment;
        }
        if (fragment instanceof FindBFragment){
            findFragment = fragment;
        }
        if (fragment instanceof MeFragment){
            meFragment = fragment;
        }
    }

    public void setSelect(int i) {
        //获取FragmentTransaction类的实例
        FragmentManager manager = getSupportFragmentManager();
        final FragmentTransaction transaction = manager.beginTransaction();
        hideFragment(transaction);//先隐藏所有界面
        Log.e("position","tab位置="+position);
        switch (i) {
            case 0:
                if (indexFragment == null) {
                    indexFragment = new IndexFragment();
                    transaction.add(R.id.main_content, indexFragment);
                } else {
                    transaction.show(indexFragment);
                }
                position=0;
                rb_index.setChecked(true);
                rb_find.setChecked(false);
                rb_charging.setChecked(false);
                rb_me.setChecked(false);
                break;
            case 1:
                if (App.islogin){
                    if (chargingFragment == null) {
                        chargingFragment = new MessageFragment();
                        transaction.add(R.id.main_content, chargingFragment);
                    } else {
                        transaction.show(chargingFragment);
                    }
                    position=1;
                    rb_index.setChecked(false);
                    rb_find.setChecked(false);
                    rb_charging.setChecked(true);
                    rb_me.setChecked(false);
                }else {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    intent.putExtra("toFragment", 3);
                    startActivityForResult(intent, 11111);
                }
                break;
            case 2:
                if (findFragment == null) {
                    findFragment = new FindBFragment();
                    transaction.add(R.id.main_content, findFragment);
                } else {
                    transaction.show(findFragment);
                }
                position=2;
                rb_index.setChecked(false);
                rb_find.setChecked(true);
                rb_charging.setChecked(false);
                rb_me.setChecked(false);
                break;
            case 3:
                if (meFragment == null) {
                    meFragment = new MeFragment();
                    transaction.add(R.id.main_content, meFragment);
                } else {
                    transaction.show(meFragment);
                }
                position=3;
                rb_index.setChecked(false);
                rb_find.setChecked(false);
                rb_me.setChecked(true);
                rb_charging.setChecked(false);
                break;
            default:
                break;
        }
        transaction.commit();
    }

    //用于隐藏界面
    private void hideFragment(FragmentTransaction transaction) {
        if (indexFragment != null) {
            transaction.hide(indexFragment);
        }
        if (chargingFragment != null) {
            transaction.hide(chargingFragment);
        }
        if (findFragment != null) {
            transaction.hide(findFragment);
        }
        if (meFragment != null) {
            transaction.hide(meFragment);
        }
    }

    @Override
    public void onBackPressed() {
        if (position==0 && (indexFragment==null||((IndexFragment)indexFragment).onBack())){
            return;
        }
        exit();
    }

    //对返回键进行监听
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (position==0 && (indexFragment==null||((IndexFragment)indexFragment).onBack())){
            return false;
        }
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            exit();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void exit() {
        if ((System.currentTimeMillis() - mExitTime) > 2000) {
            Toast.makeText(MainActivity.this, "再按一次退出", Toast.LENGTH_SHORT).show();
            mExitTime = System.currentTimeMillis();
        } else {
            App.finishAllActivity();
            System.exit(0);
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("onActivityResult","resultCode="+resultCode+",requestCode="+requestCode);
        if (requestCode == 11111){
            if (resultCode == 22222) {
//                rb_index.setChecked(true);
//                rb_charging.setChecked(false);
                setSelect(position);
            }
            if (resultCode == 33333){
                rb_charging.setChecked(true);
                rb_me.setChecked(false);
                rb_index.setChecked(false);
                rb_me.setChecked(false);
                setSelect(1);
            }
        }

        if (indexFragment!=null&&resultCode==888){
            indexFragment.onActivityResult(requestCode,resultCode,data);
        }
    }
}
