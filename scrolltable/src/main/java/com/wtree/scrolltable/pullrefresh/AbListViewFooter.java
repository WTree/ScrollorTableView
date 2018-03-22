/*
 * Copyright (C) 2012 www.amsoft.cn
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.wtree.scrolltable.pullrefresh;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wtree.helper.Utils;
import com.wtree.scrolltable.R;
import com.wtree.view.ErrorView;


/**
 * © 2012 amsoft.cn
 * 名称：AbListViewFooter.java 
 * 描述：加载更多Footer View类.
 *
 * @author 还如一梦中
 * @version v1.0
 * @date：2013-01-17 下午11:52:13
 */
public class AbListViewFooter extends LinearLayout {
	
	/** The m context. */
	private Context mContext;

	View loadingView;
	View errorView;
	View parentView;
	TextView errorMsgView;

	View rootView;

	private int state= ErrorView.STATE_LOADING;

	private OnClickListener retryListener;



	/**
	 * Instantiates a new ab list view footer.
	 *
	 * @param context the context
	 */
	public AbListViewFooter(Context context) {
		super(context);
		initView(context);
	}

	/**
	 * Instantiates a new ab list view footer.
	 *
	 * @param context the context
	 * @param attrs the attrs
	 */
	public AbListViewFooter(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);

	}
	
	/**
	 * Inits the view.
	 *
	 * @param context the context
	 */
	private void initView(Context context) {
		mContext = context;


		LayoutInflater.from(mContext).inflate(R.layout.item_foot_view_layout,this,true);
		loadingView=findViewById(R.id.rl_loading);
		errorView=findViewById(R.id.rl_error_retry_view);
		parentView =findViewById(R.id.lin_content);
		errorMsgView=(TextView)findViewById(R.id.tv_error_state);
		rootView=findViewById(R.id.rl_parent);
		setState(ErrorView.STATE_LOADING);
	}

	public int getFootHeight(){
		return rootView.getHeight();
	}
	public void setRetryListener(OnClickListener listener){
		this.retryListener=listener;
	}


	/**
	 * 设置高度.
	 *
	 * @param height 新的高度
	 */
	public void setVisiableHeight(int height) {
		if (height < 0) height = 0;
		LayoutParams lp = (LayoutParams) getLayoutParams();
		if(lp==null){
			return;
		}
		lp.height = height;
		setLayoutParams(lp);
	}


	public int getState(){
		return state;
	}

	/**
	 * 设置当前状态.
	 *
	 * @param state the new state
	 */
	public void setState(int state) {

		if(state== ErrorView.STATE_LOADING){
			loadingView.setVisibility(View.VISIBLE);
			errorView.setVisibility(View.GONE);
			parentView.setVisibility(View.VISIBLE);
			setVisibility(View.VISIBLE);
			setVisiableHeight(Utils.dip2px(getContext(),36));
		}else if(state==ErrorView.STATE_HIDE){
			parentView.setVisibility(View.GONE);
			loadingView.setVisibility(View.GONE);
			errorView.setVisibility(View.GONE);
			setVisibility(View.GONE);
			errorMsgView.setText("隐藏");
		} else{
			setVisiableHeight(Utils.dip2px(getContext(),36));
			setVisibility(View.VISIBLE);
			loadingView.setVisibility(View.GONE);
			parentView.setVisibility(View.VISIBLE);
			errorView.setVisibility(View.VISIBLE);
			errorView.setEnabled(true);
			errorMsgView.setText(R.string.touch_reload);
			if(retryListener!=null){
				errorView.setOnClickListener(retryListener);
			}

		}
		this.state=state;
	}
	


	



	

}