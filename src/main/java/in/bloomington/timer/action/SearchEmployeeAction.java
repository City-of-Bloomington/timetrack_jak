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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SearchEmployeeAction extends TopAction{

    static final long serialVersionUID = 1800L;	
    static Logger logger = LogManager.getLogger(SearchEmployeeAction.class);
    //
    EmployeeList emplst = null;
    String employeesTitle = "Current Employees";
    List<Employee> employees = null;
    List<Type> departments = null;
    String department_id = "";
    Employee emp = null; // search employee 
    public String execute(){
	String ret = SUCCESS;
	String back = "";
	System.err.println("action "+action);
	if(!action.isEmpty()){
	    back = emplst.find();
	    if(back.isEmpty()){
		List<Employee> ones = emplst.getEmployees();
								
		if(ones != null){
		    if(ones.size() > 1){
			employees = ones;
			addActionMessage("Found "+employees.size()+" employees");
		    }
		    else if(ones.size() == 1){
			String emp_id = ones.get(0).getId();
			try{
			    HttpServletResponse res = ServletActionContext.getResponse();
			    String str = "employee.action?emp_id="+emp_id;
			    res.sendRedirect(str);
			}catch(Exception ex){
			    System.err.println(ex);
														
			}	
		    }
		}
		else{
		    addMessage("No match found");
		}
	    }
	}
	else{
	    if(user != null && !(isAdmin() || user.isITSAdmin() || user.isHrAdmin())){
		String val = user.getDepartment_id();
		if(val != null)
		    department_id = val;
	    }
	}
	getEmplst();
	return ret;
    }
    @StrutsParameter(depth=3)
    public EmployeeList getEmplst(){ 
	if(emplst == null){
	    emplst = new EmployeeList();
	}		
	return emplst;
    }
    @StrutsParameter(depth=3)    
    public void setEmplst(EmployeeList val){
	if(val != null){
	    emplst = val;
	}
    }
    // needed for auto  complete and limit the user
    // to his/her department
    		
    public String getDepartment_id(){
	return department_id;
    }

    @StrutsParameter(depth=1)
    public void setDepartment_id(String val){
	if(val != null)
	    department_id = val;
    }
    public String getEmployeeTitle(){
	return employeesTitle;
    }
    public void setAction2(String val){
	if(val != null && !val.isEmpty())		
	    action = val;
    }
    public boolean hasEmployees(){
	return employees != null && employees.size() > 0;
    }
    @StrutsParameter(depth=3)
    public List<Employee> getEmployees(){
	return employees;
    }
    @StrutsParameter(depth=2)    
    public List<Type> getDepartments(){
	if(departments == null){
	    TypeList tl = new TypeList();
	    tl.setTable_name("departments");
	    tl.setActiveOnly();
	    String back = tl.find();
	    if(back.isEmpty()){
		List<Type> ones = tl.getTypes();
		if(ones != null && ones.size() > 0){
		    departments = ones;
		}
	    }
	}
	return departments;
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





































