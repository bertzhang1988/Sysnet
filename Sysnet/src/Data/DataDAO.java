package Data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.TimeZone;

import Utility.DataConnection;
import Utility.Function;
import page.Trailer;

public class DataDAO {

	public Trailer GetTrailer(String SCAC, String TrailerNB) throws SQLException {
		Connection cn = DataConnection.getConnection();
		Connection conn2 = DataConnection.getConnection();
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
		String NEXT_TERMINAL_1 = rs.getString("NEXT_TERMINAL_1");
		Date LAST_REPORTED_TIME_DT = rs.getTimestamp("LAST_REPORTED_TIME_DT");
		Date NEXT_TTMS_1_DT = rs.getTimestamp("NEXT_TTMS_1_DT");
		Date IN_OUT_TIME_DT = rs.getTimestamp("IN_OUT_TIME_DT");
		String LstReportTime = Function.getLocalTimeReport(LastReportTerminal, LAST_REPORTED_TIME_DT, conn2);
		String TTMS = Function.getLocalTimeReport(LastReportTerminal, NEXT_TTMS_1_DT, conn2);
		String ETA = Function.getLocalTimeReport(NEXT_TERMINAL_1, IN_OUT_TIME_DT, conn2);
		trailer.setSCAC(SCAC);
		trailer.setTrailerNb(TrailerNB);
		trailer.setDest(DEST_TERMINAL);
		trailer.setCurTerminal(LastReportTerminal);
		trailer.setLastReportTime(LstReportTime);
		trailer.setTTMS(TTMS);
		trailer.setETA(ETA);
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
			String NEXT_TERMINAL_1 = rs.getString("NEXT_TERMINAL_1");
			Date LAST_REPORTED_TIME_DT = rs.getTimestamp("LAST_REPORTED_TIME_DT");
			Date NEXT_TTMS_1_DT = rs.getTimestamp("NEXT_TTMS_1_DT");
			Date IN_OUT_TIME_DT = rs.getTimestamp("IN_OUT_TIME_DT");
			String SCHED_DPRTR_FLAG = rs.getString("SCHED_DPRTR_FLAG");
			// System.out.println(LAST_REPORTED_TIME_DT + SCAC + TrailerNB);

			String LstReportTime;
			try {
				LstReportTime = Function.getLocalTimeReport(LastReportTerminal, LAST_REPORTED_TIME_DT, conn2);
			} catch (ArrayIndexOutOfBoundsException ae) {
				System.out.println(SCAC + "-" + TrailerNB + " LAST_REPORTED_TIME_DT: " + LAST_REPORTED_TIME_DT
						+ " is not in range of 2016-2026, can not judge DST");
				LstReportTime = null;
			}
			String TTMS;
			try {
				TTMS = Function.getLocalTimeReport(LastReportTerminal, NEXT_TTMS_1_DT, conn2);
			} catch (ArrayIndexOutOfBoundsException ae) {
				System.out.println(SCAC + "-" + TrailerNB + " NEXT_TTMS_1_DT: " + NEXT_TTMS_1_DT
						+ " is not in range of 2016-2026, can not judge DST");
				TTMS = null;
			}
			String ETA;
			try {
				ETA = Function.getLocalTimeReport(NEXT_TERMINAL_1, IN_OUT_TIME_DT, conn2);
			} catch (ArrayIndexOutOfBoundsException ae) {
				System.out.println(SCAC + "-" + TrailerNB + " IN_OUT_TIME_DT: " + IN_OUT_TIME_DT
						+ " is not in range of 2016-2026, can not judge DST");
				ETA = null;
			}
			String SDT = null;
			if (SCHED_DPRTR_FLAG.equalsIgnoreCase("Y")) {
				SDT = ETA;
				ETA = null;
			}

			ArrayList<String> ExpectedTrailerLine = new ArrayList<String>();
			ExpectedTrailerLine.add(SCAC); // 0
			ExpectedTrailerLine.add(TrailerNB); // 1
			ExpectedTrailerLine.add(DEST_TERMINAL);// 2
			ExpectedTrailerLine.add(LastReportTerminal);// 3
			ExpectedTrailerLine.add(LstReportTime);// 4
			ExpectedTrailerLine.add(TTMS);// 5
			ExpectedTrailerLine.add(ETA);// 6
			ExpectedTrailerLine.add(SDT);// 7
			ExpectedTrailerLine.add(NEXT_TERMINAL_1);//8
			Collections.replaceAll(ExpectedTrailerLine, null, "");
			ExpectedTrailerReportList.add(ExpectedTrailerLine);
		}
		conn2.close();

		return ExpectedTrailerReportList;
	}

}