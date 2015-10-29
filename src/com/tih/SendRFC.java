package com.tih;

import com.sap.conn.jco.*;
import com.sap.conn.jco.ext.DestinationDataProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.Vector;

public class SendRFC {

    public static void main(String[] args) {
        //SendRFC singleton = new SendRFC("/H/sr.icms.it/H/213.174.164.43/W/V3n3tr0n1cPWD!/H/10.19.11.11", "00", "100", "rfcav", "ATEX2014", "it", "262", "1");
        SendRFC singleton = new SendRFC(args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7]);

        try {
            String out = singleton.sendIdoc(DBUtility.readD101(singleton.doc_id, singleton.child));
            System.out.println(out);
        }catch(Exception e){
            e.printStackTrace();

        }

        /*
        try {
            Vector odp = singleton.prodOrd_getList("1000");
            System.out.println(odp.get(0).toString());
        }catch(Exception e){
            e.printStackTrace();
        }
        */
    }

    public String doc_id = "";
    public String child = "";

    static String DESTINATION_NAME1 = "ABAP_AS_WITHOUT_POOL";
    static String DESTINATION_NAME2 = "ABAP_AS_WITH_POOL";

    static Properties connectProperties = new Properties();

    public SendRFC(String host, String sysnr, String client, String user, String pwd, String lang, String docId, String childId){
        connectProperties.setProperty(DestinationDataProvider.JCO_ASHOST, host);
        connectProperties.setProperty(DestinationDataProvider.JCO_SYSNR, sysnr);
        connectProperties.setProperty(DestinationDataProvider.JCO_CLIENT, client);
        connectProperties.setProperty(DestinationDataProvider.JCO_USER, user);
        connectProperties.setProperty(DestinationDataProvider.JCO_PASSWD, pwd);
        connectProperties.setProperty(DestinationDataProvider.JCO_LANG, lang);

        createDestinationDataFile(DESTINATION_NAME1, connectProperties);
        connectProperties.setProperty(DestinationDataProvider.JCO_POOL_CAPACITY, "3");
        connectProperties.setProperty(DestinationDataProvider.JCO_PEAK_LIMIT, "10");
        createDestinationDataFile(DESTINATION_NAME2, connectProperties);

        doc_id = docId;
        child = childId;
    }


    static void createDestinationDataFile(String destinationName, Properties connectProperties) {
            File destCfg = new File(destinationName+".jcoDestination");
            try
            {
                try (FileOutputStream fos = new FileOutputStream(destCfg, false)) {
                    connectProperties.store(fos, "for tests only !");
                }
            }
            catch (IOException e)
            {
                throw new RuntimeException("Unable to create the destination files", e);
            }
        }

    public Vector prodOrd_getList(String division) throws Exception {

        Vector result = new Vector();

        JCoDestination destination = JCoDestinationManager.getDestination(DESTINATION_NAME2);

        JCoTable bapiResultTable = null;

        try {

            JCoFunction func=destination.getRepository().getFunction("BAPI_PRODORD_GET_LIST");

            if (func != null) {
                JCoTable importParameterList = func.getTableParameterList().getTable("ORDER_NUMBER_RANGE");
                importParameterList.appendRow();
                importParameterList.setValue("SIGN", "I");
                importParameterList.setValue("OPTION", "EQ");
                importParameterList.setValue("LOW", "000000000000");
                importParameterList.setValue("HIGH", "999999999999");
            }

            // eseguo la function...
            func.execute(destination);
            //System.out.println(func);


            //verifica dei valori e di eventuali problemi
            JCoStructure returnStructure = func.getExportParameterList().getStructure("RETURN");

            if (!(returnStructure.getInt("NUMBER")!=000)){

                //recupero i dati restituiti dalla chiamata alla bapi e popolo la struttura che dovrò restituire
                bapiResultTable = func.getTableParameterList().getTable("ORDER_HEADER");
                result.add(0, bapiResultTable);
                //Hashtable oneTableRow = null;

            } else {
                result.add(0, "Errore nell'esecuzione della bapi");
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public String sendIdoc(Vector idoc) throws Exception {

        String idocNumber = "";

        JCoDestination destination = JCoDestinationManager.getDestination(DESTINATION_NAME2);

        try {

            JCoFunction func=destination.getRepository().getFunction("IDOC_INBOUND_SINGLE");

            if (func != null) {

                edi_dc40.clear();
                edi_dc40.split(idoc.get(0).toString());

                JCoStructure importParameterDC40 = func.getImportParameterList().getStructure("PI_IDOC_CONTROL_REC_40");
                importParameterDC40.setValue("TABNAM", edi_dc40.tabnam.trim());
                importParameterDC40.setValue("MANDT", edi_dc40.mandt.trim());
                importParameterDC40.setValue("DOCREL", edi_dc40.docrel.trim());
                importParameterDC40.setValue("IDOCTYP", edi_dc40.idoctyp.trim());
                importParameterDC40.setValue("CIMTYP", edi_dc40.cimtyp.trim());
                importParameterDC40.setValue("MESCOD", edi_dc40.mescod.trim());
                importParameterDC40.setValue("MESTYP", edi_dc40.mestyp.trim());
                importParameterDC40.setValue("STDVRS", edi_dc40.stdvrs.trim());
                importParameterDC40.setValue("STDMES", edi_dc40.stdmes.trim());
                importParameterDC40.setValue("SNDPOR", edi_dc40.sndpor.trim());
                importParameterDC40.setValue("SNDPRT", edi_dc40.sndprt.trim());
                importParameterDC40.setValue("SNDPFC", edi_dc40.sndpfc.trim());
                importParameterDC40.setValue("SNDPRN", edi_dc40.sndprn.trim());
                importParameterDC40.setValue("SNDSAD", edi_dc40.sndsad.trim());
                importParameterDC40.setValue("SNDLAD", edi_dc40.sndlad.trim());
                importParameterDC40.setValue("RCVPOR", edi_dc40.rcvpor.trim());
                importParameterDC40.setValue("RCVPRT", edi_dc40.rcvprt.trim());
                importParameterDC40.setValue("RCVPFC", edi_dc40.rcvpfc.trim());
                importParameterDC40.setValue("RCVPRN", edi_dc40.rcvprn.trim());
                importParameterDC40.setValue("RCVSAD", edi_dc40.rcvsad.trim());
                importParameterDC40.setValue("RCVLAD", edi_dc40.rcvlad.trim());
//                importParameterDC40.setValue("CREDAT", edi_dc40.credat);
//                importParameterDC40.setValue("CRETIM", edi_dc40.cretim);


                JCoTable importParameterList = func.getTableParameterList().getTable("PT_IDOC_DATA_RECORDS_40");
                for(int i = 1; i < idoc.size(); i++) {
                    importParameterList.appendRow();
                    importParameterList.setValue("SEGNAM", idoc.get(i).toString().substring(0, 30));
                    importParameterList.setValue("SDATA", idoc.get(i).toString().substring(63));
                }

            }

            // eseguo la function...
            func.execute(destination);
            //System.out.println(func);


            //verifica dei valori e di eventuali problemi
            idocNumber = func.getExportParameterList().getString("PE_IDOC_NUMBER");



        } catch (Exception e) {
            e.printStackTrace();
        }


        return idocNumber;
    }

}
