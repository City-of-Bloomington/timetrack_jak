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

public class EmployeeAction extends TopAction{

    static final long serialVersionUID = 1150L;	
    static Logger logger = LogManager.getLogger(EmployeeAction.class);
    //
    static final String[] roles =
    {"Employee",
     "Admin",
     "Edit",
     "EngineeringAdmin",
     "FireReport",	
     "FMLAReport",
     "HANDReport",
     "HrAdmin",	
     "MPOReport",
     "ITSAdmin",
     "ParkAdmin",
     "PoliceAdmin",	
     "PublicWorksAdmin",	
     "TargetEmployee",
     "SearchEmployee",
     "AD_Employee",
     "Timewarp"
    };
    String emp_id="";
    // dept_id needed for adding employee by non-admin users
    String dept_id="", effective_date=""; // from wizard
    boolean wizard = false;
    Employee emp = null;
    Department dept = null;
    DepartmentEmployee departmentEmployee = null; // for new employee
    // GroupEmployee groupEmployee = null; // for new employee
    List<Employee> employees = null;
    String employeesTitle = "Current Employees";
    List<Type> departments = null;
    List<PayPeriod> payPeriods = null;
    List<Group> groups = null;
    public String execute(){
	String ret = SUCCESS;
	String back = canProceed("employee.action");
	if(!back.isEmpty()){
	    return back;
	}
	getEmp();
	if(action.equals("Save")){
	    back = emp.validate();
	    if(!back.isEmpty()){
		addError(back);
		return ret;
	    }
	    back = emp.doSave();
	    if(!back.isEmpty()){
		addError(back);
	    }
	    else{
		emp_id = emp.getId();
	    }
	}				
	else if(action.startsWith("Save")){
	    back = emp.doUpdate();
	    if(!back.isEmpty()){
		addError(back);
	    }
	    else{
		addMessage("Saved Successfully");
	    }
	}
	else if(action.startsWith("Edit")){
	    getEmp();
	    back = emp.doSelect();
	    if(!back.isEmpty()){
		addError(back);
	    }
	    getDepartmentEmployee();
	}				
	else{		
	    if(!emp_id.isEmpty()){
		// emp.findAddress();
		ret="view";
	    }
	    else{
		if(canEdit()){
		    if(user.hasDepartment()){
			dept = user.getDepartment();
			dept_id= dept.getId();
		    }
		}
	    }
	    // not sure about this if still needed
	    getDepartmentEmployee();
	}
	return ret;
    }
    @StrutsParameter(depth=3)
    public Employee getEmp(){
	if(emp == null){
	    emp = new Employee();
	    if(!emp_id.isEmpty()){
		emp.setId(emp_id);
		emp.doSelect();
	    }
	}
	return emp;
						
    }
    @StrutsParameter(depth=2)
    public Department getDept(){
	if(dept == null){
	    dept = new Department(dept_id);
	    if(!dept_id.isEmpty()){
		dept.doSelect();
	    }
	}
	return dept;
    }
    @StrutsParameter(depth=2)
    public DepartmentEmployee getDepartmentEmployee(){
	if(departmentEmployee == null){
	    departmentEmployee = new DepartmentEmployee();
	}
	return departmentEmployee;
						
    }
    @StrutsParameter(depth=1)
    public void setEmp(Employee val){
	if(val != null){
	    emp = val;
	}
    }
    @StrutsParameter(depth=2)
    public void setDept(Department val){
	if(val != null){
	    dept = val;
	}
    }
    @StrutsParameter(depth=1)
    public void setEmp_id(String val){
	if(val != null){
	    emp_id = val;
	}
    }
    public String getEmp_id(){
	if(emp_id.isEmpty() && emp != null){
	    emp_id = emp.getId();
	}
	return emp_id;
    }
    public String getUsername(){
	getEmp();
	return emp.getUsername();
	
    }
    public String getFirst_name(){
	getEmp();
	if(emp != null)
	    return emp.getFirst_name();
	return "";
    }
    public String getLast_name(){
	getEmp();
	return emp.getLast_name();
	
    }    
    public String getId_code(){
	getEmp();
	return emp.getId_code();
    }
    public String getEmployee_number(){
	getEmp();
	return emp.getEmployee_number();
    }
    public String getEmail(){
	getEmp();
	return emp.getEmail();
    }
    public String getAd_sid(){
	getEmp();
	return emp.getAd_sid();
    }
    public boolean hasDepartment(){
	getEmp();
	return emp.hasDepartment();
    }
    public String getAdded_date(){
	getEmp();
	return emp.getAdded_date();
    }

    public String getDepartmentEmployee_id(){
	getEmp();
	if(emp.hasDepartment()){
	    return emp.getDepartmentEmployee().getId();
	}
	return "";
	    
    }
    @StrutsParameter(depth=3)    
    public List<DepartmentEmployee> getDepartmentEmployees(){
	getEmp();
	if(emp.hasDepartments()){
	    return emp.getDepartmentEmployees();
	}
	return null;
    }
    @StrutsParameter(depth=3)        
    public List<JobTask> getAllJobs2(){
	getEmp();
	return emp.getAllJobs2();
    }
    public boolean hasAllJobs2(){
	getEmp();
	return emp.hasAllJobs2();
    }
    @StrutsParameter(depth=3)        
    public List<JobTask> getAllJobs(){
	getEmp();
	return emp.getAllJobs();
    }
    public boolean hasAllJobs(){
	getEmp();
	return emp.hasAllJobs();
    }    
    
    
    public String getRolesText(){
	getEmp();
	return emp.getRolesText();
    }
    
