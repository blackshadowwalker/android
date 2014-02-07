package com.cxf.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import com.cxf.entity.RequestSetting;

public class HistorySettingDao extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "SysSetting.db";
	private static final int DATABASE_VERSION = 2;
	private static HistorySettingDao instance;
	Context contex;

	private HistorySettingDao(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.contex = context;
	}

	public static HistorySettingDao Instance(Context context) {
		if (instance == null) {
			instance = new HistorySettingDao(context);
		}
		return instance;
	}

	// 数据库第一次被创建时onCreate会被调用
	@Override
	public void onCreate(SQLiteDatabase db) {
		// id INTEGER PRIMARY KEY AUTOINCREMENT
		db.execSQL("CREATE TABLE IF NOT EXISTS sys_settings"
				+ "(id INTEGER PRIMARY KEY AUTOINCREMENT, host VARCHAR,userName VARCHAR,userPass VARCHAR)");

	}

	// 如果DATABASE_VERSION值被改为2,系统发现现有数据库版本不同,即会调用onUpgrade
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

	public synchronized boolean insert(List<RequestSetting> list) {

		SQLiteDatabase db = getWritableDatabase();
		String sql = "insert into sys_settings(host,userName,userpass) values(?,?,?)";
		SQLiteStatement stat = db.compileStatement(sql);
		db.beginTransaction();
		for (RequestSetting s : list) {
			stat.bindString(1, s.host);
			stat.bindString(2, "");
			stat.bindString(3, "");
			stat.executeInsert();
		}
		db.setTransactionSuccessful();
		db.endTransaction();

		System.out.println("已经保存host到数据库");
		return true;

	}

	public synchronized boolean insert(RequestSetting rs) {
		if (rs != null && !exist(rs.host)) {
			SQLiteDatabase db = getWritableDatabase();
			String sql = "insert into sys_settings(host,userName,userpass) values(?,?,?)";
			SQLiteStatement stat = db.compileStatement(sql);
			db.beginTransaction();
			stat.bindString(1, rs.host);
			stat.bindString(2, "");
			stat.bindString(3, "");
			stat.executeInsert();
			db.setTransactionSuccessful();
			db.endTransaction();

			System.out.println("已经保存host到数据库");
			return true;
		}
		return false;

	}

	public List<RequestSetting> query(int pageNumber, int pageSize) {
		List<RequestSetting> list = new ArrayList<RequestSetting>();

		SQLiteDatabase db = getWritableDatabase();
		int skipTotal = (pageNumber - 1) * pageSize;
		Cursor cursor = null;
		String sql = "select * from sys_settings   order by id desc limit ?,?";
		String[] selectionArgs = new String[] { String.valueOf(skipTotal),
				String.valueOf(pageSize) };
		cursor = db.rawQuery(sql, selectionArgs);
		cursor.moveToFirst();
		while (cursor.moveToNext()) {
			String host = cursor.getString(cursor.getColumnIndex("host"));
			int id = cursor.getInt(cursor.getColumnIndex("id"));
			RequestSetting rs = new RequestSetting();
			rs.id = id;
			rs.host = host;
			list.add(rs);
		}
		cursor.close();

		return list;
	}

	public List<RequestSetting> query(int pageNumber, int pageSize, int fromId) {
		List<RequestSetting> list = new ArrayList<RequestSetting>();

		SQLiteDatabase db = getWritableDatabase();
		int skipTotal = pageNumber * pageSize;
		Cursor cursor = null;
		String sql = "select * from sys_settings    and id<? order by id desc limit ?,?";
		String[] selectionArgs = new String[] { String.valueOf(fromId),
				String.valueOf(skipTotal), String.valueOf(pageSize) };
		cursor = db.rawQuery(sql, selectionArgs);
		cursor.moveToFirst();
		while (cursor.moveToNext()) {
			int id = cursor.getInt(cursor.getColumnIndex("id"));
			String host = cursor.getString(cursor.getColumnIndex("host"));

			RequestSetting rs = new RequestSetting();
			rs.id = id;
			rs.host = host;
			list.add(rs);

			cursor.close();
		}

		return list;
	}

	public List<RequestSetting> query() {
		List<RequestSetting> list = new ArrayList<RequestSetting>();

		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = null;
		String sql = "select * from sys_settings";

		cursor = db.rawQuery(sql, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			int id = cursor.getInt(cursor.getColumnIndex("id"));
			String host = cursor.getString(cursor.getColumnIndex("host"));

			RequestSetting rs = new RequestSetting();
			rs.id = id;
			rs.host = host;
			list.add(rs);
			cursor.moveToNext();
		}
		cursor.close();

		return list;
	}

	public int getCount() {
		SQLiteDatabase db = getReadableDatabase();
		String sql = "select count(*) from sys_settings ";

		Cursor cursor = db.rawQuery(sql, null);
		cursor.moveToFirst();
		int size = cursor.getInt(0);
		return size;
	}

	public boolean exist(String host) {
		SQLiteDatabase db = getReadableDatabase();
		String sql = "select * from sys_settings where host=? ";

		String[] selectionArgs = new String[] { host };
		Cursor cursor = db.rawQuery(sql, selectionArgs);
		int size = cursor.getCount();
		if (size > 0) {
			return true;
		}
		return false;
	}

	public synchronized boolean deleteStartFrom(long fromId) {
		SQLiteDatabase db = getWritableDatabase();
		String sql = "delete from sys_settings and id<?";
		SQLiteStatement stat = db.compileStatement(sql);
		db.beginTransaction();
		stat.bindLong(1, fromId);
		stat.executeInsert();
		db.setTransactionSuccessful();
		db.endTransaction();
		return true;
	}

}