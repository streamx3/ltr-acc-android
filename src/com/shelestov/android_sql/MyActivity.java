package com.shelestov.android_sql;

import android.app.Activity;
import android.os.Bundle;

import java.sql.DriverManager;
import java.sql.ResultSet;

import static java.lang.System.out;
import android.view.View;
import android.widget.*;


public class MyActivity extends Activity {
    ACC_NetworkThread nwt1;
    ACC_Settings acc_set1;

//    String[] data = { "one", "two", "three", "four", "five" };
    private CheckBox chbox_save;
    private EditText editText_ip;
    private EditText editText_port;
    private EditText editText_user;
    private EditText editText_database;
    private EditText editText_table;
    private EditText editText_password;
    Boolean save_login = false;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        acc_set1 = new ACC_Settings( getApplicationContext() ); // ??? Not sure about this string
        acc_set1.readHistory();

        // Interface bind
        editText_ip         = (EditText) findViewById( R.id.editText_ip );
        editText_port       = (EditText) findViewById( R.id.editText_port );
        editText_user       = (EditText) findViewById( R.id.editText_user );
        editText_database   = (EditText) findViewById( R.id.editText_database );
        editText_table      = (EditText) findViewById( R.id.editText_table );
        editText_password   = (EditText) findViewById( R.id.editText_password );

//        nwt1 = new ACC_NetworkThread();
//        try{
//            Thread.sleep( 1, 0 );
//            nwt1.askThreadStop();
//        }catch( InterruptedException e ){
//            e.printStackTrace();
//        }

        // Account sellector
        // Uncomment after SQLite is finished

//        // adapter
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>( this, android.R.layout.simple_spinner_item, data );
//        adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
//
//        Spinner spinner = ( Spinner ) findViewById( R.id.spinner );
//        spinner.setAdapter( adapter );
//        // header
//        // not working
//        // spinner.setPrompt( "Choose!" );
//        // selected element
//        spinner.setSelection( 2 );
//        // set processing
//        spinner.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                // показываем позиция нажатого элемента
//                Toast.makeText( getBaseContext(), "Position = " + position, Toast.LENGTH_SHORT ).show();
//            }
//            @Override
//            public void onNothingSelected(AdapterView<?> arg0) {
//            }
//        } );
    }


    public void addListenerOnChkIos() {

        chbox_save = (CheckBox) findViewById(R.id.checkBox_save);

        chbox_save.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chbox_save checked?
                if (((CheckBox) v).isChecked()) {
                    save_login = true;
//                    Toast.makeText( MyActivity.this,
//                            "Bro, try Android :)", Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    public void onConnectClicked( View view ){
        System.out.println( "[bmagic] Connect start;" );
        if( save_login == true ){
//            acc_set1.AddLogin(  );
        }
        System.out.println( "[bmagic] Connect finish;" );
    }

    public void onExitClicked( View view ){
        System.out.println( "[bmagic] Exit button clicked;" );
        finish();
        System.exit(0);
    }
}
