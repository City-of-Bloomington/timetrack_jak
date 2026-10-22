package in.bloomington.timer.action;

/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
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
import in.bloomington.timer.report.*;
import in.bloomington.timer.util.CommonInc;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ReportEmpNoNumAction extends TopAction{

    static final long serialVersionUID = 1800L;	
    static Logger logger = LogManager.getLogger(ReportEmpNoNumAction.class);
    //
    EmployeeNoNumberReport report = null;
    List<List<String>> entries = null;
    public String execute(){
	String ret = SUCCESS;
	String back = doPrepare();
	if(!back.isEmpty()){
	    return back;
	}
	getReport();
	if(!action.isEmpty()){
	    back = report.find();
	    if(back.isEmpty()){
		if(report.hasEntries()){
		    entries = report.getEntries();
		}
	    }
	}
	return ret;
    }
    public EmployeeNoNumberReport getReport(){
	if(report == null)
	    report = new EmployeeNoNumberReport();
	return report;
    }
    public void setReport(EmployeeNoNumberReport report){
	this.report = report;
    }

    public List<List<String>> getEntries(){
	return entries;
    }
    public boolean hasEntries(){
	return entries != null && entries.size() > 0;
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





































