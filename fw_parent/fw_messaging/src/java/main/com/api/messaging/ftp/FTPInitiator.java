package com.api.messaging.ftp;

import java.util.Hashtable;

public class FTPInitiator extends Thread {
    public FTPInitiator() {
        return;
    }

    public String sendFTP(String xmldata, Hashtable dbdetails) {

        int numofRet = 0;
        boolean succ = false;
        FTPHelper ftpProc = new FTPHelper();

        try {
            for (int i = 0; i < 3; i++) {

                try {

                    ftpProc.ParseandFTP(xmldata, dbdetails);
                    succ = true;
                } catch (Exception ee) {
                    if (ee.getMessage().equalsIgnoreCase("Archive Exception")) {
                        succ = true;
                        System.out
                                .println("Exception occured ---Please create Archive Directory");
                    }
                    else {

                        System.out.println("Exception occured while ftping");
                    }

                }

                if (succ) {
                    break;
                }
                this.sleep(10000);
            }
        } catch (Exception ee1) {

            System.out.println(ee1.getMessage());
        }

        if (succ) {
            return "Success";
        }
        else {

            return "Failed";
        }

    }

}
