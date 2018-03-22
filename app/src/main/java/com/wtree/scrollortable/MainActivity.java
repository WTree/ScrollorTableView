package com.wtree.scrollortable;

import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.wtree.scrollortable.adpater.SimpleScrollTableAdapter;
import com.wtree.scrollortable.helper.DataHelper;
import com.wtree.scrollortable.member.ItemInfo;
import com.wtree.scrolltable.ScrollorTableView;
import com.wtree.view.ErrorView;

import java.util.List;

public class MainActivity extends AppCompatActivity {


    ScrollorTableView  mScrollTableView;
    private SimpleScrollTableAdapter mAdapter;

    private boolean isExitNextPage=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mScrollTableView=(ScrollorTableView)findViewById(R.id.scrolltable);
        setUp();
    }

    private void setUp(){
        String[] arr=getResources().getStringArray(R.array.table_title);
        for(String str:arr){
            TextView textView=new TextView(this);
            textView.setGravity(Gravity.CENTER);
            textView.setText(str);
            mScrollTableView.addRightTitleView(textView);
        }

        mAdapter=new SimpleScrollTableAdapter(this,mScrollTableView);
        mScrollTableView.setAdapter(mAdapter.getLeftAdapter(),mAdapter.getRigthAdaptr());

        List<ItemInfo> data=DataHelper.createData(0);
        Log.e("TAG","tree data.size:"+data.size());
        mAdapter.setData(data);
        listens();


    }


    private void listens(){
        mScrollTableView.getRightListView().addOnScrollListener(scrollChangeListener);
        mScrollTableView.getLeftListView().addOnScrollListener(scrollChangeListener);
    }



    RecyclerView.OnScrollListener scrollChangeListener=new RecyclerView.OnScrollListener(){
        boolean isLastPos = false;
        int visibleItemCount;
        int total;
        int firstVisiblesItems;
        boolean isRealLastPos=false;
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {//这儿的逻辑可能要改
            super.onScrolled(recyclerView, dx, dy);

            RecyclerView.LayoutManager layoutManager=recyclerView.getLayoutManager();
            visibleItemCount = recyclerView.getLayoutManager().getChildCount();
            total = recyclerView.getLayoutManager().getItemCount();
            if (layoutManager instanceof StaggeredGridLayoutManager) {
                int first[] = null;
                first = ((StaggeredGridLayoutManager) layoutManager).findFirstVisibleItemPositions(first);

                firstVisiblesItems = Math.min(first[0],first[1]);
            } else {//默认是LinearLayoutManager
                firstVisiblesItems = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
            }
            if ((visibleItemCount + firstVisiblesItems) > total - 3) {
                isLastPos = true;
            } else {
                isLastPos = false;
            }
            isRealLastPos=(visibleItemCount + firstVisiblesItems) >=total;


        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if(recyclerView.getId()==R.id.listview_left){
                mScrollTableView.getRightListView().removeOnScrollListener(scrollChangeListener);
            }else{
                mScrollTableView.getLeftListView().removeOnScrollListener(scrollChangeListener);
            }
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {

                if (isLastPos && isExitNextPage) {
                    mAdapter.setFootState(ErrorView.STATE_LOADING);
                    gotoNext();
                }else{
                    if(!isExitNextPage){
                        mAdapter.setFootState(ErrorView.STATE_HIDE);
                    }
                }

                if(recyclerView.getId()==R.id.listview_left){
                    mScrollTableView.getRightListView().addOnScrollListener(scrollChangeListener);
                }else{
                    mScrollTableView.getLeftListView().addOnScrollListener(scrollChangeListener);
                }
            } else{
                if(recyclerView.getId()==R.id.listview_left){
                    mScrollTableView.getRightListView().removeOnScrollListener(scrollChangeListener);
                }else{
                    mScrollTableView.getLeftListView().removeOnScrollListener(scrollChangeListener);
                }
            }
        }
    };



    Handler handler=new Handler();

    private void gotoNext(){

        //模拟延时加载
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                List<ItemInfo> data=DataHelper.createData(mAdapter.getItemCount());
                mAdapter.addData(data);
                if(mAdapter.getItemCount()/20>20){
                    isExitNextPage=false;
                }

            }
        },1*1000);

    }
}
