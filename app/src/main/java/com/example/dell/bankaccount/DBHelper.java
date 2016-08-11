package com.example.dell.bankaccount;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
@SuppressWarnings("deprecation")
public class DBHelper extends SQLiteOpenHelper {
    public static final int DB_VERSION = 1;
    public static final String DB_NAME = "accounts.db";

    public DBHelper(Context ctx) {
        super(ctx, DB_NAME, null, DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        createTables(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {

    }

    public void createTables(SQLiteDatabase database) {
        String account_table_sql = "create table " + Database.ACCOUNTS_TABLE_NAME + " ( " +
                Database.ACCOUNTS_ID 	+ " integer  primary key autoincrement," +
                Database.ACCOUNTS_ACNO + " TEXT," +
                Database.ACCOUNTS_HOLDERS + " TEXT," +
                Database.ACCOUNTS_CNO  + " TEXT," +
                Database.ACCOUNTS_BANK + " TEXT," +
                Database.ACCOUNTS_BRANCH + " TEXT," +
                Database.ACCOUNTS_ADDRESS + " TEXT," +
                Database.ACCOUNTS_IFSC + " TEXT," +
                Database.ACCOUNTS_MICR + " TEXT," +
                Database.ACCOUNTS_BALANCE + " FLOAT," +
                Database.ACCOUNTS_LASTTRANS + " TEXT," +
                Database.ACCOUNTS_REMARKS + " TEXT)";


        String transactions_table_sql = "create table " + Database.TRANSACTIONS_TABLE_NAME + " ( " +
                Database.TRANSACTIONS_ID 	+ " integer  primary key autoincrement," +
                Database.TRANSACTIONS_ACCOUNT_ID + " TEXT," +
                Database.TRANSACTIONS_TRANSDATE + " TEXT," +
                Database.TRANSACTIONS_TRANSAMOUNT + " FLOAT," +
                Database.TRANSACTIONS_TRANSTYPE+ " TEXT," +
                Database.TRANSACTIONS_CHEQUE_NO + " TEXT," +
                Database.TRANSACTIONS_CHEQUE_PARTY + " TEXT," +
                Database.TRANSACTIONS_CHEQUE_DETAILS+ " TEXT," +
                Database.TRANSACTIONS_REMARKS + " TEXT)";

        try {
            database.execSQL(account_table_sql);
            database.execSQL(transactions_table_sql);
            Log.d("Accounts","Tables created!");

        }
        catch(Exception ex) {
            Log.d("Accounts", "Error in DBHelper.onCreate() : " + ex.getMessage());
        }
    }

}
