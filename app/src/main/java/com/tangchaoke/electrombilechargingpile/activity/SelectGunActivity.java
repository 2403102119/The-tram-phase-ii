package com.tangchaoke.electrombilechargingpile.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tangchaoke.electrombilechargingpile.R;
import com.tangchaoke.electrombilechargingpile.base.BaseActivity;

public class SelectGunActivity extends BaseActivity {

    private LinearLayout A_gun,B_gun;
    private TextView A_gun_TX,B_gun_TX,ok_1,ok_2;
    private String a_set,b_set,a_stu,b_stu;
    private int gun_num;//选择哪个枪
    private int ok_A_gun;//A枪是否可用
    private int ok_B_gun;//B枪是否可用
    private int gunN;//枪的数量
    private String identity;

    Handler handler = new Handler();

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContainer(R.layout.activity_select_gun);
        setTopTitle("选择枪位");


        A_gun=findViewById(R.id.A_gun);
        B_gun=findViewById(R.id.B_gun);
        A_gun_TX=findViewById(R.id.A_gun_TX);
        B_gun_TX=findViewById(R.id.B_gun_TX);
        ok_1=findViewById(R.id.ok_1);
        ok_2=findViewById(R.id.ok_2);
//        back=findViewById(R.id.back);

    }

    @Override
    protected void initListener() {
        A_gun.setOnClickListener(this);
        B_gun.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        if (null != getIntent()) {

            a_set = (getIntent().getStringExtra("agunSite"));
            b_set = (getIntent().getStringExtra("bgunSite"));
            a_stu = (getIntent().getStringExtra("agunStatus"));
            b_stu = (getIntent().getStringExtra("bgunStatus"));
            gun_num=getIntent().getIntExtra("gun_num",0);//XUANZE
            gunN=getIntent().getIntExtra("gunN",100);//枪数量
            identity=(getIntent().getStringExtra("identity"));//充电编号
        }

        gun_select();

    }

    public void onClick(View v){
        switch (v.getId()){
            case R.id.A_gun:
                 if (ok_A_gun==0){//A枪可使用
                     Intent intent=new Intent();
                     intent.putExtra("result_A",1);
                     intent.putExtra("toFragment",identity);
                     setResult(4,intent);
                     ok_1.setVisibility(View.VISIBLE);
                     ok_2.setVisibility(View.GONE);

                     handler.postDelayed(new Runnable() {
                         @Override
                         public void run() {
                             finish();
                         }
                     }, 500);
                 }
                 break;
            case R.id.B_gun:
                if (ok_B_gun==0){//B枪可用
                    Intent intent=new Intent();
                    intent.putExtra("result_A",2);
                    intent.putExtra("toFragment",identity);
                    setResult(4,intent);

                    ok_1.setVisibility(View.GONE);
                    ok_2.setVisibility(View.VISIBLE);

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    }, 500);
                }
                break;
/*            case R.id.back:
                finish();
                break;*/

        }
    }

    private void gun_select(){
        if (gunN==2){
            //双枪
            //显示
            if ("1".equals(a_set)&&"1".equals(a_stu)){//A已插枪，充电中
                A_gun_TX.setText("A枪  (已插枪，充电中)");
                A_gun_TX.setTextColor(getResources().getColor(R.color.gray));
                A_gun.setEnabled(false);
                ok_A_gun=1;
            }else if ("1".equals(a_set)&&"0".equals(a_stu)){//A已插枪，空闲
                A_gun_TX.setText("A枪  (已插枪，空闲)");
                A_gun.setEnabled(true);
                ok_A_gun=0;
            }else if ("0".equals(a_set)&&"0".equals(a_stu)){//A未插枪，空闲
                A_gun_TX.setText("A枪  (未插枪，空闲)");
                A_gun.setEnabled(true);
                ok_A_gun=0;
            }else {
                A_gun_TX.setText("A枪  (故障，不可用)");
                A_gun_TX.setTextColor(getResources().getColor(R.color.gray));
                A_gun.setEnabled(false);
            }


                 //显示
            if ("1".equals(b_set)&&"1".equals(b_stu)){//B已插枪，充电中
                B_gun_TX.setText("B枪  (已插枪，充电中)");
                B_gun_TX.setTextColor(getResources().getColor(R.color.gray));
                B_gun.setEnabled(false);
                ok_B_gun=1;
            }else if ("1".equals(b_set)&&"0".equals(b_stu)){//B已插枪，空闲
                B_gun_TX.setText("B枪  (已插枪，空闲)");
                B_gun.setEnabled(true);
                ok_B_gun=0;
            }else if ("0".equals(b_set)&&"0".equals(b_stu)){//B未插枪，空闲
                B_gun_TX.setText("B枪  (未插枪，空闲)");
                B_gun.setEnabled(true);
                ok_B_gun=0;
            }else {
                B_gun_TX.setText("B枪  (故障，不可用)");
                B_gun_TX.setTextColor(getResources().getColor(R.color.gray));
                B_gun.setEnabled(false);
            }

            if (ok_A_gun == 0 && ok_B_gun == 1){
                ok_1.setVisibility(View.VISIBLE);
                ok_2.setVisibility(View.GONE);
            }else if (ok_A_gun == 1 && ok_B_gun == 0){
                ok_1.setVisibility(View.GONE);
                ok_2.setVisibility(View.VISIBLE);
            }else {
                ok_1.setVisibility(View.GONE);
                ok_2.setVisibility(View.GONE);
            }

        }
        if (gunN==1){
            //单枪
            //显示
            B_gun.setVisibility(View.GONE);
            if ("1".equals(a_set)&&"1".equals(a_stu)){//A已插枪，充电中
                A_gun_TX.setText("A枪  (已插枪，充电中)");
                A_gun_TX.setTextColor(getResources().getColor(R.color.gray));
                A_gun.setEnabled(false);
                ok_A_gun=1;
            }else if ("1".equals(a_set)&&"0".equals(a_stu)){//A已插枪，空闲
                A_gun_TX.setText("A枪  (已插枪，空闲)");
                A_gun.setEnabled(true);
                ok_A_gun=0;
            }else if ("0".equals(a_set)&&"0".equals(a_stu)){//A未插枪，空闲
                A_gun_TX.setText("A枪  (未插枪，空闲)");
                A_gun.setEnabled(true);
                ok_A_gun=0;
            }else {
                A_gun_TX.setText("A枪  (故障，不可用)");
                A_gun_TX.setTextColor(getResources().getColor(R.color.gray));
                A_gun.setEnabled(false);
            }

        }

        //对号的隐藏
        if (gun_num==1){
            ok_2.setVisibility(View.GONE);
        }else if(gun_num==2){
            ok_1.setVisibility(View.GONE);
        }else if (gun_num==0){
            ok_2.setVisibility(View.GONE);
            ok_1.setVisibility(View.GONE);
        }else {

        }

    }

}
