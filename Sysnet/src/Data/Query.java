package Data;

public class Query {

	/* get van and pup for empty rail */
	public static String query1 = "select count(case when  LENGTH > 28  THEN '+' END ) as van, count(case when  LENGTH <=28 THEN '+' END ) as pup from  SLH_TRAILER where TRAILER_STATUS in (15,21) and NEXT_TERMINAL_1 like 'X%' and WEIGHT=0";

	/* get van and pup for loaded rail */
	public static String query2 = "select count(case when  LENGTH > 28  THEN '+' END ) as van, count(case when  LENGTH <=28 THEN '+' END ) as pup from slh_trailer st, slh_leg sl where st.last_reported_trm=sl.begin_terminal and st.next_terminal_1=sl.end_terminal and st.next_leg_type_1=sl.leg_type and st.weight>0 and sl.leg_type='R' and sl.transportation_mode_type_cd='R'";

	/* get van and pup for INTER-REGIONAL ROAD empties */
	public static String query3 = "select count(case when  LENGTH > 28  THEN '+' END ) as van, count(case when  LENGTH <=28 THEN '+' END ) as pup from  SLH_TRAILER where TRAILER_STATUS in (16,21) and NEXT_TERMINAL_1 <> 'X%' and WEIGHT=0 and REGIONAL_TYPE_CODE='INTER'";

	/* get van and pup for INTRA-REGIONAL ROAD empties */
	public static String query4 = "select count(case when  LENGTH > 28  THEN '+' END ) as van, count(case when  LENGTH <=28 THEN '+' END ) as pup from  SLH_TRAILER where TRAILER_STATUS in (16,21) and NEXT_TERMINAL_1 <> 'X%' and WEIGHT=0 and REGIONAL_TYPE_CODE='INTRA'";

	/* get the routePLan */
	public static String queryRoutePlan = "select PLA.PRIMARY_DOMICILE terminal, primaryTrm.terminal_alpha_cd  as terminal_Alpha"
			+ " ,round(sum (pla.OTR_LDD + pla.OTR_ARR + pla.OTR_ENR + pla.OTR_LDG) ,1) yrc_otr"
			+ " ,round(sum(pla.OTR_LDD_SCHED+pla.OTR_ARR_SCHED+pla.OTR_ENR_SCHED+pla.OTR_LDG_SCHED), 1) as yrc_otrSched ,round(sum (pla.RAIL_LDD + pla.RAIL_ARR + pla.RAIL_ENR + pla.RAIL_LDG),1) as rail_Loads"
			+ " ,round(sum(pla.RAIL_LDD_SCHED+pla.RAIL_ARR_SCHED+pla.RAIL_ENR_SCHED+pla.RAIL_LDG_SCHED), 1)  as railLoadsSched, round(sum (pla.PT_LDD + pla.PT_ARR + pla.PT_ENR + pla.PT_LDG),1) as pt_Loads, round(sum(pla.PT_LDD_SCHED+pla.PT_ARR_SCHED+pla.PT_ENR_SCHED+pla.PT_LDG_SCHED), 1) as ptLoadsSched "
			+ " ,sum (pla.ETME_BILLS)  as etme_Count, sum (pla.LATE_BILLS) as late_Count from slh_holistic_lane_vw pla ,slh_terminal primaryTrm, SLH_REFERENCE_CODE r  "
			+ "  where r.REFERENCE_TYPE_CD='terminal_type_cd' and r.REFERENCE_CD=primaryTrm.TERMINAL_TYPE_CD and primary_domicile = terminal and regional_type_cd in (?)  group by  pla.primary_domicile, primaryTrm.terminal_alpha_cd,primaryTrm.terminal_nm  order by  pla.primary_domicile ";

