package com.wtree.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wtree.helper.Utils;
import com.wtree.scrolltable.R;


/**
 *  空界面状态
 * @author tree
 *
 */
public class ErrorView extends RelativeLayout {

	public static final int STATE_ERROR=-1;
	public static final int STATE_NO_NETWORK=1;
	public static final int STATE_TIME_OUT=2;
	public static final int STATE_EMPTY=3;
	public static final int STATE_NET_ERROR=4;//网络异常
	public static final int STATE_LOADING=5;//正在加载
	public static final int STATE_HIDE=10;//隐藏
	public static final int STATE_UN_LOGIN=32;//结束
	private int state;
	private ImageView mErrImageView;
	private TextView mErrorStateView;
	private TextView mErrMsgTextView;
	private TextView mTvEmptyMsg;
	private TextView mTvEmptySubMsg;
	private View mLoadingView;
	private View mErrorLayout;
	private View mEmptyMsgLayout;
	private View mParentView;
	private int size;
//	int color,otherColor;
	public ErrorView(Context context, AttributeSet attrs) {
		super(context, attrs);
		View v=inflate(getContext(), R.layout.empty_layout,this);
		mErrImageView = (ImageView)v.findViewById(R.id.img_error);
		mErrMsgTextView=(TextView)v.findViewById(R.id.tv_error_msg);
		mLoadingView=v.findViewById(R.id.rl_loading);
		mErrorLayout=v.findViewById(R.id.rl_error_view);
		mEmptyMsgLayout = v.findViewById(R.id.empty_msg_view);
		mErrorStateView=(TextView) v.findViewById(R.id.tv_error_state);
		mTvEmptyMsg = (TextView) v.findViewById(R.id.tv_empty_msg) ;
		mTvEmptySubMsg = (TextView) v.findViewById(R.id.tv_empty_sub_msg);
		mParentView=v.findViewById(R.id.rl_content);//加一个父控件
		setState(STATE_LOADING);
		size= Utils.dip2px(context,96);
//		color= Color.parseColor("#c8c8c8");
//		otherColor= Color.parseColor("#999999");
		setClickable(true);
	}
	/**
	 * 设置背景颜色
	 */
	public void setBackgroundColor(int color){
		mParentView.setBackgroundColor(color);
	}
	private String name;
	public void setName(String name){
		this.name=name;
	}
	public void setState(int state)	{
		this.state=state;
//		mErrMsgTextView.setTextColor(otherColor);
		switch (state){
			case STATE_EMPTY:
				mErrImageView.setVisibility(VISIBLE);
				this.setVisibility(View.VISIBLE);
				mLoadingView.setVisibility(View.GONE);
				mErrorLayout.setVisibility(View.VISIBLE);
				mErrorStateView.setVisibility(VISIBLE);
				mErrMsgTextView.setVisibility(GONE);
				mErrorStateView.setText("无数据");
				break;
			case STATE_NO_NETWORK:
			case STATE_TIME_OUT:
			case STATE_NET_ERROR:
				mErrImageView.setVisibility(VISIBLE);
				this.setVisibility(View.VISIBLE);
				mErrMsgTextView.setVisibility(VISIBLE);
				mErrorStateView.setVisibility(VISIBLE);
				mLoadingView.setVisibility(View.GONE);
				mErrorLayout.setVisibility(View.VISIBLE);
				mErrorStateView.setText(R.string.error_net);
//				SvgUtils.setImageSvgDrawable(getContext(),mErrImageView,R.raw.ic_none_network, Color.BLACK,color,size,size);
				mErrMsgTextView.setText(R.string.touch_reload);
				break;
			case STATE_LOADING:
				mErrImageView.setVisibility(VISIBLE);
				this.setVisibility(View.VISIBLE);
				mLoadingView.setVisibility(View.VISIBLE);
				mErrorLayout.setVisibility(View.GONE);
				break;
			case STATE_HIDE:
				this.setVisibility(View.GONE);
				break;

		}
	}

	public int getState(){
		return state;
	}
	public void setRetryOnClik(OnClickListener listener){
		if(listener!=null){
			mErrorLayout.setOnClickListener(listener);
		}
	}

	public View getErrorImageView(){
		return mErrImageView;
	}

	public View getContentView(){
		return mErrorLayout;
	}

}
