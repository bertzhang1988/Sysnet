package Data;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.TimeZone;


import Utility.DataConnection;
import Utility.Function;

public class CommonData {

	public static HashMap<String, Object> CheckEQPStatusUpdate(String SCAC, String TrailerNB) throws SQLException {

		Connection conn3 = DataConnection.getConnection();
		Connection conn2 = DataConnection.getConnection();
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

	public static String GetSchedule(String query) throws SQLException {
		Connection cn = DataConnection.getConnection();
		Statement stat = cn.createStatement();
		ResultSet rs = stat.executeQuery(query);
		rs.next();
		int pup = rs.getInt("pup");
		int van = rs.getInt("VAN");
		DataConnection.CloseDB(cn, stat, rs);
		DecimalFormat df = new DecimalFormat("#.#");
		String schedule = String.valueOf(df.format((float) pup / 2 + van));
		return schedule;
	}

	public static String GetScheduleForLoadedRail() throws SQLException {
		return GetSchedule(Query.query2);
	}

	public static String GetScheduleForEmptyRail() throws SQLException {
		return GetSchedule(Query.query1);

	}

	public static String GetScheduleForInterRegionalRoadEmpty() throws SQLException {
		return GetSchedule(Query.query3);
	}

	public static String GetScheduleForIntraRegionalRoadEmpty() throws SQLException {
		return GetSchedule(Query.query4);
	}

	public static LinkedHashSet<ArrayList<String>> GetRoutePlanForm(String RegionalType) throws SQLException {
		RegionalType = RegionalType.toUpperCase();
		Connection cn = DataConnection.getConnection();
		PreparedStatement st = cn.prepareStatement(Query.queryRoutePlan);
		st.setString(1, RegionalType);
		ResultSet rs = st.executeQuery();
		String Terminal;
		String YRC_OTR;
		String RAIL_LOADS;
		String PT_LOADS;
		String ETME_COUNT;
		String LATE_COUNT;
		LinkedHashSet<ArrayList<String>> ExpectedRoutePlanList = new LinkedHashSet<ArrayList<String>>();
		while (rs.next()) {
			Terminal = rs.getString("TERMINAL") + "/" + rs.getString("TERMINAL_ALPHA");
			YRC_OTR = rs.getString("YRC_OTR");
			RAIL_LOADS = rs.getString("RAIL_LOADS");
			PT_LOADS = rs.getString("PT_LOADS");
			ETME_COUNT = rs.getString("ETME_COUNT");
			LATE_COUNT = rs.getString("LATE_COUNT");
			ArrayList<String> line = new ArrayList<String>();
			line.add(Terminal);
			line.add(YRC_OTR);
			line.add(RAIL_LOADS);
			line.add(PT_LOADS);
			line.add(ETME_COUNT);
			line.add(LATE_COUNT);
			line.removeAll(Collections.singleton("0"));
			ExpectedRoutePlanList.add(line);
		}
		DataConnection.CloseDB(cn, st, rs);
		return ExpectedRoutePlanList;

	}

	public static LinkedHashSet<ArrayList<String>> GetLinehaulForm() throws SQLException {
		//TerminalType = TerminalType.toUpperCase();
		Connection cn = DataConnection.getConnection();
		PreparedStatement st = cn.prepareStatement(Query.queryLinehualResouce);
		//st.setString(1, TerminalType);
		ResultSet rs = st.executeQuery();
		String INDEX;
		String Terminal;
		String Alpha;
		String Intra;
		String Inter;
		String InterPT;
		String ToRail;
		String EnrRail;
		String FromRail;
		String InterRoadTime;
		String Avl;
		String PowerPlusMinus;
		String Idle;
		String DomicileIdle;
		String DomicileAvlRst;
		String ForeignAvlRstIb;
		String ForeignLayOver;
		String Pups;
		String Vans;
		String DispatchFrom;
		String InboundTo;
		String ETME;
		String ACEL;
		String STME;

		LinkedHashSet<ArrayList<String>> ExpectedLinehaulList = new LinkedHashSet<ArrayList<String>>();
		
		while (rs.next()) {
			INDEX=rs.getString("noid");
			Terminal = rs.getString("TERMINAL") ; 
			Alpha=rs.getString("ALPHA");
			Intra = rs.getString("intraRegLoads");
			Inter = rs.getString("interRegLoads");
			InterPT = rs.getString("ptLoadsOnHand");
			ToRail = rs.getString("railLoadsOnHand");
			EnrRail = rs.getString("enrRail");
			FromRail = rs.getString("fromRail");
			InterRoadTime = rs.getString("timeInArvLtg");
			Avl = rs.getString("powerTractors");
			PowerPlusMinus=rs.getString("powerpm");
			Idle = rs.getString("idleTractors");
			DomicileIdle = rs.getString("idleDrivers");
			DomicileAvlRst = rs.getString("domicileDrivers");
			ForeignAvlRstIb = rs.getString("foreignDrivers");
			ForeignLayOver = rs.getString("layoverForeignDrivers");
			Pups = rs.getString("emptyPups");
			Vans = rs.getString("emptyVans");
			DispatchFrom = rs.getString("emptyLoadsOrigin");
			InboundTo = rs.getString("emptyLoadsDestination");
			ETME = rs.getString("etmeCount");
			ACEL = rs.getString("acelCount");
			STME = rs.getString("stmeCount");
			
			ArrayList<String> line = new ArrayList<String>();
			line.add(INDEX);
			line.add(Terminal);
			line.add(Alpha);
			line.add(Intra);
			line.add(Inter);
			line.add(InterPT);
			line.add(ToRail);
			line.add(EnrRail);
			line.add(FromRail);
			line.add(InterRoadTime);
			line.add(Avl);
			line.add(PowerPlusMinus);
			line.add(Idle);
			line.add(DomicileIdle);
			line.add(DomicileAvlRst);
			line.add(ForeignAvlRstIb);
			line.add(ForeignLayOver);
			line.add(Pups);
			line.add(Vans);
			line.add(DispatchFrom);
			line.add(InboundTo);
			line.add(ETME);
			line.add(ACEL);
			line.add(STME);
			line.removeAll(Collections.singleton("0"));
			line.removeAll(Collections.singleton(null));
			ExpectedLinehaulList.add(line);
		}
		DataConnection.CloseDB(cn, st, rs);
		return ExpectedLinehaulList;

	}

}
