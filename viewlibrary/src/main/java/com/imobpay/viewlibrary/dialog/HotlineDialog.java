package com.imobpay.viewlibrary.dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.imobpay.viewlibrary.R;
/**
 * 类名 :HotlineDialog
 * 客服热线 dialog
 * @author jics
 * 修改日期 : 2017-08-04
 */
@SuppressLint("InflateParams") 
public class HotlineDialog extends BaseDialog {
	private Context context;
	private ListView list_line;
	private TextView  tv_title;
	private String[] phoneinfo ;
	private String phone;
	private MyAdapter myadapter;

	public HotlineDialog(Context context) {
		super(context);
		this.context=context;
	}

	public HotlineDialog(Context context,
			String[] deviceinfo) {
		super(context,  R.style.mydialog);
		this.context=context;
		this.phoneinfo = deviceinfo;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_hotline);
		initView();
	}

	/**
	 * @说明：初始化dialog布局控件
	 * @Parameters 无
	 * @return     无
	 * @throws
	 */
	private void initView() {
		tv_title = (TextView) findViewById(R.id.tv_title);
		list_line = (ListView) findViewById(R.id.listhotline);
		myadapter = new MyAdapter(mContext);
		list_line.setAdapter(myadapter);
		list_line.setOnItemClickListener(itemClick);
	}

	/**
	 * 配置标题
	 * @param title
	 */
	public void setTitle(String title){
		tv_title.setText(title);
	}

	OnItemClickListener itemClick = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view,
				int position, long id) {
			phone = phoneinfo[position];
			if( null != getDialogViewClick() ){
				getDialogViewClick().doViewClick( true,CallLineNum , phone );
			}
			HotlineDialog.this.dismiss();
		}
	};
	
	class MyAdapter extends BaseAdapter {

		private Context context;

		public MyAdapter(Context context) {
			this.context = context;
		}
		@Override
		public int getCount() {
			return phoneinfo.length;
		}

		@Override
		public Object getItem(int arg0) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		class ViewHolder {
			TextView title;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final ViewHolder viewHolder;
			if (convertView == null) {
				viewHolder = new ViewHolder();
				LayoutInflater inflater = ((Activity) context)
						.getLayoutInflater();
				convertView = inflater.inflate(R.layout.dialog_hot_name, null);
				viewHolder.title = (TextView) convertView
						.findViewById(R.id.hotline_name);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			viewHolder.title.setText(phoneinfo[position]);
			return convertView;
		}
	}

	//刷新数据
	public void toRefreshData(String[] devicePhoneInfos){
		this.phoneinfo = devicePhoneInfos ;
		if( null != myadapter){
			myadapter.notifyDataSetChanged();
			list_line.setAdapter(myadapter);
		}
	}


}