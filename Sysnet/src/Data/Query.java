package Data;

public class Query {

	/* get van and pup for empty rail */
	public static String query1 = "select count(case when  LENGTH >=35 THEN '+' END ) as van, count(case when  LENGTH <35 THEN '+' END ) as pup from  SLH_TRAILER where TRAILER_STATUS in (15,21) and NEXT_TERMINAL_1 like 'X%' and WEIGHT=0";

	/* get van and pup for load rail */
	public static String query2 = "select count(case when  LENGTH >=35 THEN '+' END ) as van, count(case when  LENGTH <35 THEN '+' END ) as pup from  SLH_TRAILER where TRAILER_STATUS =15 and NEXT_TERMINAL_1 like 'X%' and WEIGHT<> 0";

}
