package com.shelestov.android_sql;

import android.app.Activity;
import android.os.Bundle;

import java.sql.DriverManager;
import java.sql.ResultSet;

import static java.lang.System.out;


public class MyActivity extends Activity {
    private String CONNECTION_URL="jdbc:mysql://192.168.1.132/test";
    private String user="test";
    private String pass="test";
    private java.sql.Statement stmt;
    private java.sql.Connection conn;
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);




        out.println("lol1");
        try{
            Class.forName("com.mysql.jdbc.Driver");
        }catch(Exception E){
            out.println("Big lol");
        }


        try {
            conn = DriverManager.getConnection( CONNECTION_URL, user, pass );
        }catch( java.sql.SQLException e1 ){
            e1.printStackTrace();
            out.println("Lol failed to get connection;");
        }finally{
            try {
                stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT id FROM books");
                String entry;
                while (rs.next()){
                    entry = rs.getString(1);
                }
                rs.close();
                stmt.close();
                conn.close();
            }catch( java.sql.SQLException e ){
                e.printStackTrace();
            }
        }
        out.println("lol1");
    }
}
