package com.api;

/**
 * An interface that defines the available methods to manage the construction
 * and deconstruction of a Product. The implementation should maintain a
 * reference to the source that contains the data needed to assemble/disassemble
 * the Product.
 * 
 * @author appdev
 * 
 */
public interface ProductBuilder {
    /**
     * The implementation of this method should focus on the step-by-step
     * process that constructs a {@link com.api.Product Product} derived object.
     * <p>
     * Merges all the separate product components into one form from a generic
     * data source. It is required of the generic data source to contain data
     * that exist in a certain format where each Product component can be
     * identifies as a separate entity.
     * 
     * @throws ProductBuilderException
     */
    void assemble() throws ProductBuilderException;

    /**
     * The implementation of this method should focus on the step-by-step
     * process that deconstructs a {@link com.api.Product Product} derived
     * object.
     * 
     * @throws ProductBuilderException
     */
    void disAssemble() throws ProductBuilderException;

    /**
     * Obtains the results of the building process as a single Product object.
     * 
     * @return {@link Product}
     */
    Product getProduct();

}
