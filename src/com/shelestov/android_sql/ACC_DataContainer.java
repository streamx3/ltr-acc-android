package com.shelestov.android_sql;

import java.util.concurrent.Semaphore;

/**
 * Created by s13 on 2/11/14.
 */
public class ACC_DataContainer {
    private static ACC_DataContainer instance;

    private static Semaphore sem = new Semaphore( 1, true );

    public static synchronized ACC_DataContainer getInstance() {
        if( instance == null ){
            instance = new ACC_DataContainer();
        }
        return instance;
    }


}