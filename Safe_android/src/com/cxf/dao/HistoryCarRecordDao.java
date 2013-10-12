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

public class HistoryCarRecordDao extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "alarms_history.db";
	private static final int DATABASE_VERSION = 1;
	private String userName;
	private String userPass;

	public HistoryCarRecordDao(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		SharedPreferences sp = context.getSharedPreferences("sys_setting", 0);
		userName = sp.getString("name", null);
		userPass = sp.getString("password", null);
	}

	// 数据库第一次被创建时onCreate会被调用
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE IF NOT EXISTS car_alarms"
				+ "(_id LONG PRIMARY KEY , time VARCHAR, location VARCHAR,LPNumber VARCHAR,shortImageA Blob, dir VARCHAR,isnew VARCHAR,userName VARCHAR,userpass VARCHAR)");
		db.execSQL("CREATE TABLE IF NOT EXISTS security_alarms"
				+ "(_id LONG PRIMARY KEY , time VARCHAR, location VARCHAR,level Integer,shortImage Blob, eventName VARCHAR,personInCharge VARCHAR,desp TEXT,isnew VARCHAR,userName VARCHAR,userpass VARCHAR )");

	}

	// 如果DATABASE_VERSION值被改为2,系统发现现有数据库版本不同,即会调用onUpgrade
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

	public boolean insert(List<CarAlarm> list) {
		if (userName != null && userPass != null) {
			SQLiteDatabase db = getWritableDatabase();
			String sql = "insert into car_alarms(_id,time,location,LPNumber,shortImageA,dir,isnew,userName,userpass) values(?,?,?,?,?,?,?,?,?)";
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
				stat.executeInsert();
			}
			db.setTransactionSuccessful();
			db.endTransaction();
			db.close();

			System.out.println("车辆告警信息已经保存到手机本地");
			return true;
		} else {
			return false;
		}
	}

	public List<CarAlarm> query(int pageNumber, int pageSize, int dir) {
		List<CarAlarm> list = new ArrayList<CarAlarm>();
		if (userName != null && userPass != null) {
			SQLiteDatabase db = getWritableDatabase();
			int skipTotal=pageNumber*pageSize;
			String sql = "select * from car_alarms  where userName=? and dir=? order by _id desc limit ?,?";
			String[] selectionArgs = new String[] {  userName, String.valueOf(dir),String.valueOf(skipTotal),
					String.valueOf(pageSize) };
			Cursor cursor = db.rawQuery(sql, selectionArgs);
			cursor.moveToFirst();
			while (cursor.moveToNext()) {
				Long id = cursor.getLong(cursor.getColumnIndex("_id"));
				String lPNumber = cursor.getString(cursor
						.getColumnIndex("LPNumber"));
				String absTime = cursor
						.getString(cursor.getColumnIndex("time"));
				String location = cursor.getString(cursor
						.getColumnIndex("location"));
				byte[] bytes = cursor.getBlob((cursor
						.getColumnIndex("shortImageA")));
				boolean isnew = Boolean.valueOf(cursor.getString(cursor
						.getColumnIndex("isnew")));

				Bitmap shortImageA = BitmapFactory.decodeByteArray(bytes, 0,
						bytes.length);

				CarAlarm c = new CarAlarm(id, lPNumber, dir, location, absTime,
						shortImageA, isnew);
				list.add(c);
			}
			cursor.close();
			db.close();
		}
		
		return list;
	}

}