package com.api.persistence.db.orm.query;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.SystemException;
import com.api.DaoApi;
import com.api.DataSourceApi;
import com.api.ProductBuilderException;
import com.api.persistence.db.DbSqlConst;
import com.api.persistence.db.orm.OrmBean;
import com.api.persistence.db.orm.bean.DataSourceColumn;

/**
 * Implements the {@link com.api.db.query.CriteriaBuilder CriteriaBuilder}
 * interface in order to build the where clause from the various types of
 * selection criteria that may exist from within an OrmBean type object. The
 * following types of selection criteria, which are compatible to SQL select,
 * update, and delete statements, are produced: normal equality, like conditions
 * (begin with, contain and end with), and in clause.
 * 
 * @author RTerrell
 * 
 */
class OrmSqlCriteria extends AbstractOrmQuery implements CriteriaBuilder {

    private static final long serialVersionUID = 5500191219853668905L;

    private Logger logger;

    private String stockCriteria = null;

    private String customCriteria = null;

    private String criteria = null;

    private String inClause = null;

    private String likeClause = null;

    private String condition = "";

    private OrmBean pojo;

    /**
     * Constructs an OrmSqlCriteria object by initializing it with a DaoApi and
     * a OrmBean pojo.
     * 
     * @param src
     *            {@link com.api.DaoApi DaoApi}
     * @param pojo
     *            {@link bean.OrmBean OrmBean}
     * @throws SystemException
     */
    public OrmSqlCriteria(Object src, OrmBean pojo) throws SystemException {
        super(src);
        this.pojo = pojo;
        this.logger = Logger.getLogger("OrmSqlCriteria");
    }

    /**
     * Drives the process of building selection criteria for a SQL statement. It
     * is capable of applying custom criteria provided by the client, selection
     * criteria provided by the DataSource View, "IN" where clause types, and
     * varous forms of the "LIKE" clause.
     * 
     * @throws {@link com.api.ProductBuilderException ProductBuilderException}.
     */
    public void assemble() throws ProductBuilderException {
        this.logger.log(Level.DEBUG, "Assembling ORM Selection Criteria");
        // If avaialble, get selection criteria which could be hard coded
        // in the DataSource or manually set by some external process.
        try {
            stockCriteria = ((DaoApi) this.getSrc()).getDataSourceAttib()
                    .getSqlAttribute(DbSqlConst.WHERE_KEY);
        } catch (Exception e) {
            stockCriteria = null;
        }

        // Get custom selection criteria
        customCriteria = this.assembleCustomCriteria(this.getSrc());
        // Get selection and ordering criteria
        criteria = this.assembleCriteria(this.getSrc());
        // Get in clause for selection criteria
        inClause = this.assembleInClause(this.getSrc());
        // Get like clause for selection criteria.
        likeClause = this.assembleLikeCriteria(this.getSrc());

        // Addemble different where clause types
        if (stockCriteria != null && !stockCriteria.equals("")) {
            condition = stockCriteria;
        }

        if (customCriteria != null) {
            if (condition.length() > 0) {
                condition += " and ";
            }
            condition += customCriteria;
        }

        if (criteria != null) {
            if (condition.length() > 0) {
                condition += " and ";
            }
            condition += criteria;
        }

        if (inClause != null) {
            if (condition.length() > 0) {
                condition += " and ";
            }
            condition += inClause;
        }

        if (likeClause != null) {
            if (condition.length() > 0) {
                condition += " and ";
            }
            condition += likeClause;
        }

        // set the criteria that was constructed to the queryString property.
        this.setQueryString((condition.equals("") ? null : condition));
    }

    /**
     * No implementation to disassemble selection criteria.
     */
    public void disAssemble() throws ProductBuilderException {
        return;
    }

