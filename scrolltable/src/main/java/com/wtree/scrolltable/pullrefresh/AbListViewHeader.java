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
import android.view.Gravity;
import android.widget.LinearLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
* © 2012 amsoft.cn
* 名称：AbListViewHeader.java 
* 描述：下拉刷新的Header View类.
*
* @author 还如一梦中
* @version v1.0
* @date：2013-01-17 下午11:52:13
*/
public class AbListViewHeader extends LinearLayout {
	
	/** 上下文. */
	private Context mContext;
	
	/** 主View. */
	private LinearLayout headerView;

	/**
	 * 初始化Header.
	 *
	 * @param context the context
	 */
	public AbListViewHeader(Context context) {
		super(context);
		initView(context);
	}

	/**
	 * 初始化Header.
	 *
	 * @param context the context
	 * @param attrs the attrs
	 */
	public AbListViewHeader(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	/**
	 * 初始化View.
	 * 
	 * @param context the context
	 */
	private void initView(Context context) {
		
		mContext  = context;
		
		//顶部刷新栏整体内容
		headerView = new LinearLayout(context);
		headerView.setOrientation(LinearLayout.HORIZONTAL);
		headerView.setGravity(Gravity.CENTER);


		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		lp.gravity = Gravity.BOTTOM;

		this.addView(headerView,lp);
	}

	/**
	 * 设置header可见的高度.
	 *
	 * @param height the new visiable height
	 */
	public void setVisiableHeight(int height) {
		if (height < 0) height = 0;
		LayoutParams lp = (LayoutParams) headerView.getLayoutParams();
		lp.height = height;
		headerView.setLayoutParams(lp);
	}

	/**
	 * 获取header可见的高度.
	 *
	 * @return the visiable height
	 */
	public int getVisiableHeight() {
		LayoutParams lp = (LayoutParams)headerView.getLayoutParams();
		return lp.height;
	}

	/**
	 * 描述：获取HeaderView.
	 *
	 * @return the header view
	 */
	public LinearLayout getHeaderView() {
		return headerView;
	}
	


	/**
	 * 描述：设置背景颜色.
	 *
	 * @param color the new background color
	 */
	public void setBackgroundColor(int color){
		headerView.setBackgroundColor(color);
	}
//
//	/**
//	 * 描述：设置Header ProgressBar样式.
//	 *
//	 * @param indeterminateDrawable the new header progress bar drawable
//	 */
//	public void setHeaderProgressBarDrawable(Drawable indeterminateDrawable) {
//		headerProgressBar.setIndeterminateDrawable(indeterminateDrawable);
//	}
	

	/**
	 * 描述：获取表示当前日期时间的字符串.
	 *
	 * @param format  格式化字符串，如："yyyy-MM-dd HH:mm:ss"
	 * @return String String类型的当前日期时间
	 */
	public String getCurrentDate(String format){
		String curDateTime = null;
		try {
			SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(format, Locale.CHINA);
			Calendar c = new GregorianCalendar();
			curDateTime = mSimpleDateFormat.format(c.getTime());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return curDateTime;
	}

}