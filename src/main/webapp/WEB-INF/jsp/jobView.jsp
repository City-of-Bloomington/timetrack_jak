<%@ include file="header.jsp" %>
<div class="internal-page">
    <h1> Job: <s:property value="job_name" /></h1>	
    <s:if test="hasMessages()">
	<s:set var="messages" value="%{messages}" />
	<%@ include file="messages.jsp" %>
    </s:if>
    <s:if test="hasErrors()">
	<s:set var="errors" value="%{errors}" />
	<%@ include file="errors.jsp" %>
    </s:if>		
    `	<div class="width-one-half">
	<label>Employee: </label>
	<a href="<s:property value='#application.url' />employee.action?emp_id=<s:property value='emp_id' />"> <s:property value="emp_name" /></a>
	<br /><br />		
	<label>Job ID: </label>
	<s:property value="id" />
	<br /><br />
	
	<label>Position: </label>
	<s:property value="job_name"/>
	<br /><br />
	<label>Salary Group: </label>
	<s:property value="salary_group_name" />
	<br /><br />
	<s:if test="jobTask.hasGroup()">
	    <label>Group: </label>			
	    <s:property value="group_name" />
	    <br /><br />			
	</s:if>
	<label>Effective Date: </label>
	<s:property value="effective_date" />
	<br /><br />			
	<s:if test="jobTask.hasExpireDate()">
	    <label>Expire Date: </label>						
	    <s:property value="jobTask.expire_date" />
	    <br /><br />			
	</s:if>
	
	<label>Primary Job? </label>
	<s:if test="primary_flag">Yes</s:if><s:else>No</s:else>
	<br /><br />			
	<label>Weekly Reg Hours: </label>
	<s:property value="weekly_regular_hours" />
	<br /><br />			
	<label>Comp Time Weekly Hours: </label>
	<s:property value="comp_time_weekly_hours" />
	<br /><br />			
	<label>Comp Time Multiple Factor: </label>
	<s:property value="comp_time_factor" />
	<br /><br />			
	<label>Holiday Comp Multiple Factor: </label>
	<s:property value="holiday_comp_factor" />
	<br /><br />
	<label>Works Irregular Work Days? </label>
	<s:if test="irregularWorkDays">Yes</s:if><s:else>No</s:else>
	<br /><br />		
	<label>Added Date: </label>
	<s:property value="added_date" />
	<br /><br />			
	<div class="button-group">	
	    <a href="<s:property value='#application.url' />jobTask.action?id=<s:property value='id' />&action=Edit" class="button">Edit Job</a>
	    <a href="<s:property value='#application.url' />jobTask.action?add_employee_id=<s:property value='emp_id' />&effective_date=<s:property value='effective_date' />" class="button">Add Another Job</a>
	    <a href="<s:property value='#application.url' />jobChange.action?id=<s:property value='id' />" class="button">Change Job</a>			
	</div>
    </div>
    <s:if test="hasJobs()">
	<s:set var="jobTasks" value="jobs" />
	<s:set var="jobTasksTitle" value="'Employee Active Jobs'" />
	<%@ include file="jobTasks.jsp" %>	
    </s:if>
    <%@ include file="footer.jsp" %>
