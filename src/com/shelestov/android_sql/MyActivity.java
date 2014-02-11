package com.shelestov.android_sql;

import android.app.Activity;
import android.os.Bundle;

import java.sql.DriverManager;
import java.sql.ResultSet;

import static java.lang.System.out;


public class MyActivity extends Activity {
    ACC_NetworkThread nwt1;
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        nwt1 = new ACC_NetworkThread();
        try{
            Thread.sleep( 1, 0 );
            nwt1.askThreadStop();
        }catch( InterruptedException e ){
            e.printStackTrace();
        }
    }
}
