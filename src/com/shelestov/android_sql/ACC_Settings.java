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

    private ArrayList<ACC_LInfo> SAVED_CONNECTIONS = new ArrayList<ACC_LInfo>();

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
            ACC_LInfo.name_id          + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            ACC_LInfo.name_ip          + " TEXT, " +
            ACC_LInfo.name_port        + " TEXT, " +
            ACC_LInfo.name_user        + " TEXT, " +
            ACC_LInfo.name_database    + " TEXT, " +
            ACC_LInfo.name_table       + " TEXT, " +
            ACC_LInfo.name_password    + " TEXT )" ;

    private static final String STATEMENT_GET_TABLE_LENGTH = "select count(" + ACC_LInfo.name_id + ") from dbses;" ;

    public ACC_Settings( Context context ){
        super( context, DATABASE_NAME, null, DATABASE_VERSION );
        System.out.println( "[bmagic] " + (new Throwable()).getStackTrace()[0].toString() + " <out>" );
    }

    @Override
    public void onCreate( SQLiteDatabase db ){
        System.out.println( "[bmagic] " + (new Throwable()).getStackTrace()[0].toString() + " <in>" );
        db.execSQL( STATEMENT_CREATE_LOGIN_TABLE );
        System.out.println( "[bmagic] " + (new Throwable()).getStackTrace()[0].toString() + " <out>" );
    }

    public void readHistory(){
        System.out.println( "[bmagic] " + (new Throwable()).getStackTrace()[0].toString() + " <in>" );
        Cursor cursor;
//        String[] COLUMNS = {
//                ACC_LInfo.name_id,
//                ACC_LInfo.name_ip,
//                ACC_LInfo.name_port,
//                ACC_LInfo.name_user,
//                ACC_LInfo.name_database,
//                ACC_LInfo.name_table,
//                ACC_LInfo.name_password };
        System.out.println( "[bmagic] " + (new Throwable()).getStackTrace()[0].toString() + " <in>" );
        SQLiteDatabase tmp_db_r = this.getReadableDatabase();
        if (tmp_db_r != null) {
            cursor= getReadableDatabase().rawQuery( "SELECT * FROM " +DATABASE_LOGIN_TABLE_NAME + ";", null );
//            Cursor cursor = tmp_db_r.query(
//                    DATABASE_LOGIN_TABLE_NAME,
//                    COLUMNS,
//                    " id = ?",
//                    new String[] { ACC_LInfo.name_id },
//                    null,
//                    null,
//                    null,
//                    null );
            if( cursor != null ){
                cursor.moveToFirst();
                for( int i = 0; i < cursor.getCount(); ++i ){
                    ACC_LInfo li1 = new ACC_LInfo();
                    System.out.println( "[bmagic] Cursor and logininfo created;" );
                    System.out.println( "[bmagic] li1.id = " + li1.id + "(default);" );
                    li1.id = cursor.getInt(0);
                    System.out.println( "[bmagic] li1.id = " + li1.id + "(changed);" );
                    li1.ip = cursor.getString(1);
                    li1.port = cursor.getString(2);
                    li1.user = cursor.getString(3);
                    li1.database = cursor.getString(4);
                    li1.table = cursor.getString(5);
                    li1.password = cursor.getString(6);
                    System.out.println( "[bmagic] Read login info: " + li1.toString() );
                    SAVED_CONNECTIONS.add( li1 );
                    cursor.moveToNext();
                }
            }else{
                System.out.print( "[bmagic] Cursor reading skipped; cursor = " +
                                  cursor == null ? "null" : "not_null" + "; " );
                if( cursor != null ){
                    System.out.print( "cursor.getCount() = " + cursor.getCount() );
                }
                System.out.print( "\n" );
            }
        }else{
            System.out.println( "[bmagic] Failed to create readable database;" );
        }
        System.out.println( "[bmagic] " + (new Throwable()).getStackTrace()[0].toString() + " <out>" );
    }

    private void accset_valbind( ContentValues values, String ip, String port, String user,
                                 String database, String table, String password ){
        System.out.println( "[bmagic] " + (new Throwable()).getStackTrace()[0].toString() + " <in>" );
        values.put( ACC_LInfo.name_ip       , ip       );
        values.put( ACC_LInfo.name_port     , port     );
        values.put( ACC_LInfo.name_user     , user     );
        values.put( ACC_LInfo.name_database , database );
        values.put( ACC_LInfo.name_table    , table    );
        values.put( ACC_LInfo.name_password , password );
        System.out.println( "[bmagic] " + (new Throwable()).getStackTrace()[0].toString() + " <out>" );
    }

    public void AddLogin( String ip, String port, String user,
                          String database, String table, String password ){
        System.out.println( "[bmagic] " + (new Throwable()).getStackTrace()[0].toString() + " <in>" );
        SQLiteDatabase tmp_db_w = this.getWritableDatabase();
        if( tmp_db_w != null ){
            System.out.println( "[bmagic] Readable database created;" );
            ContentValues values = new ContentValues();
            accset_valbind(values, ip, port, user, database, table, password);

            tmp_db_w.insert( DATABASE_LOGIN_TABLE_NAME, null, values );

            tmp_db_w.close();
        }
        System.out.println( "[bmagic] " + (new Throwable()).getStackTrace()[0].toString() + " <out>" );
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
                    ACC_LInfo.name_id + " = ?",
                    new String[] { String.valueOf( id ) }
            );

            tmp_db_w.close();
        }
    }

    public void DeleteLogin( Integer id ){
        SQLiteDatabase tmp_db_w = this.getWritableDatabase();

        tmp_db_w.delete( DATABASE_LOGIN_TABLE_NAME, ACC_LInfo.name_id + " = ?", new String[]{ String.valueOf( id ) } );

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

    ArrayList<ACC_LInfo> getLoginList(){
        return SAVED_CONNECTIONS;
    }
}
