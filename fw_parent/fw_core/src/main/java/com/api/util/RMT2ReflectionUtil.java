package com.api.util;

import java.lang.reflect.Field;

//import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.log4j.Logger;

/**
 * Utility class for Java reflection related needs.
 *
 * @author Roy Terrell
 */
public class RMT2ReflectionUtil {

    private static final Logger LOGGER = Logger.getLogger(RMT2ReflectionUtil.class);

    /**
     * Get the value of any field from any Object, even if it's private.
     *
     * @param object The object
     * @param fieldName The name of the desired field
     * @return The value of the desired field
     */
    public static Object getFieldValue(Object object, String fieldName) {
        Object fieldValue = null;
        Field field = null;
        boolean accessFailure = false, fieldFailure = false;
        try {
            Class clazz = object.getClass();
            while (field == null && clazz != null) {    // Loop through heirarchy for field.
                try {
                    field = object.getClass().getDeclaredField(fieldName);
                    if(field!=null){
                        field.setAccessible(true);
                        fieldValue = field.get(object);
                    }
                } catch (NoSuchFieldException e) {
                    fieldFailure = true;
                    LOGGER.debug("NoSuchFieldException", e);
                } catch (IllegalAccessException e) {
                    accessFailure = true;
                    LOGGER.debug("IllegalAccessException", e);
                }
                clazz = clazz.getSuperclass();  // Get parent class if exists.
            }
            if (fieldValue == null) {
                if (fieldFailure) {
                    LOGGER.error("Could not extract " + fieldName + " from " + object);
                }
                if (accessFailure) {
                    LOGGER.error("Could not access " + fieldName + " from " + object);
                }
            }
        } catch (NullPointerException e) {
            LOGGER.error("Could not access null " + object == null ? "object in field" : "field in object", e);
        }
        return fieldValue;
    }
    
    /**
     * Check if property exists within an Object.
     *
     * @param object The object to search for field.
     * @param fieldName The name of the desired field to search.
     * @return <i>true</i> when <i>fieldName</i> is found.  Otherwise, <i>false</i>.
     */
    public static boolean doesFieldExists(Object object, String fieldName) {
        try {
            Class clazz = object.getClass();
            while (clazz != null) {    // Loop through heirarchy for field.
                try {
                    clazz.getDeclaredField(fieldName);
                    return true;
                } catch (NoSuchFieldException e) {
                    LOGGER.debug("NoSuchFieldException", e);
                } 
                clazz = clazz.getSuperclass();  // Get parent class if exists.
            }
        } catch (NullPointerException e) {
            LOGGER.error("Could not access null " + object == null ? "object in field" : "field in object", e);
        }
        return false;
    }
    
    /**
     * Assign a value to any field of any Object, even if it's private.
     *
     * @param object The object containing the field to update.
     * @param fieldName The name of the desired field
     * @param value The value to assign to the field
     */
    public static void setFieldValue(Object object, String fieldName, Object value) {
        Object fieldValue = null;
        Field field = null;
        boolean accessFailure = false, fieldFailure = false;
        try {
            Class clazz = object.getClass();
            while (field == null && clazz != null) {    // Loop through heirarchy for field.
                try {
                    field = object.getClass().getDeclaredField(fieldName);
                    if(field!=null){
                        field.setAccessible(true);
                        field.set(object, value);
                        return;
                    }
                } catch (NoSuchFieldException e) {
                    fieldFailure = true;
                    LOGGER.debug("NoSuchFieldException", e);
                } catch (IllegalAccessException e) {
                    accessFailure = true;
                    LOGGER.debug("IllegalAccessException", e);
                }
                clazz = clazz.getSuperclass();  // Get parent class if exists.
            }
            if (fieldValue == null) {
                if (fieldFailure) {
                    LOGGER.error("Could not extract " + fieldName + " from " + object);
                }
                if (accessFailure) {
                    LOGGER.error("Could not access " + fieldName + " from " + object);
                }
            }
        } catch (NullPointerException e) {
            LOGGER.error("Could not access null " + object == null ? "object in field" : "field in object", e);
        }
    }
}
