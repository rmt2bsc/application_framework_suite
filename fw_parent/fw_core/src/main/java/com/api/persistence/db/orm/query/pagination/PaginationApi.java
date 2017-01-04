package com.api.persistence.db.orm.query.pagination;

import com.api.DaoApi;
import com.api.persistence.DatabaseException;
import com.api.persistence.db.orm.OrmBean;

/**
 * @author appdev
 * 
 */
public interface PaginationApi extends DaoApi {

    /**
     * The default number of rows included in the result set. To use a different
     * page size, add key, <i>pagination_page_size</i>, to the
     * SystemParms.properties and set to desired number. For example:
     * pagination_page_size=20.
     */
    static final int DEFAULT_PAGESIZE_COUNT = 10;

    /**
     * he default number of pages included in page link set. To use a different
     * page link size, add key, <i>page_link_max</i>, to the
     * SystemParms.properties and set to desired number. For example:
     * page_link_max=25.
     */
    static final int DEFAULT_MAXPAGE_SET_COUNT = 10;

    /**
     * Get the current page number which the query is executing.
     * 
     * @return int
     */
    int getPageNo();

    /**
     * SGet the current page number for query is execution.
     * 
     * @param pageNo
     *            int the page number
     */
    void setPageNo(int pageNo);

    /**
     * Get the total number of rows which query returned.
     * 
     * @return int
     */
    int getRowCount();

    /**
     * Set the total number of rows which query returned.
     * 
     * @param rowCount
     */
    void setRowCount(int rowCount);

    /**
     * Executes a paginated query based on the ORM bean, <i>ormObj</i>, for a
     * specified page. The paginated query is based on the follow SQL Select
     * statement: <blockquote> SELECT &lt; select list from derived table &gt;<br>
     * &nbsp;&nbsp;&nbsp;FROM (select ROW_NUMBER() OVER (order by surname asc,
     * givenname asc) as rownumber, &lt; select list &gt; from &lt; table &gt;)
     * as pageResults<br>
     * &nbsp;&nbsp;&nbsp;WHERE rownumber between &lt; start position &gt; and
     * &lt; end position &gt; </blockquote>
     * 
     * @param ormObj
     * @param pageNo
     * @return
     * @throws DatabaseException
     * @throws PaginationException
     */
    PaginationQueryResults retrieveList(OrmBean ormObj, int pageNo)
            throws DatabaseException, PaginationException;

    /**
     * 
     * @param obj
     * @return
     * @throws DatabaseException
     */
    long retrieveCount(OrmBean obj) throws DatabaseException;

}