	/* get the driver column for route plan */
	public static String queryDriverColumnForRP = "select  NVL(drvr.PRIMARY_DOMICILE,drvr.DRIVER_DOMICILE) as terminal, t.TERMINAL_ALPHA_CD as terminalAlpha"
			+ "  , sum(drvr.Bid_avl_cnt + drvr.Bid_rst_cnt + drvr.Bid_enr_cnt + drvr.Bid_asn_cnt) as bidDrivers"
			+ "  , sum(drvr.Bid_asn_cnt)  as bidDriversSched, sum(drvr.Xb_avl_cnt + drvr.Xb_rst_cnt + drvr.Xb_enr_cnt + drvr.Xb_asn_cnt) as xbDrivers"
			+ "  , sum(drvr.Xb_asn_cnt)  as xbDriversSched from SLH_holistic_driver_vw drvr, SLH_TERMINAL t"
			+ "  , SLH_REFERENCE_CODE r where (case when drvr.REGIONAL_TYPE_CD is null then 'UNKNOWN' else drvr.REGIONAL_TYPE_CD  end in (?)) "
			+ "  and drvr.HRS_AWAY <= 4 and t.TERMINAL = NVL(drvr.PRIMARY_DOMICILE,drvr.DRIVER_DOMICILE) and r.REFERENCE_TYPE_CD='terminal_type_cd' and r.REFERENCE_CD=t.TERMINAL_TYPE_CD"
			+ "  and terminal= ? and t.TERMINAL_ALPHA_CD= ? group by  NVL(drvr.PRIMARY_DOMICILE,drvr.DRIVER_DOMICILE)"
			+ " , t.TERMINAL_ALPHA_CD , t.TERMINAL_NM, r.REFERENCE_CODE_DS , t.CDO_REGION_CD order by  NVL(drvr.PRIMARY_DOMICILE,drvr.DRIVER_DOMICILE)";

	/* get the power column for route plan */
	public static String queryPowerColumnForRP = "select  trac.PRIMARY_DOMICILE, t.TERMINAL_ALPHA_CD  as terminalAlpha"
			+ " , sum(trac.asn_count + trac.avl_count + trac.enr_count) as power, sum(trac.asn_count) as powerSched from SLH_HOLISTIC_TRACTOR_VW trac"
			+ " , SLH_TERMINAL t, SLH_REFERENCE_CODE r where (case when trac.REGIONAL_TYPE_CD is null then 'UNKNOWN' else trac.REGIONAL_TYPE_CD "
			+ "  end in ('INTER', 'INTRA', 'UNKNOWN'))  and trac.HRS_AWAY <= 4  and t.TERMINAL = trac.PRIMARY_DOMICILE"
			+ "  and r.REFERENCE_TYPE_CD='terminal_type_cd' and r.REFERENCE_CD=t.TERMINAL_TYPE_CD and terminal= ? and t.TERMINAL_ALPHA_CD= ? group by  trac.PRIMARY_DOMICILE"
			+ " , t.TERMINAL_ALPHA_CD , t.TERMINAL_NM, r.REFERENCE_CODE_DS, t.CDO_REGION_CD	order by  trac.PRIMARY_DOMICILE";

