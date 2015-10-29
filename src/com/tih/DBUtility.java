package com.tih;

import java.util.Vector;
import java.util.Iterator;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.*;

/**
 * @author AL - TIH
 */

class DBUtility  {
    // JDBC driver name and database URL
    static final String JDBC_DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    static final String DB_URL = "jdbc:sqlserver://vm4338.cloud.seeweb.it:1433;SelectMethod=cursor;DatabaseName=dEDIcated;sendStringParametersAsUnicode=false;integratedSecurity=false";

    //  Database credentials
    static final String USER = "sa";
    static final String PASS = "Prova22!";

    public static String direction = "";
    public static String sender = "";
    public static String MsgType = "";
    public static String DocType = "";
    public static String Version = "";
    public static String Cust_Type = "";
    public static String Cust_Code = "";
    public static String TP_Connection = "";
    public static String fileFormat = "";
    public static String filetype = "";
    public static String process_id = "";

    public static String envelope_sender = "";
    public static String envelope_receiver = "";

    public static Vector readD101(String doc_id, String child) { //files table
        Connection conn = null;
        Statement stmt = null;
        Vector output = new Vector();
        try{
            //Register JDBC driver
            Class.forName(JDBC_DRIVER);

            //Open the connection
            //System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);

            //Execute thequery
            //System.out.println("Creating statement...");
            stmt = conn.createStatement();
            String sql;
            sql = "SELECT * FROM D101 WHERE document_id = " + doc_id + " AND child =" + child;
            ResultSet rs = stmt.executeQuery(sql);

            //Extract data from result set
            while(rs.next()){
                //Retrieve by column name
                //int process_id  = rs.getInt("process_id");

                InputStream fin = rs.getBinaryStream("file_out");
                BufferedReader doc = new BufferedReader(new InputStreamReader(fin));

                //Display values
                while(true){
                    String line = doc.readLine();
                    if (line == null) {
                        break;
                    }else{
                        output.add(line);
                        //System.out.println(line);
                    }
                }


                //System.out.print(", process: " + process);
            }
            //Clean-up environment
            rs.close();
            stmt.close();
            conn.close();
        }catch(SQLException se){
            //Handle errors for JDBC
            se.printStackTrace();
            System.exit(1); //exit code 1 -> db error
        }catch(Exception e){
            //Handle errors for Class.forName
            e.printStackTrace();
        }finally{
            //finally block used to close resources
            try{
                if(stmt!=null)
                    stmt.close();
            }catch(SQLException se2){
            }// nothing we can do
            try{
                if(conn!=null)
                    conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }//end finally try
        }//end try
        //System.out.println("DB Disconnected");
        return output;
    }//end method writeFileD101

    public static void readD100(String doc_id) {
        Connection conn = null;
        Statement stmt = null;

        try{
            //Register JDBC driver
            Class.forName(JDBC_DRIVER);

            //Open the connection
            //System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);

            //Execute thequery
            //System.out.println("Creating statement...");
            stmt = conn.createStatement();
            String sql;
            sql = "SELECT * FROM D100 WHERE document_id = " + doc_id + " AND child = 0";
            ResultSet rs = stmt.executeQuery(sql);

            //Extract data from result set
            while(rs.next()){
                //Retrieve by column name
                direction  = rs.getString("direction");
                sender = rs.getString("sender");
                fileFormat = rs.getString("fileformat");
                filetype = rs.getString("filetype");
                //System.out.print(", process: " + process);
            }
            //Clean-up environment
            rs.close();
            stmt.close();
            conn.close();
        }catch(SQLException se){
            //Handle errors for JDBC
            se.printStackTrace();
        }catch(Exception e){
            //Handle errors for Class.forName
            e.printStackTrace();
            System.exit(1); //exit code 1 -> db error
        }finally{
            //finally block used to close resources
            try{
                if(stmt!=null)
                    stmt.close();
            }catch(SQLException se2){
            }// nothing we can do
            try{
                if(conn!=null)
                    conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }//end finally try
        }//end try
        //System.out.println("DB Disconnected");
    }//end method readD100

    public static void updateD100_process_id(String doc_id){ //
        Connection conn = null;
        Statement stmt = null;
        //update tab D100
        try{
            //Register JDBC driver
            Class.forName(JDBC_DRIVER);

            //Open the connection
            //System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);

            //Execute thequery
            //System.out.println("Creating statement...");
            stmt = conn.createStatement();
            String sql;
            sql = "UPDATE D100 SET process_id = '" + process_id + "' WHERE document_id = " + doc_id + " AND child = 0";

            stmt.executeUpdate(sql);

            //Extract data from result set
           /*while(rs.next()){
              //Retrieve by column name
              //int process_id  = rs.getInt("process_id");


           }*/
            //Clean-up environment
            stmt.close();
            conn.close();
        }catch(SQLException se){
            //Handle errors for JDBC
            se.printStackTrace();
            System.exit(1); //exit code 1 -> db error
        }catch(Exception e){
            //Handle errors for Class.forName
            e.printStackTrace();
        }finally{
            //finally block used to close resources
            try{
                if(stmt!=null)
                    stmt.close();
            }catch(SQLException se2){
            }// nothing we can do
            try{
                if(conn!=null)
                    conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }//end finally try
        }//end try
    }

