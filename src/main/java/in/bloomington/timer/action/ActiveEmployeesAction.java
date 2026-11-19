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

public class ActiveEmployeesAction extends TopAction{

    static final long serialVersionUID = 1810L;	
    static Logger logger = LogManager.getLogger(ActiveEmployeesAction.class);
    List<Employee> employees = null;
    //
    // employees who are active and used timetrack since last two weeks
    //
    String activeEmployeesTitle = "Current Active Employees";
    List<Department> departments = null;
    String dept_id = "";
    String pay_period_id="", group_id="";
    String selected_group_id = "";
    List<String> deptList = null;
    PayPeriod currentPayPeriod=null, previousPayPeriod=null,
	nextPayPeriod=null, payPeriod = null;
    List<Group> groups = null;    
    Group group = null;
    Department department = null;
    List<PayPeriod> payPeriods = null;
    String employee_ids = "";
    List<EmpTerminate> terms = null;    
    public String execute(){
	String ret = SUCCESS;
	resetEmployee();
	selected_group_id = obtainGroupIdFromSession();
	String back = canProceed("activeEmployees.action");
	if(!back.isEmpty()){
	    return back;
	}
	if(!action.isEmpty()){
	    // find groups
	    /**
	    back = findEmployees();
	    if(!back.isEmpty()){
		addError(back);
	    }
	    */
	}
	findManagedGroups();
	return ret;
    }
    @StrutsParameter(depth=1)
    public void setAction2(String val){
	if(val != null && !val.isEmpty())		
	    action = val;
    }
    @StrutsParameter(depth=1)
    public void setGroup_id(String val){
	if(val != null && !val.isEmpty() && !val.equals("all")){		
	    group_id = val;
	    selected_group_id = group_id;
	    addGroupIdToSession(selected_group_id);
	}
    }
    @StrutsParameter(depth=1)
    public void setDept_id(String val){
	if(val != null && !val.isEmpty() && !val.equals("all")){		
	    dept_id = val;
	}
    }
    public String getDept_id(){
	return dept_id;
    }
    @StrutsParameter(depth=2)
    public void setGroup(Group val){
	if(val != null){		
	    group = val;
	    if(!group_id.isEmpty())
		group.doSelect();
	}
    }    
    public String getGroup_id(){
	if(!selected_group_id.isEmpty())
	    group_id = selected_group_id;
	if(group_id.isEmpty()){
	    getGroup();
	    group_id = group.getId();
	}
	return group_id;
    }
    public boolean isGroupManager(){
	findManagedGroups();
	return groups != null && groups.size() > 0;
    }
    /**
    String findNonNotifiedTerms(){
	EmpTerminateList etl = new EmpTerminateList();
	etl.setSubmitted_by_id(user.getId());
	etl.setNotificationReminder();
	String back = etl.find();
	if(back.isEmpty()){
	    List<EmpTerminate> ones = etl.getTerms();
	    if(ones != null && ones.size() > 0){
		terms = ones;
	    }
	}
	return back;
    }
    */
    void findManagedGroups(){
	if(groups != null ) return;
	List<TermManager> managers = null;
	TermManagerList tml = new TermManagerList();
	tml.setEmployee_id(user.getId());
	String back = tml.find();
	if(back.isEmpty()){
	    List<TermManager> ones = tml.getManagers();
	    if(ones != null && ones.size() > 0){
		managers = ones;
	    }
	}
	if(managers != null && managers.size() > 0){
	    if(deptList == null){
		deptList = new ArrayList<>();
		departments = new ArrayList<>();
	    }
	    for(TermManager one:managers){
		if(dept_id.isEmpty()){
		    dept_id = one.getDepartment_id();
		}
		deptList.add(one.getDepartment_id());		
		Department dept = one.getDepartment();
		if(dept != null)
		    departments.add(dept);

	    }
	}
	if(dept_id.isEmpty() &&
	   deptList != null &&
	   deptList.size() > 0){
	    dept_id = deptList.get(0);
	}
	if(!dept_id.isEmpty()){
	    GroupList gl = new GroupList();
	    gl.setDepartment_id(dept_id);
	    gl.setActiveOnly();
	    back = gl.find();
	    if(back.isEmpty()){
		List<Group> ones = gl.getGroups();
		if(ones != null && ones.size() > 0){
		    groups = ones;
		}
	    }
	}
    }
    @StrutsParameter(depth=2)
    public PayPeriod getPayPeriod(){
	//
	if(payPeriod == null){
	    if(!pay_period_id.isEmpty()){
		PayPeriod one = new PayPeriod(pay_period_id);
		String back = one.doSelect();
		if(back.isEmpty()){
		    payPeriod = one;
		    pay_period_id = payPeriod.getId();
		}
	    }
	    else {
		getCurrentPayPeriod();
	    }
	}
	return payPeriod;
    }
    @StrutsParameter(depth=2)
    public PayPeriod getCurrentPayPeriod(){
	//
	if(currentPayPeriod == null){
	    PayPeriodList ppl = new PayPeriodList();
	    ppl.currentOnly();
	    String back = ppl.find();
	    if(back.isEmpty()){
		List<PayPeriod> ones = ppl.getPeriods();
		if(ones != null && ones.size() > 0){
		    currentPayPeriod = ones.get(0);
		    if(pay_period_id.isEmpty()){
			pay_period_id = currentPayPeriod.getId();
			payPeriod = currentPayPeriod;
		    }
		}
	    }
	}
	return currentPayPeriod;
    }
    public boolean hasMoreThanOneDept(){
	return departments != null && departments.size() > 1;
    }
    @StrutsParameter(depth=2)
    public List<Department> getDepartments(){
	return departments;
    }
    @StrutsParameter(depth=2)
    public Group getGroup(){
	getGroups();
	if(group == null && !group_id.isEmpty()){
	    Group one = new Group(group_id);
	    String back = one.doSelect();
	    if(back.isEmpty()){
		group = one;
	    }
	}
	else if(group != null && !group_id.isEmpty()){
	    if(!group.getId().equals(group_id)){
		group.setId(group_id);
		group.doSelect();
	    }
	}
	else if(groups.size() > 0){ // one or more
	    group = groups.get(0); // only one group is shown
	    group_id = group.getId();
	}
	return group;
    }
    public boolean hasGroups(){
	return groups != null && groups.size() > 0;
				
    }
    public boolean hasMoreThanOneGroup(){
	return groups != null && groups.size() > 1;
    }
    /**
    public boolean hasTerms(){
	if(terms == null ){
	    findNonNotifiedTerms();
	}
	return terms != null && terms.size() > 0;
    }
    */
    @StrutsParameter(depth=2)
    public List<EmpTerminate> getTerms(){
	return terms;
    }
    @StrutsParameter(depth=2)
    public List<Group> getGroups(){
	return groups;
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





































