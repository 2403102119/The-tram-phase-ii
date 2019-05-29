package com.tangchaoke.electrombilechargingpile.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.tangchaoke.electrombilechargingpile.App;
import com.tangchaoke.electrombilechargingpile.R;
import com.tangchaoke.electrombilechargingpile.adapter.GridImageAdapter;
import com.tangchaoke.electrombilechargingpile.base.BaseActivity;
import com.tangchaoke.electrombilechargingpile.bean.Bean;
import com.tangchaoke.electrombilechargingpile.thread.MApiResultCallback;
import com.tangchaoke.electrombilechargingpile.thread.ThreadPoolManager;
import com.tangchaoke.electrombilechargingpile.util.NetUtil;
import com.tangchaoke.electrombilechargingpile.util.StringUtil;
import com.tangchaoke.electrombilechargingpile.view.NiceRecyclerView;
import com.tangchaoke.electrombilechargingpile.view.RatingBar;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * 评价
 */
public class EvaluationActivity extends BaseActivity {
    private NiceRecyclerView recyclerView;
    private GridImageAdapter adapter;
    private int maxSelectNum = 9;
    private List<LocalMedia> selectList = new ArrayList<>();
    private String chargePlace,oid,stars = "0",oid2="";
    private Dialog dialog;
    private View view;
    private Button fabu;
    private EditText content;
    private RatingBar ratingBar1;
    private List<String> imagpath =new ArrayList<>();
    private String Imagpath_data;


    @Override
    protected void initView(Bundle savedInstanceState) {
        setContainer(R.layout.activity_evaluation);
        setTopTitle("评价");

        recyclerView = findViewById(R.id.pictureList);
        fabu=findViewById(R.id.fabu);
        ratingBar1=findViewById(R.id.ratingBar1);
        content=findViewById(R.id.content);


        dialog = new Dialog(EvaluationActivity.this, R.style.processDialog);
        view = LayoutInflater.from(EvaluationActivity.this).inflate(R.layout.item_dialog_evaluation, null);
    }

    @Override
    protected void initListener() {
        fabu.setOnClickListener(this);
    }

