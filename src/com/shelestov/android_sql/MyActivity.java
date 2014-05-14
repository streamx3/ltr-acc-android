package com.shelestov.android_sql;

import android.app.Activity;
import android.content.ComponentName;
import android.os.Bundle;

import android.view.View;
import android.widget.*;

import java.util.ArrayList;


public class MyActivity extends Activity {
    ACC_NetworkThread       acc_nwt1;
    ACC_Settings            acc_set1;
    private ACC_LInfo               acc_current_login;
    private ArrayList<ACC_LInfo>    CONNECTIONS;

    String[] drop_down_logins;

    private CheckBox chbox_save;
    private EditText editText_ip;
    private EditText editText_port;
    private EditText editText_user;
    private EditText editText_database;
    private EditText editText_table;
    private EditText editText_password;
    private Spinner  spinner_login;
    private int spinner_position = 0;
    Boolean save_login = true;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        acc_set1 = new ACC_Settings( getApplicationContext() ); // ??? Not sure about this string

        // Interface bind
        editText_ip         = (EditText) findViewById( R.id.editText_ip );
        editText_port       = (EditText) findViewById( R.id.editText_port );
        editText_user       = (EditText) findViewById( R.id.editText_user );
        editText_database   = (EditText) findViewById( R.id.editText_database );
        editText_table      = (EditText) findViewById( R.id.editText_table );
        editText_password   = (EditText) findViewById( R.id.editText_password );

        spinner_login = ( Spinner ) findViewById( R.id.spinner_saved_login_select );

        refresh();
    }


    public void addListenerOnChkSave() {

        chbox_save = ( CheckBox ) findViewById( R.id.checkBox_save );

        chbox_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                //is chbox_save checked?
                if ( ( ( CheckBox ) v ).isChecked() ) {
                    save_login = true;
                    Toast.makeText( MyActivity.this,
                            "WARNING! Credentials will be pushed to local SQLite database after pressing \"Connect\"",
                            Toast.LENGTH_LONG).show();
                }else{
                    save_login = false;
                }
                System.out.println( "[bmagic] Save login = " + save_login + ";" );
            }
        });
    }

    public void onConnectClicked( View view ){
        System.out.println( "[bmagic] Connect start;" );
        if( save_login == true ){
            System.out.println( "[bmagic] Saving login" );
            if(spinner_position == CONNECTIONS.size() ){
                acc_set1.AddLogin(
                        editText_ip.getText().toString(),
                        editText_port.getText().toString(),
                        editText_user.getText().toString(),
                        editText_database.getText().toString(),
                        editText_table.getText().toString(),
                        editText_password.getText().toString()
                );
            }else{
                acc_set1.UpdateLogin(
                        CONNECTIONS.get( spinner_position ).id,
                        editText_ip.getText().toString(),
                        editText_port.getText().toString(),
                        editText_user.getText().toString(),
                        editText_database.getText().toString(),
                        editText_table.getText().toString(),
                        editText_password.getText().toString()
                );
            }

//            acc_nwt1 = new ACC_NetworkThread();
//            try{
//                Thread.sleep( 1, 0 );
//                acc_nwt1.askThreadStop();
//            }catch( InterruptedException e ){
//                e.printStackTrace();
//            }
        }else{
            System.out.println( "[bmagic] Not saving login" );
        }
        System.out.println( "[bmagic] Connect finish;" );
    }

    public void onExitClicked( View view ){
        System.out.println( "[bmagic] Exit button clicked;" );
        finish();
        System.exit(0);
    }

    // to make array of String-s for selection bar;
    public String[] LInfoList2StrArr( ArrayList<ACC_LInfo> list ){
        String []retval;
        retval = new String[ list.size() + 1 ];
        System.out.println( "[bmagic] list.size() = " + list.size() );
        for( Integer i = 0; i < list.size(); ++i ){
            retval[ i ] = list.get( i ).user + "@" + list.get( i ).ip + ":" + list.get( i ).port + "/" +
                        list.get( i ).database + "/" + list.get( i ).table;
        }
        retval [ list.size() ] = "New...";
        return retval;
    }

    public void setLoginbyId( Integer id ){
        if( id < CONNECTIONS.size() ){
            acc_current_login = CONNECTIONS.get( id );
            if( acc_current_login == null ){
                acc_current_login = new ACC_LInfo();
            }
        }else{
            acc_current_login = new ACC_LInfo();
        }

        editText_ip.setText(        acc_current_login.ip        );
        editText_port.setText(      acc_current_login.port      );
        editText_user.setText(      acc_current_login.user      );
        editText_database.setText(  acc_current_login.database  );
        editText_table.setText(     acc_current_login.table     );
        editText_password.setText(  acc_current_login.password  );
    }

    public void handleLoginSelection(){
        if( spinner_position == CONNECTIONS.size() ){   // "New..." option selected
            chbox_save.setText("Save");
            setLoginbyId( spinner_position );
        }else{ // Updating option selected
            chbox_save.setText("Update");
            setLoginbyId( spinner_position );
        }
    }

    public void handleDeleteButton( View view ){
        if( spinner_position < CONNECTIONS.size() - 1 ){
            acc_set1.DeleteLogin(CONNECTIONS.get(spinner_position).id);
        }
        refresh();
    }

    private void refresh(){
        acc_set1.readHistory();
        CONNECTIONS = acc_set1.getLoginList();
        drop_down_logins = LInfoList2StrArr(CONNECTIONS);

        // Account sellector

        // adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<String>( this, android.R.layout.simple_spinner_item,
                drop_down_logins);
        adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );

        spinner_login.setAdapter( adapter );
        // header
        spinner_login.setSelection( 0 );
        // set processing
        spinner_login.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected( AdapterView<?> parent, View view, int position, long id ) {
                // showing possition of pressed element
                spinner_position = position;
                // TODO Remove this toast after debug
                Toast.makeText( getBaseContext(), "Position = " + position, Toast.LENGTH_SHORT ).show();
                handleLoginSelection();
            }
            @Override
            public void onNothingSelected( AdapterView<?> arg0 ){}
        } );
        addListenerOnChkSave();
    }
}
