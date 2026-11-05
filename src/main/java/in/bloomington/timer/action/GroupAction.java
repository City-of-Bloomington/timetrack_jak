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
import in.bloomington.timer.util.CommonInc;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GroupAction extends TopAction{

    static final long serialVersionUID = 1800L;	
    static Logger logger = LogManager.getLogger(GroupAction.class);
    //
    Group group = null;
    List<Group> groups = null;
    List<GroupManager> groupManagers;
    List<HourCode> hourCodes = null;
    String groupsTitle = "Current groups";
    List<Type> departments = null;
    // for excess hour calculation types
    // either CE1.0, OT1.0 or (Donation) ignore
    // CommonInc.excess_hours_earn_types;
    String department_id="";
    public String execute(){
	String ret = SUCCESS;
	String back = canProceed("group.action");
	if(!back.isEmpty()){
	    return back;
	}
	getGroup();
	if(action.equals("Save")){
	    back = group.doSave();
	    if(!back.isEmpty()){
		addError(back);
	    }
	    else{
		id = group.getId();
		addMessage("Added Successfully");
	    }
	}				
	else if(action.startsWith("Save")){
	    back = group.doUpdate();
	    if(!back.isEmpty()){
		addError(back);								
	    }
	    else{
		id = group.getId();
		addMessage("Updated Successfully");
	    }
	}
	else if(action.startsWith("Edit")){
	    if(!id.isEmpty()){
		back = group.doSelect();
		if(!back.isEmpty()){
		    addError(back);								
		}
	    }
	}				
	else{		
	    if(!id.isEmpty()){
		back = group.doSelect();
		if(!back.isEmpty()){
		    addError(back);
		}
		ret = "view";
	    }
	}
	return ret;
    }
    @StrutsParameter(depth=3)    
    public Group getGroup(){ 
	if(group == null){
	    group = new Group();
	    group.setId(id);
	    group.setDepartment_id(department_id);
	}		
	return group;
    }
    @StrutsParameter(depth=3)
    public void setGroup(Group val){
	if(val != null){
	    group = val;
	}
    }
    public String getGroup_id(){
	return group.getId();
    }
    public String getGroup_name(){
	return group.getName();
    }
    public String getDept_name(){
	return group.getDepartment().getName();
    }    
    public String getDescription(){
	return group.getDescription();
    }
    public String getExcessHoursEarnType(){
	return group.getExcessHoursEarnType();
    }
    public boolean getClock_time_required(){
	return group.getClock_time_required();
    }
    public boolean getAllowPendingAccrual(){
	return group.getAllowPendingAccrual();
    }
    public boolean isInactive(){
	return group.isInactive();
    }
    public boolean hasEmployees(){
	return group.hasEmployees();
    }
    @StrutsParameter(depth=3)    
    public List<Employee> getEmployees(){
	return group.getEmployees();
    }
    public boolean hasJobs(){
	return group.hasJobs();
    }
    @StrutsParameter(depth=3)        
    public List<JobTask> getJobs(){
	return group.getJobs();
    }
    public boolean hasGroupShifts(){
	return group.hasGroupShifts();
    }
    @StrutsParameter(depth=3)       
    public List<GroupShift> getGroupShifts(){
	return group.getGroupShifts();
    }
    public boolean hasLocation(){
	return group.hasLocation();
    }    
    @StrutsParameter(depth=2)
    public Location getLocation(){
	return group.getLocation();
    }
    /**
    public boolean hasGroupManagers(){
	return group.hasGroupManagers();
    }

    @StrutsParameter(depth=3)    
    public List<GroupManager> getGroupManagers(){
	return group.getGoupManagers();
    }
    */
    public String getGroupsTitle(){
	return groupsTitle;
    }
    @StrutsParameter(depth=1)
    public void setAction2(String val){
	if(val != null && !val.isEmpty())		
	    action = val;
    }
    @StrutsParameter(depth=1)
    public void setDepartment_id(String val){
	if(val != null && !val.isEmpty())		
	    department_id = val;
    }
    @StrutsParameter(depth=3)
    public List<Group> getGroups(){
	if(groups == null){
	    GroupList tl = new GroupList();
	    tl.setActiveOnly();
	    if(!department_id.isEmpty()){
		tl.setDepartment_id(department_id);
	    }
	    String back = tl.find();
	    if(back.isEmpty()){
		List<Group> ones = tl.getGroups();
		if(ones != null && ones.size() > 0){
		    groups = ones;
		}
	    }
	}
	return groups;
    }
    @StrutsParameter(depth=2)
    public List<Type> getDepartments(){
	TypeList tl = new TypeList("departments");
	String back = tl.find();
	if(back.isEmpty()){
	    List<Type> ones = tl.getTypes();
	    if(ones != null && ones.size() > 0){
		departments = ones;
	    }
	}
	return departments;
    }
    public boolean hasGroupManagers(){
	getGroupManagers();
	return groupManagers != null;
    }
    @StrutsParameter(depth=3)
    public List<GroupManager> getGroupManagers(){
	//
	if(groupManagers == null){
	    GroupManagerList gml = new GroupManagerList();
	    gml.setGroup_id(id);
	    String back = gml.find();
	    if(back.isEmpty()){
		List<GroupManager> ones = gml.getManagers();
		if(ones != null && ones.size() > 0){
		    groupManagers = ones;
		}
	    }
	}
	return groupManagers;
    }
    @StrutsParameter(depth=3)
    public List<HourCode> getHourCodes(){
	if(hourCodes == null){
	    HourCodeList gml = new HourCodeList();
	    gml.setEarnTypesOnly();
	    gml.setCurrentOnly();
	    String back = gml.find();
	    if(back.isEmpty()){
		List<HourCode> ones = gml.getHourCodes();
		if(ones != null && ones.size() > 0){
		    hourCodes = ones;
		}
	    }
	}
	return hourCodes;				
    }
    @StrutsParameter(depth=2)    //
    public String[] getExcessHoursEarnTypes(){
	return CommonInc.excess_hours_earn_types;
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





































