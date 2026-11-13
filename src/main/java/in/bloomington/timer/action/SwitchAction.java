package in.bloomington.timer.action;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.io.*;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.ServletContext;
import org.apache.struts2.ServletActionContext;  
import org.apache.struts2.action.SessionAware;
import org.apache.struts2.action.ServletContextAware;
import org.apache.struts2.interceptor.parameter.StrutsParameter;
import in.bloomington.timer.list.*;
import in.bloomington.timer.bean.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SwitchAction extends TopAction{

    static final long serialVersionUID = 1150L;	
    static Logger logger = LogManager.getLogger(SwitchAction.class);
    //
    String new_employee_id = "",emp_name="",
	document_id="", working_date="",
	source="",type="",
	department_id="";
    String employeesTitle = "Current Employees";
    public String execute(){
	String ret = SUCCESS;
	String back = doPrepare("switch.action");
	if(!action.isEmpty()){ // normally 'Change'
	    if(!new_employee_id.isEmpty() &&
	       working_date.isEmpty()
	       && document_id.isEmpty()){
		setEmployee_id(new_employee_id);
		getEmployee();
		if(employee == null){
		    back = "could not get employee info ";
		    addError(back);
		}
	    }
	    else{
		if(!new_employee_id.isEmpty()){
		    setEmployee_id(new_employee_id);
		    getEmployee();
		}
		try{
		    HttpServletResponse res = ServletActionContext.getResponse();
		    // we do not need the url here
		    String str = "timeDetails.action?";
		    boolean found = false;
		    if(!document_id.isEmpty()){
			str += "document_id="+document_id;
			found = true;
		    }
		    else if(!working_date.isEmpty()){
			document_id = findDocument(working_date, new_employee_id);
			System.err.println("doc_id "+document_id);
			if(!document_id.isEmpty()){
			    str += "document_id="+document_id;
			    found = true;
			}
		    }
		    if(!source.isEmpty()){
			str += "&source="+source;
		    }
		    if(found){
			res.sendRedirect(str);
		    }
		    else{
			back = "could not get employee info ";
			addError(back);
		    }
		}catch(Exception ex){
		    System.err.println(ex);
		}
	    }
	}
	else {
	    if(user != null && !(isAdmin() || user.isITSAdmin() || user.isHrAdmin())){
		String val = user.getDepartment_id();
		    if(val != null)
			department_id = val;
	    }
	}
	return ret;
    }
    public String getEmployeesTitle(){
	return employeesTitle;
    }
    public String getDepartment_id(){
	return department_id;
    }
    @StrutsParameter(depth=1)        
    public void setAction2(String val){
	if(val != null && !val.isEmpty())		
	    action = val;
    }
    @StrutsParameter(depth=1)        
    public void setType(String val){
	// needed for js
    }
    @StrutsParameter(depth=1)    
    public void setNew_employee_id(String val){
	if(val != null && !val.isEmpty())		
	    new_employee_id = val;
    }
    @StrutsParameter(depth=1)    		
    public void setEmployee_name(String val){
	emp_name = val;
	// for auto complete
    }
    
    public String getEmployee_name(){
	return emp_name;
    }
    public String getNew_employee_name(){
	return new_employee_id;
    }    
    public String getWorkingDate(){
	return working_date;
    }
    @StrutsParameter(depth=1)    		
    public void setWorkingDate(String val){
	if(val != null)
	   working_date = val;
    }    
    @StrutsParameter(depth=1)        
    public void setDocument_id(String val){
	if(val != null && !val.isEmpty())		
	    document_id = val;
    }
    @StrutsParameter(depth=1)    
    public void setDepartment_id(String val){
	if(val != null && !val.isEmpty())		
	    department_id = val;
    }
    @StrutsParameter(depth=1)    
    public void setSource(String val){
	if(val != null && !val.isEmpty())		
	    source = val;
    }
    private String findDocument(String wdate, String emp_id){
	String doc_id = "";
	DocumentList dl = new DocumentList();
	dl.setDate(wdate);
	dl.setEmployee_id(emp_id);
	String back = dl.find();
	if(back.isEmpty()){
	    List<Document> ones = dl.getDocuments();
	    if(ones != null && ones.size() > 0){
		doc_id = ones.get(0).getId();
	    }
	}
	return doc_id;
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





































