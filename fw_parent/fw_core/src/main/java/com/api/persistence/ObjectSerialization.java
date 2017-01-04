package com.api.persistence;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Utility class that provides common functionality for persisting object
 * (Serialization/deserialization).
 * 
 * @author rterrell
 * 
 */
public class ObjectSerialization {

    /**
     * Creates a AppUtil.java object.
     */
    public ObjectSerialization() {
        return;
    }

    /**
     * Saves the state of an object to disk.
     * 
     * @param obj
     *            the object that is to be serialized
     * @param fileName
     *            the path and file name where the object is to be persisted
     * @throws CannotPersistException
     */
    public static final void backupObject(Serializable obj, String fileName)
            throws CannotPersistException {
        String msg = null;
        try {
            // Serialize to a file
            ObjectOutput out = new ObjectOutputStream(new FileOutputStream(
                    fileName));
            out.writeObject(obj);
            out.close();
        } catch (IOException e) {
            throw new CannotPersistException(msg, e);
        }
    }

    /**
     * Restores the state of an object from disk.
     * 
     * @param fileName
     *            the path and file name of the object serialized source.
     * @return an instance of {@link Serializable}
     * @throws CannotRetrieveException
     */
    public static final Serializable restoreObject(String fileName)
            throws CannotRetrieveException {
        Serializable obj = null;
        String msg = null;

        // Read from disk using FileInputStream
        try {
            FileInputStream f_in = new FileInputStream(fileName);
            // Read object using ObjectInputStream
            ObjectInputStream obj_in = new ObjectInputStream(f_in);

            // Read an object
            obj = (Serializable) obj_in.readObject();
            return obj;
        } catch (FileNotFoundException e) {
            throw new CannotRetrieveException(msg, e);
        } catch (ClassNotFoundException e) {
            throw new CannotRetrieveException(msg, e);
        } catch (IOException e) {
            throw new CannotRetrieveException(msg, e);
        }
    }

}
