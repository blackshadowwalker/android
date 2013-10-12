package com.cxf.dao;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.cxf.entity.CarAlarm;
import com.cxf.entity.SecurityAlarm;

public class HistorySecurityRecordDao extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "alarms_history.db";
	private static final int DATABASE_VERSION = 1;
	private String userName;
	private String userPass;

	public HistorySecurityRecordDao(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		SharedPreferences sp = context.getSharedPreferences("sys_setting", 0);
		userName = sp.getString("name", null);
		userPass = sp.getString("password", null);
	}

	// 数据库第一次被创建时onCreate会被调用
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE IF NOT EXISTS security_alarms"
				+ "(_id LONG PRIMARY KEY , time VARCHAR, location VARCHAR,level Integer,shortImage Blob, eventName VARCHAR,personInCharge VARCHAR,desp TEXT,isnew BOOLEAN,userName VARCHAR,userpass VARCHAR )");
		db.execSQL("CREATE TABLE IF NOT EXISTS car_alarms"
				+ "(_id LONG PRIMARY KEY , time VARCHAR, location VARCHAR,LPNumber VARCHAR,shortImageA Blob, dir VARCHAR,isnew BOOLEAN,userName VARCHAR,userpass VARCHAR)");

	}

	// 如果DATABASE_VERSION值被改为2,系统发现现有数据库版本不同,即会调用onUpgrade
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

	public boolean insert(List<SecurityAlarm> list) {
		if (userName != null && userPass != null) {
			SQLiteDatabase db = getWritableDatabase();
			String sql = "insert into security_alarms(_id,time,location,level,shortImage,eventName,personInCharge,desp,isnew,userName,userpass) values(?,?,?,?,?,?,?,?,?,?,?)";
			SQLiteStatement stat = db.compileStatement(sql);
			db.beginTransaction();
			for (SecurityAlarm s : list) {
				stat.bindLong(1, s.id);
				stat.bindString(2, s.absTime);
				stat.bindString(3, s.location);
				stat.bindString(4, String.valueOf(s.level));
				ByteArrayOutputStream bout = new ByteArrayOutputStream();
				s.shortImage.compress(Bitmap.CompressFormat.JPEG, 100, bout);
				byte[] img = bout.toByteArray();
				stat.bindBlob(5, img);
				stat.bindString(6, s.eventName);
				stat.bindString(7, s.personInCharge);
				stat.bindString(8, s.desp);
				stat.bindString(9, String.valueOf(s.isNew));
				stat.bindString(10, userName);
				stat.bindString(11, userPass);
				stat.executeInsert();
			}
			db.setTransactionSuccessful();
			db.endTransaction();
			db.close();

			System.out.println("保卫安防告警已经保存到手机本地");
			return true;
		} else {
			return false;
		}
	}

	public List<SecurityAlarm> query(int pageNumber, int pageSize, int level) {
		List<SecurityAlarm> list = new ArrayList<SecurityAlarm>();
		if (userName != null && userPass != null) {
			SQLiteDatabase db = getWritableDatabase();
			int skipTotal = pageNumber * pageSize;
			String sql = null;
			Cursor cursor = null;

			if (level < 0) {
				sql = "select * from security_alarms  where userName=? order by _id desc limit ?,?";
				String[] selectionArgs = new String[] { userName,
						String.valueOf(skipTotal), String.valueOf(pageSize) };
				cursor = db.rawQuery(sql, selectionArgs);

			} else {
				sql = "select * from security_alarms  where userName=? and level=? order by _id desc limit ?,?";
				String[] selectionArgs = new String[] { userName,
						String.valueOf(level), String.valueOf(skipTotal),
						String.valueOf(pageSize) };
				cursor = db.rawQuery(sql, selectionArgs);
			}

			cursor.moveToFirst();
			while (cursor.moveToNext()) {
				Long id = cursor.getLong(cursor.getColumnIndex("_id"));
				String absTime = cursor
						.getString(cursor.getColumnIndex("time"));
				String location = cursor.getString(cursor
						.getColumnIndex("location"));
				int level2 = cursor.getInt(cursor.getColumnIndex("level"));
				byte[] bytes = cursor.getBlob((cursor
						.getColumnIndex("shortImage")));
				boolean isNew = Boolean.valueOf(cursor.getString(cursor
						.getColumnIndex("isnew")));

				Bitmap shortImage = BitmapFactory.decodeByteArray(bytes, 0,
						bytes.length);
				String eventName = cursor.getString(cursor
						.getColumnIndex("eventName"));
				String personInCharge = cursor.getString(cursor
						.getColumnIndex("personInCharge"));
				String desp = cursor.getString(cursor.getColumnIndex("desp"));
				SecurityAlarm sa = new SecurityAlarm(id, absTime, location,
						level2, eventName, personInCharge, desp, shortImage,
						isNew);
				list.add(sa);
			}
			cursor.close();
			db.close();
		}

		return list;
	}
}