    /**
     * Builds simple selection criteria from _obj to be applies as part of the
     * SQL where clause. The following predicate types are created using the
     * appropriate relational operators: equality, null, and not null. Equality
     * is the default operator.
     * <p>
     * 
     * <pre>
     * Example of setting the selection criteria of an ORM bean instance:
     *   <i>ormbean</i>.addCriteria(<i>property name</i>, OrmBean.DB_NULL);
     *   <i>ormbean</i>.addCriteria(<i>property name</i>, OrmBean.DB_NOTNULL);
     *   <i>ormbean</i>.addCriteria(<i>property name</i>, OrmBean.DB_EQUAL);
     * </pre>
     * 
     * @param src
     *            An instance of {@link com.api.DaoApi DaoApi}
     * @return selection criteria as a String. Return null if there is no
     *         criteria available or if <i>src</i> is not of type
     *         {@link com.api.DaoApi DaoApi}
     * @see bean.OrmBean
     */
    public String assembleCriteria(Object src) {
        StringBuffer sql = new StringBuffer(100);
        String criteriaVal = null;
        String prop = null;
        String result = null;
        Iterator criteriaIter = null;
        Hashtable colDefs = null;
        DataSourceColumn colBean = null;

        // No need to continue selection criteria probe if selection criteria
        // does not exist.
        if (!this.pojo.isCriteriaAvailable()) {
            return null;
        }

        // Get selection criteria property keys
        criteriaIter = this.pojo.getCriteria().keySet().iterator();
        // Get DataSource Column bean objects
        colDefs = this.dsAttr.getColumnDef();

        while (criteriaIter.hasNext()) {
            prop = (String) criteriaIter.next();
            if (prop == null) {
                continue;
            }
            // Use property to get corresponding DB name
            colBean = (DataSourceColumn) colDefs.get(prop);
            if (colBean == null) {
                continue;
            }

            // Make the equality relational operator the default
            String operator = " = ";

            // Build criteria for current DB Column
            criteriaVal = this.pojo.getCriteria(colBean.getName());
            if (criteriaVal != null) {
                if (sql.length() > 0) {
                    sql.append(" and ");
                }
                // Change the relational operator to "is" for "null" or
                // "not null" predicates
                if (criteriaVal.equals(OrmBean.DB_NULL)
                        || criteriaVal.equals(OrmBean.DB_NOTNULL)) {
                    operator = " is ";
                }

                sql.append(colBean.getDbName());
                sql.append(operator);
                sql.append(((DataSourceApi) src).getSqlColumnValue(
                        colBean.getSqlType(), criteriaVal));
            }
        } // end while

        result = sql.toString();
        return result;
    }

    /**
     * Builds custom SQL predicates and attaches to the where clause of a SQL
     * statement.
     * 
     * @param src
     *            An instance of {@link com.api.DaoApi DaoApi}
     * @return selection criteria as a String. Return null if there is no custom
     *         criteria available.
     */
    public String assembleCustomCriteria(Object src) {
        StringBuffer sql = new StringBuffer(100);
        String criteria = null;
        String result = null;
        Iterator criteriaIter = null;

        // No need to continue selection criteria probe if selection criteria
        // does not exist.
        if (!this.pojo.isCustomCriteriaAvailable()) {
            return null;
        }

        // Get selection criteria property keys
        criteriaIter = this.pojo.getCustomCriteria().iterator();
        while (criteriaIter.hasNext()) {
            criteria = (String) criteriaIter.next();
            if (criteria == null) {
                continue;
            }
            if (sql.length() > 0) {
                sql.append(" and ");
            }
            sql.append(criteria);
        } // end while

        result = sql.toString();
        return result;
    }

    /**
     * Builds an "In" clause from _obj to be applied as part of the SQL where
     * clause.
     * 
     * @param src
     *            An instance of {@link com.api.DaoApi DaoApi}
     * @return selection criteria as a String. Return null if there is no "In"
     *         criteria available or if <i>src</i> is not of type
     *         {@link com.api.DaoApi DaoApi}
     */
    public String assembleInClause(Object src) {
        StringBuffer sql = new StringBuffer(100);
        String criteriaVal = null;
        String criteriaValues[];
        String prop = null;
        String result = null;
        Iterator criteriaIter = null;
        Hashtable colDefs = null;
        DataSourceColumn colBean = null;

        // No need to continue selection criteria probe if selection criteria
        // does not exist.
        if (!this.pojo.isInClauseAvailable()) {
            return null;
        }

        // Get selection criteria property keys
        criteriaIter = this.pojo.getInClause().keySet().iterator();
        // Get DataSource Column bean objects
        colDefs = this.dsAttr.getColumnDef();

        // Attach an IN clause to each db column
        while (criteriaIter.hasNext()) {
            prop = (String) criteriaIter.next();
            if (prop == null) {
                continue;
            }
            // Use property to get corresponding DB name
            colBean = (DataSourceColumn) colDefs.get(prop);
            if (colBean == null) {
                continue;
            }

            // Build the value list for current IN clause
            StringBuffer inClause = new StringBuffer(100);
            String sqlPrefix = colBean.getDbName() + " in (";
            criteriaValues = this.pojo.getInClause(colBean.getName());
            for (int ndx = 0; ndx < criteriaValues.length; ndx++) {
                criteriaVal = criteriaValues[ndx];
                if (criteriaVal != null) {
                    if (inClause.length() > 0) {
                        inClause.append(", ");
                    }
                    inClause.append(((DataSourceApi) src).getSqlColumnValue(
                            colBean.getSqlType(), criteriaVal));
                }
            }

            // Assemble current IN clause and concatenate with other previously
            // built clauses.
            if (sql.length() > 0) {
                sql.append(" and ");
            }
            sql.append(sqlPrefix);
            sql.append(inClause);
            sql.append(") ");
        } // end while

        result = sql.toString();
        return result;
    }

