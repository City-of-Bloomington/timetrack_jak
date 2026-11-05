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

public class TimeActionAction extends TopAction{

    static final long serialVersionUID = 4350L;	
    static Logger logger = LogManager.getLogger(TimeActionAction.class);
    //
    TimeAction timeAction = null;
    List<TimeAction> otherActions = null; 
    String timeActionsTitle = "Time Actions";
    String document_id = "", workflow_id="", pay_period_id="";
    String source = ""; // where we are coming from
    //
    public String execute(){
	String ret = SUCCESS;
	String back = doPrepare("timeAction");
	if(!back.isEmpty()){
	    return back;
	}
	getTimeAction();	
	if(action.startsWith("Submit")){
	    getUser();
	    if(user != null){
		timeAction.setAction_by(user.getId());
		back = timeAction.doSave();
		if(!back.isEmpty()){
		    addError(back);
		}
		else{ // normally we return to TimeDetails
		    addMessage("Saved Successfully");
		}
	    }
	}
	else if(action.startsWith("Cancel")){
	    getUser();
	    if(user != null){
		timeAction.setCancelled_by(user.getId());
		back = timeAction.doCancel();
		if(!back.isEmpty()){
		    addError(back);
		}
		else{ // normally we return to TimeDetails
		    if(hasOtherActions()){
			for(TimeAction one:otherActions){
			    if(!one.getId().equals(timeAction.getId())){
				one.setCancelled_by(user.getId());
				back = one.doCancel();
			    }
			}
		    }
		    addMessage("Cancelled Successfully");
		    ret = "timeDetails";
		}
	    }
	}				
	else{		
	    if(!id.isEmpty()){
		back = timeAction.doSelect();
		if(!back.isEmpty()){
		    addError(back);
		}
		else{
		    document_id = timeAction.getDocument_id();
		    workflow_id = timeAction.getWorkflow_id();
		}
	    }
	}
	if(!source.isEmpty()){
	    try{
		HttpServletResponse res = ServletActionContext.getResponse();
		// we do not need the url here
		String str = source+".action?";
		if(!document_id.isEmpty()){
		    str += "document_id="+document_id;
		}
		res.sendRedirect(str);
	    }catch(Exception ex){
		System.err.println(ex);
	    }
	}
	return ret;
    }
    @StrutsParameter(depth=2)        
    public TimeAction getTimeAction(){ 
	if(timeAction == null){
	    timeAction = new TimeAction();
	    timeAction.setId(id);
	    timeAction.setDocument_id(document_id);
	    timeAction.setWorkflow_id(workflow_id);
	}
	return timeAction;
    }
    @StrutsParameter(depth=1)
    public void setPay_period_id(String val){
	if(val != null)
	    pay_period_id = val;
    }
	
    private boolean hasOtherActions(){
	if(otherActions == null)
	    findOtherTimeActions();
	return otherActions != null && otherActions.size() > 0;
    }
    private void findOtherTimeActions(){
	TimeActionList tal = new TimeActionList();
	tal.setDocument_id(document_id);
	tal.setActiveOnly();
	tal.setApproversAndProcessorsOnly();
	String back = tal.find();
	if(back.isEmpty()){
	    List<TimeAction> ones = tal.getTimeActions();
	    if(ones != null && ones.size() > 0){
		otherActions = ones;
	    }
	}
    }
    @StrutsParameter(depth=2)    
    public void setTimeAction(TimeAction val){
	if(val != null){
	    timeAction = val;
	}
    }
    public String getTimeActionsTitle(){
	return timeActionsTitle;
    }
    @StrutsParameter(depth=1)    
    public void setAction2(String val){
	if(val != null && !val.isEmpty())		
	    action = val;
    }
    @StrutsParameter(depth=1)    
    public void setSource(String val){
	if(val != null && !val.isEmpty())		
	    source = val;
    }		
    //
    // this is passed through the link
    public String getDocument_id(){
	if(document_id.isEmpty() && timeAction != null){
	    document_id = timeAction.getDocument_id();
	}
	return document_id;
    }
    @StrutsParameter(depth=1)    
    public void setDocument_id(String val){
	if(val != null && !val.isEmpty())		
	    document_id = val;
    }
    @StrutsParameter(depth=1)    
    public void setWorkflow_id(String val){
	if(val != null && !val.isEmpty())		
	    workflow_id = val;
    }		
    public String getWorkflow_id(){
	return workflow_id;
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





































