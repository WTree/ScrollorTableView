package com.wtree.scrolltable;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;



/**
 * Created by WTree on 2018/2/28.
 *
 * 具体实现在这里
 */
public class ScrollorTableView extends FrameLayout {



    private TextView mLeftTitleView;
    private LinearLayout mRightTitleContentView;
    private SyncHorizontalScrollView mTitleScrollView,mListScrollView;
    private MyExpandRecycleView mRightListView;
    private LeftListView mLeftListView;


    //每一栏的宽度
    private int itemWidth;
    public ScrollorTableView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(getContext()).inflate(R.layout.scrollor_table_layout,this,true);
        itemWidth=context.getResources().getDisplayMetrics().widthPixels/3;
    }


    public int getItemWidth(){
        return itemWidth;
    }
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

       mLeftTitleView=(TextView)findViewById(R.id.tv_left_title);
       mRightTitleContentView=(LinearLayout)findViewById(R.id.lin_right_title_parent);
       mTitleScrollView=(SyncHorizontalScrollView)findViewById(R.id.scrollView_title);
       mListScrollView=(SyncHorizontalScrollView)findViewById(R.id.scrollView_list);
       mLeftListView=(LeftListView)findViewById(R.id.listview_left);
       mRightListView=(MyExpandRecycleView)findViewById(R.id.listview_right);


       LinearLayoutManager leftManger=new LinearLayoutManager(getContext());
       LinearLayoutManager rightManger=new LinearLayoutManager(getContext());
       mLeftListView.setLayoutManager(leftManger);
        mRightListView.setLayoutManager(rightManger);


        setUp();
        syncScroll(mLeftListView,mRightListView);
    }

    private void syncScroll(final RecyclerView rlvLeft, final RecyclerView rlvRight) {

        mLeftListView.addOnScrollListener(leftScrollListens);
        mRightListView.addOnScrollListener(rightScrollListens);
    }





    RecyclerView.OnScrollListener leftScrollListens=new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            if (recyclerView.getScrollState() != RecyclerView.SCROLL_STATE_IDLE) {
                mRightListView.removeOnScrollListener(rightScrollListens);
                mRightListView.scrollBy(dx, dy);
                mRightListView.addOnScrollListener(rightScrollListens);
            }
        }
    };
    RecyclerView.OnScrollListener rightScrollListens=new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            if (recyclerView.getScrollState() != RecyclerView.SCROLL_STATE_IDLE) {
                mLeftListView.removeOnScrollListener(leftScrollListens);
                mLeftListView.scrollBy(dx, dy);
                mLeftListView.addOnScrollListener(leftScrollListens);
            }
        }
    };




    void setUp(){

        mLeftTitleView.getLayoutParams().width=itemWidth;
        LinearLayoutManager left=new LinearLayoutManager(getContext());
        mLeftListView.setLayoutManager(left);

        LinearLayoutManager right=new LinearLayoutManager(getContext());
        mRightListView.setLayoutManager(right);
        //联动设置
        mTitleScrollView.setScrollView(mListScrollView);
        mListScrollView.setScrollView(mTitleScrollView);


//        mLeftListView.addItemDecoration(initDefaultDiverLine());
//        mRightListView.addItemDecoration(initDefaultDiverLine());


        ((RelativeLayout.LayoutParams)mListScrollView.getLayoutParams()).leftMargin=itemWidth;

        //优化卡顿
        mRightListView.setHasFixedSize(true);
        mRightListView.setNestedScrollingEnabled(false);

        mLeftListView.setHasFixedSize(true);
        mLeftListView.setNestedScrollingEnabled(false);
        listens();
    }


    private void listens(){

        mRightListView.setClickable(true);
        mRightListView.setView(mLeftListView);
    }




    public void setLeftTile(String title){
        mLeftTitleView.setText(title);
    }


    //这个宽度是
    public void addRightTitleView(View view){
        ViewGroup.LayoutParams params=new ViewGroup.LayoutParams(itemWidth,ViewGroup.LayoutParams.MATCH_PARENT);
        view.setLayoutParams(params);
        mRightTitleContentView.addView(view);
    }


    public RecyclerView getListView(){
        return mRightListView;
    }
    /**
     *
     * 设置两个的适配器
     * @param leftAdapter
     * @param rightAdapter
     */
    public void setAdapter(RecyclerView.Adapter leftAdapter,RecyclerView.Adapter rightAdapter){
        mLeftListView.setAdapter(leftAdapter);
        mRightListView.setAdapter(rightAdapter);
    }


    public RecyclerView getLeftListView(){
        return mLeftListView;
    }
    public RecyclerView getRightListView(){
        return mRightListView;
    }

}