	/* get linehaul resource list */
	public static String queryLinehualResouce = "select row_number() over ( order by trm.TERMINAL ) as noid, trm.TERMINAL as terminal,trm.TERMINAL_ALPHA_CD Alpha, tstat.INTRA_REG_LOADS as intraRegLoads, tstat.INTER_REG_LOADS as interRegLoads "
			+ ", (select sum(case when length<=28 then 0.5 when length>28 then 1 end) from SLH_TRAILER trlr6, SLH_LEG l6, SLH_TERMINAL t5 "
			+ " where trlr6.TABLE_SET=(select c17.TABLE_SET from SLH_CONTROL c17 where c17.DISPLAY_TIME=(select min(c18.DISPLAY_TIME) from SLH_CONTROL c18)) "
			+ "  and (trlr6.TRAILER_STATUS in (19 , 28)) and (trlr6.LAST_REPORTED_TRM=t5.TERMINAL or trlr6.LAST_REPORTED_TRM "
			+ "   in (select l7.BEGIN_TERMINAL from SLH_LEG l7 where l7.ALIGNMENT_ORG_TRM=t5.TERMINAL and l7.ALIGNMENT_DEST_TRM=l6.END_TERMINAL and l7.ALIGNMENT_LEG_TYPE=l6.LEG_TYPE)) "
			+ "   and trlr6.NEXT_TERMINAL_1=l6.END_TERMINAL  and trlr6.NEXT_LEG_TYPE_1=l6.LEG_TYPE  and trlr6.LAST_REPORTED_TRM=l6.BEGIN_TERMINAL  "
			+ "   and (l6.SIMULATION_FLAG in ('1' , '5' , '6'))  and trlr6.TRANSPORTATION_MODE_TYPE_CD='PT'   and (t5.TERMINAL not like 'X%') and t5.TERMINAL=trm.TERMINAL) as ptLoadsOnHand"
			+ "   , (select sum(case when length<=28 then 0.5 when length>28 then 1 end)  from SLH_TRAILER trlr5, SLH_LEG l4, SLH_TERMINAL t4 "
			+ "   where trlr5.TABLE_SET=(select c15.TABLE_SET from SLH_CONTROL c15 where c15.DISPLAY_TIME=(select min(c16.DISPLAY_TIME) from SLH_CONTROL c16)) "
			+ "   and (trlr5.TRAILER_STATUS in (19 , 28)) and (trlr5.LAST_REPORTED_TRM=t4.TERMINAL "
			+ "   or trlr5.LAST_REPORTED_TRM in (select l5.BEGIN_TERMINAL  from SLH_LEG l5  where l5.ALIGNMENT_ORG_TRM=t4.TERMINAL  and l5.ALIGNMENT_DEST_TRM=l4.END_TERMINAL  and l5.ALIGNMENT_LEG_TYPE=l4.LEG_TYPE)) "
			+ "   and trlr5.NEXT_TERMINAL_1=l4.END_TERMINAL  and trlr5.NEXT_LEG_TYPE_1=l4.LEG_TYPE  and trlr5.LAST_REPORTED_TRM=l4.BEGIN_TERMINAL "
			+ "   and (l4.SIMULATION_FLAG in ('1' , '5' , '6'))   and (trlr5.NEXT_TERMINAL_1 like 'X%') and t4.TERMINAL=trm.TERMINAL) as railLoadsOnHand"

			+ "   ,(select sum(case when trlr10.LENGTH<=28 then 0.5 when trlr10.LENGTH>28 then 1 end) "
			+ "  from SLH_TRAILER trlr10, SLH_LEG l9, SLH_TERMINAL t7, SLH_BREAKBULK bb2 "
			+ "  where t7.TERMINAL=bb2.terminal(+)  and (bb2.SEQUENCE_NUMBER(+)=1) "
			+ "  and trlr10.TABLE_SET=(select c23.TABLE_SET from SLH_CONTROL c23 where c23.DISPLAY_TIME=(select min(c24.DISPLAY_TIME) from SLH_CONTROL c24)) "
			+ "  and (trlr10.LAST_REPORTED_TRM like 'X%')  and (trlr10.NEXT_TERMINAL_1 like 'X%')  and (t7.TERMINAL_TYPE_CD=20    and trlr10.NEXT_TERMINAL_1=trm.TERMINAL "
			+ "  or t7.TERMINAL_TYPE_CD<>20  and nvl(l9.PRIMARY_DOMICILE_TERMINAL_CD, bb2.BREAK_TERMINAL)=trm.TERMINAL) and trlr10.NEXT_TERMINAL_1=t7.TERMINAL "
			+ "  and l9.BEGIN_TERMINAL=trlr10.LAST_REPORTED_TRM and l9.END_TERMINAL=trlr10.NEXT_TERMINAL_1    and l9.LEG_TYPE=trlr10.NEXT_LEG_TYPE_1) as enrRail"

