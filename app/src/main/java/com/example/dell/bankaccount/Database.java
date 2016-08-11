package com.example.dell.bankaccount;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;

import java.util.ArrayList;
@SuppressWarnings("deprecation")
public class Database {
    public static final String ACCOUNTS_TABLE_NAME = "accounts";
    public static final String ACCOUNTS_ID = "_id";
    public static final String ACCOUNTS_ACNO = "acno";
    public static final String ACCOUNTS_HOLDERS = "holders";
    public static final String ACCOUNTS_CNO = "customerno";
    public static final String ACCOUNTS_BANK = "bank";
    public static final String ACCOUNTS_BRANCH = "branch";
    public static final String ACCOUNTS_ADDRESS = "address";
    public static final String ACCOUNTS_IFSC = "ifsc";
    public static final String ACCOUNTS_MICR = "micr";
    public static final String ACCOUNTS_BALANCE = "balance";
    public static final String ACCOUNTS_LASTTRANS = "last_tran_date";
    public static final String ACCOUNTS_REMARKS  = "remarks";

    public static final String TRANSACTIONS_TABLE_NAME = "transactions";
    public static final String TRANSACTIONS_ID = "_id";
    public static final String TRANSACTIONS_ACCOUNT_ID = "account_id";
    public static final String TRANSACTIONS_TRANSDATE = "transdate";
    public static final String TRANSACTIONS_TRANSTYPE = "transtype";
    public static final String TRANSACTIONS_TRANSAMOUNT = "transamount";
    public static final String TRANSACTIONS_CHEQUE_NO = "cheque_no";
    public static final String TRANSACTIONS_CHEQUE_PARTY = "cheque_party";
    public static final String TRANSACTIONS_CHEQUE_DETAILS = "cheque_details";
    public static final String TRANSACTIONS_REMARKS  = "remarks";

    public static Account cursorToAccount(Cursor accounts) {
        Account account = new Account();
        account.setId( accounts.getString(accounts.getColumnIndex(Database.ACCOUNTS_ID)));
        account.setHolder(accounts.getString(accounts.getColumnIndex(Database.ACCOUNTS_HOLDERS)));
        account.setBank( accounts.getString(accounts.getColumnIndex(Database.ACCOUNTS_BANK)));
        return account;
    }

    public  static void populateAccounts(Spinner spinnerAccounts) {
        Context context = spinnerAccounts.getContext();
        DBHelper dbhelper = new DBHelper(context);
        SQLiteDatabase db = dbhelper.getReadableDatabase();
        Cursor accounts = db.query(Database.ACCOUNTS_TABLE_NAME, null, null,null, null, null, null);
        ArrayList<Account> list = new ArrayList<Account>();

        // adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        while (accounts.moveToNext()) {
            Account account =  Database.cursorToAccount(accounts);
            list.add(account);
        }
        accounts.close();
        db.close();
        dbhelper.close();

        ArrayAdapter<Account> adapter = new ArrayAdapter<Account>(context, android.R.layout.simple_spinner_item,list);
        spinnerAccounts.setAdapter(adapter);
    }

    public  static boolean updateAccountBalance(SQLiteDatabase db, String accountId, String transType, double amount, String transDate) {
        try {
            if ( transType.equals("d"))
                db.execSQL( " update " + Database.ACCOUNTS_TABLE_NAME + " set balance = balance + " + amount + " where " + Database.ACCOUNTS_ID + " = " + accountId);
            else
                db.execSQL( " update " + Database.ACCOUNTS_TABLE_NAME + " set balance = balance - " + amount + " where " + Database.ACCOUNTS_ID + " = " + accountId);
            return true;
        }
        catch(Exception ex) {
            Log.d("Accounts", "Error in UpdateBalance : " + ex.getMessage());
            return false;
        }
    }

    public static String getAccountId(Spinner spinnerAccounts) {
        Account account = (Account) spinnerAccounts.getSelectedItem();
        return account.getId();
    }

    public static String getDateFromDatePicker(DatePicker dp) {
        return dp.getYear() + "-" + dp.getMonth() + 1 +  "-" + dp.getDayOfMonth();
    }

    public static boolean addTransaction(Context context, String accountId, String transType, String transDate, String transAmount, String chequeNo, String chequeParty,
                                         String chequeDetails, String remarks) {

        DBHelper dbhelper = null;
        SQLiteDatabase db = null;
        try {
            dbhelper = new DBHelper(context);
            db = dbhelper.getWritableDatabase();
            db.beginTransaction();

            // execute insert command
            ContentValues values = new ContentValues();
            values.put(Database.TRANSACTIONS_ACCOUNT_ID, accountId);
            values.put(Database.TRANSACTIONS_TRANSDATE, transDate);
            values.put(Database.TRANSACTIONS_TRANSAMOUNT, transAmount);
            values.put(Database.TRANSACTIONS_CHEQUE_NO, chequeNo);
            values.put(Database.TRANSACTIONS_CHEQUE_PARTY, chequeParty);
            values.put(Database.TRANSACTIONS_CHEQUE_DETAILS,chequeDetails);
            values.put(Database.TRANSACTIONS_REMARKS, remarks);
            values.put(Database.TRANSACTIONS_TRANSTYPE, transType);

            long rowid = db.insert(Database.TRANSACTIONS_TABLE_NAME, null, values);
            Log.d("Accounts","Inserted into TRANSACTIONS " + rowid);
            if ( rowid != -1) {
                // update Accounts Table
                boolean done = Database.updateAccountBalance(db,accountId,transType, Double.parseDouble(transAmount),transDate);
                Log.d("Accounts","Updated Account Balance");
                if ( done ) {
                    db.setTransactionSuccessful();
                    db.endTransaction();
                    return true;
                }
                else {
                    db.endTransaction();
                    return false;
                }
            }
            else
                return false;
        }
        catch(Exception ex) {
            Log.d("Account", "Error in addTransaction -->" + ex.getMessage());
            return false;
        }
        finally {
            if ( db != null && db.isOpen()) {
                db.close();
            }
        }
    } // addTransaction

}
