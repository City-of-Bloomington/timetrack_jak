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

public class TermRecipientAction extends TopAction{

    static final long serialVersionUID = 3800L;	
    static Logger logger = LogManager.getLogger(TermRecipientAction.class);
    //
    String recipientsTitle = "Current Termination Nofication Recipients";
    TermRecipient recipient = null;
    List<TermRecipient> recipients = null;

    public String execute(){
	String ret = SUCCESS;
	String back = canProceed("termRecipient.action");
	if(!back.isEmpty()){
	    return back;
	}
	getRecipient();
	if(action.equals("Save")){
	    back = recipient.doSave();
	    if(!back.isEmpty()){
		addError(back);
		addActionError(back);
	    }
	    else{
		addMessage("Saved Successfully");
	    }
	}				
	else if(action.startsWith("Save")){
	    back = recipient.doUpdate();
	    if(!back.isEmpty()){
		addError(back);
	    }
	    else{
		addMessage("Saved Successfully");
	    }
	}
	else if(action.startsWith("Remove")){
	    if(recipient == null){
		getRecipient();
	    }
	    back = recipient.doDelete();
	    if(!back.isEmpty()){
		addError(back);
	    }
	    else{
		addMessage("Deleted Successfully");
	    }
	}	
	else{		
	    if(!id.isEmpty()){
		back = recipient.doSelect();
		if(!back.isEmpty()){
		    addError(back);
		}
	    }
	}
	return ret;
    }
    @StrutsParameter(depth=2)
    public TermRecipient getRecipient(){
	if(recipient == null){
	    recipient = new TermRecipient();
	    recipient.setId(id);
	}
	return recipient;
						
    }
    @StrutsParameter(depth=2)
    public void setRecipient(TermRecipient val){
	if(val != null){
	    recipient = val;
	}
    }
		
    public String getRecipientsTitle(){
				
	return recipientsTitle;
    }
    @StrutsParameter(depth=1)
    public void setAction2(String val){
	if(val != null && !val.isEmpty())		
	    action = val;
    }
    @StrutsParameter(depth=3)
    public List<TermRecipient> getRecipients(){
	if(recipients == null){
	    TermRecipientList tl = new TermRecipientList();
	    String back = tl.find();
	    if(back.isEmpty()){
		List<TermRecipient> ones = tl.getRecipients();
		if(ones != null && ones.size() > 0){
		    recipients = ones;
		}
	    }
	}
	return recipients;
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





