			+ "  , (select sum(case when trlr9.LENGTH<=28 then 0.5 when trlr9.LENGTH>28 then 1 end) "
			+ "   from SLH_TRAILER trlr9, SLH_LEG l8, SLH_TERMINAL t6, SLH_BREAKBULK bb "
			+ "   where t6.TERMINAL=bb.terminal(+)   and (bb.SEQUENCE_NUMBER(+)=1) "
			+ "   and trlr9.TABLE_SET=(select c21.TABLE_SET from SLH_CONTROL c21 where c21.DISPLAY_TIME=(select min(c22.DISPLAY_TIME) from SLH_CONTROL c22)) "
			+ "   and (trlr9.LAST_REPORTED_TRM like 'X%')   and (trlr9.NEXT_TERMINAL_1 not like 'X%')  and (t6.TERMINAL_TYPE_CD=20 "
			+ "   and trlr9.NEXT_TERMINAL_1=trm.TERMINAL   or t6.TERMINAL_TYPE_CD<>20  and nvl(l8.PRIMARY_DOMICILE_TERMINAL_CD, bb.BREAK_TERMINAL)=trm.TERMINAL) "
			+ "   and trlr9.NEXT_TERMINAL_1=t6.TERMINAL    and l8.BEGIN_TERMINAL=trlr9.LAST_REPORTED_TRM   and l8.END_TERMINAL=trlr9.NEXT_TERMINAL_1  and l8.LEG_TYPE=trlr9.NEXT_LEG_TYPE_1) as fromRail"

			+ "   , tstat.TIME_IN_ARV_LTG as timeInArvLtg , tstat.POWER_TRACTORS as powerTractors, (tstat.POWER_TRACTORS - ( tstat.INTER_REG_LOADS +  tstat.INTRA_REG_LOADS)) as powerpm"

			+ "  , (select count(*) from SLH_TRACTOR trac   where trac.ASSIGNMENT_FLAG=0  and trac.TRACTOR_PREFIX<>'REIM' and trac.OUT_OF_SERVICE='N' "
			+ "   and (trac.ACTIVITY_TIME_DT<=(select c1.CURRENT_TIME_DT-(7*60/(24*60)) from SLH_CONTROL c1 where c1.TABLE_SET=tstat.TABLE_SET) "
			+ "   and trm.TERMINAL_TYPE=0 "
			+ "   or trac.ACTIVITY_TIME_DT<=(select c2.CURRENT_TIME_DT-(17*60/(24*60)) from SLH_CONTROL c2 where c2.TABLE_SET=tstat.TABLE_SET) "
			+ "   and trm.TERMINAL_TYPE<>0)  and trac.TABLE_SET=tstat.TABLE_SET and trac.ORIGIN_TERMINAL=trm.TERMINAL) as idleTractors  "

			+ "   , (select count(*)  from SLH_DRIVER drvr1  where drvr1.DRIVER_STATUS<>'DSP'   and (drvr1.SLEEPER_STATUS<>2 or drvr1.SLEEPER_TRIP_FLAG<>'1') "
			+ "   and drvr1.CURRENT_TRM=drvr1.BASE_DOM_TRM  and drvr1.DRIVER_TIME_DT<=(select c3.CURRENT_TIME_DT-(16*60/(24*60)) from SLH_CONTROL c3 where c3.TABLE_SET=drvr1.TABLE_SET) "
			+ "   and drvr1.TABLE_SET=(select c4.TABLE_SET from SLH_CONTROL c4 where c4.DISPLAY_TIME=(select min(c5.DISPLAY_TIME) from SLH_CONTROL c5)) "
			+ "   and drvr1.CURRENT_TRM=trm.TERMINAL) as idleDrivers"
			+ "   , tstat.DOMICILE_DRIVERS as domicileDrivers   , tstat.FOREIGN_DRIVERS as foreignDrivers"

