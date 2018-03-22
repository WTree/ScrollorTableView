package com.wtree.scrollortable.adpater;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wtree.adapter.ScrollorTableAdadperImpl;
import com.wtree.helper.Utils;
import com.wtree.scrollortable.R;
import com.wtree.scrollortable.helper.decoration.HorizontalDividerItemDecoration;
import com.wtree.scrollortable.member.ItemInfo;
import com.wtree.scrolltable.ScrollorTableView;
import com.wtree.view.ErrorView;

/**
 * Created by WTree on 2018/3/22.
 */

public class SimpleScrollTableAdapter extends ScrollorTableAdadperImpl<ItemInfo> {
    private ScrollorTableView mTableView;
    public SimpleScrollTableAdapter(Context context,ScrollorTableView tableView) {
        super(context);
        this.mTableView=tableView;
        LeftPageAdapter leftAdapter=new LeftPageAdapter(this,mContext,tableView.getLeftListView());
        RightPageadapter rightAdapter =new RightPageadapter(this,mContext,tableView.getRightListView());
        setAdapterImpl(leftAdapter,rightAdapter);

        mTableView.getLeftListView().addItemDecoration(initDefaultDiverLine());
        mTableView.getRightListView().addItemDecoration(initDefaultDiverLine());


    }



    public void setFootState(int state){
        getLeftAdapter().setFootViewState(state);
        getRigthAdaptr().setFootViewState(state);
    }

    /**
     * 使用默认的分割线
     */
    public HorizontalDividerItemDecoration initDefaultDiverLine(){
        int diver= Color.parseColor("#eeeeee");
        HorizontalDividerItemDecoration itemDecoration=new HorizontalDividerItemDecoration.Builder(mContext)
                .color(diver)
                .size(1)
                .build();
        return itemDecoration;
    }


    /**
     * footView 和item的按击效果都在这里实现
     */
    public static class LeftPageAdapter extends BaseScrollTableAdater{
        public LeftPageAdapter(ScrollorTableAdadperImpl impl, Context context,RecyclerView listView) {
            super(impl, context, listView);
            setShowFootView(true);
        }
        @Override
        public RecyclerView.ViewHolder onCreateViewHolderImpl(ViewGroup parent, int viewType) {
            View view=mInflate.inflate(R.layout.item_left_layout,parent,false);
            return new LeftViewHolder(view);
        }

        @Override
        public void onBindViewHolderImpl(RecyclerView.ViewHolder holder, int pos) {
            if(holder instanceof LeftViewHolder){
                ((LeftViewHolder) holder).bind((ItemInfo) getItem(pos));
            }

        }
    }



    public static class RightPageadapter extends BaseScrollTableAdater{

        public RightPageadapter(ScrollorTableAdadperImpl impl, Context context, RecyclerView listView) {
            super(impl, context, listView);
            setShowFootView(true);
        }


        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if(viewType==TYPE_ITEM_FOOT){
                View view=new View(parent.getContext());
                return new EmptyFooterViewHolder(view);
            }
            return super.onCreateViewHolder(parent,viewType);
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolderImpl(ViewGroup parent, int viewType) {
            View view=mInflate.inflate(R.layout.item_right_layout,parent,false);
            return new RightViewHold(view);
        }


        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            int type=getItemViewType(position);
            if(type==TYPE_ITEM_FOOT){
                if(holder instanceof EmptyFooterViewHolder){
                    ((EmptyFooterViewHolder) holder).bind(getFootViewState());
                }
            }else{
                super.onBindViewHolder(holder, position);
            }
        }

        @Override
        public void onBindViewHolderImpl(RecyclerView.ViewHolder holder, int pos) {

            if(holder instanceof RightViewHold){
                ((RightViewHold) holder).bind((ItemInfo) getItem(pos));
            }
        }
    }

    static class LeftViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        private int width;
        public LeftViewHolder(View itemView) {
            super(itemView);
            width=itemView.getResources().getDisplayMetrics().widthPixels/3;
            textView=(TextView)itemView.findViewById(R.id.tv_title);
            textView.setTextColor(Color.BLACK);

            textView.getLayoutParams().width=width;
        }

        public void bind(ItemInfo info){
            textView.setText(info.title);
        }
    }

    static class RightViewHold extends RecyclerView.ViewHolder{

        //实际得项目不应该这样
        TextView[] viewArr;

        LinearLayout linearLayout;
        int width;
        public RightViewHold(View itemView) {
            super(itemView);
            linearLayout=(LinearLayout)itemView.findViewById(R.id.lin_parent);
            width=itemView.getResources().getDisplayMetrics().widthPixels/3;

            linearLayout.getLayoutParams().width=width*6;
            viewArr=new TextView[6];
            for(int i=0;i<6;i++){
                viewArr[i]=new TextView(itemView.getContext());
                LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
                params.weight=1;
                viewArr[i].setTextColor(Color.BLACK);
                viewArr[i].setGravity(Gravity.CENTER);
                linearLayout.addView(viewArr[i],params);
                viewArr[i].setId(i);
            }


        }

        public void bind(ItemInfo info){
            viewArr[0].setText(info.item1);
            viewArr[1].setText(info.item2);
            viewArr[2].setText(info.item3);
            viewArr[3].setText(info.item4);
            viewArr[4].setText(info.item4);
            viewArr[5].setText(info.item6);
        }
    }

    /**
     * 这儿的状态要和
     * 保持联动
     * 和left 的footter保持一致
     */
    private static class EmptyFooterViewHolder extends RecyclerView.ViewHolder{
        private int itemWidth;
        public EmptyFooterViewHolder(View itemView) {
            super(itemView);
            itemWidth=itemView.getContext().getResources().getDisplayMetrics().widthPixels/3;
        }

        public void bind(int state){
            if(state== ErrorView.STATE_HIDE){
                itemView.setVisibility(View.GONE);
            }else if(state==ErrorView.STATE_LOADING){
                ViewGroup.LayoutParams layoutParams=new ViewGroup.LayoutParams(itemWidth, Utils.dip2px(itemView.getContext(),36));
                itemView.setLayoutParams(layoutParams);
            }
        }
    }
}
