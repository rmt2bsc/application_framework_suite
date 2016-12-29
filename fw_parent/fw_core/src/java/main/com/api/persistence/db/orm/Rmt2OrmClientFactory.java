package com.api.persistence.db.orm;

import org.apache.log4j.Logger;

import com.api.persistence.PersistenceClient;
import com.api.persistence.db.DatabaseConnectionBean;
import com.api.persistence.db.DatabaseConnectionFactory;

/**
 * Factory designed to create new instances pertaining to a RMT2 ORM mapper
 * client.
 * 
 * @author roy.terrell
 * 
 */
public class Rmt2OrmClientFactory {

    /**
     * Creates an instance of PersistenceClient interface using
     * {@link Rmt2OrmDatabaseClientImpl} implenetation.
     * <p>
     * It is understood that the DAO obtained is going to be manipulating a
     * configuration that designed for one and only one database.
     * 
     * @param con
     *            an instance of {@link DatabaseConnectionBean}
     * @return an instance of {@link PersistenceClient}
     */
    public static PersistenceClient createOrmClientInstance() {
        return createOrmClientInstance(null);
    }

    /**
     * Creates an instance of PersistenceClient interface using
     * {@link Rmt2OrmDatabaseClientImpl} implenetation.
     * <p>
     * The behavior characteristics of the DAO client obtain is expected to
     * possess the capability to manage multiple database configurations. These
     * database configurations are distinguished by <i>contextName</i>. Every
     * database resource property is fully qualified by <i>contextName</i> and
     * the character, "." to distinguish like properter declarations.
     * 
     * @param contextName
     * @return
     */
    public static PersistenceClient createOrmClientInstance(String contextName) {
        DatabaseConnectionFactory f = new DatabaseConnectionFactory();
        DatabaseConnectionBean con = f.getProviderConnection(contextName);
        if (con == null) {
            return null;
        }
        try {
            return new Rmt2OrmDatabaseClientImpl(con);
        } catch (OrmInitialziationException e) {
            Logger logger = Logger.getLogger(Rmt2OrmClientFactory.class);
            logger.error(e);
            e.printStackTrace();
            return null;
        }
    }
}
