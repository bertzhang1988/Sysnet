package Data;

public class Query {

	/* get van and pup for empty rail */
	public static String query1 = "select count(case when  LENGTH >=35 THEN '+' END ) as van, count(case when  LENGTH <35 THEN '+' END ) as pup from  SLH_TRAILER where TRAILER_STATUS in (15,21) and NEXT_TERMINAL_1 like 'X%' and WEIGHT=0";

	/* get van and pup for loaded rail */
	public static String query2 = "select count(case when  LENGTH >=35 THEN '+' END ) as van, count(case when  LENGTH <35 THEN '+' END ) as pup from slh_trailer st, slh_leg sl where st.last_reported_trm=sl.begin_terminal and st.next_terminal_1=sl.end_terminal and st.next_leg_type_1=sl.leg_type and st.weight>0 and sl.leg_type='R' and sl.transportation_mode_type_cd='R'";

	/* get van and pup for INTER-REGIONAL ROAD empties */
	public static String query3 = "select count(case when  LENGTH >=35 THEN '+' END ) as van, count(case when  LENGTH <35 THEN '+' END ) as pup from  SLH_TRAILER where TRAILER_STATUS in (16,21) and NEXT_TERMINAL_1 <> 'X%' and WEIGHT=0 and REGIONAL_TYPE_CODE='INTER'";

	/* get van and pup for INTRA-REGIONAL ROAD empties */
	public static String query4 = "select count(case when  LENGTH >=35 THEN '+' END ) as van, count(case when  LENGTH <35 THEN '+' END ) as pup from  SLH_TRAILER where TRAILER_STATUS in (16,21) and NEXT_TERMINAL_1 <> 'X%' and WEIGHT=0 and REGIONAL_TYPE_CODE='INTRA'";
}
