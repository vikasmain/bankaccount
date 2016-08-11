package com.example.dell.bankaccount;

import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
@SuppressWarnings("deprecation")
public class Utils {

    public static boolean  inflateMenu(Activity activity, Menu menu) {
        MenuInflater inflater = activity.getMenuInflater();
        inflater.inflate( R.menu.common_menu, menu);
        return true;
    }

    public static boolean handleMenuOption(Activity activity, MenuItem item) {
        Intent intent;
        switch(item.getItemId()) {
            case R.id.optAddAccount :
                intent = new Intent(activity,AddAccount.class);
                activity.startActivity(intent);
                break;
            case R.id.optAddTransaction :
                intent = new Intent(activity,AddTransaction.class);
                activity.startActivity(intent);
                break;

            case R.id.optSearchTransactions :
                intent = new Intent(activity,SearchTransactions.class);
                activity.startActivity(intent);
                break;
            case R.id.optListAccounts :
                intent = new Intent(activity,MainActivity.class);
                activity.startActivity(intent);
                break;

            case R.id.optRecentTransactions :
                intent = new Intent(activity,ListRecentTransactions.class);
                activity.startActivity(intent);
                break;
        }
        return true;
    }

}

