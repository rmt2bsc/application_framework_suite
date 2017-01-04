package com.api.persistence.db.orm.query.pagination;

import com.RMT2Base;
import com.api.config.ConfigConstants;

/**
 * Contains functionality to perform various page navigation calculations.
 * 
 * @author rterrell
 * 
 */
public class PageCalculator extends RMT2Base {

    public static final int BOF = -1;

    public static final int EOF = -99;

    private double itemTotal;

    private int pageSize;

    private double totalPages;

    /**
     * 
     */
    public PageCalculator() {
        return;
    }

    public PageCalculator(long itemTotal) {
        this.totalPages = this.calcTotalPages(itemTotal, 0);
        return;
    }

    /**
     * 
     * @param itemTotal
     * @param pageSize
     */
    public PageCalculator(long itemTotal, int pageSize) {
        this.totalPages = this.calcTotalPages(itemTotal, pageSize);
        return;
    }

    /**
     * 
     * @param curPage
     * @return
     */
    public boolean isFirstPage(int curPage) {
        return (curPage * pageSize) == pageSize;
    }

    /**
     * 
     * @param curPage
     * @return
     */
    public boolean isLastPage(int curPage) {
        return curPage == this.calcTotalPages();
    }

    /**
     * 
     * @param curPage
     * @return
     */
    public long getNextPage(int curPage) {
        if (++curPage <= this.calcTotalPages()) {
            return curPage;
        }
        return PageCalculator.EOF;
    }

    /**
     * 
     * @param curPage
     * @return
     */
    public long getPrevPage(int curPage) {
        if (!this.isFirstPage(curPage)) {
            return --curPage;
        }
        return PageCalculator.BOF;
    }

    /**
     * 
     * @return
     */
    public double calcTotalPages() {
        if (this.pageSize <= 0) {
            this.pageSize = PageCalculator.getPageSize();
        }
        double unRounded = this.itemTotal / this.pageSize;
        // long totPages = Math.round(unRounded);
        // if (unRounded > totPages) {
        // ++totPages;
        // }
        return unRounded;
    }

    /**
     * 
     * @param totItems
     * @param pgSize
     * @return
     */
    public double calcTotalPages(long totItems, int pgSize) {
        this.itemTotal = totItems;
        this.pageSize = pgSize;
        return this.calcTotalPages();
    }

    /**
     * 
     * @return
     */
    public static final int getPageSize() {
        int pageSize = 0;
        try {
            String temp = System
                    .getProperty(ConfigConstants.PROPNAME_ORM_PAGE_SIZE);
            pageSize = Integer.parseInt(temp);
        } catch (NumberFormatException e) {
            pageSize = PaginationApi.DEFAULT_PAGESIZE_COUNT;
        }
        return pageSize;
    }

    /**
     * 
     * @return
     */
    public static final int getMaxPageLinkSetSize() {
        int linkSize = 0;
        try {
            String temp = System
                    .getProperty(ConfigConstants.PROPNAME_ORM_PAGE_LINK_TOTAL);
            linkSize = Integer.parseInt(temp);
        } catch (NumberFormatException e) {
            linkSize = PaginationApi.DEFAULT_PAGESIZE_COUNT;
        }
        return linkSize;
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        return;
    }

    /**
     * @return the itemTotal
     */
    public double getItemTotal() {
        return itemTotal;
    }

    /**
     * @param itemTotal
     *            the itemTotal to set
     */
    public void setItemTotal(double itemTotal) {
        this.itemTotal = itemTotal;
    }

    /**
     * @param pageSize
     *            the pageSize to set
     */
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    /**
     * @return the totalPages
     */
    public double getTotalPages() {
        return totalPages;
    }

}
