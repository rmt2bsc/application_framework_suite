package com.api.jsp.taglib;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.jsp.PageContext;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.NotFoundException;
import com.RMT2Base;
import com.SystemException;
import com.api.DaoApi;
import com.api.jsp.taglib.bean.dao.BeanDao;
import com.api.jsp.taglib.bean.dao.BeanDaoFactory;
import com.api.persistence.DatabaseException;
import com.api.xml.XmlDao;
import com.util.RMT2BeanUtility;
import com.util.RMT2Date;
import com.util.RMT2Money;
import com.util.RMT2Utility;

/**
 * Provides a convenient implementation for identifying and obtaining the value
 * of JSP variable expressions. Basically, there are two types of variable
 * expressions: literal and object. A literal expression is intended to utilized
 * by the server as-is without any further translation. On the other hand, an
 * object expression can be resolved or mapped to an Object as a named attribute
 * that may exist in one of the web container variable scopes (page, request,
 * session, and application). An object expression can exist as one of three
 * data types: {@link com.api.DaoApi DaoApi}, a {@link bean.RMT2Base RMT2Base}
 * based POJO, or a String.
 * <p>
 * The format for an object expression goes as follows:
 * 
 * <pre>
 * &quot;#&lt;object attribute name&gt;.&lt;property name&gt;&quot;
 * </pre>
 * 
 * Example uses of the different object expression types:
 * <ul>
 * <li><b>DaoApi</b> - #dao.CreditorName</li>
 * <li><b>POJO</b> - #bean.CreditorName</li>
 * <li><b>String</b> - #CreditorName</li>
 * </ul>
 * <b>Object expression usage rules:</b>
 * <ol>
 * <li>It is required that all object expressions are prefixed with the "#"
 * character in order to be recognized as such.</li>
 * <li>The expression shall be delimited by "." character for the purpose of
 * splitting the expression into separte components (array of Strings).</li>
 * <li>The "object attribute name" will be the name of the attribute that exist
 * on one of the varible scopes.</li>
 * <li>If the object is not found, an exception is thrown detailing the reason.</li>
 * <li>The target object shall be of type DaoApi, RMTBase, or String. Be advised
 * that a RMT2Base object type will be masqueraded as a DaoApi in order to
 * maintain consistency in managing its properties</li>
 * </ol>
 * 
 * @author RTerrell
 * 
 */
public class PageVariableHelper extends RMT2Base {
    private static Logger logger = Logger.getLogger("PageVariableHelper");

    /** The name of the target property set for the current variable expression. */
    protected String property;

    public PageVariableHelper() {
        return;
    }

    /**
     * This method is used to obtain the value of a variable expression, whether
     * literal or object.
     * 
     * @param pageCtx
     *            The JSP page context which holds the data needed to bind
     *            variable expressions.
     * @param expr
     *            The identifies the object or literal variable expression to
     *            manage.
     * @param scope
     *            The variable scope to locate the variable expression.
     * @param format
     *            The format that is to be applied to the value.
     * @return The value mapped to the named attribute of the object expression,
     *         the actual contents of <i>expr</i> when the expression is
     *         evaulated as a literal, or null when <i>expr</i> is null or
     *         invalid.
     * @throws SystemException
     *             Variable experssion is not found,
     */
    public Object getValue(PageContext pageCtx, String expr, String scope,
            String format) throws SystemException {
        if (expr == null) {
            return null;
        }

        // String array to contain parsed values of the expression. Index 0 will
        // contain the attribute name of the base object to obtain from one of
        // the
        // variable scopes. Index position 1 will contain the name of the base
        // object's property to obtain the actual value.
        String splitExpr[];

        // Check whether the expr is intended to be used as a literal or as some
        // object.
        String varExpr;
        if (expr.indexOf("#") == 0) {
            varExpr = expr.substring(1);
            // Since the split command uses regular expressions and "." is a
            // special character, the period character must be escaped with
            // an escaped backslash "\\".
            splitExpr = varExpr.split("\\.");
        }
        else {
            // Return literal value back to caller.
            return expr;
        }

        // Try to locate base object on one of the variables scopes
        Object attribObj = null;
        if (scope != null) {
            attribObj = pageCtx.getAttribute(splitExpr[0],
                    PageVariableHelper.translateScope(scope));
        }
        else {
            attribObj = pageCtx.findAttribute(splitExpr[0]);
        }

        // Produce an error if base object is not obtainable
        if (attribObj == null) {
            throw new SystemException("JSP variable expression is not found: "
                    + expr);
        }

        // Try to obtain the desired value
        Object exprObj = null;
        String value = null;
        try {
            exprObj = this.processVarExpr(attribObj, splitExpr);
            value = this.formatValue(exprObj, format);
            return value;
        } catch (SystemException e) {
            throw e;
        }
    }

