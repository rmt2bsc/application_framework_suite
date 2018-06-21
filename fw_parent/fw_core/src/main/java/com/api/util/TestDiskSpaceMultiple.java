/*
 *   Feb 2, 2017 Project:ClientPortal By: John Bender
 * 
 *   Copyright (c) 2017 DHISCO, Inc.
 *           All Rights Reserved
 * 
 *   This software is the confidential and proprietary information of
 *   DHISCO, Inc.
 */
package com.api.util;

import java.io.File;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Scanner;

import org.apache.log4j.Logger;

//import com.dhisco.arimanagementconsole.dao.DhiscoDatabase;
//import com.dhisco.arimanagementconsole.scheduler.ReportBase;

/**
 */
public class TestDiskSpaceMultiple {
    /**
     * Field log.
     */
    private static Logger log = Logger.getLogger(TestDiskSpaceMultiple.class);
    /**
     * Field myResults.
     */
    private HealthResults myResults;
    /**
     * Field passed.
     */
    private static String passed = "<b><font color=\"green\">Passed</font></b>";
    /**
     * Field failed.
     */
    private static String failed = "<b><font color=\"red\">Failed</font></b>";
    /**
     * Field percentFree.
     */
    private LinkedHashMap<String, Double> percentFree;
    /**
     * Field drivesToCheck.
     */
    private String drivesToCheck = "a b c d e f g h i j k l m n o p q r s t u v w x y z";

    /**
     * Method toHtmlString.
     * 
     * @return String
     * @see com.dhisco.arimanagementconsole.scheduler.monitor.ClientPortalTest#toHtmlString()
     */
    public String toHtmlString() {
        String errorMsg = " <span style=\"color:red; font-weight:bold; \">Disk space on this drive is below the minimum threshold of "
                + getPercentFreePrettyPrint(diskSpaceThreshold) + " free.</span>";
        StringBuffer buff = new StringBuffer("<tr><td>Disk space test</td><td>" + (myResults.isDiskSpaceOK() ? passed : failed) + "</td><td>");

        HashMap<String, Double> hm = getPercentFree();
        Iterator<String> iter = hm.keySet().iterator();
        while (iter.hasNext()) {
            String drive = iter.next();
            Double percent = hm.get(drive);

            buff.append("Free disk space on drive " + drive + " is " + getPercentFreePrettyPrint(percent)
                    + ((percent < diskSpaceThreshold) ? errorMsg : "") + "<br>");
        }

        buff.append("</td></tr>");
        return buff.toString();
    }

    /**
     * Field diskSpaceThreshold.
     */
    private static double diskSpaceThreshold = 0.15; // 15% free space on hard drive

    /**
     * Method execute.
     * 
     * @param results
     *            HealthResults
     * @return HealthResults
     * @see com.dhisco.arimanagementconsole.scheduler.monitor.ClientPortalTest#execute(HealthResults)
     */
    public HealthResults execute(HealthResults results) {

        Scanner scanner = new Scanner(drivesToCheck);
        String drive;
        while (scanner.hasNext()) {
            drive = scanner.next();

            File file = new File(drive + ":");
            if (file.exists()) {
                log.info("Drive " + drive + " exists.");
                long totalSpace = file.getTotalSpace(); // total disk space in bytes.
                @SuppressWarnings("unused")
                long usableSpace = file.getUsableSpace(); /// unallocated / free disk space in bytes.
                long freeSpace = file.getFreeSpace(); // unallocated / free disk space in bytes.

                getPercentFree().put(drive, (double) freeSpace / totalSpace);
            }
        }
        scanner.close();

        // Set overall pass fail after iterating over individual disk results
        boolean isOK = true;
        HashMap<String, Double> hm = getPercentFree();
        Iterator<String> iter = hm.keySet().iterator();
        while (iter.hasNext()) {
            String key = iter.next();
            Double val = hm.get(key);
            if (val < diskSpaceThreshold) {
                isOK = false; // < 15% disk space available
            }
        }
        results.setDiskSpaceOK(isOK);
        this.myResults = results;
        return results;

    }

    /**
     * @param args
     */
    public static void main(String[] args) {
//        DhiscoDatabase.setUnitTesting(true);
        TestDiskSpaceMultiple test = new TestDiskSpaceMultiple();
        HealthResults results = new HealthResults();
        results = test.execute(results);

        HashMap<String, Double> hm = test.getPercentFree();
        Iterator<String> iter = hm.keySet().iterator();
        while (iter.hasNext()) {
            String key = iter.next();
            Double val = hm.get(key);
            System.out.println("key,val: " + key + "," + val);
        }
        log.info("HTML string is: " + test.toHtmlString());
//        DhiscoDatabase.setUnitTesting(false);
    }

    /**
     * 
     * @return the percentFree
     */
    private LinkedHashMap<String, Double> getPercentFree() {
        if (percentFree == null) {
            percentFree = new LinkedHashMap<String, Double>();
        }
        return percentFree;
    }

    /**
     * @param percentFree
     *            the percentFree to set
     */
    @SuppressWarnings("unused")
    private void setPercentFree(LinkedHashMap<String, Double> percentFree) {
        this.percentFree = percentFree;
    }

    /**
     * Method getPercentFreePrettyPrint.
     * 
     * @param percent
     *            Double
     * @return String
     */
    private String getPercentFreePrettyPrint(Double percent) {
        NumberFormat percentFormat = NumberFormat.getPercentInstance();
        percentFormat.setMaximumFractionDigits(1);
        return percentFormat.format(percent);
    }

}
