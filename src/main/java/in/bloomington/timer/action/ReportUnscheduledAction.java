package in.bloomington.timer.action;

/**
 * @copyright Copyright (C) 2014-2019 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.util.*;
import java.io.*;
import java.text.*;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.ServletContext;
import org.apache.struts2.ServletActionContext;  
import org.apache.struts2.action.SessionAware;
import org.apache.struts2.action.ServletContextAware;
import org.apache.struts2.interceptor.parameter.StrutsParameter;
import in.bloomington.timer.list.*;
import in.bloomington.timer.bean.*;
import in.bloomington.timer.util.*;
import in.bloomington.timer.report.UnscheduledReport;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ReportUnscheduledAction extends TopAction{

    static final long serialVersionUID = 1850L;	
    static Logger logger = LogManager.getLogger(ReportUnscheduledAction.class);
    UnscheduledReport report = null;
    String reportTitle = "Unscheduled Times Report ";
    public String execute(){
	String ret = SUCCESS;
	String back = doPrepare();
	if(!back.isEmpty()){
	    return back;
	}
	if(!action.isEmpty()){
	    back = report.find();
	    if(!back.isEmpty()){
		addError(back);
	    }
	    else{
		if(!report.hasUnscheduleds()){
		    addMessage("No records found");
		}
	    }
	}
	else{
	    getReport();
	}
	return ret;
    }
    public UnscheduledReport getReport(){ 
	if(report == null){
	    report = new UnscheduledReport();
	}		
	return report;
    }

    public void setReport(UnscheduledReport val){
	if(val != null){
	    report = val;
	}
    }

    public String getReportTitle(){
	return reportTitle;
    }
    public void setAction2(String val){
	if(val != null && !val.isEmpty())		
	    action = val;
    }
    @Override  
    public void withSession(Map<String, Object> map) {  
	sessionMap=map;  
    }
    @Override  	
    public void withServletContext(ServletContext ctx) {  
        this.ctx = ctx;  
    }    
				
}





































