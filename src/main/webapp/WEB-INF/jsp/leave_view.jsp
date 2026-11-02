<%@ include file="header.jsp" %>
<div class="internal-page">
    <s:if test="hasErrors()">
	<s:set var="errors" value="errors" />
	<%@ include file="errors.jsp" %>
    </s:if>
    <s:elseif test="hasMessages()">
	<s:set var="messages" value="messages" />
	<%@ include file="messages.jsp" %>
    </s:elseif>    
    <h1>Leave Request <s:property value="leave_id" /> </h1>
    <br />
    <ul>
	<li><b>Employee:</b> <s:property value="emp_name" /></li>	
	<li><b>Leave Request for Job:</b> <s:property value="job_title" /><br /></li>
	<Li><b>Request Date:</b> <s:property value="requestDate" /></Li>
	<li><b>Work Group:</b> <s:property value="emp_group" /></li>
	<li><b>Group Manager to be notified:</b> <s:property value="managerName"/></li>
	<li><b>Date Range: </b><s:property value="date_range" /></li>
	<li><b>Proposed Hour Codes to be used: </b><s:property value="earnCodes" /></li>
	<li><b>Length of Proposed Leave (Total Hours): </b><s:property value="totalHours" /></li>
	<li><b>Proposed Leave Description: </b><s:property value="requestDetails" /></li>
	<li><b>Review Status: </b><s:property value="reviewStatus" /></li>
	<s:if test="hasReviewer()">
	    <li><b>Reviewer: </b><s:property value="reviewerName" /></li>
	    <s:if test="hasReviewNotes()">
	        <s:if test="isCancelled()">	    
 		    <li><b>Cancel Reason(s): </b><s:property value="reviewNotes" /></li>
		</s:if>
		<s:else>
		   <li><b>Review Notes: </b><s:property value="reviewNotes" /></li>
		</s:else>
	    </s:if>
	</s:if>
    </ul>
    <div class="button-group">    
	<s:if test="leave.canBeEdited()">
	    <a href="<s:property value='#application.url' />leave_request.action?id=<s:property value='leave_id' />&action=Edit" class="button">Edit</a>&nbsp;&nbsp;
	</s:if>
	<s:if test="leave.canBeCancelled()">
	  <a href="<s:property value='#application.url' />leave_request.action?id=<s:property value='leave_id' />&action=startCancel" class="button">Cancel Request</a>
	</s:if>    
    </div>
</div>
<s:if test="hasLeaveLogs()">
    <h2> Leave Request History </h2>
    <s:set var="leave_logs" value="leaveLogs" />      
    <%@ include file="leave_logs.jsp" %>
</s:if>
<s:if test="hasRequests()">
    <s:set var="leave_requests" value="requests" />
    <s:set var="leavesTitle" value="leavesTitle" />
    <%@ include file="leave_requests.jsp" %>
</s:if>
<%@ include file="footer.jsp" %>
