package com.example.dell.bankaccount;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;
@SuppressWarnings("deprecation")
public class MainActivity extends Activity {
    ListView listAccounts;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listAccounts = (ListView) this.findViewById(R.id.listAccounts);
        listAccounts.setOnItemClickListener( new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View selectedView, int arg2,long arg3) {
                TextView  textAccountId = (TextView) selectedView.findViewById(R.id.textAccountId);
                Log.d("Accounts", "Selected Account Id : " + textAccountId.getText().toString());
                Intent intent = new Intent(MainActivity.this, UpdateAccount.class);
                intent.putExtra("accountid", textAccountId.getText().toString());
                startActivity(intent);
            }
        });
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
        try {
            DBHelper dbhelper = new DBHelper(this);
            SQLiteDatabase db = dbhelper.getReadableDatabase();
            Cursor  accounts = db.query( Database.ACCOUNTS_TABLE_NAME,null,null,null,null,null,null);

            String from [] = { Database.ACCOUNTS_ID, Database.ACCOUNTS_BANK, Database.ACCOUNTS_HOLDERS, Database.ACCOUNTS_BALANCE };
            int to [] = { R.id.textAccountId,R.id.textBank, R.id.textHolder, R.id.textBalance};

            SimpleCursorAdapter ca  = new SimpleCursorAdapter(this, R.layout.account, accounts,from,to);

            ListView listAccounts = (ListView) this.findViewById( R.id.listAccounts);
            listAccounts.setAdapter(ca);
            dbhelper.close();
        } catch (Exception ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void addAccount(View v)
    {
        Intent intent = new Intent(this,AddAccount.class);
        startActivity(intent);
    }

    public void addTransaction(View v)
    {
        Intent intent = new Intent(this,AddTransaction.class);
        startActivity(intent);
    }


    public void recentTransactions(View v)
    {
        Intent intent = new Intent(this,ListRecentTransactions.class);
        startActivity(intent);
    }
}