    public static String space(int num) {
        String space = "";
        //stringa di spazi per concatenazioni
        for (int i = 0; i < num; i++) {
            space = space.concat(" ");
        }//endfor
        return space;
    }//endspace

    public static String zeros(int num) {
        String zero = "";
        //stringa di zeri per concatenazioni
        for (int i = 0; i < num; i++) {
            zero = zero.concat("0");
        }//endfor
        return zero;
    }//endzeros

    public static String fixedLenthString(String string, int length) {
        return String.format("%1$"+length+ "s", string);
    }//end fixedLenthString

}

class edi_dc40 {

    public static String
            tabnam,
            mandt,
            docnum,
            docrel,
            status,
            direct,
            outmod,
            exprss,
            test,
            idoctyp,
            cimtyp,
            mestyp,
            mescod,
            mesfct,
            std,
            stdvrs,
            stdmes,
            sndpor,
            sndprt,
            sndpfc,
            sndprn,
            sndsad,
            sndlad,
            rcvpor,
            rcvprt,
            rcvpfc,
            rcvprn,
            rcvsad,
            rcvlad,
            credat,
            cretim,
            refint,
            refgrp,
            refmes,
            arckey,
            serial;

    public static void clear() {
        tabnam = DBUtility.space(10);
        mandt = DBUtility.space(3);
        docnum = DBUtility.space(16);
        docrel = DBUtility.space(4);
        status = DBUtility.space(2);
        direct = DBUtility.space(1);
        outmod = DBUtility.space(1);
        exprss = DBUtility.space(1);
        test = DBUtility.space(1);
        idoctyp = DBUtility.space(30);
        cimtyp = DBUtility.space(30);
        mestyp = DBUtility.space(30);
        mescod = DBUtility.space(3);
        mesfct = DBUtility.space(3);
        std = DBUtility.space(1);
        stdvrs = DBUtility.space(6);
        stdmes = DBUtility.space(6);
        sndpor = DBUtility.space(10);
        sndprt = DBUtility.space(2);
        sndpfc = DBUtility.space(2);
        sndprn = DBUtility.space(10);
        sndsad = DBUtility.space(21);
        sndlad = DBUtility.space(70);
        rcvpor = DBUtility.space(10);
        rcvprt = DBUtility.space(2);
        rcvpfc = DBUtility.space(2);
        rcvprn = DBUtility.space(10);
        rcvsad = DBUtility.space(21);
        rcvlad = DBUtility.space(70);
        credat = DBUtility.space(8);
        cretim = DBUtility.space(6);
        refint = DBUtility.space(14);
        refgrp = DBUtility.space(14);
        refmes = DBUtility.space(14);
        arckey = DBUtility.space(70);
        serial = DBUtility.space(20);
    }//endclear

    public static String concatena() {
        return tabnam.concat(
                mandt).concat(
                docnum).concat(
                docrel).concat(
                status).concat(
                direct).concat(
                outmod).concat(
                exprss).concat(
                test).concat(
                idoctyp).concat(
                cimtyp).concat(
                mestyp).concat(
                mescod).concat(
                mesfct).concat(
                std).concat(
                stdvrs).concat(
                stdmes).concat(
                sndpor).concat(
                sndprt).concat(
                sndpfc).concat(
                sndprn).concat(
                sndsad).concat(
                sndlad).concat(
                rcvpor).concat(
                rcvprt).concat(
                rcvpfc).concat(
                rcvprn).concat(
                rcvsad).concat(
                rcvlad).concat(
                credat).concat(
                cretim).concat(
                refint).concat(
                refgrp).concat(
                refmes).concat(
                arckey).concat(
                serial);
    }//endconcatena

    public static void split(String line) {
        tabnam = line.substring(0, 10);
        mandt = line.substring(10, 13);
        docnum = line.substring(13, 29);
        docrel = line.substring(29, 33);
        status = line.substring(33, 35);
        direct = line.substring(35, 36);
        outmod = line.substring(36, 37);
        exprss = line.substring(37, 38);
        test = line.substring(38, 39);
        idoctyp = line.substring(39, 69);
        cimtyp = line.substring(69, 99);
        mestyp = line.substring(99, 129);
        mescod = line.substring(129, 132);
        mesfct = line.substring(132, 135);
        std = line.substring(135, 136);
        stdvrs = line.substring(136, 142);
        stdmes = line.substring(142, 148);
        sndpor = line.substring(148, 158);
        sndprt = line.substring(158, 160);
        sndpfc = line.substring(160, 162);
        sndprn = line.substring(162, 172);
        sndsad = line.substring(172, 193);
        sndlad = line.substring(193, 263);
        rcvpor = line.substring(263, 273);
        rcvprt = line.substring(273, 275);
        rcvpfc = line.substring(275, 277);
        rcvprn = line.substring(277, 287);
        rcvsad = line.substring(287, 308);
        rcvlad = line.substring(308, 378);
        credat = line.substring(378, 386);
        cretim = line.substring(386, 393);
        /*
        refint = line.substring(393, 407);
        refgrp = line.substring(407, 421);
        refmes = line.substring(421, 435);
        arckey = line.substring(435, 505);
        serial = line.substring(505, 525);
        */
    }//endsplit

}//end edi_dc40
