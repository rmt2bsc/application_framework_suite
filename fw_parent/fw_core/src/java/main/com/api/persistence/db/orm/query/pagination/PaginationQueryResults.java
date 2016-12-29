package com.api.persistence.db.orm.query.pagination;

import com.RMT2Base;

/**
 * Keeps track of the metrics pertaing to a pagination query such as the current
 * page number, the data set results, page row count, the total row count, and
 * the total number of pages.
 * 
 * @author rterrell
 * 
 */
public class PaginationQueryResults extends RMT2Base {

    private int pageNo;

    private Object results;

    private int pageRowCount;

    private long totalRowCount;

    private double totalPageCount;

    private String query;

    /**
     * 
     */
    public PaginationQueryResults() {
        return;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public Object getResults() {
        return results;
    }

    public void setResults(Object results) {
        this.results = results;
    }

    public long getTotalRowCount() {
        return totalRowCount;
    }

    public void setTotalRowCount(long resultCount) {
        this.totalRowCount = resultCount;
    }

    /**
     * @return the totalPageCount
     */
    public double getTotalPageCount() {
        return totalPageCount;
    }

    /**
     * @param totalPageCount
     *            the totalPageCount to set
     */
    public void setTotalPageCount(double totalPageCount) {
        this.totalPageCount = totalPageCount;
    }

    /**
     * @return the pageRowCount
     */
    public int getPageRowCount() {
        return pageRowCount;
    }

    /**
     * @param pageRowCount
     *            the pageRowCount to set
     */
    public void setPageRowCount(int pageRowCount) {
        this.pageRowCount = pageRowCount;
    }

    /**
     * @return the query
     */
    public String getQuery() {
        return query;
    }

    /**
     * @param query
     *            the query to set
     */
    public void setQuery(String query) {
        this.query = query;
    }

}
