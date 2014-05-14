package com.shelestov.android_sql;

/**
 * Created by s13 on 5/14/14.
 */
public class ACC_LInfo {
    public static final String name_id          = "id";
    public static final String name_ip          = "ip";
    public static final String name_port        = "port";
    public static final String name_user        = "user";
    public static final String name_database    = "database";
    public static final String name_table       = "table_name";
    public static final String name_password    = "password";

    public Integer id;
    public String ip;
    public String port;
    public String user;
    public String database;
    public String table;
    public String password;

    ACC_LInfo(){
        id          = 0;
        ip          = "";
        port        = "3306";
        user        = "";
        database    = "";
        table       = "";
        password    = "";
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
