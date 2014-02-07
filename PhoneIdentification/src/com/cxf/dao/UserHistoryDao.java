package com.cxf.dao;

import java.util.ArrayList;
import java.util.List;
import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.cxf.entity.User;

public class UserHistoryDao extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "user.db";
	private static final int DATABASE_VERSION = 1;
	private static UserHistoryDao instance;
	@SuppressWarnings("unused")
	private Context context;

	private UserHistoryDao(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.context = context;
	}

	public static UserHistoryDao Instance(Context context) {
		if (instance == null) {
			instance = new UserHistoryDao(context);
		}
		return instance;
	}

	// 数据库第一次被创建时onCreate会被调用
	@Override
	public void onCreate(SQLiteDatabase db) {
		// id INTEGER PRIMARY KEY AUTOINCREMENT
		db.execSQL("CREATE TABLE IF NOT EXISTS users"
				+ "(id INTEGER PRIMARY KEY AUTOINCREMENT,telephone VARCHAR, password VARCHAR)");
		db.execSQL("CREATE TABLE IF NOT EXISTS server"
				+ "(id INTEGER PRIMARY KEY AUTOINCREMENT,url VARCHAR)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

	// 当登录成功时调用此方法，如果已存在则不作处理，如果是新user或url则保存入库
	public void insert(User user, String url) {
		SQLiteDatabase db = getWritableDatabase();
		Cursor cursor = null;
		if (user != null) {
			cursor = db.rawQuery("select * from users where telephone=?",
					new String[] { user.telephone });
			if (cursor == null || !cursor.moveToNext()) {
				db.compileStatement(
						"insert into users(telephone,password) values ('"
								+ user.telephone + "','" + user.password + "')")
						.executeInsert();
			}
		}
		if (url != null) {
			cursor = db.rawQuery("select * from server where url=?",
					new String[] { url });
			if (cursor == null || !cursor.moveToNext()) {
				db.compileStatement(
						"insert into server(url) values ('" + url + "')")
						.executeInsert();
			}
		}
	}

	// 删除用户，以手机号为参数
	@SuppressLint("NewApi")
	public boolean deleteUser(String tel) {
		SQLiteDatabase db = getWritableDatabase();
		int i = 0;
		i = db.compileStatement(
				"delete from users where telephone='" + tel + "'")
				.executeUpdateDelete();
		if (i == 1)
			return true;
		else
			return false;
	}

	// 删除服务器，以url为参数
	@SuppressLint("NewApi")
	public boolean deleteServer(String url) {
		SQLiteDatabase db = getWritableDatabase();
		int i = 0;
		i = db.compileStatement("delete from server where url='" + url + "'")
				.executeUpdateDelete();
		if (i == 1)
			return true;
		else
			return false;
	}

	// 查询所有用户，初始化登录页面时调用
	public List<String> queryUsers() {
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from users", null);
		List<String> list = new ArrayList<String>();
		while (cursor.moveToNext()) {
			// System.out.println(cursor.getString(1)+"   "+cursor.getString(2));
			list.add(cursor.getString(1));
		}
		return list;
	}

	// 查询所有服务器，初始化登录页面时调用
	public List<String> queryServers() {
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from server", null);
		List<String> list = new ArrayList<String>();
		while (cursor.moveToNext()) {
			// System.out.println(cursor.getString(1));
			list.add(cursor.getString(1));
		}
		return list;
	}

	// 修改用户
	@SuppressLint("NewApi")
	public boolean updateUser(User user) {
		SQLiteDatabase db = getWritableDatabase();
		int i = 0;
		i = db.compileStatement(
				"update users Set password='" + user.password
						+ "' where telephone='" + user.telephone + "'")
				.executeUpdateDelete();

		if (i == 1)
			return true;
		else
			return false;
	}
	/*
	 * public synchronized boolean insert(List<User> list) { if (list != null) {
	 * SQLiteDatabase db = getWritableDatabase(); String sql =
	 * "insert into users(telephone,password) values(?,?)"; SQLiteStatement stat
	 * = db.compileStatement(sql); db.beginTransaction(); for (User u : list) {
	 * stat.bindString(1, u.telephone); stat.bindString(2, u.password);
	 * stat.executeInsert(); } db.setTransactionSuccessful();
	 * db.endTransaction(); return true; } else { return false; } }
	 * 
	 * public List<User> query(String s, int count) { ArrayList<User> list = new
	 * ArrayList<User>(); if (s != null) { SQLiteDatabase db =
	 * getWritableDatabase(); Cursor cursor = null; String sql =
	 * "select * from users  where telephone=?"; String[] selectionArgs = new
	 * String[] { s }; cursor = db.rawQuery(sql, selectionArgs);
	 * cursor.moveToFirst(); cursor.moveToFirst(); while (!cursor.isAfterLast())
	 * { String tele = cursor.getString(cursor .getColumnIndex("telephone"));
	 * 
	 * String pass = cursor.getString(cursor .getColumnIndex("password")); User
	 * user = new User(); user.telephone = tele; user.password = pass;
	 * list.add(user); cursor.moveToNext(); } cursor.close(); } return list; }
	 * 
	 * public void update(User user) { if (user != null && user.telephone !=
	 * null && user.password != null) { SQLiteDatabase db =
	 * getWritableDatabase(); String[] selectionArgs = new String[] {
	 * user.telephone, }; ContentValues values = new ContentValues();
	 * values.put("password", user.password);
	 * 
	 * db.update("users", values, " telephone=?", selectionArgs); } }
	 * 
	 * public int getCount() { SQLiteDatabase db = getReadableDatabase(); String
	 * sql = "select count(*) from users"; Cursor cursor = db.rawQuery(sql,
	 * null); cursor.moveToFirst(); int size = cursor.getInt(0); return size; }
	 * 
	 * public synchronized boolean deleteStartFrom(long fromId) { SQLiteDatabase
	 * db = getWritableDatabase(); String sql = "delete from users id<?";
	 * SQLiteStatement stat = db.compileStatement(sql); db.beginTransaction();
	 * stat.bindLong(1, fromId); stat.execute(); db.setTransactionSuccessful();
	 * db.endTransaction(); return true;
	 * 
	 * }
	 */

}