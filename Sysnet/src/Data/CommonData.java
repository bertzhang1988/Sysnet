package Data;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

import Utility.DataConnection;
import Utility.Function;

public class CommonData {
	public static HashMap<String, Object> CheckEQPStatusUpdate(String SCAC, String TrailerNB) throws SQLException {

		Connection conn3 = DataConnection.getConnection();
		Connection conn2 = DataConnection.getDevConnection();
		Statement stat = conn3.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
		String query3 = "select * from  SLH_TRAILER  where TRAILER_NUMBER='" + TrailerNB + "' and TRAILER_PREFIX='"
				+ SCAC + "'";
		ResultSet rs = stat.executeQuery(query3);
		HashMap<String, Object> TrailerInfo = new HashMap<String, Object>();
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
		rs.absolute(1);
		String DEST_TERMINAL = rs.getString("DEST_TERMINAL");
		String LastReportTerminal = rs.getString("LAST_REPORTED_TRM");
		Date LAST_REPORTED_TIME_DT = rs.getTimestamp("LAST_REPORTED_TIME_DT");
		String LstReportTime = Function.getLocalTimeReport(LastReportTerminal, LAST_REPORTED_TIME_DT, conn2);
		TrailerInfo.put("Cur Trm", LastReportTerminal);
		TrailerInfo.put("Dst", DEST_TERMINAL);
		TrailerInfo.put("Lst Rptd Time", LstReportTime);
		DataConnection.CloseDB(conn3, stat, rs);
		conn2.close();

		return TrailerInfo;
	}

}
