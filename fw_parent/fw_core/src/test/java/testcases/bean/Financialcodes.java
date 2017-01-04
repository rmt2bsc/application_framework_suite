package testcases.bean;

import java.util.Date;

import com.SystemException;
import com.api.persistence.db.orm.OrmBean;

/**
 * Peer object that maps to the financialcodes database table/view.
 * 
 * 
 */
public class Financialcodes extends OrmBean {

    // Property name constants that belong to respective DataSource,
    // FinancialcodesView

    /**
     * The property name constant equivalent to property, Code, of respective
     * DataSource view.
     */
    public static final String PROP_CODE = "Code";
    /**
     * The property name constant equivalent to property, Type, of respective
     * DataSource view.
     */
    public static final String PROP_TYPE = "Type";
    /**
     * The property name constant equivalent to property, Description, of
     * respective DataSource view.
     */
    public static final String PROP_DESCRIPTION = "Description";

    /** The javabean property equivalent of database column financialcodes.code */
    private char code;
    /** The javabean property equivalent of database column financialcodes.type */
    private char type;
    /**
     * The javabean property equivalent of database column
     * financialcodes.description
     */
    private char description;

    // Getter/Setter Methods

    /**
     * Default constructor.
     * 
     * 
     */
    public Financialcodes() throws SystemException {
        super();
    }

    /**
     * Sets the value of member variable code
     * 
     * 
     */
    public void setCode(char value) {
        this.code = value;
    }

    /**
     * Gets the value of member variable code
     * 
     * 
     */
    public char getCode() {
        return this.code;
    }

    /**
     * Sets the value of member variable type
     * 
     * 
     */
    public void setType(char value) {
        this.type = value;
    }

    /**
     * Gets the value of member variable type
     * 
     * 
     */
    public char getType() {
        return this.type;
    }

    /**
     * Sets the value of member variable description
     * 
     * 
     */
    public void setDescription(char value) {
        this.description = value;
    }

    /**
     * Gets the value of member variable description
     * 
     * 
     */
    public char getDescription() {
        return this.description;
    }

    /**
     * Stubbed initialization method designed to implemented by developer.
     * 
     * 
     * 
     */
    public void initBean() throws SystemException {
    }
}