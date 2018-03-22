package com.wtree.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WTree on 2018/2/28.
 */

public abstract class ScrollorTableAdadperImpl<T> {



    private BaseScrollTableAdater mLeftAdapter,mRightAdapter;

    protected Context mContext;

    public ScrollorTableAdadperImpl(Context context) {
        this.mContext = context;

    }

    public void setAdapterImpl( BaseScrollTableAdater left,BaseScrollTableAdater right){
        mLeftAdapter=left;
        mRightAdapter=right;
    }


    public BaseScrollTableAdater getLeftAdapter(){
        return mLeftAdapter;
    }
    public BaseScrollTableAdater getRigthAdaptr(){
        return mRightAdapter;
    }

    private List<T> mData;
    public void setData(List<T> data){
        mData=data;
        notifyDataSetChanged();
    }
    public  void notifyDataSetChanged(){
        if(getLeftAdapter()!=null){
            getLeftAdapter().notifyDataSetChanged();
        }
        if(getRigthAdaptr()!=null){
            getRigthAdaptr().notifyDataSetChanged();
        }
    }


    public List<T> getData(){
        return mData;
    }

    public void addData(List<T> data){
        if(mData==null){
            mData=new ArrayList<>(10);
        }
        mData.addAll(data);
        notifyDataSetChanged();
    }


    public boolean isEmpty(){
        return mData==null?true:mData.isEmpty();
    }
    public int getItemCount() {
        return mData!=null?mData.size():0;
    }


    public void clear(){

        if(mData!=null){
            mData.clear();
        }
        notifyDataSetChanged();
    }

    public T getItem(int pos){
        return mData==null?null:mData.get(pos);
    }


    public static abstract class BaseScrollTableAdater extends PageAdapter{

        private ScrollorTableAdadperImpl baseImpl;
        public BaseScrollTableAdater(ScrollorTableAdadperImpl impl,  Context context, RecyclerView listView) {
            super(context, listView);
            baseImpl=impl;
        }

        /**
         * @see ScrollorTableAdadperImpl#getData()
         * @return
         */
        @Override
        public  List getData(){
            return baseImpl.getData();
        }

        @Override
        public Object getItem(int pos) {
            return baseImpl.getData().get(isShowHeadView()?pos+1:pos);
        }

        @Override
        public int getItemCount() {
            return baseImpl.isEmpty()?0: baseImpl.getItemCount()+getOtherCount();
        }
    }
}
