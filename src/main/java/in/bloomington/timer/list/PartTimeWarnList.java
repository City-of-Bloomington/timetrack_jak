package in.bloomington.timer.list;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */

import java.util.*;
import java.sql.*;
import java.io.*;
import java.text.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import javax.sql.*;
import in.bloomington.timer.util.Helper;
import in.bloomington.timer.util.UnoConnect;
import in.bloomington.timer.util.CommonInc;
import in.bloomington.timer.bean.PartTimeWarn;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PartTimeWarnList extends CommonInc{

    static Logger logger = LogManager.getLogger(PartTimeWarnList.class);
    final static long serialVersionUID = 302L;
    String job_id="", pay_period_id="";
    boolean debug = false;
    List<PartTimeWarn> warns = null;
    public PartTimeWarnList(){
    }		
    public PartTimeWarnList(String val, String val2){
	setJob_id(val);
	setPay_period_id(val2);
    }
    public void setJob_id(String val){
	if(val != null)
	    job_id = val;
    }
    public void setPay_period_id(String val){
	if(val != null)
	    pay_period_id = val;
    }
    public List<PartTimeWarn> getWarnings(){
	return warns;
    }
    public String find(){
	String back = "";
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String qw = "", qq="";
	qq = " select "+
	    "w.id,"+
	    "w.job_id,"+
	    "w.pay_week_num,"+
	    "w.warn_type,"+
	    "w.week_of_year,"+
	    "w.week_total,"+
	    "w.critical_value,"+
	    "w.pay_period_id "+
	    "from part_time_warns w ";
	if(!job_id.isEmpty()){
	    if(!qw.isEmpty()) qw += " and ";
	    qw += " w.job_id = ? ";
	}	
	if(!pay_period_id.isEmpty()){
	    if(!qw.isEmpty()) qw += " and ";
	    qw += " w.pay_period_id = ? ";
	}
	if(!qw.isEmpty()){
	    qq += " where "+qw;
	}
	qq += " order by w.id ";
	if(debug)
	    logger.debug(qq);
	con = UnoConnect.getConnection();
	if(con == null){
	    back = "Could not connect to DB ";
	    return back;
	}
	try{
	    int jj=1;
	    pstmt = con.prepareStatement(qq);
	    if(!job_id.isEmpty()){
		pstmt.setString(jj++, job_id);
	    }	    
	    if(!pay_period_id.isEmpty()){
		pstmt.setString(jj++, pay_period_id);
	    }	    	    
	    
	    rs = pstmt.executeQuery();
	    while(rs.next()){
		PartTimeWarn one =
		    new PartTimeWarn(
				     rs.getString(1),
				     rs.getString(2),
				     rs.getInt(3),
				     rs.getInt(4),
				     rs.getInt(5),
				     rs.getDouble(6),
				     rs.getInt(7),
				     rs.getString(8)
				     );
		if(warns == null)
		    warns = new ArrayList<>();
		warns.add(one);
	    }
	}catch(Exception ex){
	    logger.error(ex);
	}
	finally{
	    Helper.databaseDisconnect(pstmt, rs);
	    UnoConnect.databaseDisconnect(con);
	}
	return back;				
    }
		
}























