    @Override
    protected void initData() {


        if (null!=getIntent()){
            chargePlace=getIntent().getStringExtra("place_oid");
            oid=getIntent().getStringExtra("order_oid");
        }

        adapter = new GridImageAdapter(this, onAddPicClickListener);
        adapter.setList(selectList);
        adapter.setSelectMax(maxSelectNum);
        recyclerView.setAdapter(adapter);


        ratingBar1.setOnRatingChangeListener(new RatingBar.OnRatingChangeListener() {
            @Override
            public void onRatingChange(float ratingCount) {
                stars=ratingCount+"";
            }
        });

        adapter.setOnItemClickListener(new GridImageAdapter.OnItemClickListener(){
            @Override
            public void onItemClick(int position, View v) {
                if (selectList.size() > 0) {
                    LocalMedia media = selectList.get(position);
                    String pictureType = media.getPictureType();
                    int mediaType = PictureMimeType.pictureToVideo(pictureType);
                    switch (mediaType) {
                        case 1:
                            // 预览图片 可自定长按保存路径
                            //PictureSelector.create(EvaluateSubmitActivity.this).externalPicturePreview(position, "/custom_file", selectList);
                            PictureSelector.create(EvaluationActivity.this).externalPicturePreview(position, selectList);
                            break;
                        case 2:
                            // 预览视频
                            PictureSelector.create(EvaluationActivity.this).externalPictureVideo(media.getPath());
                            break;
                        case 3:
                            // 预览音频
                            PictureSelector.create(EvaluationActivity.this).externalPictureAudio(media.getPath());
                            break;
                    }
                }
            }

            @Override
            public void onDelete(int position) {
                imagpath.remove(position);
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fabu:
                String content1=content.getText().toString();

                if ("0".equals(stars)){
                    toast("请选择评价星级");
                    break;
                }
                if (StringUtil.isSpace(content1)){
                    toast("请输入评价内容");
                    break;
                }

                if (imagpath.size() > 0) {
                    StringBuffer sb = new StringBuffer();
                    for (int i = 0; i < imagpath.size(); i++) {
                        try {
                            sb.append(imagpath.get(i) + ",");

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    oid2 = sb.deleteCharAt(sb.length() - 1).toString();
                }else {
                    oid2 = "";
                }
                Evalluate(App.token,stars,content1,oid,chargePlace,oid2);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片选择结果回调
                    List<LocalMedia> backList = PictureSelector.obtainMultipleResult(data);
                    // 例如 LocalMedia 里面返回三种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
                    // 如果裁剪并压缩了，已取压缩路径为准，因为是先裁剪后压缩的

                    if (backList.size()>0){
//                        StringBuffer sb=new StringBuffer();
                        List<String> pathlist=new ArrayList<>();
                        for (int i=0;i<backList.size();i++){
                            pathlist.add(backList.get(i).getCompressPath());

                            /*try {
//                                sb.append(StringUtil.encodeBase64File(backList.get(i).getCompressPath())+",");
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                            String path=sb.deleteCharAt(sb.length()-1).toString();*/
                        }

                        for (int i = 0; i < pathlist.size(); i++) {
                            try {
                                uploadBusinessCase(App.token, StringUtil.encodeBase64File(pathlist.get(i)),"jpeg");

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                    }

                    selectList.addAll(backList);
                    adapter.notifyDataSetChanged();
                    break;
            }
        }
    }


    private GridImageAdapter.onAddPicClickListener onAddPicClickListener = new GridImageAdapter.onAddPicClickListener() {
        @Override
        public void onAddPicClick() {
            // 进入相册 以下是例子：不需要的api可以不写
            PictureSelector.create(EvaluationActivity.this)
                    .openGallery(PictureMimeType.ofImage())// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                    .theme(R.style.picture_default_style)// 主题样式设置 具体参考 values/styles   用法：R.style.picture.white.style
                    .maxSelectNum(maxSelectNum - selectList.size())// 最大图片选择数量
                    .minSelectNum(0)// 最小选择数量
                    .imageSpanCount(4)// 每行显示个数
                    .selectionMode(PictureConfig.MULTIPLE)// 多选 or 单选
                    .previewImage(true)// 是否可预览图片
                    .previewVideo(true)// 是否可预览视频
                    .enablePreviewAudio(true) // 是否可播放音频
                    .isCamera(true)// 是否显示拍照按钮
                    .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                    .enableCrop(false)// 是否裁剪
                    .compress(true)// 是否压缩
                    .synOrAsy(true)//同步true或异步false 压缩 默认同步
                    .glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                    .withAspectRatio(1, 1)// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                    .hideBottomControls(false)// 是否显示uCrop工具栏，默认不显示
                    .isGif(false)// 是否显示gif图片
                    .freeStyleCropEnabled(true)// 裁剪框是否可拖拽
                    .circleDimmedLayer(false)// 是否圆形裁剪
                    .showCropFrame(false)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false
                    .showCropGrid(false)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false
                    .openClickSound(false)// 是否开启点击声音
                    .minimumCompressSize(100)// 小于100kb的图片不压缩
                    .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
        }

    };

    Handler handler = new Handler();

    //评价接口
    public void Evalluate(final String token, final String stars,final String context,final String oid, final String oid1, final String oid2){
        if (NetUtil.isNetWorking(EvaluationActivity.this)){
            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    httpInterface.Evalluate(token, stars,context,oid, oid1, oid2 , new MApiResultCallback() {
                        @Override
                        public void onSuccess(String result) {
                            Bean data = new Gson().fromJson(result, Bean.class);
                            if (1==data.status){


                                /*dialog.setContentView(view);
                                dialog.setCanceledOnTouchOutside(false);
                                Window window = dialog.getWindow();
                                //底部弹出
                                window.setGravity(Gravity.CENTER);
                                //弹出动画
                                window.setWindowAnimations(R.style.bottomDialog);
                                window.setLayout(WindowManager.LayoutParams.FILL_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                                dialog.show();*/
                                toast("评价成功");

                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
//                                        dialog.dismiss();

                                        Intent intent=getIntent();
                                        setResult(11,intent);
                                        finish();
                                    }
                                }, 1000);
                            }else {
                                toast(data.message);
                            }
                        }

                        @Override
                        public void onFail(String response) {

                        }

                        @Override
                        public void onError(Call call, Exception exception) {

                        }

                        @Override
                        public void onTokenError(String response) {

                        }
                    });
                }
            });
        }
    }



    //评论上传图片
   public void uploadBusinessCase(final String token,final String imageUrl,final String imgType){
        if (NetUtil.isNetWorking(EvaluationActivity.this)){
            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    httpInterface.uploadBusinessCase(token, imageUrl, imgType, new MApiResultCallback() {
                        @Override
                        public void onSuccess(String result) {
                            Bean.UploadBusinessCase data=new Gson().fromJson(result,Bean.UploadBusinessCase.class);
                            if (1==data.status){
                                imagpath.add(data.oid);
                            }else {
                                toast(data.message);
                            }
                        }

                        @Override
                        public void onFail(String response) {

                        }

                        @Override
                        public void onError(Call call, Exception exception) {

                        }

                        @Override
                        public void onTokenError(String response) {

                        }
                    });
                }
            });
        }
   }


    //点击空白区域隐藏键盘.
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (EvaluationActivity.this.getCurrentFocus() != null) {
                if (EvaluationActivity.this.getCurrentFocus().getWindowToken() != null) {
                    imm.hideSoftInputFromWindow(EvaluationActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        }
        return super.onTouchEvent(event);
    }

    //隐藏软键盘
    protected void hideInputKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }



}
