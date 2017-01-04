/**
 * 
 */
package com.api.xml;

import java.util.List;

import com.NotFoundException;
import com.SystemException;
import com.api.DaoApi;

import com.api.persistence.DatabaseException;

/**
 * 
 * @author appdev
 * 
 */
public interface XmlDao extends DaoApi {

    /**
     * Retrieve the name of the current node.
     * 
     * @return String as the node name
     * @throws DatabaseException
     */
    String getCurrentNodeName() throws DatabaseException;

    /**
     * Verifies whether or not a node has children.
     * 
     * @return true if children exist and false otherwise.
     * @throws DatabaseException
     */
    boolean hasChildren() throws DatabaseException;

    /**
     * Verfifies whether or not a node identified as <i>element</i> has
     * children.
     * 
     * @param element
     *            The name of the node to target.
     * @return true if children exist and false otherwise.
     * @throws DatabaseException
     * @throws NotFoundException
     */
    boolean hasChildren(String element) throws DatabaseException,
            NotFoundException, SystemException;

    /**
     * Obtain the names of all children that exist for a given node.
     * 
     * @param element
     *            The name of the node to target.
     * @return List of Strings.
     * @throws DatabaseException
     * @throws NotFoundException
     * @throws SystemException
     */
    List getChildrenNames(String element) throws DatabaseException,
            NotFoundException, SystemException;
}
