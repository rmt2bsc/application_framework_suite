package com.api.foundation;

import com.RMT2Base;

/**
 * @author Roy Terrell
 * 
 */
public abstract class AbstractBaseDtoImpl extends RMT2Base implements BaseDto {

    private String header;
    
    private String customCriteria;

    /**
     * 
     */
    public AbstractBaseDtoImpl() {
        super();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.foundation.BaseDto#setHeader(java.lang.String)
     */
    @Override
    public void setHeader(String value) {
        this.header = value;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.foundation.BaseDto#getHeader()
     */
    @Override
    public String getHeader() {
        return this.header;
    }

    
    @Override
    public void setCriteria(String value) {
        this.customCriteria = value;
    }

    @Override
    public String getCriteria() {
        return this.customCriteria;
    }
}