    /**
     * Processes object variable expessions by distinguishing if the object is
     * of type DaoApi, POJO, or String. POJO's will undergo a deep property
     * search if it contains heirachies of properties.
     * 
     * @param obj
     *            A reference to the base object component of an object variable
     *            expression. The reference is genereally found in one of the
     *            variable scopes.
     * @param expression
     *            A String array of property names separated from the object
     *            variable expression.
     * @return {@link com.api.DaoApi DaoApi} or a primitive wrapper object.
     * @throws SystemException
     *             Variable expression object resolves to an invalid data type
     *             or when the value is not obtainable using the stated
     *             property.
     */
    private Object processVarExpr(Object obj, String expression[])
            throws SystemException {
        Object value = null;
        String msg;
        int lastPropNdx = expression.length - 1;

        try {
            if ((obj instanceof DaoApi) || (RMT2Utility.isWrapperType(obj))) {
                value = this.processVarExpr(obj, expression[lastPropNdx]);
            }
            else if (obj instanceof RMT2Base) {
                if (expression.length <= 2) {
                    value = this.processVarExpr(obj, expression[lastPropNdx]);
                }
                else {
                    // POJO conains at least a 3 level property hierarchy.
                    value = this.doVarExprDeepSearch(obj, expression);
                }
            }
            else {
                msg = "JSP variable expression object resolved to an invalid data type.  Only DaoApi, RMT2Base based POJO's or String types are acceptable data types";
                PageVariableHelper.logger.log(Level.ERROR, msg);
                throw new SystemException(msg);
            }
            return value;
        } catch (Exception e) {
            throw new SystemException(e);
        }
    }

    /**
     * Retrieves the value mapped to <i>property</i> of the object expression,
     * <i>obj</i>.
     * 
     * @param obj
     *            A reference to the base object component of an object variable
     *            expression. The reference is genereally found in one of the
     *            variable scopes.
     * @param property
     *            The name of the target property obtain the mapped value.
     * @return {@link com.api.DaoApi DaoApi} or a primitive wrapper object.
     * @throws SystemException
     *             Expression variable resolved to an invalid data type or
     *             general DaoApi error.
     */
    private Object processVarExpr(Object obj, String property)
            throws SystemException {
        Object data = null;
        this.property = property;

        try {
            if (obj instanceof DaoApi) {
                if (property != null) {
                    data = obj;
                }
            }
            else if (obj instanceof RMT2Base) {
                if (property != null) {
                    BeanDao dao = (BeanDao) BeanDaoFactory.createApi();
                    dao.retrieve(obj);
                    dao.nextRow();
                    data = dao;
                }
            }
            else {
                data = obj;
            }
            return data;
        } catch (Exception e) {
            throw new SystemException(e);
        }
    }

    /**
     * Obtains the value of the varialbe expression's target property from a
     * POJO with a deep property/object heirarchy. Uses reflection to navigate
     * through all properties that were found in the object variable expression
     * in hopes that a value is obtained from the last property. This is
     * generally used for POJO's taht have properties within properties.
     * 
     * @param obj
     *            The base object of the variable expression.
     * @param property
     *            A String array of property names to search.
     * @return The value of the last property in <i>property</i>
     * @throws SystemException
     *             Problem accessing a property.
     */
    private Object doVarExprDeepSearch(Object obj, String property[])
            throws SystemException {
        String msg;
        int total = property.length - 1;
        Object curObj = obj;
        String curProp;
        for (int ndx = 1; ndx <= total; ndx++) {
            // Introspect the POJO
            RMT2BeanUtility beanUtil = new RMT2BeanUtility(curObj);

            try {
                // Get next property
                curProp = property[ndx];
                // Get the value assoicated with the current property. Value
                // could
                // be a simple primitive wrapper class or another POJO.
                curObj = beanUtil.getPropertyValue(curProp);

                // Bail out if we have reached the next to the last property
                // which should be a class.
                if ((ndx + 2) > total) {
                    this.property = property[ndx + 1];
                    break;
                }
            } catch (NotFoundException e) {
                msg = e.getMessage();
                PageVariableHelper.logger.log(Level.ERROR, msg);
                throw new SystemException(msg);
            }
        }

        // Now setup a DaoApi instance using the curObj found above.
        try {
            BeanDao dao = (BeanDao) BeanDaoFactory.createApi();
            dao.retrieve(curObj);
            dao.nextRow();
            return dao;
        } catch (DatabaseException e) {
            msg = e.getMessage();
            logger.log(Level.ERROR, msg);
            throw new SystemException(msg);
        }

    }