			+ "  , (select count(*)   from SLH_DRIVER drvr2 where drvr2.DRIVER_STATUS<>'DSP'  and (drvr2.SLEEPER_STATUS<>2 or drvr2.SLEEPER_TRIP_FLAG<>'1') "
			+ "  and drvr2.CURRENT_TRM<>drvr2.BASE_DOM_TRM  and drvr2.DRIVER_TIME_DT<=(select c6.CURRENT_TIME_DT-(6*60/(24*60)) from SLH_CONTROL c6 where c6.TABLE_SET=drvr2.TABLE_SET) "
			+ "  and drvr2.TABLE_SET=(select c7.TABLE_SET from SLH_CONTROL c7 where c7.DISPLAY_TIME=(select min(c8.DISPLAY_TIME) from SLH_CONTROL c8)) "
			+ "  and drvr2.CURRENT_TRM=trm.TERMINAL) as layoverForeignDrivers"
			+ "  , tstat.EMPTY_PUPS as emptyPups , tstat.EMPTY_VANS as emptyVans"

			+ "   , (select sum(case when trlr1.LENGTH<=28 then 0.5 when trlr1.LENGTH>28 then 1 end) "
			+ "    from SLH_TRAILER trlr1  where (trlr1.TRAILER_STATUS in (15 , 16))  and trlr1.WEIGHT=0  and trlr1.ORIGIN_TERMINAL=trm.TERMINAL  and trlr1.TABLE_SET=tstat.TABLE_SET) as emptyLoadsOrigin"
			+ "    , (select sum(case when trlr2.LENGTH<=28 then 0.5 when trlr2.LENGTH>28 then 1 end) from SLH_TRAILER trlr2 "
			+ "  where (trlr2.TRAILER_STATUS in (15 , 16))  and trlr2.WEIGHT=0 and trlr2.DEST_TERMINAL=trm.TERMINAL   and trlr2.TABLE_SET=tstat.TABLE_SET) as emptyLoadsDestination"

