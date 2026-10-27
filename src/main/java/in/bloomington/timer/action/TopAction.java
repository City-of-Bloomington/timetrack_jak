/**
 * @copyright Copyright (C) 2014-2015 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
package in.bloomington.timer.action;
import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.time.Instant;
import java.time.Duration;
import java.io.*;
import java.text.*;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.inject.Inject;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.context.ApplicationScoped;
import org.apache.struts2.interceptor.parameter.StrutsParameter;
import org.apache.struts2.ServletActionContext;  
import org.apache.struts2.dispatcher.SessionMap;  
import org.apache.struts2.action.SessionAware;
import org.apache.struts2.action.ServletContextAware;  
import in.bloomington.timer.util.*;
import in.bloomington.timer.bean.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// @RequestScoped
// @ApplicationScoped
public abstract class TopAction implements SessionAware, ServletContextAware{

    // @Inject
    private HttpServletRequest request;
    
    static String LOGIN="login";
    static String INPUT="input";    
    static String SUCCESS="success";
    static String ERROR="error";
    static final long serialVersionUID = 3600L;
    static Logger logger = LogManager.getLogger(TopAction.class);		
    boolean debug = false;
    static boolean activeMail = false;
    static String server_path="";
    static EnvBean envBean = null;
    static String mail_host = null;
    static String uri="",url="", proxy_url="";
    static String xls_output_location="";
    static String app_mode="";
    // for timeClock and locations
    static final long period_length = 5L; // minutes
    // static Instant startTime = null;		
    static Set<String> ipSet = null;
    static Hashtable<String, String> locationHash = null;
    String action="", id="", employee_id="";
    List<String> errors = new ArrayList<>(),
	messages = new ArrayList<>();
    Employee user = null;
    Employee employee = null;
    boolean has_session = false;
    ServletContext ctx;
    Map<String, Object> sessionMap;

    @StrutsParameter(depth=1)    
    public void setAction(String val){
	if(val != null)
	    action = val;
    }
    @StrutsParameter(depth=1)        
    public void setAction2(String val){
	if(val != null && !val.isEmpty())
	    action = val;
    }		
    public String getAction(){
	return action;
    }
    @StrutsParameter(depth=1)    
    public void setId(String val){
	if(val != null)
	    id = val;
    }
    public String getId(){
	return id;
    }
    @StrutsParameter(depth=3)    
    public Employee getUser(){
	if(user == null){
	    if(sessionMap == null || sessionMap.get("user") == null){
		//
		// timeClock we do not need login
		//
	    }
	    else{
		user = (Employee)sessionMap.get("user");
	    }
	}
	return user;
    }
    public boolean canEdit(){
	getUser();
	return user != null && user.canEdit();
    }
    public boolean isAdmin(){
	getUser();
	return user != null && user.isAdmin();
    }
    public boolean isHrAdmin(){
	getUser();
	return user != null && user.isHrAdmin();
    }    
    public boolean isITSAdmin(){
	getUser();
	return user != null && user.isITSAdmin();
    }
    public boolean canMaintain(){
	getUser();
	return user != null && user.canMaintain();
    }    
    public boolean canReview(){
	getUser();
	return user != null && user.canReview();
    }
    public boolean canApprove(){
	getUser();
	return user != null && user.canApprove();
    }
    public boolean isTermManager(){
	getUser();
	return user != null && user.isTermManager();
    }
    public boolean canPayrollProcess(){
	getUser();
	return user != null && user.canPayrollProcess();
    }        
    public boolean isLeaveEligible(){
	getUser();
	return user != null && user.isLeaveEligible();
    }
    public boolean canLeaveReview(){
	getUser();
	return user != null && user.canLeaveReview();
    }
    public boolean canRunTargetEmployee(){
	getUser();
	return user != null && user.canRunTargetEmployee();
    }    
    public boolean canSearchEmployee(){
	getUser();
	return user != null && user.canSearchEmployee();
    }
    public boolean canAdSearch(){
	getUser();
	return user != null && user.canAdSearch();
    }
    public boolean isEngineeringAdmin(){
	getUser();
	return user != null && user.isEngineeringAdmin();
    }
    public boolean isPublicWorksAdmin(){
	getUser();
	return user != null && user.isPublicWorksAdmin();
    }
    public boolean isPoliceAdmin(){
	getUser();
	return user != null && user.isPoliceAdmin();
    }    
    public boolean canRunMpoReport(){
	getUser();
	return user != null && user.canRunMpoReport();
    }
    public boolean canRunHandReport(){
	getUser();
	return user != null && user.canRunHandReport();
    }
    public boolean canRunFireReport(){
	getUser();
	return user != null && user.canRunFireReport();
    }
    public boolean isParkAdmin(){
	getUser();
	return user != null && user.isParkAdmin();
    }    
    public boolean canRunTimewarp(){
	getUser();
	return user != null && user.canRunTimewarp();
    }
    String doPrepare(String source){
	String back = "", val="";
	try{
	    if(url.isEmpty()){
		val = ctx.getInitParameter("url");
		if(val != null)
		    url = val;
		val = ctx.getInitParameter("proxy_url");
		if(val != null)
		    proxy_url = val;								
		val = ctx.getInitParameter("server_path");
		if(val != null)
		    server_path = val;
		val = ctx.getInitParameter("mail_host");
		if(val != null)
		    mail_host = val;
		val = ctx.getInitParameter("xls_output_location");
		if(val != null)
		    xls_output_location = val;
		val = ctx.getInitParameter("activeMail");
		if(val != null && val.equals("true")){
		    activeMail = true;
		}
		val = ctx.getInitParameter("app_mode");
		if(val != null && !val.isEmpty()){
		    app_mode = val;
		}		
	    }
	    if(envBean == null){
		envBean = new EnvBean();
		val = ctx.getInitParameter("ldap_url");
		if(val != null)
		    envBean.setUrl(val);
		val = ctx.getInitParameter("ldap_principle");
		if(val != null)
		    envBean.setPrinciple(val);
		val = ctx.getInitParameter("ldap_password");
		if(val != null)
		    envBean.setPassword(val);
	    }

	    if(sessionMap == null || sessionMap.get("user") == null){
		//
		// timeClock we do not need login
		//
		back = "No Sesion ";
		return back;
	    }
	    else{
		has_session = true;
		user = (Employee)sessionMap.get("user");
		if(sessionMap != null && sessionMap.containsKey("employee_id")){
		    Object obj = sessionMap.get("employee_id");
		    if(obj != null){
			employee_id = (String) obj;
		    }
		}
		if(url.isEmpty())
		    setUrls();
		clearAll();
	    }
	}catch(Exception ex){
	    logger.error(ex);
	}
	return back;
    }
    String doPrepare(){
	return doPrepare(null);
    }
    @StrutsParameter(depth=1)    
    public void setEmployee_id(String val){
	if(val != null && !val.isEmpty()){		
	    employee_id = val;
	    if(sessionMap != null){
		sessionMap.put("employee_id", employee_id);
	    }
	}
    }
    
    public String getEmployee_id(){
	if(employee_id.isEmpty()){
	    if(sessionMap != null && sessionMap.containsKey("employee_id")){
		Object obj = sessionMap.get("employee_id");
		if(obj != null){
		    employee_id = (String) obj;
		}
	    }
	    else {
		if(user != null){
		    String str = user.getId();
		    if(str != null && str.length() > 0){
			setEmployee_id(str);
			employee = user;
			employee_id = user.getId();
		    }
		}
	    }
	}
	return employee_id;
    }
    @StrutsParameter(depth=3)        
    public Employee getEmployee(){
	if(employee_id.isEmpty()){
	    getEmployee_id();
	}
	if(employee == null && !employee_id.isEmpty()){
	    Employee one = new Employee(employee_id);
	    String back = one.doSelect();
	    if(back.isEmpty()){
		employee = one;
	    }
	}				
	if(employee == null){
	    if(user != null){
		employee = user;
		employee_id = user.getId();
	    }
	}
	return employee;
    }
    public String getEmployee_name(){
	getEmployee();
	if(employee != null)
	    return employee.getFull_name();
	return "";
    }
    public boolean isAppInTestMode(){
	return app_mode.equals("Test");
    }
    public boolean hasEmployee(){
	getEmployee();
	return employee != null;
    }
    public boolean isUserCurrentEmployee(){
	if(user == null){
	    getUser();
	}
	if(user != null){
	    getEmployee();
	    return employee_id.equals(user.getId());
	}
	return false;
    }
    // to change proxy employee back to main user
    void resetEmployee(){
	if(user != null){
	    setEmployee_id(user.getId());
	    getEmployee();
	}
    }
    void addError(String str){
	if(errors == null)
	    errors = new ArrayList<>();
	if(str != null)
	    errors.add(str);
    }
    void addMessage(String str){
	if(messages == null)
	    messages = new ArrayList<>();
	if(str != null){
	    if(!messages.contains(str.trim()))
		messages.add(str.trim());
	}
    }
    public void setMessages(String[] vals){
	if(vals != null){
	    for(String val:vals){
		addMessage(val);
	    }
	}
    }
    void clearAll(){
	if(errors.size() > 0)
	    errors = new ArrayList<>();
	if(messages.size() > 0)
	    messages = new ArrayList<>();
    }
    @StrutsParameter(depth=1)             
    public boolean hasErrors(){
	return errors != null && errors.size() > 0;
    }
    @StrutsParameter(depth=2)         
    public List<String> getErrors(){
	return errors;
    }
    public void setErrors(List<String> vals){
	if(vals != null)
	    errors = vals;
    }

    public void addActionMessage(String str){
	messages.add(str);
    }
    public void addActionError(String str){
	errors.add(str);
    }
    public boolean hasActionErrors(){
	return errors.size() > 0;
    }
    public boolean hasActionMessages(){
	return messages.size() > 0;
    }    
    public List<String> getActionErrors(){
	return errors;
    }
    public List<String> getActionMessages(){
	return messages;
    }
													
    public String getErrorsAll(){
	String ret = "";
	if(hasErrors()){
	    for(String str:errors){
		if(!ret.isEmpty()) ret += ", ";
		ret += str;
	    }
	}
	return ret;
    }
    public boolean hasMessages(){
	return messages != null && messages.size() > 0;
    }
    @StrutsParameter(depth=2)         
    public List<String> getMessages(){
	return messages;
    }
    String obtainGroupIdFromSession(){
	String val = "";
	try{
	    if(sessionMap != null && sessionMap.containsKey("selected_group_id")){
		Object obj = sessionMap.get("selected_group_id");
		if(obj != null){
		    String str = (String) obj;
		    if(str != null)
			val = str;
		}
	    }
	}catch(Exception ex){
	    System.err.println(ex);
	}
	return val;
    }
    void addGroupIdToSession(String val){
	if(val != null && !val.isEmpty()){
	    if(sessionMap != null){						
		try{
		    sessionMap.put("selected_group_id", val);						
		}catch(Exception ex){
		    System.err.println(ex);
		}
	    }
	}
    }
    public  String canProceed(String val){
	String back = doPrepare(val);
	if(back.isEmpty() && user != null && 
	   (canEdit() || isAdmin() || isITSAdmin())){
	    return "";
	}
	return "Not allowed";
    }
    private void setUrls(){
	if(!url.isEmpty()) return;
	// HttpServletRequest request = ServletActionContext.getRequest();				
	String host_forward = request.getHeader("X-Forwarded-Host");
	String host = request.getHeader("host");				

	System.err.println(" host forward "+host_forward);
	System.err.println(" host "+host);	
	if(host_forward != null){
	    if(host_forward.indexOf("/timetrack") == -1){
		// url = host_forward+"/timetrack/";
		
	    }
	    else
		url = host_forward;
	}
	else if(host != null){
	    if(host.indexOf("timetrack") > -1){
		url = host;
	    }
	    else{
		// url = host+"/timetrack/";
	    }
	}
	if(true){
	    String pat = "timetrack";
	    Pattern pattern = Pattern.compile(pat);
	    Matcher matcher = pattern.matcher(url);
	    int cnt = 0, end_id=0;
	    while(matcher.find()){
		if(end_id == 0)
		    end_id = matcher.end();
		cnt++;
	    }
	    if(cnt > 1){
		url = url.substring(end_id+1);
	    }				
	    System.err.println(" url "+url);
	}
    }
		
}





































