package com.shelestov.android_sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by Andrew Shelestov on 5/13/14.
 */
public class ACC_Settings extends SQLiteOpenHelper {
    private class login_info{
        public static final String name_id          = "id";
        public static final String name_ip          = "ip";
        public static final String name_port        = "port";
        public static final String name_user        = "user";
        public static final String name_database    = "database";
        public static final String name_table       = "table";
        public static final String name_password    = "password";

        public Integer id;
        public String ip;
        public String port;
        public String user;
        public String database;
        public String table;
        public String password;

        login_info(){
            id          = 0;
            ip          = "127.0.0.1";
            port        = "3306";
            user        = "root";
            database    = "database";
            table       = "table";
            password    = "root";
        }

        public String toString(){
            return  name_id +       " = " + id + ";" +
                    name_ip +       " = " + ip + ";" +
                    name_port +     " = " + port + ";" +
                    name_user +     " = " + user + ";" +
                    name_database + " = " + database + ";" +
                    name_table +    " = " + table + ";" +
                    name_password + " = " + password + ";";
        }
    }

    private ArrayList<login_info> SAVED_CONNECTIONS;

    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "isildur_accounting";
    // Login table name
    private static final String DATABASE_LOGIN_TABLE_NAME = "isildur_accounting_logins";
    // Assure table exists
    private static final String STATEMENT_CREATE_LOGIN_TABLE =
            "CREATE TABLE IF NOT EXISTS " +
            DATABASE_LOGIN_TABLE_NAME   + " ( " +
            login_info.name_id          + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            login_info.name_ip          + " TEXT, " +
            login_info.name_port        + " TEXT, " +
            login_info.name_user        + " TEXT, " +
            login_info.name_database    + " TEXT, " +
            login_info.name_table       + " TEXT, " +
            login_info.name_password    + " TEXT )" ;

    private static final String STATEMENT_GET_TABLE_LENGTH = "select count(" + login_info.name_id + ") from dbses;" ;

    public ACC_Settings( Context context ){
        super( context, DATABASE_NAME, null, DATABASE_VERSION );
    }

    @Override
    public void onCreate( SQLiteDatabase db ){
        db.execSQL( STATEMENT_CREATE_LOGIN_TABLE );
    }

    public void readHistory(){
        String[] COLUMNS = {
                login_info.name_id,
                login_info.name_ip,
                login_info.name_port,
                login_info.name_user,
                login_info.name_database,
                login_info.name_table,
                login_info.name_password };

        SQLiteDatabase tmp_db_r = this.getReadableDatabase();
        if (tmp_db_r != null) {
            Cursor cursor = tmp_db_r.query(
                    DATABASE_LOGIN_TABLE_NAME,
                    COLUMNS,
                    " id = ?",
                    new String[] { login_info.name_id },
                    null,
                    null,
                    null,
                    null );
            if( cursor != null ){
                cursor.moveToFirst();
                login_info li1 = new login_info();
                li1.id = cursor.getInt(0);
                li1.ip = cursor.getString(1);
                li1.port = cursor.getString(2);
                li1.user = cursor.getString(3);
                li1.database = cursor.getString(4);
                li1.table = cursor.getString(5);
                li1.password = cursor.getString(6);
                System.out.println( "[bmagic] Read login info: " + li1.toString() );
                SAVED_CONNECTIONS.add( li1 );
            }
        }
    }

    private void accset_valbind( ContentValues values, String ip, String port, String user,
                                 String database, String table, String password ){
        values.put( login_info.name_ip       , ip       );
        values.put( login_info.name_port     , port     );
        values.put( login_info.name_user     , user     );
        values.put( login_info.name_database , database );
        values.put( login_info.name_table    , table    );
        values.put( login_info.name_password , password );
    }

    public void AddLogin( String ip, String port, String user,
                          String database, String table, String password ){
        SQLiteDatabase tmp_db_w = this.getWritableDatabase();
        if( tmp_db_w != null ){
            ContentValues values = new ContentValues();
            accset_valbind(values, ip, port, user, database, table, password);

            tmp_db_w.insert( DATABASE_LOGIN_TABLE_NAME, null, values );

            tmp_db_w.close();
        }
    }

    public void UpdateLogin( Integer id, String ip, String port, String user,
                          String database, String table, String password ){
        SQLiteDatabase tmp_db_w = this.getWritableDatabase();
        if( tmp_db_w != null ){
            ContentValues values = new ContentValues();

            accset_valbind(values, ip, port, user, database, table, password);

            int i = tmp_db_w.update(
                    DATABASE_LOGIN_TABLE_NAME,
                    values,
                    login_info.name_id + " = ?",
                    new String[] { String.valueOf( id ) }
            );

            tmp_db_w.close();
        }
    }

    public void DeleteLogin( Integer id ){
        SQLiteDatabase tmp_db_w = this.getWritableDatabase();

        tmp_db_w.delete( DATABASE_LOGIN_TABLE_NAME, login_info.name_id + " = ?", new String[]{ String.valueOf( id ) } );

        tmp_db_w.close();
    }

    @Override
    public void onUpgrade( SQLiteDatabase db, int oldVersion, int newVersion ){
        // Drop older books table if existed
        if( oldVersion != newVersion ){
            db.execSQL( "DROP TABLE IF EXISTS books" );
        }

        // create fresh books table
        this.onCreate( db );
    }
}
