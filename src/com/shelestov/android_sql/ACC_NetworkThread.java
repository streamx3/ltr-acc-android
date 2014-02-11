package com.shelestov.android_sql;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.concurrent.Semaphore;

import static java.lang.System.out;

/**
 * Created by Andrew Shelestov on 2/11/14.
 */

public class ACC_NetworkThread {
    private Semaphore sem = new Semaphore( 1, true );

    private Boolean need_push = false;
    private Boolean need_pull = false;
    private Boolean stop_thread = false;

    private LinkedList<ACC_TableRow> queue = new LinkedList<ACC_TableRow>(); //to process
    private LinkedList<ACC_TableRow> shot = new LinkedList<ACC_TableRow>();  //temporal data for data container

    private String CONNECTION_URL = "jdbc:mysql://192.168.1.132/test";
    private String user = "test";
    private String pass = "test";
    private java.sql.Statement stmt;
    private java.sql.Connection conn;

    private Thread nw_thread = new Thread() {
        public void run(){
            out.println( "lol1" );
            try{
                Class.forName( "com.mysql.jdbc.Driver" );
            }catch( Exception E ){
                out.println( "Big lol" );
            }

            try {
                conn = DriverManager.getConnection(CONNECTION_URL, user, pass);
            }catch( java.sql.SQLException e1 ){
                e1.printStackTrace();
                out.println( "Lol failed to get connection;" );
            }finally{
                try {
                    stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery( "SELECT id, name FROM books" );
                    String id, name;
                    while (rs.next()){
                        id = String.valueOf( rs.getInt( 1 ) );
                        name = rs.getString("name");
                        out.println( "ololo " + id + " " + name );
                    }
                    rs.close();
                    stmt.close();
                    conn.close();
                }catch( java.sql.SQLException e ){
                    e.printStackTrace();
                }
            }
            out.println( "lol1" );

            while( true ){
                try{
                    sem.acquire();
                    if( stop_thread ){
                        break;
                    }
                    if( need_push ){
                        // TODO implement pushing to mysql
                    }
                    if( need_pull ){
                        //  TODO implement pulling from mysql to ACC_DataContainer
                    }
                    sem.release();
                }catch( InterruptedException e ){
                    e.printStackTrace();
                }
                try{
                    Thread.sleep( 1000 );
                }catch( InterruptedException v ){
                    System.out.println( v );
                }
            }
            System.out.print( "Android SQL leaving thread" );
            sem.release();
        }
    };

    public ACC_NetworkThread(){
        nw_thread.start();
    }

    public Boolean pushRecord( ACC_TableRow record ){
        try {
            sem.acquire();
            queue.addLast( record );
            need_push = true;
            sem.release();
        }catch( InterruptedException e ){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public Boolean askForPull(){
        try{
            sem.acquire();
            if( need_pull == false ){
                need_pull = true;
            }
            sem.release();
        }catch( InterruptedException e ){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public Boolean askThreadStop(){
        try{
            sem.acquire();
            stop_thread = true;
            sem.release();
        }catch( InterruptedException e ){
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