    public String getDeptName(){
	getEmp();
	if(hasDepartment())
	    return emp.getDepartment().getName();
	return "";
    }
    public boolean hasDepartments(){
	getEmp();
	return emp.hasDepartments();
    }
    public boolean isInactive(){
	getEmp();
	return emp.isInactive();
    }
    @StrutsParameter(depth=1)
    public void setDept_id(String val){
	if(val != null){
	    dept_id = val;
	}
    }
    public String getDept_id(){
	if(dept_id.isEmpty() && dept != null){
	    dept_id = dept.getId();
	}
	return dept_id;
    }
    public boolean canPayrollProcess(){
	getEmp();
	return emp.canPayrollProcess();
    }
    @StrutsParameter(depth=3)    
    public List<GroupManager> getProcessors(){
	getEmp();
	return emp.getProcessors();
    }
    public boolean canApprove(){
	getEmp();
	return emp.canApprove();
    }
    @StrutsParameter(depth=3)    
    public List<GroupManager> getApprovers(){
	getEmp();
	return emp.getApprovers();
    }
    public boolean canReview(){
	getEmp();
	return emp.canReview();
    }
    @StrutsParameter(depth=3)    
    public List<GroupManager> getReviewers(){
	getEmp();
	return emp.getReviewers();
    }
    public boolean canMaintain(){
	getEmp();
	return emp.canMaintain();
    }
    @StrutsParameter(depth=3)    
    public List<GroupManager> getEnterors(){
	getEmp();
	return emp.getEnterors();
    }    
    public boolean canLeaveReview(){
	getEmp();
	return emp.canLeaveReview();
    }
    @StrutsParameter(depth=3)    
    public List<GroupManager> getLeaveReviewers(){
	getEmp();
	return emp.getLeaveReviewers();
    }
    
    public void setWizard(boolean val){
	if(val){
	    wizard = true;
	}
    }
    public boolean isWizard(){
	return wizard;
    }
    @StrutsParameter(depth=3)
    public void setDepartmentEmployee(DepartmentEmployee val){
	if(val != null){
	    departmentEmployee = val;
	}
    }
    public String getEmployeesTitle(){
	return employeesTitle;
    }
    public String getEffective_date(){
	return effective_date;
    }
    @StrutsParameter(depth=1)
    public void setEffective_date(String val){
	if(val != null)
	    effective_date = val;
    }		
    public void setAction2(String val){
	if(val != null && !val.isEmpty())		
	    action = val;
    }
    @StrutsParameter(depth=3)
    public List<Employee> getEmployees(){
	if(emp == null){
	    EmployeeList tl = new EmployeeList();
	    tl.setActiveOnly();
	    String back = tl.find();
	    if(back.isEmpty()){
		List<Employee> ones = tl.getEmployees();
		if(ones != null && ones.size() > 0){
		    employees = ones;
		}
	    }
	}
	return employees;
    }
    @StrutsParameter(depth=2)
    public List<Type> getDepartments(){
	if(departments == null){
	    TypeList tl = new TypeList("departments");
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
    @StrutsParameter(depth=3)
    public List<Group> getGroups(){
	if(groups == null && !dept_id.isEmpty()){
	    GroupList gl = new GroupList();
	    if(emp.hasDepartments()){
		List<DepartmentEmployee> depEmps = emp.getDepartmentEmployees();
		if(depEmps != null && depEmps.size() > 0){
		    for(DepartmentEmployee one:depEmps){
			gl.setDepartment_id(one.getDepartment_id());
		    }
		}
		else{
		    gl.setDepartment_id(dept_id);
		}
	    }
	    else{
		gl.setDepartment_id(dept_id);
	    }
	    gl.setActiveOnly();
	    String back = gl.find();
	    if(back.isEmpty()){
		List<Group> ones = gl.getGroups();
		if(ones != null && ones.size() > 0){
		    groups = ones;
		}
	    }
	}
	return groups;
    }
    public boolean hasGroups(){
	getGroups();
	return groups != null && groups.size() > 0;
    }
    @StrutsParameter(depth=2)
    public String[] getRoles(){
	return roles;
    }
    @StrutsParameter(depth=2)
    public List<PayPeriod> getPayPeriods(){
	if(payPeriods == null){
	    PayPeriodList tl = new PayPeriodList();
	    tl.setTwoPeriodsAheadOnly();
	    tl.setLimit("5");								
	    String back = tl.find();
	    if(back.isEmpty()){
		List<PayPeriod> ones = tl.getPeriods();
		if(ones != null && ones.size() > 0){
		    payPeriods = ones;
		}
	    }
	}
	return payPeriods;
    }		
    public boolean canAssignRoles(){
	getUser();
	return user.isAdmin();
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





































