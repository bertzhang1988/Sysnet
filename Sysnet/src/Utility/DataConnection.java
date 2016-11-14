package Utility;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public final class DataConnection {

	public static Connection getConnection() {
		ConfigRd conf = new ConfigRd();
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		Connection cn = null;
		try {
			cn = DriverManager.getConnection(conf.GetDatabase(), conf.GetDbUserName(), conf.GetDbPassword());
		} catch (SQLException e) {

			e.printStackTrace();
		}
		try {
			cn.createStatement().execute("alter session set current_schema=" + conf.GetUserSchema() + " ");
		} catch (SQLException e) {

			e.printStackTrace();
		}
		return cn;
	}

	public static Connection getSitConnection() {
		ConfigRd conf = new ConfigRd();
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		Connection cn = null;
		try {
			cn = DriverManager.getConnection(conf.GetSitDatabase(), conf.GetDbUserName(), conf.GetDbPassword());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			cn.createStatement().execute("alter session set current_schema=" + conf.GetSitUserSchema() + " ");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return cn;
	}

	public static Connection getDevConnection() {
		ConfigRd conf = new ConfigRd();
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		Connection cn = null;
		try {
			cn = DriverManager.getConnection(conf.GetDevDatabase(), conf.GetDbUserName(), conf.GetDbPassword());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			cn.createStatement().execute("alter session set current_schema=" + conf.GetDevUserSchema() + " ");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return cn;
	}

	public static Connection getQaConnection() {
		ConfigRd conf = new ConfigRd();
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		Connection cn = null;
		try {
			cn = DriverManager.getConnection(conf.GetQaDatabase(), conf.GetDbUserName(), conf.GetDbPassword());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			cn.createStatement().execute("alter session set current_schema=" + conf.GetQaUserSchema() + " ");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return cn;
	}

	public static void CloseDB(Connection cn, Statement stat, ResultSet rs) {
		try {
			if (rs != null)
				rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (stat != null)
					stat.close();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {

				try {
					if (cn != null)
						cn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