			+ "  , (select count(*) from SLH_SHIPMENT_CODE sc1, SLH_SHIPMENT s1  where sc1.TABLE_SET=s1.TABLE_SET "
			+ "  and sc1.PRO_NUMBER=s1.PRO_NUMBER and sc1.PRO_SUFFIX=s1.PRO_SUFFIX  and s1.LAST_REPORTED_TERMINAL_CD=trm.TERMINAL  and s1.TABLE_SET=tstat.TABLE_SET and sc1.SHIPMENT_CODEWORD_TYPE_CD='I' "
			+ "  and sc1.SHIPMENT_CODEWORD_CD='ETME' and s1.DEST_TERMINAL<>s1.LAST_REPORTED_TERMINAL_CD) as etmeCount"
			+ "  , (select count(*) from SLH_SHIPMENT_CODE sc2, SLH_SHIPMENT s2  where sc2.TABLE_SET=s2.TABLE_SET and sc2.PRO_NUMBER=s2.PRO_NUMBER "
			+ "  and sc2.PRO_SUFFIX=s2.PRO_SUFFIX and s2.LAST_REPORTED_TERMINAL_CD=trm.TERMINAL  and s2.TABLE_SET=tstat.TABLE_SET  and sc2.SHIPMENT_CODEWORD_TYPE_CD='I' "
			+ "  and sc2.SHIPMENT_CODEWORD_CD='ACEL'  and s2.DEST_TERMINAL<>s2.LAST_REPORTED_TERMINAL_CD) as acelCount"
			+ "  , (select count(*)   from SLH_SHIPMENT_CODE sc3, SLH_SHIPMENT s3  where sc3.TABLE_SET=s3.TABLE_SET "
			+ "  and sc3.PRO_NUMBER=s3.PRO_NUMBER and sc3.PRO_SUFFIX=s3.PRO_SUFFIX and s3.LAST_REPORTED_TERMINAL_CD=trm.TERMINAL and s3.TABLE_SET=tstat.TABLE_SET and sc3.SHIPMENT_CODEWORD_TYPE_CD='I' "
			+ "  and sc3.SHIPMENT_CODEWORD_CD='STME' and s3.DEST_TERMINAL<>s3.LAST_REPORTED_TERMINAL_CD) as stmeCount"
			+ "  , sum(case when trunc(a.TIME_STAMP)=trunc(SYSDATE-1) then a.TIME_IN_ARV_LTG else 0 end) as hvdYesterday"
			+ " , sum(case when trunc(a.TIME_STAMP)=trunc(SYSDATE-1) then a.TRLS_IN_ARV_LTG else 0 end) as hvdTrailersYesterday"
			+ " , sum(case when sign(a.TIME_STAMP-(SYSDATE-0.5))=1 then a.TIME_IN_ARV_LTG else 0 end) as hvdBack12Hours"
			+ " , sum(case when sign(a.TIME_STAMP-(SYSDATE-0.5))=1 then a.TRLS_IN_ARV_LTG else 0 end) as hvdTrailersBack12Hours"
			+ "  , tstat.TA_TRACTORS_QT as taTractorsQt"
			+ "  , (select nvl(sum(case when l1.TRIPLES_FLAG=1 then case when trlr3.LENGTH<=28 then 0.3 when trlr3.LENGTH>28 then 0.6 end when l1.TRIPLES_FLAG=0 then case when trlr3.LENGTH<=28 then 0.5 when trlr3.LENGTH>28 then 1 end end), 0) "
			+ "  from SLH_TRAILER trlr3, SLH_LEG l1, SLH_TERMINAL t2, SLH_CONTROL c9  where trlr3.TABLE_SET=c9.TABLE_SET "
			+ "  and trlr3.TABLE_SET=(select c10.TABLE_SET from SLH_CONTROL c10 where c10.DISPLAY_TIME=(select min(c11.DISPLAY_TIME) from SLH_CONTROL c11)) "
			+ "  and (trlr3.TRAILER_STATUS in (19 , 28))  and (l1.BEGIN_TERMINAL in (t2.TERMINAL)) and trlr3.LAST_REPORTED_TRM=l1.BEGIN_TERMINAL and trlr3.NEXT_TERMINAL_1=l1.END_TERMINAL "
			+ "  and trlr3.NEXT_LEG_TYPE_1=l1.LEG_TYPE and (l1.SIMULATION_FLAG in ('2' , '3' , '4')) and (t2.TERMINAL_TYPE=0 "
			+ "  and NEW_TIME(c9.CURRENT_TIME_DT, 'GMT', 'CST')-NEW_TIME(trlr3.LAST_REPORTED_TIME_DT, 'GMT', 'CST')>NEW_TIME(c9.CURRENT_TIME_DT, 'GMT', 'CST')-to_date(to_char(trunc(NEW_TIME(c9.CURRENT_TIME_DT, 'GMT', 'CST')-1), 'MM/DD/YYYY')||'23:00:00', 'MM/DD/YYYY HH24:MI:SS') or t2.TERMINAL_TYPE=2 "
			+ "  and NEW_TIME(c9.CURRENT_TIME_DT, 'GMT', 'CST')-NEW_TIME(trlr3.LAST_REPORTED_TIME_DT, 'GMT', 'CST')>NEW_TIME(c9.CURRENT_TIME_DT, 'GMT', 'CST')-to_date(to_char(trunc(NEW_TIME(c9.CURRENT_TIME_DT, 'GMT', 'CST')), 'MM/DD/YYYY')||'06:00:00', 'MM/DD/YYYY HH24:MI:SS')) "
			+ "  and (trlr3.LAST_REPORTED_TRM not like 'X%')  and t2.TERMINAL=trm.TERMINAL) as priorDayCycleIntraField"
			+ "  , (select nvl(sum(case when l2.TRIPLES_FLAG=1 then case when trlr4.LENGTH<=28 then 0.3 when trlr4.LENGTH>28 then 0.6 end when l2.TRIPLES_FLAG=0 then case when trlr4.LENGTH<=28 then 0.5 when trlr4.LENGTH>28 then 1 end end), 0) "
			+ "  from SLH_TRAILER trlr4, SLH_LEG l2, SLH_TERMINAL t3, SLH_CONTROL c12 "
			+ "  where trlr4.TABLE_SET=c12.TABLE_SET "
			+ "  and trlr4.TABLE_SET=(select c13.TABLE_SET from SLH_CONTROL c13 where c13.DISPLAY_TIME=(select min(c14.DISPLAY_TIME) from SLH_CONTROL c14)) "
			+ "  and (trlr4.TRAILER_STATUS in (19 , 28)) and (l2.BEGIN_TERMINAL in (t3.TERMINAL)) and ((trlr4.LAST_REPORTED_TRM , trlr4.NEXT_TERMINAL_1) "
			+ "  in (select l3.BEGIN_TERMINAL, l3.END_TERMINAL from SLH_LEG l3 where l3.ALIGNMENT_ORG_TRM=l2.BEGIN_TERMINAL and l3.ALIGNMENT_DEST_TRM=l2.END_TERMINAL  and l3.ALIGNMENT_LEG_TYPE=l2.LEG_TYPE and trlr4.LAST_REPORTED_TRM<>l2.BEGIN_TERMINAL)) "
			+ "  and (l2.SIMULATION_FLAG in ('2' , '3' , '4'))  and (t3.TERMINAL_TYPE=0 "
			+ "  and NEW_TIME(c12.CURRENT_TIME_DT, 'GMT', 'CST')-NEW_TIME(trlr4.LAST_REPORTED_TIME_DT, 'GMT', 'CST')>NEW_TIME(c12.CURRENT_TIME_DT, 'GMT', 'CST')-to_date(to_char(trunc(NEW_TIME(c12.CURRENT_TIME_DT, 'GMT', 'CST')-1), 'MM/DD/YYYY')||'23:00:00', 'MM/DD/YYYY HH24:MI:SS') "
			+ "  or t3.TERMINAL_TYPE=2 and NEW_TIME(c12.CURRENT_TIME_DT, 'GMT', 'CST')-NEW_TIME(trlr4.LAST_REPORTED_TIME_DT, 'GMT', 'CST')>NEW_TIME(c12.CURRENT_TIME_DT, 'GMT', 'CST')-to_date(to_char(trunc(NEW_TIME(c12.CURRENT_TIME_DT, 'GMT', 'CST')), 'MM/DD/YYYY')||'06:00:00', 'MM/DD/YYYY HH24:MI:SS')) "
			+ "  and (trlr4.LAST_REPORTED_TRM not like 'X%')  and t3.TERMINAL=trm.TERMINAL) as scheduleload"
			+ " , r1.REFERENCE_CODE_DS as terminalType"
			+ " from SLH_TERMINAL_STAT tstat, SLH_TERMINAL trm, SLH_AVERAGES a, SLH_REFERENCE_CODE r1, SLH_REFERENCE_CODE r2 "
			+ " where tstat.TERMINAL=trm.TERMINAL "
			+ "  and tstat.TABLE_SET=(select slhcontrol65_.TABLE_SET from SLH_CONTROL slhcontrol65_ where slhcontrol65_.DISPLAY_TIME=(select min(slhcontrol66_.DISPLAY_TIME) from SLH_CONTROL slhcontrol66_)) "
			+ "  and trm.TERMINAL=a.TERMINAL (+)"
			+ "  and r1.REFERENCE_TYPE_CD='terminal_type_cd' and r1.REFERENCE_CD=trm.TERMINAL_TYPE_CD "
			+ "   and r2.REFERENCE_CD=trm.GEOGRAPHY_CD and r2.REFERENCE_TYPE_CD='geography_cd' "
			+ "   and ( trm.LHR_REPORT_IN='Y' or trm.terminal_Type_CD = '21' )"
			+ " group by trm.TERMINAL,  trm.CITY , trm.STATE , tstat.DOMICILE_DRIVERS , tstat.FOREIGN_DRIVERS , tstat.POWER_TRACTORS , tstat.EMPTY_PUPS , tstat.EMPTY_VANS , tstat.TIME_IN_ARV_LTG , tstat.INTER_REG_LOADS , tstat.INTRA_REG_LOADS , tstat.TA_TRACTORS_QT , tstat.TABLE_SET , trm.TERMINAL_TYPE , r1.REFERENCE_CODE_DS , r2.REFERENCE_CODE_DS , trm.TERMINAL_ALPHA_CD "
			+ "order by trm.TERMINAL";

}
