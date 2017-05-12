package Utility;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.dbcp2.BasicDataSource;

/* create connection by using connection pool*/
public class DBCPconnection {
	static BasicDataSource ds = null;
	static {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			ds = new BasicDataSource();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

	}

	public static Connection GetConnection(String environment) {

		ConfigRd conf = new ConfigRd(environment);
		ds.setUrl(conf.GetDatabase());
		ds.setUsername(conf.GetDbUserName());
		ds.setPassword(conf.GetDbPassword());
		ds.setInitialSize(1);
		ds.setMaxTotal(5);
		ds.setTimeBetweenEvictionRunsMillis(60 * 1000);
		Connection Con = null;
		try {
			Con = ds.getConnection();
		} catch (SQLException e) {

			e.printStackTrace();
		}
		try {
			Con.createStatement().execute("alter session set current_schema=" + conf.GetUserSchema() + " ");
		} catch (SQLException e) {

			e.printStackTrace();
		}

		return Con;
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
