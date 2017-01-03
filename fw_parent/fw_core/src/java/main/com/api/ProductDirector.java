package com.api;

import com.RMT2BaseBean;
import com.SystemException;

/**
 * 
 * @author appdev
 * 
 */
public class ProductDirector extends RMT2BaseBean {
    private static final long serialVersionUID = 8235346160587691304L;

    /**
     * Creates an empty ProductDirector object that is not capable of building
     * anything.
     * 
     * @throws SystemException
     */
    protected ProductDirector() throws SystemException {
        super();
    }

    /**
     * Initialzes ProductDirector
     * 
     * @see bean.RMT2BaseBean#initBean()
     */
    @Override
    public void initBean() throws SystemException {
        return;
    }

    /**
     * Uses a specific ProductBuilder implementation associated with this
     * instance to create an assemble Product.
     * 
     * @param builder
     *            the name of the selected ProductBuilder implementation.
     * @return {@link Product}, or null if the ProductBuilder could not be
     *         obtained.
     */
    public static Product construct(ProductBuilder builder)
            throws ProductBuilderException {
        if (builder == null) {
            return null;
        }
        try {
            builder.assemble();
            return builder.getProduct();
        } catch (ProductBuilderException e) {
            throw new ProductBuilderException(e);
        }
    }

    /**
     * Uses a specific ProductBuilder implementation associated with this
     * instance to present a disassembled Product.
     * 
     * @param builder
     *            the name of the selected ProductBuilder implementation.
     * @return {@link Product}, or null if the ProductBuilder could not be
     *         obtained.
     * @throws ProductBuilderException
     */
    public static Product deConstruct(ProductBuilder builder)
            throws ProductBuilderException {
        if (builder == null) {
            return null;
        }
        try {
            builder.disAssemble();
            return builder.getProduct();
        } catch (ProductBuilderException e) {
            throw new ProductBuilderException(e);
        }
    }
}