    /**
     * Apply formatting to the value found in <i>expreObj</i> using the current
     * property, <i>property</i>, which was set for this instance.
     * 
     * @param exprObj
     *            The variable expression which is a DaoApi or primitive wrapper
     *            object.
     * @param format
     *            The format mask to apply. When null, formatting is not
     *            applied.
     * @return The formatted value or the value as-is when <i>format</i> is
     *         null.
     * @throws SystemException
     *             General Dao exceptions.
     */
    public String formatValue(Object exprObj, String format)
            throws SystemException {
        int javaType = 0;
        String msg;
        String value = null;

        if (exprObj instanceof BeanDao) {
            try {
                javaType = ((BeanDao) exprObj).getJavaType(this.property);
                value = ((BeanDao) exprObj).getColumnValue(this.property);
            } catch (Exception e) {
                msg = e.getMessage() == null ? "Failure to process JSP datasource property, "
                        + this.property
                        : e.getMessage();
                PageVariableHelper.logger.log(Level.ERROR, msg);
                throw new SystemException(msg);
            }
        }
        else if (exprObj instanceof DaoApi) {
            try {
                if (((DaoApi) exprObj).getDataSourceAttib() != null) {
                    Integer type = (Integer) ((DaoApi) exprObj)
                            .getDataSourceAttib().getColumnAttribute(
                                    this.property, "javaType");
                    javaType = type.intValue();
                }
                else {
                    // Format XML element values...either dates are numbers at
                    // this time.
                    javaType = Types.VARCHAR;
                    if (format != null) {
                        if (exprObj instanceof XmlDao) {
                            if ((format.indexOf("#") + format.indexOf("0")) >= 0) {
                                javaType = Types.DOUBLE;
                            }
                            // All possible date time formatting pattern letters
                            if (RMT2Date.isFormat(format)) {
                                javaType = Types.DATE;
                            }
                        }
                    }
                }
                value = ((DaoApi) exprObj).getColumnValue(this.property);
            } catch (Exception e) {
                msg = e.getMessage();
                PageVariableHelper.logger.log(Level.ERROR, msg);
                throw new SystemException(msg);
            }
        }
        else if (RMT2Utility.isWrapperType(exprObj)) {
            javaType = RMT2Utility.getJavaType(exprObj);
            switch (javaType) {
                case java.sql.Types.TIMESTAMP:
                case java.sql.Types.DATE:
                case java.sql.Types.TIME:
                    value = RMT2Date.formatDate((java.util.Date) exprObj,
                            "MM-dd-yyyy hh:mm:ss");
                    break;
                default:
                    value = exprObj.toString();
            }
        }

        return this.formatValue(value, javaType, format);
    }

    /**
     * Format <i>value</i> using the formatting mask as <i>format</i>.
     * <i>javaType</i> is used to identify special processing requirments for
     * when <i>value</i> is derived from one of the following primitive wrapper
     * types: java.util.Date, Integer, Float, Double, or SmallInt.
     * 
     * @param value
     *            The value to be formatted.
     * @param javaType
     *            One of the java.sql.Types values.
     * @param format
     *            The format that is to be applied. When null, no formatting is
     *            applied.
     * @return Formatted value.
     * @throws SystemException
     *             The date or numeric value cannot be formatted.
     */
    public String formatValue(String value, int javaType, String format)
            throws SystemException {

        if (format != null && value != "" && value != null) {
            switch (javaType) {
                case java.sql.Types.TIMESTAMP:
                case java.sql.Types.DATE:
                case java.sql.Types.TIME:
                    java.util.Date date = RMT2Date.stringToDate(value);
                    value = RMT2Date.formatDate(date, format);
                    break;

                case java.sql.Types.INTEGER:
                case java.sql.Types.FLOAT:
                case java.sql.Types.DOUBLE:
                case java.sql.Types.SMALLINT:
                    value = RMT2Money.formatNumber(new Double(value), format);
                    break;
            }
        }
        return value;
    }

