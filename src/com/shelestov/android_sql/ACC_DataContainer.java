package com.shelestov.android_sql;

import java.util.*;
import java.util.concurrent.Semaphore;

/**
 * Created by Andrew Shelestov on 2/11/14.
 */
public class ACC_DataContainer {
    private static ACC_DataContainer instance;

    private static Semaphore sem = new Semaphore( 1, true );
    private LinkedList<ACC_TableRow> acc_table = new LinkedList<ACC_TableRow>();


    public static synchronized ACC_DataContainer getInstance() {
        if( instance == null ){
            instance = new ACC_DataContainer();
        }
        return instance;
    }

    public LinkedList<ACC_TableRow> get_acc_table(){
        LinkedList<ACC_TableRow> acc_table_copy;
        try{
            sem.acquire();
            acc_table_copy = new LinkedList<ACC_TableRow>( acc_table );
            sem.release();
        }catch( InterruptedException e ){
            e.printStackTrace();
            acc_table_copy = new LinkedList<ACC_TableRow>();
        }
        return acc_table_copy;
    }

    public Boolean set_acc_table(LinkedList<ACC_TableRow> inc_table){
        try{
            sem.acquire();
            acc_table = inc_table;
            System.gc();
            sem.release();
        }catch( InterruptedException e ){
            e.printStackTrace();
            return false;
        }
        return true;
    }
}