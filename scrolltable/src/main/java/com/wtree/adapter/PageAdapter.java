package com.wtree.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.wtree.helper.Utils;
import com.wtree.scrolltable.R;
import com.wtree.view.ErrorView;

import java.util.ArrayList;
import java.util.List;


/**
 * 列表页面适配器
 *  Created by tree on 2016/2/1.
 *
 */
public abstract class PageAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public final int TYPE_ITEM_HEAD=1;
    public final int TYPE_ITEM_NORMAL=2;
    public final int TYPE_ITEM_FOOT=3;
    private boolean isShowHeadView;
    private boolean isShowFootView;
    public List<T> mData;
    public Context mContext;
    private int state= ErrorView.STATE_LOADING;
    private View.OnClickListener mRetryListens;
    public RecyclerView mListView;
    private int footSize;
    public int mWidth=0;
    protected boolean isScrolld=false;
    private Fragment fragment;
    protected LayoutInflater mInflate;
    public PageAdapter(Context context, RecyclerView listView){
        this.mContext=context;
        this.mListView=listView;
        int screenW=context.getResources().getDisplayMetrics().widthPixels;
        mWidth=(screenW- Utils.dip2px(context,24))/2;
        mInflate= LayoutInflater.from(mContext);

    }

    /**
     * 方便Glide 加载图片使用
     * @param fragment
     */
    public void attachFragmet(Fragment fragment){
        this.fragment=fragment;
    }

    public RecyclerView getListView(){
        return mListView;
    }


    /**
     *设置显示HeadView
     * @param isShow
     */
    public void setShowHeadView(boolean isShow){
        this.isShowHeadView =isShow;
    }

    /**
     * 设置 显示footView
     * @param isShow
     */
    public void setShowFootView(boolean isShow){
        this.isShowFootView =isShow;
        footSize=isShow?1:0;
    }
    public void setData(List<T> data){
        if(this.mData!=null){
            mData.clear();
        }
        this.mData=data;
        notifyDataSetChanged();
    }

    public  List<T> getData(){
        return mData;
    }
    public boolean isShowHeadView(){
        return isShowHeadView;
    }

    public boolean isShowFootView(){
        return isShowFootView;
    }

    public void addData(List<T> data){
        if(mData==null) {
            mData=new ArrayList<T>();
        }
        int pos=mData.size();

        mData.addAll(data);
        if(isShowHeadView){
            pos+=getHeadCout();
        }
        notifyItemRangeInserted(pos, data.size());

    }

    protected int getHeadCout(){
        return isShowHeadView?1:0;
    }

    public void clear(){
        if(mData!=null){
            mData.clear();
            notifyDataSetChanged();
        }
    }

    public void hideFootView(){
        if(footSize>0){
            notifyItemRangeRemoved(getItemCount(),1);
        }
        setShowFootView(false);
        footSize=0;
    }


    /**
     * 设置footView状态
     * 1、正在加载
     * 2、网络错误
     * 3、网络超时
     * @param state
     */
    public void setFootViewState(int state){
        this.state=state;
        if(state==ErrorView.STATE_HIDE){
            hideFootView();
        }else{
            footSize=1;
        }
        updateFootState();
    }

    public void setFootViewStateValues(int state){
        this.state=state;
        setShowFootView(state!=ErrorView.STATE_HIDE);
    }
    public int getFootViewState(){
        return state;
    }

    /**
     * 更新GootView的状态
     */
    private void updateFootState(){
        if(isShowFootView){
            int pos=mListView.getChildCount();
            View view=mListView.getChildAt(pos - 1);
            if(view!=null){
                RecyclerView.ViewHolder holder=mListView.getChildViewHolder(view);
                if(holder!=null){
                    if(holder instanceof FootViewHold){
                        ((FootViewHold)holder).setState(state,mRetryListens);
                    }
                }
            }
        }
    }

    public void setFootViewListens(View.OnClickListener listens){
        mRetryListens=listens;
    }

    public boolean isEmpty(){
        return mData!=null?mData.isEmpty():true;
    }
    public T getItem(int pos){
        return mData.get(isShowHeadView?pos-1:pos);
    }
    public T getLastItem(){
        int pos=getItemCount();
        pos=isShowFootView?pos-1:pos;
        return  getItem(pos-1);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType==TYPE_ITEM_HEAD){
            return onCreateHeadViewImpl(parent, viewType);
        }else if(viewType==TYPE_ITEM_FOOT){
            View view= View.inflate(mContext, R.layout.item_foot_view_layout,null);
            return new FootViewHold(view);
        }
        return onCreateViewHolderImpl(parent,viewType);
    }

    public abstract RecyclerView.ViewHolder onCreateViewHolderImpl(ViewGroup parent, int viewType);
    public abstract void onBindViewHolderImpl(RecyclerView.ViewHolder holder, int pos);

    /**
     * 实现创建头部控件
     * @return
     */
    public  RecyclerView.ViewHolder onCreateHeadViewImpl(ViewGroup parent, int viewType){return  null;};

    /**
     * 实现绑定头部
     * @param holder
     * @param pos
     */
    public  void onBindHeadViewImpl(RecyclerView.ViewHolder holder, int pos){};


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        int type=getItemViewType(position);
        if(type==TYPE_ITEM_HEAD){
            if(holder instanceof  HeadViewHold){
                onBindHeadViewImpl(holder,position);
            }
        }else if(type==TYPE_ITEM_FOOT){
            if(holder instanceof FootViewHold){
                ((FootViewHold)holder).bind(state,mRetryListens);
            }
        }else{
            onBindViewHolderImpl(holder,position);
        }

    }
    @Override
    public int getItemCount() {
        return mData!=null&&!mData.isEmpty()?(mData.size()+getOtherCount()):0;
    }

    /**
     * 重写这个方法时
     * type 值不要和
     * @see #TYPE_ITEM_FOOT
     * @see #TYPE_ITEM_HEAD
     * @see #TYPE_ITEM_NORMAL
     * 相同
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        if(isShowHeadView&&position==0){
            return TYPE_ITEM_HEAD;
        }
        if(isShowFootView&&position==getItemCount()-1){
            return TYPE_ITEM_FOOT;
        }
        int type=getItemViewTypeBodyIml(position);
        return type==-1?TYPE_ITEM_NORMAL:type;
    }

    /**
     * listview body item view 实现
     * @param pos
     * -1 则使用 默认的
     * @return
     */
    public int getItemViewTypeBodyIml(int pos){
        return -1;
    }

    /**
     * 获取 headView 和 footView的数量
     * @return
     */
    public int getOtherCount(){
        return (isShowFootView ?1:0)+(isShowHeadView ?1:0);
    }


    public static class FootViewHold extends  RecyclerView.ViewHolder{
        View loadingView;
        View errorView;
        View parentView;
        TextView errorMsgView;

        public FootViewHold(View view){
            super(view);
            loadingView=view.findViewById(R.id.rl_loading);
            errorView=view.findViewById(R.id.rl_error_retry_view);
            parentView =view.findViewById(R.id.lin_content);
            errorMsgView=(TextView)view.findViewById(R.id.tv_error_state);
        }
        protected void bind(int state,View.OnClickListener listener){

            if(state!= ErrorView.STATE_LOADING){
                if(listener!=null){
                    errorView.setOnClickListener(listener);
                }
            }
            setState(state,listener);
        }
        public void setState(int state,View.OnClickListener listener){
            if(state== ErrorView.STATE_LOADING){
                loadingView.setVisibility(View.VISIBLE);
                errorView.setVisibility(View.GONE);
                parentView.setVisibility(View.VISIBLE);
                itemView.setVisibility(View.VISIBLE);
            }else if(state==ErrorView.STATE_HIDE){
                parentView.setVisibility(View.GONE);
                loadingView.setVisibility(View.GONE);
                errorView.setVisibility(View.GONE);
                itemView.setVisibility(View.GONE);
                errorMsgView.setText("隐藏");
            } else{
                itemView.setVisibility(View.VISIBLE);
                loadingView.setVisibility(View.GONE);
                parentView.setVisibility(View.VISIBLE);
                errorView.setVisibility(View.VISIBLE);
                errorView.setEnabled(true);
                errorMsgView.setText(R.string.touch_reload);
                if(listener!=null){
                    errorView.setOnClickListener(listener);
                }
            }
        }
    }
    public static class HeadViewHold extends  RecyclerView.ViewHolder{
        public HeadViewHold(View view){
            super(view);
        }
    }

    public void setScrolld(boolean isScrolld){
        this.isScrolld=isScrolld;
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        this.fragment=null;
        clear();
    }
}
