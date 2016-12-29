package com.api.foundation;

/**
 * The base class for all DTO's
 * 
 * @author Roy Terrell
 * 
 */
public interface BaseDto {

    /**
     * Set the header information for the DTO
     * 
     * @param value
     *            String
     */
    void setHeader(String value);

    /**
     * Gets the header information for the DTO
     * 
     * @return String
     */
    String getHeader();
    
    /**
     * Sets the value of custom criteria
     */
    void setCriteria(String value);

    /**
     * Gets the value of custom criteria
     */
    String getCriteria();
}
