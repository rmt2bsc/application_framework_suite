package com.util;

/**
 * 
 * @author roy.terrell
 *
 */
public class HealthResults {
    /**
     * Field isDbConnectionAlive.
     */
    private boolean isDbConnectionAlive = false;
    /**
     * Field numLogins.
     */
    private int numLogins = 0;
    /**
     * Field isDiskSpaceOK.
     */
    private boolean isDiskSpaceOK = false;
    /**
     * Field isDatabaseBackupsOK.
     */
    private boolean isDatabaseBackupsOK = false;

    /**
     * Field overallResults.
     */
    private String overallResults = "";

    /**
     * 
     * @return the isDbConnectionAlive
     */
    public boolean isDbConnectionAlive() {
        return isDbConnectionAlive;
    }

    /**
     * @param isDbConnectionAlive
     *            the isDbConnectionAlive to set
     */
    public void setDbConnectionAlive(boolean isDbConnectionAlive) {
        this.isDbConnectionAlive = isDbConnectionAlive;
    }

    /**
     * Method getNumLogins.
     * 
     * @return int
     */
    public int getNumLogins() {
        return numLogins;
    }

    /**
     * @param numLogins
     *            the numLogins to set
     */
    public void setNumLogins(int numLogins) {
        this.numLogins = numLogins;
    }

    /**
     * Method toString.
     * 
     * @return String
     */
    public String toString() {
        return "Today's overall health results: " + getOverallResults();
    }

    /**
     * 
     * @return the overallResults
     */
    private String getOverallResults() {
        if (overallResults.isEmpty()) {
            setOverallResults(computeResults());
        }
        return overallResults;
    }

    /**
     * Method computeResults.
     * 
     * @return String
     */
    private String computeResults() {
        if ((getNumLogins() > 0) && isDbConnectionAlive()) {
            return "<b><font color=\"green\">Passed</font></b>";
        } else {
            return "<b><font color=\"red\">Failed</font></b>";
        }
    }

    /**
     * @param overallResults
     *            the overallResults to set
     */
    private void setOverallResults(String overallResults) {
        this.overallResults = overallResults;
    }
    /**
     * 
     * @return the isDiskSpaceOK
     */
    public boolean isDiskSpaceOK() {
        return isDiskSpaceOK;
    }

    /**
     * @param isDiskSpaceOK
     *            the isDiskSpaceOK to set
     */
    public void setDiskSpaceOK(boolean isDiskSpaceOK) {
        this.isDiskSpaceOK = isDiskSpaceOK;
    }

    /**
     * 
     * @return the isDatabaseBackupsOK
     */
    public boolean isDatabaseBackupsOK() {
        return isDatabaseBackupsOK;
    }

    /**
     * @param isDatabaseBackupsOK
     *            the isDatabaseBackupsOK to set
     */
    public void setDatabaseBackupsOK(boolean isDatabaseBackupsOK) {
        this.isDatabaseBackupsOK = isDatabaseBackupsOK;
    }

}
