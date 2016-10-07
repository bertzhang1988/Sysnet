package Utility;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class Function {

	public static Date gettime(String timezone) {
		Calendar c = Calendar.getInstance(TimeZone.getTimeZone(timezone));
		return c.getTime();
	}

	public static String GetTimeValue(String timezone) {
		SimpleDateFormat f = new SimpleDateFormat("YYYY-MM-dd--HH-mm-ss");
		Calendar c = Calendar.getInstance();
		f.setTimeZone(TimeZone.getTimeZone(timezone));
		return f.format(c.getTime());

	}

	public static Date getLocalTime(String terminal, Date Utctime) throws SQLException {
		Connection conn2 = DataConnection.getConnection();
		String query2 = " SELECT TERMINAL,TIME_ZONE,DAYLIGHT_SAVINGS_IN,TIME_ZONE_CD FROM SLH_TERMINAL where terminal=? ";
		PreparedStatement stat = conn2.prepareStatement(query2, ResultSet.TYPE_SCROLL_SENSITIVE,
				ResultSet.CONCUR_READ_ONLY);
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
		Calendar cal = Calendar.getInstance();
		cal.setTime(Utctime);
		int year = cal.get(Calendar.YEAR);
		stat.setString(1, terminal);
		ResultSet rs = stat.executeQuery();
		rs.absolute(1);
		int Offset_NB = rs.getInt("TIME_ZONE");
		String Daylight_Savings_IN = rs.getString("DAYLIGHT_SAVINGS_IN");
		if (rs != null)
			rs.close();
		if (stat != null)
			stat.close();
		if (conn2 != null)
			conn2.close();
		ArrayList<Date> Range = GetDstRange(year);
		if (Daylight_Savings_IN.equalsIgnoreCase("Y")) {
			if (Utctime.after(Range.get(0)) && Utctime.before(Range.get(1))) {
				cal.add(Calendar.HOUR, -Offset_NB + 2 + 1);

			} else {
				cal.add(Calendar.HOUR, -Offset_NB + 2);
			}
		} else {
			cal.add(Calendar.HOUR, -Offset_NB + 2);
		}
		Date ConvertedTime = cal.getTime();
		return ConvertedTime;
	}

	public static String getLocalTimeReport(String terminal, Date Utctime, Connection conn2) throws SQLException {

		// Connection conn2 = DataConnection.getDevConnection();
		String query2 = " SELECT TERMINAL,TIME_ZONE,DAYLIGHT_SAVINGS_IN,TIME_ZONE_CD FROM SLH_TERMINAL where terminal=? ";
		PreparedStatement stat = conn2.prepareStatement(query2, ResultSet.TYPE_SCROLL_SENSITIVE,
				ResultSet.CONCUR_READ_ONLY);
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
		Calendar cal = Calendar.getInstance();
		cal.setTime(Utctime);
		int year = cal.get(Calendar.YEAR);
		stat.setString(1, terminal);
		ResultSet rs = stat.executeQuery();
		rs.absolute(1);
		int Offset_NB = rs.getInt("TIME_ZONE");
		String Daylight_Savings_IN = rs.getString("DAYLIGHT_SAVINGS_IN");
		String TIME_ZONE_CD = rs.getString("TIME_ZONE_CD");
		if (rs != null)
			rs.close();
		if (stat != null)
			stat.close();
		// if (conn2 != null)conn2.close();
		ArrayList<Date> Range = GetDstRange(year);
		if (Daylight_Savings_IN.equalsIgnoreCase("Y")) {
			if (Utctime.after(Range.get(0)) && Utctime.before(Range.get(1))) {
				cal.add(Calendar.HOUR, -Offset_NB + 2 + 1);
				TIME_ZONE_CD = TIME_ZONE_CD.replace("S", "D");

			} else {
				cal.add(Calendar.HOUR, -Offset_NB + 2);
			}
		} else {
			cal.add(Calendar.HOUR, -Offset_NB + 2);
		}
		Date ConvertedTime = cal.getTime();
		SimpleDateFormat LSRformat = new SimpleDateFormat("MM-dd-yy HH:mm");
		String LstReportTime = LSRformat.format(ConvertedTime);
		LstReportTime = LstReportTime + " " + TIME_ZONE_CD;
		return LstReportTime;
	}

	public static ArrayList<Date> GetDstRange(int year) {
		String[][] DstRange = { { "03/13/2016 02:00:00.000", "11/06/2016 02:00:00.000" },
				{ "03/12/2017 02:00:00.000", "11/05/2017 02:00:00.000" },
				{ "03/11/2018 02:00:00.000", "11/04/2018 02:00:00.000" },
				{ "03/10/2019 02:00:00.000", "11/03/2019 02:00:00.000" },
				{ "03/08/2020 02:00:00.000", "11/01/2020 02:00:00.000" },
				{ "03/14/2021 02:00:00.000", "11/07/2021 02:00:00.000" },
				{ "03/13/2022 02:00:00.000", "11/06/2022 02:00:00.000" },
				{ "03/12/2023 02:00:00.000", "11/05/2023 02:00:00.000" },
				{ "03/10/2024 02:00:00.000", "11/03/2024 02:00:00.000" },
				{ "03/09/2025 02:00:00.000", "11/02/2025 02:00:00.000" },
				{ "03/08/2026 02:00:00.000", "11/01/2026 02:00:00.000" } };
		SimpleDateFormat Dstformat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss.SSS");
		ArrayList<Date> Range = new ArrayList<Date>();
		try {
			Range.add(Dstformat.parse(DstRange[year - 2016][0]));
			Range.add(Dstformat.parse(DstRange[year - 2016][1]));
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return Range;

	}

	public static Date ConvertUtcTime(String terminal, Date LocalTime) throws SQLException {

		Connection conn2 = DataConnection.getConnection();
		String query2 = " SELECT OPSF.Facility_CD,OPSF.utc_Offset_NB,OPSF.Daylight_Savings_IN, SS.[Calendar_Year_NB],SS.[Daylight_Savings_Start_TS],SS.[Daylight_Savings_End_TS] FROM EQP.Facility_vw OPSF, Shared.Daylight_Savings_Schedule SS WHERE  OPSF.Country_abbreviated_NM=SS.ISO_3_Country_CD AND Calendar_Year_NB= ? and OPSF.Facility_CD= ? ";
		PreparedStatement stat = conn2.prepareStatement(query2, ResultSet.TYPE_SCROLL_SENSITIVE,
				ResultSet.CONCUR_READ_ONLY);
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
		Calendar cal = Calendar.getInstance();
		cal.setTime(LocalTime);
		int year = cal.get(Calendar.YEAR);
		stat.setInt(1, year);
		stat.setString(2, terminal);
		ResultSet rs = stat.executeQuery();
		rs.absolute(1);
		int Offset_NB = rs.getInt("utc_Offset_NB");
		String Daylight_Savings_IN = rs.getString("Daylight_Savings_IN");
		Timestamp Daylight_Savings_Start_TS = rs.getTimestamp("Daylight_Savings_Start_TS");
		Timestamp Daylight_Savings_End_TS = rs.getTimestamp("Daylight_Savings_End_TS");
		if (rs != null)
			rs.close();
		if (stat != null)
			stat.close();
		if (conn2 != null)
			conn2.close();

		Date ConvertedTime;
		if (Daylight_Savings_IN.equalsIgnoreCase("Y")) {
			if (LocalTime.after(Daylight_Savings_Start_TS) && LocalTime.before(Daylight_Savings_End_TS)) {
				cal.add(Calendar.HOUR, -Offset_NB - 1);
			} else {
				cal.add(Calendar.HOUR, -Offset_NB);
			}
		} else {
			cal.add(Calendar.HOUR, -Offset_NB);
		}

		ConvertedTime = cal.getTime();
		return ConvertedTime;

	}

	public static Date ConvertLastReportTime(int LAST_REPORTED_TIME) {
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		Date BASE = null;
		try {
			BASE = dateFormat.parse("1980-01-01");
		} catch (ParseException pe) {
			pe.getStackTrace();
		}
		cal.setTime(BASE);
		cal.add(Calendar.MINUTE, LAST_REPORTED_TIME);
		return cal.getTime();
	}

	public static String GetDisplayTime() throws SQLException {
		Connection cn = DataConnection.getConnection();
		String query = "select DISPLAY_TIME from SLH_CONTROL ";
		Statement st = cn.createStatement();
		ResultSet rs = st.executeQuery(query);
		rs.next();
		String DISPLAY_TIME = rs.getString("DISPLAY_TIME");
		DataConnection.CloseDB(cn, st, rs);
		return DISPLAY_TIME;
	}

}
