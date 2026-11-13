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

public class SearchJobAction extends TopAction{

    static final long serialVersionUID = 1800L;	
    static Logger logger = LogManager.getLogger(SearchJobAction.class);
    //
    List<JobTask> jobs = null;
    JobTaskList joblst = null;
    String jobTasksTitle = "Current jobs";
    List<Type> salaryGroups = null;
    List<Type> positions = null;
    List<Employee> employees = null;
    List<Type> departments = null;
    public String execute(){
	String ret = SUCCESS;
	String back = canProceed("searchJob.action");
	if(!back.isEmpty()){
	    return back;
	}
	getJoblst();
	if(!action.isEmpty()){
	    back = joblst.find();
	    if(!back.isEmpty()){
		addError(back);
	    }
	    else{
		List<JobTask> ones = joblst.getJobs();
		if(ones != null){
		    if(ones.size() > 1){
			jobs = ones;
			addMessage("Found "+jobs.size()+" Jobs");
		    }
		    else if(ones.size() == 1){
			String job_id = ones.get(0).getId();
			try{
			    HttpServletResponse res = ServletActionContext.getResponse();
			    String str = "jobTask.action?id="+job_id;
			    res.sendRedirect(str);
			}catch(Exception ex){
			    System.err.println(ex);
			}	
		    }
		}
		else{
		    addActionMessage("No match found");
		    addMessage("No match found");
		}
	    }
	}
	return ret;
    }
    @StrutsParameter(depth=2)
    public JobTaskList getJoblst(){ 
	if(joblst == null){
	    joblst = new JobTaskList();
	}		
	return joblst;
    }
    @StrutsParameter(depth=2)
    public void setJoblst(JobTaskList val){
	if(val != null){
	    joblst = val;
	}
    }
    public String getEmployee_name(){
	return ""; // we are using auto complete
    }
    public String getEmployee_id(){
	return joblst.getEmployee_id();
    }
    public String getSalary_group_id(){
	return joblst.getSalary_group_id();
    }
    public String getPosition_id(){
	return joblst.getPosition_id();
    }
    public String getDate_from(){
	return joblst.getDate_from();
    }
    public String getWhich_date(){
	return joblst.getWhich_date();
    }
    public String getActive_status(){
	return joblst.getActive_status();
    }
    public String getDepartment_id(){
	return joblst.getDepartment_id();
    }
    public String getGroup_id(){
	return joblst.getGroup_id();
    }
    public String getId(){
	return joblst.getId();
    }
    public String getClock_status(){
	return joblst.getClock_status();
    }
    public boolean getIncludeInAutoBatch(){
	return joblst.getIncludeInAutoBatch();
    }
    public boolean getIrregularWorkDays(){
	return joblst.getIrregularWorkDays();
    }
	
    public String getJobTasksTitle(){
	return jobTasksTitle;
    }
    @StrutsParameter(depth=1)
    public void setAction2(String val){
	if(val != null && !val.isEmpty())		
	    action = val;
    }
    // todo
    @StrutsParameter(depth=3)
    public List<JobTask> getJobs(){
	return jobs;
    }

    public boolean hasJobs(){
	return jobs != null && jobs.size() > 0;
    }
    @StrutsParameter(depth=2)
    public List<Type> getSalaryGroups(){
	TypeList tl = new TypeList("salary_groups");
	String back = tl.find();
	if(back.isEmpty()){
	    List<Type> ones = tl.getTypes();
	    if(ones != null && ones.size() > 0){
		salaryGroups = ones;
	    }
	}
	return salaryGroups;
    }
    @StrutsParameter(depth=2)
    public List<Type> getPositions(){
	TypeList tl = new TypeList("positions");
	String back = tl.find();
	if(back.isEmpty()){
	    List<Type> ones = tl.getTypes();
	    if(ones != null && ones.size() > 0){
		positions = ones;
	    }
	}
	return positions;
    }
    @StrutsParameter(depth=2)
    public List<Employee> getEmployees(){
	EmployeeList tl = new EmployeeList();
	tl.setActiveOnly();
	String back = tl.find();
	if(back.isEmpty()){
	    List<Employee> ones = tl.getEmployees();
	    if(ones != null && ones.size() > 0){
		employees = ones;
	    }
	}
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





































