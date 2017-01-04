package com.api;

import com.SystemException;

/**
 * An interface that provides common functionality to query a particular data
 * source any given entity.
 * 
 * <p>
 * For those queries that may return a an array of Objects masquerading as one,
 * It wise to devise test logic to determine if the result is a single object or
 * an array of Objects. Example: <blockquote> Object arrayResults[] = null;<br>
 * Object results = this.get("x = 1");<br>
 * try {<br>
 * // Try to cast the return results as an array of Objects<br>
 * arrayResults = (Object []) results;<br>
 * }<br>
 * catch (Exception e) {<br>
 * // An Exception is thrown if the results exist as a single object.<br>
 * // Just use the varible, results, as is.<br>
 * }<br>
 * </blockquote>
 * 
 * @author RTerrell
 * 
 */
public interface CommonQueryApi {

    /**
     * Find an entity using a unique identifier.
     * 
     * @param uid
     *            A uniuqe identifier representing an entity. This is usually a
     *            primary key.
     * @return An arbitrary object representing the result set of the query.
     * @throws SystemException
     */
    Object get(int uid) throws SystemException;

    /**
     * Find one or more like entities using custom selection criteria.
     * 
     * @param criteria
     *            An arbitrary object representing selection criteria.
     * @return An arbitrary object representing the result set of the query.
     * @throws SystemException
     */
    Object get(Object criteria) throws SystemException;

    /**
     * Find one or more like entities using custom selection and ordering
     * criteria.
     * 
     * @param criteria
     *            Arbitrary object representing selection criteria or a
     *            predicate.
     * @param ordering
     *            Arbitrary object representing the ordering of the result set.
     * @return An arbitrary object representing the result set of the query.
     * @throws SystemException
     */
    Object get(Object criteria, Object ordering) throws SystemException;
}