    /**
     * Builds selection criteria as "like" condition from <i>src</i> which is to
     * be applied the SQL where clause.
     * 
     * @param src
     *            An instance of {@link com.api.DaoApi DaoApi}
     * @return selection criteria as a String.
     */
    public String assembleLikeCriteria(Object src) {
        StringBuffer sql = new StringBuffer(100);
        String result1 = null;
        String result2 = null;
        String result3 = null;

        // No need to continue selection criteria probe if selection criteria
        // does not exist.
        if (this.pojo.isLikeClauseAvailable(OrmBean.LIKE_BEGIN)) {
            result1 = this.assembleLikeCriteria(
                    this.pojo.getLikeClause(OrmBean.LIKE_BEGIN),
                    OrmBean.LIKE_BEGIN);
        }
        if (this.pojo.isLikeClauseAvailable(OrmBean.LIKE_CONTAINS)) {
            result2 = this.assembleLikeCriteria(
                    this.pojo.getLikeClause(OrmBean.LIKE_CONTAINS),
                    OrmBean.LIKE_CONTAINS);
        }
        if (this.pojo.isLikeClauseAvailable(OrmBean.LIKE_END)) {
            result3 = this
                    .assembleLikeCriteria(
                            this.pojo.getLikeClause(OrmBean.LIKE_END),
                            OrmBean.LIKE_END);
        }
        if (result1 != null) {
            sql.append(result1);
        }
        if (result2 != null) {
            if (sql.length() > 0) {
                sql.append(" and ");
            }
            sql.append(result2);
        }

        if (result3 != null) {
            if (sql.length() > 0) {
                sql.append(" and ");
            }
            sql.append(result3);
        }
        return (sql.length() <= 0 ? null : sql.toString());
    }

    /**
     * Builds one or more SQL "like" condition clauses in the form of
     * "begin with", "contains" or "end with" types.
     * 
     * @param dsAttr
     *            Datasource attribute object.
     * @param criteria
     *            Map of Like condition criteria.
     * @param conditionType
     *            One of the following Like condtion types:
     *            {@link OrmBean.LIKE_BEGIN}, {@link OrmBean.LIKE_CONTAINS} or
     *            {@link OrmBean.LIKE_END}
     * @return selection criteria as a String.
     */
    private String assembleLikeCriteria(Map criteria, int conditionType) {
        StringBuffer sql = new StringBuffer(100);
        String criteriaVal = null;
        String prop = null;
        String result = null;
        Iterator criteriaIter = null;
        Hashtable colDefs = null;
        DataSourceColumn colBean = null;

        // Get selection criteria property keys
        criteriaIter = criteria.keySet().iterator();
        // Get DataSource Column bean objects
        colDefs = this.dsAttr.getColumnDef();

        while (criteriaIter.hasNext()) {
            prop = (String) criteriaIter.next();
            if (prop == null) {
                continue;
            }
            // Use property to get corresponding DB name
            colBean = (DataSourceColumn) colDefs.get(prop);
            if (colBean == null) {
                continue;
            }

            // Build criteria for current DB Column
            criteriaVal = (String) criteria.get(colBean.getName());
            if (criteriaVal != null) {
                if (sql.length() > 0) {
                    sql.append(" and ");
                }
                sql.append(colBean.getDbName());
                switch (conditionType) {
                    case OrmBean.LIKE_BEGIN:
                        sql.append(" like \'");
                        sql.append(criteriaVal);
                        sql.append("%\'");
                        break;
                    case OrmBean.LIKE_CONTAINS:
                        sql.append(" like \'%");
                        sql.append(criteriaVal);
                        sql.append("%\'");
                        break;
                    case OrmBean.LIKE_END:
                        sql.append(" like \'%");
                        sql.append(criteriaVal);
                        sql.append("\'");
                        break;
                    default:
                        // Abort...Invalid condition type specified.
                        return null;
                }
            }
        } // end while

        result = sql.toString();
        return result;
    }

}
