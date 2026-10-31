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
import org.apache.struts2.util.Element;
import org.apache.struts2.util.Key;
import org.javatuples.Triplet;
import in.bloomington.timer.list.*;
import in.bloomington.timer.bean.*;
import in.bloomington.timer.util.Helper;
import in.bloomington.timer.util.CommonInc;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TimeDetailsAction extends TopAction{

    static final long serialVersionUID = 4300L;	
    static Logger logger = LogManager.getLogger(TimeDetailsAction.class);
    DecimalFormat df = new DecimalFormat("###.00");
    //
    PayPeriod payPeriod = null, currentPayPeriod=null,
	previousPayPeriod=null, nextPayPeriod=null;
    String timeBlocksTitle = "Time Details";
    String pay_period_id = "";
    String document_id = "", job_id="";
    Document document = null;
    MultiJobDoc mjdoc = null;
    String date = "", source="";
    JobTask job = null;
    List<JobTask> jobs = null;
    List<Type> jobTypes = null;
    List<PayPeriod> payPeriods = null;
    Annuancement annuance = null;
    boolean inAltPayPeriodSet = false;
    public String execute(){
	String ret = SUCCESS;
	String back = doPrepare("timeDetails.action");
	document_id = getDocument_id();
	if(document_id == null){
	    addError(" No job found for employee ");
	    return ret;
	}
	if(!hasEmployee()){
	    addError(" No employee found ");
	    return ret;
	}
	if(hasNoJob()){
	    addError(" No job found for employee ");
	    return ret;
	}
	else{
	    getDocument();
	}
	if(action.equals("View")){
	    ret = "view";
	}
	if(showAllJobs()){
	    ret = "view";
	}
	return ret;
    }
    public String getTimeBlocksTitle(){
	return timeBlocksTitle;
    }
    public void setAction2(String val){
	if(val != null && !val.isEmpty())		
	    action = val;
    }
    public void setSource(String val){
	if(val != null && !val.isEmpty())		
	    source = val;
    }
    //
    // This is the first thing that will be called on timedetails page		
    // check if we have document_id, if not we assume
    // it is a new pay period and we will create one
    //
    public String getDocument_id(){
	//
	if(document_id.isEmpty()){
	    DocumentList dl = new DocumentList();
	    if(employee_id.isEmpty()){
		getEmployee_id();
	    }
	    getEmployee();
	    if(hasEmployee()){
		job = employee.getJob();
		if(pay_period_id.isEmpty()){
		    getPayPeriod();
		}
		if(job != null){
		    job_id = job.getId();
		    if(pay_period_id.compareTo(CommonInc.pay_period_cut_id) > 0){
			inAltPayPeriodSet = job.isInAltPayPeriodSet();
		    }
		}
		dl.setEmployee_id(employee_id);		
		employee.setPay_period_id(pay_period_id);
		dl.setPay_period_id(pay_period_id);
		if(!job_id.equals("all")){
		    dl.setJob_id(job_id);
		}
		String back = dl.find();
		if(back.isEmpty()){
		    List<Document> ones = dl.getDocuments();
		    if(ones != null && ones.size() > 0){
			document = ones.get(0);
			document_id = document.getId();
		    }
		}
	    }
	}
	// 
	// if we could not find, then we create a new one
	//
	if(document_id.isEmpty()){
	    if(pay_period_id.isEmpty()){
		getPayPeriod();
		if(pay_period_id.isEmpty()){
		    addError(" Pay period not set ");
		}
	    }
	    if(employee_id.isEmpty()){
		getEmployee();
		getEmployee_id();
		if(employee_id.isEmpty()){
		    addError(" Employee not set ");
		}
		else{
		    job = employee.getJob();
		    if(job != null){
			job_id = job.getId();
			if(pay_period_id.compareTo(CommonInc.pay_period_cut_id) > 0){
			    inAltPayPeriodSet = job.isInAltPayPeriodSet();
			}
		    }		    
		}
	    }
	    if(hasEmployee()){
		employee.setPay_period_id(pay_period_id);
		if(job_id.isEmpty()){
		    getJob();
		    if(job_id.isEmpty()){
			addError(" Job not set ");
			return null;
		    }
		}
		if(!job_id.equals("all")){
		    getJob();
		    Document one = new Document(null, employee_id, pay_period_id, job_id, null, user.getId());
		    String back = one.doSave();
		    if(back.isEmpty()){
			document_id = one.getId();
			document = one;
		    }
		}
	    }
	}
	if(document != null){
	    inAltPayPeriodSet = document.isInAltPayPeriodSet();
	    // to overide the old one
	    if(inAltPayPeriodSet)
		payPeriod = document.getPayPeriod();
	}
	return document_id;
    }
    @StrutsParameter(depth=1)    		
    public void setDocument_id(String val){
	if(val != null && !val.isEmpty())		
	    document_id = val;
    }
    @StrutsParameter(depth=1)    
    public void setPay_period_id(String val){
	if(val != null && !val.isEmpty())		
	    pay_period_id = val;
    }
    @StrutsParameter(depth=1)
    public void setJob_id(String val){
	if(val != null && !val.equals("-1"))		
	    job_id = val;
    }
    @StrutsParameter(depth=1)
    public void setDate(String val){
	if(val != null && !val.isEmpty())		
	    date = val;
    }
    public String getPay_period_id(){
	if(pay_period_id.isEmpty() && !document_id.isEmpty()){
	    getDocument();
	    if(document != null)
		pay_period_id = document.getPay_period_id();
	}
	return pay_period_id;
    }
    public String getSource(){
	return source;
    }
    public boolean hasMjdoc(){
	getMjdoc();
	return mjdoc != null;
    }
    @StrutsParameter(depth=3)
    public MultiJobDoc getMjdoc(){
	if(mjdoc == null){
	    getEmployee();
	    if(employee_id.isEmpty()){
		addError(" employee id not set ");
		return null;
	    }
	    getPayPeriod();
	    if(pay_period_id.isEmpty()){
		addError(" pay period not set ");
		return null;
	    }
	    MultiJobDoc one = new MultiJobDoc(employee_id, pay_period_id);
	    String back = one.findDocuments();
	    if(back.isEmpty()){
		mjdoc = one;
	    }
	    else{
		System.err.println(" error "+back);
		addError(back);
	    }
	}
	return mjdoc;
    }
    @StrutsParameter(depth=3)    
    public Document getDocument(){
	if(document == null && !document_id.isEmpty()){
	    Document one = new Document(document_id);
	    String back = one.doSelect();
	    if(back.isEmpty()){
		document = one;
		job_id = document.getJob_id();
		pay_period_id = document.getPay_period_id();
		employee_id = document.getEmployee_id();
		if(employee == null){
		    getEmployee();
		}
		employee.setPay_period_id(pay_period_id);
		inAltPayPeriodSet = document.isInAltPayPeriodSet();
	    }
	}
	return document;
    }
    @StrutsParameter(depth=3)
    public List<PayPeriod> getPayPeriods(){
	if(payPeriods == null){
	    getPayPeriod(); // so that we can initialize the list
	    PayPeriodList tl = new PayPeriodList();
	    if(inAltPayPeriodSet){
		tl.setInAltPayPeriodSet();
	    }
	    tl.setTwoPeriodsAheadOnly();
	    tl.setEmployee_id(employee_id);
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
    public boolean userCanEdit(){
	boolean ret = false;
	if(document == null){
	    getDocument();
	}
	if(user == null){
	    getUser();
	}
	if(document != null && user != null){
	    ret = document.canEdit(user);
	}
	return ret;
    }
    public boolean isInAltPayPeriodSet(){
	return inAltPayPeriodSet;
    }
    public boolean isNotEditable(){
	boolean ret = false;
	if(document == null){
	    getDocument();
	}				
	ret = document.isProcessed() ||
	    (document.isApproved() && !userCanEdit()) ||
	    (isUserCurrentEmployee() &&
	     (document.isPunchClockOnly() || document.isApproved()));
	return ret;
    }
    public boolean canUserRequestChange(){
	return !isThereSimilarRequest() &&
	    isUserCurrentEmployee() &&
	    document.isApproved();
    }
    private boolean isThereSimilarRequest(){
	if(document_id.isEmpty()){
	    getDocument();
	    document_id = document.getId();
	}
	RequestCancelList rcl = new RequestCancelList(document_id);
	String back = rcl.find();
	if(back.isEmpty()){
	    List<RequestCancel> ones = rcl.getRequests();
	    return ones != null && ones.size() > 0;
	}
	return false;
    }
    @StrutsParameter(depth=2)
    public PayPeriod getPayPeriod(){
	//
	// if pay period is not set, we look for current one
	//
	if(payPeriod == null){
	    if(pay_period_id.isEmpty()){
		if(document_id.isEmpty()){
		    PayPeriodList ppl = new PayPeriodList();
		    ppl.currentOnly();
		    if(inAltPayPeriodSet){
			ppl.setInAltPayPeriodSet();
		    }
		    String back = ppl.find();
		    if(back.isEmpty()){
			List<PayPeriod> ones = ppl.getPeriods();
			if(ones != null && ones.size() > 0){
			    payPeriod = ones.get(0);
			    pay_period_id = payPeriod.getId();
			}
		    }
		}
		else{
		    getDocument();
		    if(document != null){
			payPeriod = document.getPayPeriod();
			pay_period_id = document.getPay_period_id();
		    }
		}
	    }
	    else{
		PayPeriod one = new PayPeriod(pay_period_id);
		if(inAltPayPeriodSet){
		    one.setInAltPayPeriodSet();
		}
		String back = one.doSelect();
		if(back.isEmpty()){
		    payPeriod = one;
		}
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
	    if(inAltPayPeriodSet){
		ppl.setInAltPayPeriodSet();
	    }	    
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
    @StrutsParameter(depth=2)
    public PayPeriod getPreviousPayPeriod(){
	//
	if(previousPayPeriod == null){
	    if(pay_period_id.isEmpty())
		getPayPeriod();
	    PayPeriodList ppl = new PayPeriodList();
	    ppl.setPreviousTo(pay_period_id); // relative to currently used
	    if(inAltPayPeriodSet){
		ppl.setInAltPayPeriodSet();
	    }
	    String back = ppl.find();
	    if(back.isEmpty()){
		List<PayPeriod> ones = ppl.getPeriods();
		if(ones != null && ones.size() > 0){
		    previousPayPeriod = ones.get(0);
		}
	    }
	}
	return previousPayPeriod;
    }
    @StrutsParameter(depth=2)
    public PayPeriod getNextPayPeriod(){
	//
	if(nextPayPeriod == null){
	    if(pay_period_id.isEmpty())
		getPayPeriod();
	    PayPeriodList ppl = new PayPeriodList();
	    ppl.setNextTo(pay_period_id); // relative to this currently used
	    if(inAltPayPeriodSet){
		ppl.setInAltPayPeriodSet();
	    }
	    String back = ppl.find();
	    if(back.isEmpty()){
		List<PayPeriod> ones = ppl.getPeriods();
		if(ones != null && ones.size() > 0){
		    nextPayPeriod = ones.get(0);
		}
	    }
	}
	return nextPayPeriod;
    }				
    public boolean isCurrentPayPeriod(){
	getCurrentPayPeriod();
	getPay_period_id();
	return pay_period_id.equals(currentPayPeriod.getId());
    }
    @StrutsParameter(depth=3)
    public JobTask getJob(){
	if(job_id.isEmpty() && job == null){
	    getJobs();
	    if(jobs != null && jobs.size() > 1){
		for(JobTask one:jobs){
		    if(one.isPrimary()){
			job = one;
			break;
		    }
		}
		//
		// if no job is marked as primary,
		// then we pick the first
		if(job == null && jobs != null && jobs.size() > 0){
		    job = jobs.get(0);
		}
	    }
	    else if(jobs != null && jobs.size() == 1){
		job = jobs.get(0);
	    }
	    if(job != null && !job_id.equals("all")){
		job_id = job.getId();
	    }
	}
	else if(!job_id.isEmpty() && !job_id.equals("all") && job == null){
	    JobTask one = new JobTask(job_id);
	    String back = one.doSelect();
	    if(back.isEmpty()){
		job = one;
	    }
	}
	return job;
    }
    public boolean showAllJobs(){
	return hasMultipleJobs() && job_id.equals("all");
    }
    public boolean hasAllAccruals(){
	return document.hasAllAccruals();
    }
    public boolean isPendingAccrualAllowed(){
	return document.isPendingAccrualAllowed();
    }
    public Map<String, List<String>> getAllAccruals(){
	return document.getAllAccruals();
    }    
    public boolean hasJob(){
	getJob();
	return job != null;
    }
    public String getJob_id(){
	return job_id;
    }
    public boolean canCancelAction(){
	getDocument();
	getUser();				
	if(document != null && user != null){
	    //
	    // we want to know if the user is one of payroll processors
	    //
	    boolean ret = document != null &&
		// (document.isProcesser(user) ||
		 document.isActionedBy(user);
	    if(ret){
		TimeAction lastAction = document.getLastTimeAction();
		ret =  lastAction != null && lastAction.canBeCancelled();
		return ret;
	    }
	}
	return false;
    }
    @StrutsParameter(depth=3)
    public List<JobTask> getJobs(){
	if(jobs == null){
	    JobTaskList jl = new JobTaskList(getEmployee_id());
	    if(pay_period_id.isEmpty()){
		getPayPeriod();
	    }
	    if(!pay_period_id.isEmpty()){
		jl.setPay_period_id(pay_period_id);
	    }
	    String back = jl.find();
	    if(back.isEmpty()){
		List<JobTask> ones = jl.getJobTasks();
		if(ones != null && ones.size() > 0){
		    jobs = ones;
		    if(jobs.size() == 1){
			job = jobs.get(0);
			job_id = job.getId();
		    }
		}
	    }
	}
	return jobs;
    }		
    public boolean hasNoJob(){
	getJobs();
	return jobs == null || jobs.size() == 0;
    }
    public boolean hasMultipleJobs(){
	getJobs();
	return jobs != null && jobs.size() > 1;
    }
    public boolean hasJobTypes(){
	getJobTypes();
	return jobTypes != null && jobTypes.size() > 0;
    }
    @StrutsParameter(depth=3)
    public List<Type> getJobTypes(){
	getJobs();
	if(jobs != null && jobs.size() > 1){
	    if(jobTypes == null){
		jobTypes = new ArrayList<>();
		Type tp = new Type("all","All");
		jobTypes.add(tp);					
		for(JobTask one:jobs){
		    tp = new Type(one.getId(), one.getName());
		    jobTypes.add(tp);
		}
	    }
	}
	return jobTypes;
    }
    void findCurrentAnnuance(){
	AnnuancementList al = new AnnuancementList();
	if(al.hasCurrentAnnuance()){
	    annuance = al.getCurrentAnnuance();
	}
    }
    public String getWeek1DateRange(){
	if(payPeriod == null)
	    getPayPeriod();
	return payPeriod.getWeek1DateRange();
    }
    public String getWeek2DateRange(){
	if(payPeriod == null)
	    getPayPeriod();
	return payPeriod.getWeek2DateRange();
    }
    public String getNextPayPeriod_id(){
	return getNextPayPeriod().getId();
    }
    public String getPreviousPayPeriod_id(){
	return getPreviousPayPeriod().getId();
    }
    public String getMonthNames(){
	getPayPeriod();
	return payPeriod.getMonthNames();
    }
    public Map<JobType, Map<Integer, String>> getDaily(){
	if(document == null){
	    getDocument();
	}
	if(document != null){	
	    return document.getDaily();
	}
	return null;
    }	    
    public boolean isUnionned(){
	if(document == null){
	    getDocument();
	}
	if(document != null){
	    return document.isUnionned();
	}
	return false;
    }
    public String getSubmitted_time(){
	if(document == null){
	    getDocument();
	}
	if(document != null){
	    return document.getSubmitTimeAction().getAction_time();
	}
	return "";
    }
    public int getStartYear(){
	getPayPeriod();
	return payPeriod.getStartYear();	
    }
    public String getCurrentPayPeriod_id(){
	return getCurrentPayPeriod().getId();
    }    
    public String getPayPeriodTotal(){
	return document.getPayPeriodTotal();
    }
    public String getPayPeriodAmount(){
	return document.getPayPeriodAmount();
    }
    public boolean hasAnnuance(){
	findCurrentAnnuance();
	return annuance != null;
    }
    public boolean hasDailyBlocks(){
	return document.hasDailyBlocks();
    }
    @Key(Integer.class)
    @Element(value=in.bloomington.timer.bean.DayBlocks.class)
    @StrutsParameter(depth=5)
    //@org.apache.struts2.interceptor.validation.SkipValidation
    public Map<Integer, DayBlocks> getDayBlocksMap(){
	return document.getDayBlocksMap();
    }
    @Key(Integer.class)
    @Element(value=in.bloomington.timer.bean.TimeBlock.class)
    @StrutsParameter(depth=5)
    //@org.apache.struts2.interceptor.validation.SkipValidation
    public Map<Integer, List<TimeBlock>> getDailyBlocks(){
	return document.getDailyBlocks();
    }
    @StrutsParameter(depth=4)
    public List<DayBlocks> getDayBlocksList(){
	List<DayBlocks> ll = new ArrayList<>();
	Map<Integer, DayBlocks> dayBlocksMap = document.getDayBlocksMap();	
	for(int i=0;i<14;i++){
	    ll.add(dayBlocksMap.get(i));
	}
	return ll;
    }    
    public boolean hasLastWorkflow(){
	getDocument();
	return document.hasLastWorkflow();
    }
    public boolean hasNextWorkflow(){
	getDocument();
	return document.getLastWorkflow().hasNextNode();
    }    
    public String getNextWorkflowName(){
	if(hasLastWorkflow()){
	    if(document.getLastWorkflow().hasNextNode()){
		return document.getLastWorkflow().getNextNode().getName();
	    }
	}
	return "";
    }
    public String getNextActionerNames(){
	getDocument();
	return document.getNextActionerNames();
    }
    public boolean hasAccruals(){
	return document.hasAccruals();
    }
    public boolean canSubmit(){
	return document.getLastWorkflow().canSubmit();
    }
    public String getNext_workflow_id(){
	return document.getLastWorkflow().getNext_workflow_id();
    }
    public String getWeek1Total(){
	return document.getWeek1Total();
    }
    public String getWeek2Total(){
	return document.getWeek2Total();
    }
    public String getWeek1_flsa(){
	return document.getWeek1_flsa();
    }
    public String getWeek2_flsa(){
	return document.getWeek2_flsa();
    }
    public boolean hasWarnings(){
	return document.hasWarnings();
    }
    @StrutsParameter(depth=2)
    public List<String> getWarnings(){
	return document.getWarnings();
    }
    public boolean hasTimeActions(){
	getDocument();
	return document.hasTimeActions();
    }
    @StrutsParameter(depth=5)
    public List<TimeAction> getTimeActions(){
	return document.getTimeActions();
    }
    public boolean hasTimeNotes(){
	return document.hasTimeNotes();
    }
    @StrutsParameter(depth=2)
    public List<TimeNote> getTimeNotes(){
	return document.getTimeNotes();
    }    
    public boolean hasTmwrpRun(){
	return document.hasTmwrpRun();
    }
    public int getWeekly_regular_hours(){
	getJob();
	return job.getWeekly_regular_hours();
    }
    public int getComp_time_weekly_hours(){
	getJob();
	return job.getComp_time_weekly_hours();
    }
    public boolean hasUnscheduleds(){
	return document.hasUnscheduleds();
    }
    @StrutsParameter(depth=2)    
    public List<Triplet<String, String, String>> getUnscheduleds(){
	return document.getUnscheduleds();
    }
    public String getEmployeeAccrualsShort(){
	return document.getEmployeeAccrualsShort();
    }
    public boolean isProcessed(){
	return document.isProcessed();
    }
    public boolean hasWeek1Rows(){
	return document.getTmwrpRun().hasWeek1Rows();
    }
    public boolean hasWeek2Rows(){
	return document.getTmwrpRun().hasWeek2Rows();
    }
    @StrutsParameter(depth=3)
    public List<List<String>> getWeek1Rows(){
	return document.getTmwrpRun().getWeek1Rows();
    }
    @StrutsParameter(depth=3)
    public List<List<String>> getWeek2Rows(){
	return document.getTmwrpRun().getWeek2Rows();
    }
    public boolean hasCycleRows(){
	return document.getTmwrpRun().hasCycleRows();
    }
    @StrutsParameter(depth=3)
    public List<List<String>> getCycleRows(){
	return document.getTmwrpRun().getCycleRows();
    }
    public boolean hasLeaveRequests(){
	return document.hasLeaveRequests();
    }
    @StrutsParameter(depth=3)
    public List<LeaveRequest> getLeaveRequests(){
	return document.getLeaveRequests();
    }
    @StrutsParameter(depth=2)
    public Annuancement getAnnuance(){
	return annuance;
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






































