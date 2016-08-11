package com.example.dell.bankaccount;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
@SuppressWarnings("deprecation")
public class UpdateAccount extends Activity {
    private String accountId;
    private EditText editAcno, editCno, editHolders, editBankName,
            editBranchName, editAddress, editIFSC, editMICR, editBalance,
            editRemarks;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_account);
        editAcno = (EditText) this.findViewById(R.id.editAcno);
        editCno = (EditText) this.findViewById(R.id.editCno);
        editHolders = (EditText) this.findViewById(R.id.editHolders);
        editBankName = (EditText) this.findViewById(R.id.editBankName);
        editBranchName = (EditText) this.findViewById(R.id.editBranchName);
        editAddress = (EditText) this.findViewById(R.id.editAddress);
        editIFSC = (EditText) this.findViewById(R.id.editIFSC);
        editMICR = (EditText) this.findViewById(R.id.editMICR);
        editBalance = (EditText) this.findViewById(R.id.editBalance);
        editRemarks = (EditText) this.findViewById(R.id.editRemarks);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return Utils.inflateMenu(this,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return  Utils.handleMenuOption(this,item);
    }



    @Override
    public void onStart() {
        super.onStart();
        accountId = this.getIntent().getStringExtra("accountid");
        Log.d("Accounts", "Account Id : " + accountId);
        DBHelper dbhelper = new DBHelper(this);
        SQLiteDatabase db = dbhelper.getReadableDatabase();
        Cursor account = db.query(Database.ACCOUNTS_TABLE_NAME, null,
                " _id = ?", new String[] { accountId }, null, null, null);
        //startManagingCursor(accounts);
        if (account.moveToFirst()) {
            // update view
            editAcno.setText(account.getString(account
                    .getColumnIndex(Database.ACCOUNTS_ACNO)));
            editCno.setText(account.getString(account
                    .getColumnIndex(Database.ACCOUNTS_CNO)));
            editHolders.setText(account.getString(account
                    .getColumnIndex(Database.ACCOUNTS_HOLDERS)));
            editBankName.setText(account.getString(account
                    .getColumnIndex(Database.ACCOUNTS_BANK)));
            editBranchName.setText(account.getString(account
                    .getColumnIndex(Database.ACCOUNTS_BRANCH)));
            editAddress.setText(account.getString(account
                    .getColumnIndex(Database.ACCOUNTS_ADDRESS)));
            editIFSC.setText(account.getString(account
                    .getColumnIndex(Database.ACCOUNTS_IFSC)));
            editMICR.setText(account.getString(account
                    .getColumnIndex(Database.ACCOUNTS_MICR)));
            editBalance.setText(account.getString(account
                    .getColumnIndex(Database.ACCOUNTS_BALANCE)));
            editRemarks.setText(account.getString(account
                    .getColumnIndex(Database.ACCOUNTS_REMARKS)));
        }
        account.close();
        db.close();
        dbhelper.close();

    }

    public void updateAccount(View v) {
        try {
            DBHelper dbhelper = new DBHelper(this);
            SQLiteDatabase db = dbhelper.getWritableDatabase();
            // execute insert command
            ContentValues values = new ContentValues();
            values.put(Database.ACCOUNTS_ACNO, editAcno.getText().toString());
            values.put(Database.ACCOUNTS_CNO, editCno.getText().toString());
            values.put(Database.ACCOUNTS_HOLDERS, editHolders.getText()
                    .toString());
            values.put(Database.ACCOUNTS_BANK, editBankName.getText()
                    .toString());
            values.put(Database.ACCOUNTS_BRANCH, editBranchName.getText()
                    .toString());
            values.put(Database.ACCOUNTS_ADDRESS, editAddress.getText()
                    .toString());
            values.put(Database.ACCOUNTS_IFSC, editIFSC.getText().toString());
            values.put(Database.ACCOUNTS_MICR, editMICR.getText().toString());
            values.put(Database.ACCOUNTS_BALANCE, editBalance.getText()
                    .toString());
            values.put(Database.ACCOUNTS_REMARKS, editRemarks.getText()
                    .toString());

            long rows = db.update(Database.ACCOUNTS_TABLE_NAME, values,
                    "_id = ?", new String[] { accountId });

            db.close();
            if (rows > 0)
                Toast.makeText(this, "Updated Account Successfully!",
                        Toast.LENGTH_LONG).show();
            else
                Toast.makeText(this, "Sorry! Could not update account!",
                        Toast.LENGTH_LONG).show();
        } catch (Exception ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void deleteAccount(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to delete this account?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        deleteCurrentAccount();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }



    public void deleteCurrentAccount() {
        try {
            DBHelper dbhelper = new DBHelper(this);
            SQLiteDatabase db = dbhelper.getWritableDatabase();
            int rows = db.delete(Database.ACCOUNTS_TABLE_NAME, "_id=?", new String[] { accountId});
            dbhelper.close();
            if ( rows == 1) {
                Toast.makeText(this, "Account Deleted Successfully!", Toast.LENGTH_LONG).show();
                this.finish();
            }
            else
                Toast.makeText(this, "Could not delet account!", Toast.LENGTH_LONG).show();

        } catch (Exception ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }

    }

    public void listAccountTransactions(View v) {
        Intent intent = new Intent(this,ListAccountTransactions.class);
        intent.putExtra("accountid", accountId);
        startActivity(intent);
    }
}
