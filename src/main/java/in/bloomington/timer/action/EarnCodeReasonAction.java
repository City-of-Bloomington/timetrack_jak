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

public class EarnCodeReasonAction extends TopAction{

    static final long serialVersionUID = 750L;	
    static Logger logger = LogManager.getLogger(EarnCodeReasonAction.class);
    //
    EarnCodeReason reason = null;
    List<EarnCodeReason> reasons = null;
    List<ReasonCategory> categories = null;
    String reasonsTitle = "Current Earn Codde Reasons";
    public String execute(){
	String ret = SUCCESS;
	String back = canProceed("earnCodeReason.action");
	if(!back.isEmpty()){
	    return back;
	}
	getReason();
	if(action.equals("Save")){
	    back = reason.doSave();
	    if(!back.isEmpty()){
		addError(back);
	    }
	    else{
		addMessage("Added Successfully");
	    }
	}				
	else if(action.startsWith("Save")){
	    back = reason.doUpdate();
	    if(!back.isEmpty()){
		addError(back);
	    }
	    else{
		addMessage("Saved Successfully");
	    }
	}
	else{		
	    if(!id.isEmpty()){
		back = reason.doSelect();
		if(!back.isEmpty()){
		    addError(back);
		}
	    }
	}
	return ret;
    }
    @StrutsParameter(depth=2)
    public EarnCodeReason getReason(){
	if(reason == null){
	    reason = new EarnCodeReason(id);
	}
	return reason;
						
    }
    @StrutsParameter(depth=2)
    public void setReason(EarnCodeReason val){
	if(val != null){
	    reason = val;
	}
    }
    public String getReasonsTitle(){
	return reasonsTitle;
    }
    @StrutsParameter(depth=1)
    public void setAction2(String val){
	if(val != null && !val.isEmpty())		
	    action = val;
    }
    public boolean hasReasons(){
	getReasons();
	return reasons != null;
    }
    @StrutsParameter(depth=2)
    public List<EarnCodeReason> getReasons(){
	if(reasons == null){
	    EarnCodeReasonList tl = new EarnCodeReasonList();
	    String back = tl.find();
	    if(back.isEmpty()){
		List<EarnCodeReason> ones = tl.getReasons();
		if(ones != null && ones.size() > 0){
		    reasons = ones;
		}
	    }
	}
	return reasons;
    }
    public boolean hasCategories(){
	getCategories();
	return categories != null;
    }
    @StrutsParameter(depth=2)
    public List<ReasonCategory> getCategories(){
	if(categories == null){
	    ReasonCategoryList tl = new ReasonCategoryList();
	    String back = tl.find();
	    if(back.isEmpty()){
		List<ReasonCategory> ones = tl.getReasonCategories();
		if(ones != null && ones.size() > 0){
		    categories = ones;
		}
	    }
	}
	return categories;
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





































