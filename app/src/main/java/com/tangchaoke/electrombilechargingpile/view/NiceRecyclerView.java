package com.tangchaoke.electrombilechargingpile.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ViewUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.tangchaoke.electrombilechargingpile.R;
import com.tangchaoke.electrombilechargingpile.util.ViewUtil;



/*
 * kylin on 2017年12月11日16:04:04
 */

public class NiceRecyclerView extends FrameLayout {
    public RecyclerView rv;
    public TextView tv;

    //RecyclerView 是listview还是gridview
    private ListDirection listDirection;
    //为空提示语是否显示
    private boolean isShowEmptyText;
    //列表为空提示语
    private String emptyText;
    //gridview每行显示数量
    private int gridNum;
    //是否显示为空图片
    private boolean showEmptyImg;
    //为空提示语第一次是否显示
    private boolean isFirstShowEmptyText;
    //为空图片宽度
    private float imgWidth;
    //为空图片高度
    private float imgHeight;
    //为空文字padding
    private float txtPadding;
    //为空文字padding
    private float txtImgPadding;
    //为空文字大小
    private float txtSize;

    private OnEmptyClickListener onEmptyClickListener;

    /**
     * 只支持在xml文件定义
     * @param context 上下文activity
     * @param attrs 自定义属性
     */
    public NiceRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.NiceRecyclerView);
        emptyText=typedArray.getString(R.styleable.NiceRecyclerView_emptyText);
        listDirection= ListDirection.fromStep(typedArray.getInt(R.styleable.NiceRecyclerView_listDirection,1));
        gridNum=typedArray.getInt(R.styleable.NiceRecyclerView_gridNum,1);
        isShowEmptyText=typedArray.getBoolean(R.styleable.NiceRecyclerView_isShowEmptyText,false);
        showEmptyImg=typedArray.getBoolean(R.styleable.NiceRecyclerView_showEmptyImg,false);
        imgWidth=typedArray.getDimension(R.styleable.NiceRecyclerView_imgWidth,0);
        imgHeight=typedArray.getDimension(R.styleable.NiceRecyclerView_imgHeight,0);
        txtPadding=typedArray.getDimension(R.styleable.NiceRecyclerView_txtPadding,0);
        txtImgPadding=typedArray.getDimension(R.styleable.NiceRecyclerView_txtImgPadding,0);
        txtSize=typedArray.getDimension(R.styleable.NiceRecyclerView_txtSize,0);
        isFirstShowEmptyText=typedArray.getBoolean(R.styleable.NiceRecyclerView_isFirstShowEmptyText,true);


        rv=new RecyclerView(context);
        LayoutParams lpRV=new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        rv.setLayoutParams(lpRV);

        tv=new TextView(context);
        tv.setGravity(Gravity.CENTER_VERTICAL);
        LayoutParams lpTV=new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        tv.setPadding((int)txtPadding,(int)txtPadding,(int)txtPadding,(int)txtPadding);
        tv.setLayoutParams(lpTV);
        tv.setTextSize(txtSize);
        if (showEmptyImg) {
            Drawable drawable = getResources().getDrawable(R.mipmap.ic_sousuo);
            drawable.setBounds(0, 0, (int)imgWidth, (int)imgHeight);
            tv.setCompoundDrawables(drawable, null, null, null);
            tv.setCompoundDrawablePadding((int)txtImgPadding);
        }
        tv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//                onEmptyClickListener.onClick(v);
            }
        });

        this.addView(rv);
        this.addView(tv);

        setvisibility();

        if (listDirection== ListDirection.HORIZONTAL){
            GridLayoutManager gridLayoutManager = new GridLayoutManager(context,gridNum);
            rv.setLayoutManager(gridLayoutManager);
        }else {
            LinearLayoutManager lm = new LinearLayoutManager(context);
            lm.setOrientation(LinearLayoutManager.VERTICAL);
            rv.setLayoutManager(lm);
        }

        tv.setText(emptyText);
        isFirstShowEmptyText=true;
    }

    public void setLayoutManager(RecyclerView.LayoutManager layoutManager) {
        rv.setLayoutManager(layoutManager);
    }

    public void setNestedScrol(boolean enable){
        rv.setNestedScrollingEnabled(enable);
    }

    public void setHasFixedSize(boolean b){
        rv.setHasFixedSize(b);
    }

    public void setEmptyText(String emptyText){
        tv.setText(emptyText);
    }

    public void setEmptyImg(int resourceId){
        tv.setCompoundDrawablesWithIntrinsicBounds(0,0,0,resourceId);
        tv.setCompoundDrawablePadding(ViewUtil.dp2px(tv.getContext(),80));
    }

    private void setvisibility() {
        if(isShowEmptyText&&isFirstShowEmptyText){
            rv.setVisibility(GONE);
            tv.setVisibility(VISIBLE);
        }else {
            rv.setVisibility(VISIBLE);
            tv.setVisibility(GONE);
        }
    }

    public void showEmpty(){
        rv.setVisibility(GONE);
        tv.setVisibility(VISIBLE);
    }

    public RecyclerView.Adapter<?> getAdapter() {
        return rv.getAdapter();
    }

    public void setAdapter(RecyclerView.Adapter<?> adapter) {
        rv.setAdapter(adapter);
        adapter.registerAdapterDataObserver(observer);
        observer.onChanged();
    }

    public boolean isShowEmptyText() {
        return isShowEmptyText;
    }

    public void setShowEmptyText(boolean showEmptyText) {
        isShowEmptyText = showEmptyText;
        setvisibility();
    }

    /**
     * RecyclerView 是listview还是gridview
     */
    private enum ListDirection {
        HORIZONTAL(0), VERTICAL(1);
        int step;

        ListDirection(int step) {
            this.step = step;
        }

        public static ListDirection fromStep(int step) {
            for (ListDirection f : values()) {
                if (f.step == step) {
                    return f;
                }
            }
            throw new IllegalArgumentException();
        }
    }


    private RecyclerView.AdapterDataObserver observer = new RecyclerView.AdapterDataObserver() {
        @Override
        public void onChanged() {
            super.onChanged();
            RecyclerView.Adapter adapter = getAdapter();
            if (adapter.getItemCount()==0){
                setShowEmptyText(true);
            }else{
                setShowEmptyText(false);
            }
        }
    };



    public interface OnEmptyClickListener{
        void onClick(View view);
    }

    public void setOnEmptyClickListener(OnEmptyClickListener onEmptyClickListener) {
        this.onEmptyClickListener = onEmptyClickListener;
    }


    /*
    *
    * <com.tck.kuailaibang.view.NiceRecyclerView
    android:id="@+id/filterView"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:layout_gravity="center"
    android:background="#ffffff"
    android:paddingBottom="2dp"
    android:paddingLeft="6dp"
    android:paddingRight="6dp"
    android:paddingTop="2dp"
    app:listDirection="HORIZONTAL"
    app:emptyText="现在还没有数据哦~~"
    app:gridNum="4">
</com.tck.kuailaibang.view.NiceRecyclerView>

<com.tck.kuailaibang.view.NiceRecyclerView
    android:id="@+id/productView"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:layout_gravity="center"
    android:layout_marginBottom="7dp"
    android:background="#ffffff"
    app:listDirection="VERTICAL"
    app:emptyText="现在还没有数据哦~~">

</com.tck.kuailaibang.view.NiceRecyclerView>
    *
    * */
}
