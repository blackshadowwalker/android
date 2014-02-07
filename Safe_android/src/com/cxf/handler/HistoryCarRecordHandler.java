package com.cxf.handler;

import java.util.List;

import android.content.Context;
import android.util.Log;

import com.cxf.dao.HistoryCarRecordDao;
import com.cxf.entity.CarAlarm;

public class HistoryCarRecordHandler {
	HistoryCarRecordDao historyCarRecordDao;
	Context context;
	static HistoryCarRecordHandler instance;
	final String TAG = HistoryCarRecordHandler.class.getName();

	private HistoryCarRecordHandler(Context context) {
		super();
		this.historyCarRecordDao = HistoryCarRecordDao.Instance(context);
	}

	public static HistoryCarRecordHandler instance(Context context) {
		if (instance != null) {
			return instance;
		} else {
			instance = new HistoryCarRecordHandler(context);
			return instance;
		}

	}

	public void saveCarAlarms(List<CarAlarm> list) {
		new saveHistoryRecord(list).start();
	}

	class saveHistoryRecord extends Thread {
		List<CarAlarm> list;

		public saveHistoryRecord(List<CarAlarm> list) {
			super();
			this.list = list;
		}

		@Override
		public void run() {
			// 保存到手机数据库
			int size = historyCarRecordDao.getCount();
			if (size > 50) {
				long tempId = historyCarRecordDao.queryId(50, -1);
				historyCarRecordDao.deleteStartFrom(tempId);
				Log.i(TAG, "清理车辆告警数据库");
			}
			historyCarRecordDao.insert(list);
		}

	}

}
