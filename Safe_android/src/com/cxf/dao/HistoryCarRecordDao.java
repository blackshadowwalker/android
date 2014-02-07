package com.cxf.dao;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.cxf.entity.CarAlarm;

public class HistoryCarRecordDao extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "car_alarms_history.db";
	private static final int DATABASE_VERSION = 2;
	private static String userName;
	private static String userPass;
	private static HistoryCarRecordDao instance;
	private Context context;
	SharedPreferences sp;

	private HistoryCarRecordDao(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.context=context;
		sp = context.getSharedPreferences("sys_setting", 0);
	}

	public static HistoryCarRecordDao Instance(Context context) {
		if (instance == null) {
			instance = new HistoryCarRecordDao(context);
		}
		
		
		return instance;
	}

	// 数据库第一次被创建时onCreate会被调用
	@Override
	public void onCreate(SQLiteDatabase db) {
		// id INTEGER PRIMARY KEY AUTOINCREMENT
		db.execSQL("CREATE TABLE IF NOT EXISTS car_alarms"
				+ "(id INTEGER PRIMARY KEY AUTOINCREMENT,_id LONG , time VARCHAR, location VARCHAR,LPNumber VARCHAR,shortImageA Blob, dir VARCHAR,isnew VARCHAR,userName VARCHAR,userpass VARCHAR,bigImageUrl VARCHAR)");

	}

	// 如果DATABASE_VERSION值被改为2,系统发现现有数据库版本不同,即会调用onUpgrade
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

	public synchronized boolean insert(List<CarAlarm> list) {
		userName = sp.getString("name", null);
		userPass = sp.getString("password", null);
		if (userName != null && userPass != null) {
			SQLiteDatabase db = getWritableDatabase();
			String sql = "insert into car_alarms(_id,time,location,LPNumber,shortImageA,dir,isnew,userName,userpass,bigImageUrl) values(?,?,?,?,?,?,?,?,?,?)";
			SQLiteStatement stat = db.compileStatement(sql);
			db.beginTransaction();
			for (CarAlarm c : list) {
				stat.bindLong(1, c.id);
				stat.bindString(2, c.absTime);
				stat.bindString(3, c.location);
				stat.bindString(4, c.LPNumber);
				ByteArrayOutputStream bout = new ByteArrayOutputStream();
				c.shortImageA.compress(Bitmap.CompressFormat.JPEG, 100, bout);
				byte[] img = bout.toByteArray();
				stat.bindBlob(5, img);
				stat.bindString(6, String.valueOf(c.dir));
				stat.bindString(7, String.valueOf(c.isNew));
				stat.bindString(8, userName);
				stat.bindString(9, userPass);
				stat.bindString(10, c.bigImageUrl);
				stat.executeInsert();
			}
			db.setTransactionSuccessful();
			db.endTransaction();

			System.out.println("车辆告警信息已经保存到手机本地");
			return true;
		} else {
			return false;
		}
	}

	public List<CarAlarm> query(int pageNumber, int pageSize, int dir) {
		List<CarAlarm> list = new ArrayList<CarAlarm>();
	
		userName = sp.getString("name", null);
		userPass = sp.getString("password", null);
		if (userName != null && userPass != null) {
			SQLiteDatabase db = getWritableDatabase();
			int skipTotal = (pageNumber - 1) * pageSize;
			Cursor cursor = null;
			if (dir == 0 || dir == 1) {
				String sql = "select * from car_alarms  where userName=? and dir=? order by _id desc limit ?,?";
				String[] selectionArgs = new String[] { userName,
						String.valueOf(dir), String.valueOf(skipTotal),
						String.valueOf(pageSize) };
				cursor = db.rawQuery(sql, selectionArgs);
			} else {
				String sql = "select * from car_alarms  where userName=?  order by _id desc limit ?,?";
				String[] selectionArgs = new String[] { userName,
						String.valueOf(skipTotal), String.valueOf(pageSize) };
				cursor = db.rawQuery(sql, selectionArgs);
			}
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				Long id = cursor.getLong(cursor.getColumnIndex("_id"));
				String lPNumber = cursor.getString(cursor
						.getColumnIndex("LPNumber"));

				String absTime = cursor
						.getString(cursor.getColumnIndex("time"));
				String location = cursor.getString(cursor
						.getColumnIndex("location"));
				if (location == null) {
					location = "";
				}
				byte[] bytes = cursor.getBlob((cursor
						.getColumnIndex("shortImageA")));
				boolean isnew = Boolean.valueOf(cursor.getString(cursor
						.getColumnIndex("isnew")));
				int direction = Integer.parseInt(cursor.getString(cursor
						.getColumnIndex("dir")));

				Bitmap shortImageA = BitmapFactory.decodeByteArray(bytes, 0,
						bytes.length);
				String bigImageUrl = cursor.getString(cursor
						.getColumnIndex("bigImageUrl"));

				CarAlarm c = new CarAlarm(id, lPNumber, direction, location,
						absTime, shortImageA, isnew, bigImageUrl);
				list.add(c);
				cursor.moveToNext();
			}
			cursor.close();
		}

		return list;
	}

	public List<CarAlarm> query(int pageNumber, int pageSize, int dir,
			long fromId) {
		List<CarAlarm> list = new ArrayList<CarAlarm>();
		userName = sp.getString("name", null);
		userPass = sp.getString("password", null);
		if (userName != null && userPass != null) {
			SQLiteDatabase db = getWritableDatabase();
			int skipTotal = pageNumber * pageSize;
			Cursor cursor = null;
			if (dir == 0 || dir == 1) {
				String sql = "select * from car_alarms  where userName=? and dir=? and _id<? order by _id desc limit ?,?";
				String[] selectionArgs = new String[] { userName,
						String.valueOf(dir), String.valueOf(fromId),
						String.valueOf(skipTotal), String.valueOf(pageSize) };
				cursor = db.rawQuery(sql, selectionArgs);
			} else {
				String sql = "select * from car_alarms  where userName=? and _id<?  order by _id desc limit ?,?";
				String[] selectionArgs = new String[] { userName,
						String.valueOf(fromId), String.valueOf(skipTotal),
						String.valueOf(pageSize) };
				cursor = db.rawQuery(sql, selectionArgs);
			}
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				Long id = cursor.getLong(cursor.getColumnIndex("_id"));
				String lPNumber = cursor.getString(cursor
						.getColumnIndex("LPNumber"));

				String absTime = cursor
						.getString(cursor.getColumnIndex("time"));
				String location = cursor.getString(cursor
						.getColumnIndex("location"));
				if (location == null) {
					location = "";
				}
				byte[] bytes = cursor.getBlob((cursor
						.getColumnIndex("shortImageA")));
				boolean isnew = Boolean.valueOf(cursor.getString(cursor
						.getColumnIndex("isnew")));
				int direction = Integer.parseInt(cursor.getString(cursor
						.getColumnIndex("dir")));

				Bitmap shortImageA = BitmapFactory.decodeByteArray(bytes, 0,
						bytes.length);
				String bigImageUrl = cursor.getString(cursor
						.getColumnIndex("bigImageUrl"));
				CarAlarm c = new CarAlarm(id, lPNumber, direction, location,
						absTime, shortImageA, isnew, bigImageUrl);
				list.add(c);
				cursor.moveToNext();
			}
			cursor.close();
		}

		return list;
	}

	public Long queryId(int skipby, int dir) {
		Long tempId = 0l;
		userName = sp.getString("name", null);
		userPass = sp.getString("password", null);
		if (userName != null && userPass != null) {
			SQLiteDatabase db = getWritableDatabase();
			Cursor cursor = null;
			if (dir == 0 || dir == 1) {
				String sql = "select * from car_alarms  where userName=? and dir=?  order by _id desc limit ?,1";
				String[] selectionArgs = new String[] { userName,
						String.valueOf(dir), String.valueOf(skipby) };
				cursor = db.rawQuery(sql, selectionArgs);
			} else {
				String sql = "select * from car_alarms  where userName=?  order by _id desc limit ?,1";
				String[] selectionArgs = new String[] { userName,
						String.valueOf(skipby) };
				cursor = db.rawQuery(sql, selectionArgs);
			}
			cursor.moveToFirst();
			
			tempId = cursor.getLong(cursor.getColumnIndex("_id"));

			cursor.close();
		}

		return tempId;
	}

	public void update(CarAlarm c) {
		userName = sp.getString("name", null);
		userPass = sp.getString("password", null);
		if (userName != null && userPass != null && c != null) {
			SQLiteDatabase db = getWritableDatabase();
			String[] selectionArgs = new String[] { userName,
					String.valueOf(c.id) };
			ContentValues values = new ContentValues();
			values.put("isnew", "false");

			db.update("car_alarms", values, " userName=? and _id=?",
					selectionArgs);
		}
	}

	public int getCount() {
		userName = sp.getString("name", null);
		userPass = sp.getString("password", null);
		SQLiteDatabase db = getReadableDatabase();
		String sql = "select count(*) from car_alarms where userName=? ";
		
		String[] selectionArgs = new String[] { userName};
		Cursor cursor = db.rawQuery(sql, selectionArgs);
		cursor.moveToFirst();
		int size = cursor.getInt(0);
		return size;
	}

	public synchronized boolean deleteStartFrom(long fromId) {
		userName = sp.getString("name", null);
		userPass = sp.getString("password", null);
		if (userName != null && userPass != null) {
			SQLiteDatabase db = getWritableDatabase();
			String sql = "delete from car_alarms where userName=? and userPass=? and _id<?";
			SQLiteStatement stat = db.compileStatement(sql);
			db.beginTransaction();
			stat.bindString(1, userName);
			stat.bindString(2, userPass);
			stat.bindLong(3, fromId);
			stat.executeInsert();
			db.setTransactionSuccessful();
			db.endTransaction();
			return true;
		} else {
			return false;
		}
	}

}