/**
 * Database connection manager
 */
package org.djw.db;

import java.sql.SQLException;

import javax.sql.DataSource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;
//import org.djw.tools.utils.GeneralUtils;

public class DBConMgr {

	private static DataSource ds = null;
	final static Logger logger = Logger.getLogger(DBConMgr.class);
	public static Connection getConnection() {
		Connection con = null;
		if (ds == null) {
			try {
				Context initContext = new InitialContext();
				Context envContext = (Context) initContext.lookup("java:/comp/env");
				String DBDescriptor = "";
				//TODO Setup ini file for app
				DBDescriptor = "jdbc/learning";
//				if (GeneralUtils.getConfigPropValue("db_descriptor") != null){
//			    	DBDescriptor = GeneralUtils.getConfigPropValue("db_descriptor");
//			    }
				ds = (DataSource) envContext.lookup(DBDescriptor);
			} catch (NamingException nx) {
				logger.fatal("Exception while getting the data source :" + nx);
			}
		}
		try {
			con = ds.getConnection();
		} catch (Exception e) {
			logger.fatal("Exception while getting connection :" + e);
		}
		return con;
	}

	public static void closeDBResources(Connection connection,
			Statement statement, ResultSet reSet) {
		if (reSet != null) {
			try {
				reSet.close();
			} catch (SQLException sqlEx) {
			}
		}
		reSet = null;
		if (statement != null) {
			try {
				statement.close();
			} catch (SQLException sqlEx) {
			}
			statement = null;
		}

		try {
			if (connection != null) {
				connection.close();
			}
		} catch (SQLException sqlEx) {
		}
		connection = null;
	}

	public static void closeDBResources(Connection connection,
			Statement statement) {
		if (statement != null) {
			try {
				statement.close();
			} catch (SQLException sqlEx) {
			}
			statement = null;
		}
		try {
			if (connection != null) {
				connection.close();
			}
		} catch (SQLException sqlEx) {
		}
		connection = null;
	}
}
