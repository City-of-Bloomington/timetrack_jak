<%@ include file="header.jsp" %>
<div class="internal-page">
	<h1>Employee: <s:property value="emp.full_name" /></h1>
	<s:if test="hasMessages()">
		<s:set var="messages" value="%{messages}" />
		<%@ include file="messages.jsp" %>
	</s:if>
	<s:if test="hasErrors()">
		<s:set var="errors" value="%{errors}" />
		<%@ include file="errors.jsp" %>
	</s:if>		
	<div class="width-one-half">
		<label>ID: </label>
		<s:property value="id" /> <br /><br />
		
		<label>Username: </label>
		<s:property value="username" /> <br /> <br />
		
		<label>First Name: </label>
		<s:property value="first_name" /> <br /> <br />
		
		<label>Last Name: </label>
		<s:property value="last_name" /> <br /><br />
		
		<label>ID Code #: </label>
		<s:property value="id_code" /> <br /><br />

		<label>Employee #: </label>
		<s:property value="employee_number" /> <br /><br />
		
		<label>AD Sid </label>
		<s:property value="ad_sid" /> <br /><br />

		<label>Email: </label>
		<s:property value="email" /> <br /><br />
		
		<s:if test="hasDepartment()">
		    <label>Department: </label>
		    <s:property value="deptName" /> <br /><br />
		</s:if>
		
		<label>Added Date: </label>
		<s:property value="added_date" /> <br /><br />
		
		<label>Roles: </label>
		<s:property value="rolesText" /> <br /><br />
		
		<label>Active? </label>
		<s:if test="inactive"> No </s:if><s:else>Yes</s:else> <br /><br />
	</div>		
	<div class="button-group">
	    <a href="<s:property value='#application.url' />employee.action?action=Edit&emp_id=<s:property value='emp_id' />" class="button">Edit</a> 
	    <s:if test="!emp.hasDepartments()">
		<a href="<s:property value='#application.url' />departmentEmployee.action?emp_id=<s:property value='emp_id' />" class="button">Add to Department</a>
	    </s:if>
	    <s:else>
		<a href="<s:property value='#application.url' />departmentEmployee.action?emp_id=<s:property value='emp_id' />" class="button">Add to Another Department</a>			
		<a href="<s:property value='#application.url' />deptEmpChange.action?id=<s:property value='departmentEmployee_id' />" class="button">Change Department</a>
	    </s:else>
	    <s:if test="hasNoJob()">
		<a href="<s:property value='#application.url' />jobTask.action?add_employee_id=<s:property value='emp_id' />&employee_number=<s:property value='employee_number' />&effective_date=<s:property value='effective_date' />" class="button"> Add A Job</a>
	    </s:if>
	    <s:else>
		<a href="<s:property value='#application.url' />jobTask.action?add_employee_id=<s:property value='emp_id' />&effective_date=<s:property value='effective_date' />" class="button"> Add Another Job</a>
		<a href="<s:property value='#application.url' />terminate.action?emp_id=<s:property value='emp_id' />" class="button"> Terminate </a>					    
	    </s:else>
	</div>
	<s:if test="hasDepartments()">
	    <s:set var="departmentEmployees" value="%{departmentEmployees}" />
	    <s:set var="departmentEmployeesTitle" value="'Employee Department'" />
	    <%@ include file="departmentEmployees.jsp" %>
	    <s:if test="hasAllJobs2()">
		<s:set var="jobTasks" value="%{allJobs2}" />
		<s:set var="jobTasksTitle" value="'Employee Jobs'" />
		<%@ include file="jobTasks.jsp" %>
	    </s:if>
	    <s:if test="canPayrollProcess()">
		<s:set var="groupManagers" value="%{processors}" />
		<s:set var="groupManagersTitle" value="'Payroll Approver of Groups '" />
		<%@ include file="groupManagers.jsp" %>
	    </s:if>			
	    <s:if test="canApprove()">
		<s:set var="groupManagers" value="%{approvers}" />
		<s:set var="groupManagersTitle" value="'Approver of Groups '" />
		<%@ include file="groupManagers.jsp" %>
	    </s:if>
	    <s:if test="canReview()">
		<s:set var="groupManagers" value="%{reviewers}" />
		<s:set var="groupManagersTitle" value="'Review of Groups'" />
		<%@ include file="groupManagers.jsp" %>
	    </s:if>
	    <s:if test="canMaintain()">
		<s:set var="groupManagers" value="%{enterors}" />
		<s:set var="groupManagersTitle" value="'Maintainers of Groups'" />
		<%@ include file="groupManagers.jsp" %>
	    </s:if>
	    <s:if test="canLeaveReview()">
		<s:set var="groupManagers" value="%{leaveReviewers}" />
		<s:set var="groupManagersTitle" value="'Leave Reviewer of Groups'"/>
		<%@ include file="groupManagers.jsp" %>
	    </s:if>	    
	</s:if>
	
</div>
<%@ include file="footer.jsp" %>
