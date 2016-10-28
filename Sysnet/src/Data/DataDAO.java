package Data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.TimeZone;

import Utility.DataConnection;
import Utility.Function;
import page.Trailer;

public class DataDAO {

	public Trailer GetTrailer(String SCAC, String TrailerNB) throws SQLException {
		Connection cn = DataConnection.getConnection();
		Connection conn2 = DataConnection.getDevConnection();
		Statement stat;
		Trailer trailer;
		ResultSet rs;
		stat = cn.createStatement();
		String query3 = "select * from  SLH_TRAILER  where TRAILER_NUMBER='" + TrailerNB + "' and TRAILER_PREFIX='"
				+ SCAC + "'";
		rs = stat.executeQuery(query3);
		trailer = new Trailer();
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
		rs.next();
		String DEST_TERMINAL = rs.getString("DEST_TERMINAL");
		String LastReportTerminal = rs.getString("LAST_REPORTED_TRM");
		Date LAST_REPORTED_TIME_DT = rs.getTimestamp("LAST_REPORTED_TIME_DT");
		String LstReportTime = Function.getLocalTimeReport(LastReportTerminal, LAST_REPORTED_TIME_DT, conn2);
		trailer.setSCAC(SCAC);
		trailer.setTrailerNb(TrailerNB);
		trailer.setDest(DEST_TERMINAL);
		trailer.setCurTerminal(LastReportTerminal);
		trailer.setDest(DEST_TERMINAL);
		trailer.setLastReportTime(LstReportTime);
		conn2.close();
		DataConnection.CloseDB(cn, stat, rs);
		return trailer;
	}

	public LinkedHashSet<ArrayList<String>> GetTrailerInforReport(LinkedHashSet<ArrayList<String>> TrailerReportList)
			throws SQLException {
		Connection cn = DataConnection.getConnection();
		Connection conn2 = DataConnection.getConnection();
		String query3 = "select * from  SLH_TRAILER  where TRAILER_NUMBER=? and TRAILER_PREFIX=?";
		PreparedStatement stat = cn.prepareStatement(query3);
		String SCAC;
		String TrailerNB;
		ResultSet rs = null;
		LinkedHashSet<ArrayList<String>> ExpectedTrailerReportList = new LinkedHashSet<ArrayList<String>>();
		for (ArrayList<String> TrailerLine : TrailerReportList) {
			SCAC = TrailerLine.get(0);
			TrailerNB = TrailerLine.get(1);
			stat.setString(1, TrailerNB);
			stat.setString(2, SCAC);
			rs = stat.executeQuery();
			TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
			rs.next();
			String DEST_TERMINAL = rs.getString("DEST_TERMINAL");
			String LastReportTerminal = rs.getString("LAST_REPORTED_TRM");
			Date LAST_REPORTED_TIME_DT = rs.getTimestamp("LAST_REPORTED_TIME_DT");
			System.out.println(LAST_REPORTED_TIME_DT + SCAC + TrailerNB);
			String LstReportTime = Function.getLocalTimeReport(LastReportTerminal, LAST_REPORTED_TIME_DT, conn2);
			ArrayList<String> ExpectedTrailerLine = new ArrayList<String>();
			ExpectedTrailerLine.add(SCAC);
			ExpectedTrailerLine.add(TrailerNB);
			ExpectedTrailerLine.add(DEST_TERMINAL);
			ExpectedTrailerLine.add(LastReportTerminal);
			ExpectedTrailerLine.add(LstReportTime);
			ExpectedTrailerReportList.add(ExpectedTrailerLine);
		}
		conn2.close();

		return ExpectedTrailerReportList;
	}

}