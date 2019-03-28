package com.imobpay.viewlibrary.dialog;

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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.imobpay.viewlibrary.R;
import com.imobpay.viewlibrary.bean.DeviceInfo;
import com.imobpay.viewlibrary.interfaces.BlueToothListener;

import java.util.ArrayList;


/**
 * 类名 :BluetoothDialog
 * 蓝牙搜索结果展示列表 dialog
 * @author jics
 * 修改日期 : 2017-08-04
 */
public class BluetoothDialog extends BaseDialog {
	private ListView list_device;
	Context context;
	private TextView tv_state, tv_title;
	private ProgressBar pro_state;
	private ArrayList<DeviceInfo> deviceinfo = new ArrayList<DeviceInfo>();
	BlueToothListener myListener;
	String address;
	MyAdapter myadapter;

	public BluetoothDialog(Context context) {
		super(context);
	}

	public BluetoothDialog(Context context, int theme,
			ArrayList<DeviceInfo> deviceinfo, BlueToothListener myListener) {
		super(context, theme);
		this.context = context;
		this.deviceinfo = deviceinfo;
		this.myListener = myListener;
	}

	public void RefreshList(ArrayList<DeviceInfo> deviceinfo) {
		this.deviceinfo = deviceinfo;
		myadapter.notifyDataSetChanged();
	}


	/**
	 * 设置搜索状态
	 * @param iscomplete  是否完成搜索
	 */
	public void SearchComplete(boolean iscomplete) {

		if (iscomplete) {
			tv_state.setText(context.getResources().getString(R.string.bluetooth_search_completed));
			pro_state.setVisibility(View.GONE);
		} else {
			tv_state.setText(context.getResources().getString(R.string.bluetooth_searching));
			pro_state.setVisibility(View.VISIBLE);
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_bluedevice);
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_title.setText(context.getResources().getString(R.string.bluetooth_device_list));
		tv_state = (TextView) findViewById(R.id.tv_state);
		pro_state = (ProgressBar) findViewById(R.id.pro_state);
		tv_state.setText(context.getResources().getString(R.string.bluetooth_searching));
		list_device = (ListView) findViewById(R.id.listdevices);
		myadapter = new MyAdapter(context);
		list_device.setAdapter(myadapter);
		list_device.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				address = deviceinfo.get(position).getDeviceid();
				myListener.getBlueToothMac(address,deviceinfo.get(position).getDevicename());
				BluetoothDialog.this.dismiss();
			}
		});

	}

	class MyAdapter extends BaseAdapter {

		private Context context;

		// private List<String> arrayList;

		public MyAdapter(Context context) {
			this.context = context;

		}

		@Override
		public int getCount() {


			return deviceinfo.size();
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
				LayoutInflater inflater = ((Activity) context).getLayoutInflater();
				convertView = inflater.inflate(R.layout.dialog_device_name, null);
				viewHolder.title = (TextView) convertView.findViewById(R.id.device_name);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}

			viewHolder.title.setText(deviceinfo.get(position).getDevicename()
					+ "\n" + deviceinfo.get(position).getDeviceid());

			return convertView;
		}
	}

	

}