    /**
     * Determines the integer equivalent of a JSP variable scope using _scope.
     * 
     * @param _scope
     *            A String that represents the name of one of the JSP variable
     *            scopes. Valid values are:
     *            <ul>
     *            <li>PageContext.PAGE_SCOPE - Page context</li>
     *            <li>PageContext.REQUEST_SCOPE - Request context</li>
     *            <li>PageContext.SESSION_SCOPE - Session context</li>
     *            <li>PageContext.APPLICATION_SCOPE - Application context</li>
     *            </ul>
     * 
     * @return int as one of the PageContext's JSP variable scope values:
     * @throws SystemException
     *             When <i>scope</i> cannot be translated.
     */
    public static int translateScope(String scope) throws SystemException {
        if (scope.equalsIgnoreCase(RMT2BodyTagSupportBase.PAGE_ID)) {
            return PageContext.PAGE_SCOPE;
        }
        else if (scope.equalsIgnoreCase(RMT2BodyTagSupportBase.REQUEST_ID)) {
            return PageContext.REQUEST_SCOPE;
        }
        else if (scope.equalsIgnoreCase(RMT2BodyTagSupportBase.SESSION_ID)) {
            return PageContext.SESSION_SCOPE;
        }
        else if (scope.equalsIgnoreCase(RMT2BodyTagSupportBase.APPLICATION_ID)) {
            return PageContext.APPLICATION_SCOPE;
        }
        throw new SystemException("Scope, " + scope + ", cannot be translated.");
    }

    /**
     * Get the name of the property set for the current expression variable.
     * 
     * @return String The property name.
     */
    public String getProperty() {
        return property;
    }

    /**
     * Parses source by identifying and packaging all Variable Expression
     * Language (VEL) tokens into a String array.
     * 
     * @param source
     *            The expression containing one or more VEL tokens
     * @return A String array of all tokens found. Each token will be stripped
     *         of its surrounding braces.
     */
    public String[] splitExpressionTokens(String source) {
        List list = new ArrayList<String>();
        int begPos;
        int endPos;
        String exp;
        begPos = source.indexOf("{");
        while (begPos > 0) {
            endPos = source.indexOf("}", begPos);
            exp = source.substring(begPos + 1, endPos);
            list.add(exp);
            begPos = source.indexOf("{", endPos);
        }

        String exps[] = null;
        if (list.size() > 0) {
            exps = (String[]) list.toArray(new String[list.size()]);
        }
        return exps;
    }

    /**
     * Binds the value of the context variable to one or more Variable
     * Expression Language (VEL) tokens that may exist in <i>expression</i>. A
     * VEL token serves as place holder for a given context variable used in
     * complex expressions. The value is identified at runtime. A token is
     * basically a VEL variable surrounded by braces "{ }" which signals to the
     * VEL processor to treat as a place holder for actual variable values.
     * <p>
     * For example, we are using an XPath expression in the XML custom tag api
     * to filter results the value of an XML element, service type id:
     * 
     * <pre>
     *    &lt;xml:InputControl dataSource="xmlDoc" 
     *                      query="//general_codes[code_id = <b>{#rec.servType}</b>]" 
     *                      property="longdesc"/&gt;
     * </pre>
     * 
     * @param pageCtx
     *            The JSP page context which holds the data needed to bind
     *            variable expressions.
     * @param expression
     *            The expression containing one or more VEL tokens.
     * @return String <i>expression</i> with the tokens replaced with actual
     *         values.
     * @throws SystemException
     *             Problem obtaining the value for a token.
     */
    public String bindTokenValues(PageContext pageCtx, String expression)
            throws SystemException {
        String exp = expression;
        String parms[] = this.splitExpressionTokens(expression);

        if (parms == null || parms.length <= 0) {
            return expression;
        }

        for (int ndx = 0; ndx < parms.length; ndx++) {
            String token = "\\{" + parms[ndx] + "\\}";
            String value = null;
            value = (String) this.getValue(pageCtx, parms[ndx], null, null);
            try {
                Double.parseDouble(value);
            } catch (NumberFormatException e) {
                value = "\'" + value + "\'";
            }
            exp = exp.replaceAll(token, value);
        }
        return exp;
    }

}
