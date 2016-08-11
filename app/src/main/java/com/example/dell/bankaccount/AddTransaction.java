package com.example.dell.bankaccount;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
@SuppressWarnings("deprecation")
public class AddTransaction extends Activity {
    private Spinner spinnerAccounts;
    private TextView textTransDate;
    private int day, month, year;
    private final int DATE_DIALOG = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_transaction);
        spinnerAccounts = (Spinner) this.findViewById(R.id.spinnerAccounts);
        Database.populateAccounts(spinnerAccounts);
        textTransDate = (TextView) this.findViewById(R.id.textTransDate);
        // get the current date
        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        updateDateDisplay();
    }

    private DatePickerDialog.OnDateSetListener dateSetListener =
            new DatePickerDialog.OnDateSetListener() {

                public void onDateSet(DatePicker view, int pYear,int pMonth, int pDay) {
                    year = pYear;
                    month = pMonth;
                    day = pDay;
                    updateDateDisplay();
                }
            };


    @Override
    public void onStart() {
        super.onStart();

    }

    public void showDateDialog(View v) {
        showDialog(DATE_DIALOG);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        super.onCreateDialog(id);

        switch (id) {
            case DATE_DIALOG:
                return new DatePickerDialog(this,
                        dateSetListener, year, month, day);
        }
        return null;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return Utils.inflateMenu(this,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return  Utils.handleMenuOption(this,item);
    }

    private void updateDateDisplay() {
        // Month is 0 based so add 1
        textTransDate.setText( String.format("%d-%d-%d",year,month + 1,day));
    }

    public void addTransaction(View v) {
        // get access to views
        String accountId = Database.getAccountId(spinnerAccounts);
        RadioButton  radioDeposit = (RadioButton) this.findViewById(R.id.radioDeposit);

        EditText editTransAmount = (EditText) this.findViewById(R.id.editTransAmount);
        EditText editChequeNo = (EditText) this.findViewById(R.id.editChequeNo);
        EditText editChequeParty = (EditText) this.findViewById(R.id.editChequeParty);
        EditText editChequeDetails = (EditText) this.findViewById(R.id.editChequeDetails);
        EditText editRemarks = (EditText) this.findViewById(R.id.editRemarks);

        boolean done = Database.addTransaction(this,
                accountId,
                radioDeposit.isChecked() ? "d" : "w",   // trans type
                textTransDate.getText().toString(),
                editTransAmount.getText().toString(),
                editChequeNo.getText().toString(),
                editChequeParty.getText().toString(),
                editChequeDetails.getText().toString(),
                editRemarks.getText().toString());

        if ( done )
            Toast.makeText(this,"Added Transaction Successfully!", Toast.LENGTH_LONG).show();
        else
            Toast.makeText(this, "Sorry Could Not Add Transaction!", Toast.LENGTH_LONG).show();
    } // addDeposit


}
