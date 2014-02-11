package com.shelestov.android_sql;

import java.util.LinkedList;
import java.util.concurrent.Semaphore;

/**
 * Created by Andrew Shelestov on 2/11/14.
 */

public class ACC_NetworkThread {
    private Semaphore sem = new Semaphore( 1, true );
    private Boolean need_push;
    private Boolean need_pull;
    private LinkedList<ACC_TableRow> queue = new LinkedList<ACC_TableRow>(); //to process
    private LinkedList<ACC_TableRow> shot = new LinkedList<ACC_TableRow>();  //temporal data for data container

    public Boolean pushRecord( ACC_TableRow record ){
        try {
            sem.acquire();
            queue.addLast( record );
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

    public Boolean askForPush(){
        try{
            sem.acquire();
            if( need_push == false ){
                need_push = true;
            }
            sem.release();
        }catch( InterruptedException e ){
            e.printStackTrace();
            return false;
        }
        return true;
    }

}
