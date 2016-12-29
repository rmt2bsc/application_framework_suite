package com.api.persistence.db.orm.query;

import com.api.ProductBuilder;

/**
 * Builder interface for constructing various forms of selection criteria.
 * 
 * @author RTerrell
 * 
 */
public interface CriteriaBuilder extends ProductBuilder {

    /**
     * Builds simple selection criteria using <i>src</i>.
     * 
     * @param src
     *            An arbitray object representing the data.
     * @return String.
     */
    String assembleCriteria(Object src);

    /**
     * Builds custom SQL predicates and attaches to the where clause of a SQL
     * statement.
     * 
     * @param src
     *            An arbitray object representing the data.
     * @return String
     */
    String assembleCustomCriteria(Object src);

    /**
     * Builds an "In" clause criteria using <i>src</i> which is to be applied as
     * part of the SQL where clause.
     * 
     * @param src
     *            An arbitray object representing the data.
     * @return String
     */
    String assembleInClause(Object src);

    /**
     * Builds selection criteria as "like" condition from <i>src</i> which is to
     * be applied the SQL where clause.
     * 
     * @param src
     *            An arbitray object representing the data.
     * @return String.
     */
    String assembleLikeCriteria(Object src);

}
