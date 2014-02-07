package com.cxf.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.cxf.PhoneIdentification.R;
import com.cxf.dao.UserHistoryDao;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

public class ServerAutoCompleteAdapter extends BaseAdapter implements Filterable {

	private Context context;
	private ArrayFilter filter;
	private List<String> originalValues;
	private List<String> objects;
	private final Object lock = new Object();
	private int maxMatch = 10;
	private final String TAG = "AutoCompleteAdapter";
	private UserHistoryDao userHistoryDao;

	public ServerAutoCompleteAdapter(Context context, List<String> mOriginalValues,
			int maxMatch) {
		this.context = context;
		this.originalValues = mOriginalValues;
		this.maxMatch = maxMatch;
		userHistoryDao = UserHistoryDao.Instance(context);
	}

	@Override
	public Filter getFilter() {
		if (filter == null) {
			filter = new ArrayFilter();
		}
		return filter;
	}

	private class ArrayFilter extends Filter {

		private static final String TAG = "AutoCompleteAdapter";

		@Override
		protected FilterResults performFiltering(CharSequence prefix) {
			FilterResults results = new FilterResults();

			try {
				if (prefix == null || prefix.length() == 0) {
					synchronized (lock) {
						Log.i("tag",
								"mOriginalValues.size=" + originalValues.size());
						ArrayList<String> list = new ArrayList<String>(
								originalValues);
						results.values = list;
						results.count = list.size();
						return results;
					}
				} else {
					Locale lang = Locale.getDefault();
					String prefixString = prefix.toString().toLowerCase(lang);

					final int count = originalValues.size();

					final ArrayList<String> newValues = new ArrayList<String>(
							count);

					for (int i = 0; i < count; i++) {
						final String value = originalValues.get(i);
						final String valueText = value.toLowerCase(lang);
						if (valueText.startsWith(prefixString)) {
							newValues.add(value);
						}
						if (maxMatch > 0) {
							if (newValues.size() > maxMatch - 1) {
								break;
							}
						}
					}

					results.values = newValues;
					results.count = newValues.size();
				}
			} catch (Exception e) {
				Log.e(TAG, e.getMessage());
				e.printStackTrace();
			}

			return results;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void publishResults(CharSequence constraint,
				FilterResults results) {
			try {
				objects = (List<String>) results.values;
				if (results.count > 0) {
					notifyDataSetChanged();
				} else {
					notifyDataSetInvalidated();
				}
			} catch (Exception e) {
				Log.e(TAG, e.getMessage());
				e.printStackTrace();
			}
		}

	}

	@Override
	public int getCount() {
		if (objects != null) {
			return objects.size();
		} else {
			return 0;
		}
	}

	@Override
	public Object getItem(int position) {
		return objects.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		try {
			if (convertView == null) {
				holder = new ViewHolder();
				LayoutInflater inflater = (LayoutInflater) context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater
						.inflate(R.layout.item_autocomplete, null);
				holder.tv = (TextView) convertView
						.findViewById(R.id.simple_item_0);
				holder.iv = (ImageView) convertView
						.findViewById(R.id.simple_item_1);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.tv.setText(objects.get(position));
			holder.iv.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					String obj = objects.remove(position);
					originalValues.remove(obj);
					userHistoryDao.deleteServer(obj);
					notifyDataSetChanged();
				}
			});
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
			e.printStackTrace();
		}
		return convertView;
	}

	class ViewHolder {
		TextView tv;
		ImageView iv;
	}

	public List<String> getAllItems() {
		return originalValues;
	}
}
