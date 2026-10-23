<%@  include file="header.jsp" %>
<!--
 * @copyright Copyright (C) 2014-2018 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
	-->
<script>
var myTimer = setInterval(loginTimer, 7200000); // 2 hours
function loginTimer() {
	// alert("Session Timeout you are going to be redirected to login page ");
  window.location = "<s:property value='#application.url' />Login";
} 
</script>
<div class="homepage">
    <s:if test="hasErrors()">
	<s:set var="errors" value="%{errors}" />
	<%@ include file="errors.jsp" %>
    </s:if>
    <s:if test="!hasErrors()">
	<s:if test="hasMessages()">
	    <s:set var="messages" value="%{messages}" />
	    <%@ include file="messages.jsp" %>
	</s:if>
	<s:if test="hasDailyBlocks()">
	    <s:set var="dailyBlocks" value="dailyBlocks" />
	    <s:set var="dayBlocksMap" value="dayBlocksMap" />
	</s:if>
	<s:if test="isNotEditable()">
	    <div class="alert"><p><b>Note:</b> Time Details (View Only)</p></div>
	    <%@ include file="calendarTopDetails.jsp" %>
	    <s:if test="isInAltPayPeriodSet()">		
		<%@ include file="calendarFullViewAlt.jsp" %>
	    </s:if>
	    <s:else>
		<%@ include file="calendarFullView.jsp" %>
	    </s:else>
	</s:if>
	<s:else>
	    <%@ include file="calendarTopDetails.jsp" %>
	    <s:if test="isInAltPayPeriodSet()">
		<%@ include file="calendarFullAlt.jsp" %>
	    </s:if>
	    <s:else>
		<%@ include file="calendarFullNew.jsp" %>
	    </s:else>
	</s:else>
	
	<div class="time-details">
	    <div class="calendar-summary-controls m-b-40">
		<a class="button pay-notes" data-doc-id="<s:property value='%{document.id}' />">Add Pay Period Note</a>
		
		<s:if test="hasLastWorkflow()" >
		    <s:if test="canSubmit()">
			<s:if test="isUserCurrentEmployee() || canApprove() || canProcess()">
			    <form action="<s:property value='#application.url' />timeAction" id="form_id" class="timesheet-submit" method="post" >
				<input type="hidden" name="source" value="timeDetails" />
				<s:hidden name="document_id" value="%{document_id}" />
				<s:hidden name="workflow_id" value="%{next_workflow_id}" />
				<s:submit
				    name="action"
				    type="submit"
				    class="button"
				    value="Submit for Approval"
				    data-week-one-total="%{week1Total}"
				    data-week-two-total="%{week2Total}" />
			    </form>
			</s:if>
			<s:else>
			    <div>
				<strong>&nbsp; Not submitted yet&nbsp;</strong>
			    </div>
			</s:else>
		    </s:if>
		    <s:else>
			<div class="submitted-on">
			    <strong>Submitted on:&nbsp;<s:property value="document.submitTimeAction.action_time" />&nbsp;&nbsp;</strong>
			</div>
			</s:else>
		</s:if>
	    </div>
	    <s:set var="week1DateRange" value="week1DateRange" />
	    <s:set var="week2DateRange" value="week2DateRange" />
	    
	    <hr class="m-b-40" />
	    <s:if test="hasMultipleJobs()">
		<s:set var="payPeriodTotal" value="mjdoc.payPeriodTotal" />
		<s:set var="payPeriodAmount" value="mjdoc.payPeriodAmount" />
		<s:set var="daily" value="mjdoc.daily" />
		<s:set var="week1Total" value="mjdoc.week1Total" />
		<s:set var="week2Total" value="mjdoc.week2Total" />
		
		<s:if test="document.isUnionned()">
		    <s:set var="unionned" value="'true'" />
		    <s:set var="week1Flsa" value="mjdoc.week1_flsa" />
		    <s:set var="week2Flsa" value="mjdoc.week2_flsa" />
		</s:if>
		<s:else>
		    <s:set var="unionned" value="'false'" />
		</s:else>
	    </s:if>
	    <s:else>
		<s:set var="payPeriodTotal" value="payPeriodTotal" />
		<s:set var="payPeriodAmount" value="payPeriodAmount" />
		<s:set var="daily" value="document.daily" />
		<s:set var="week1Total" value="week1Total" />
		<s:set var="week2Total" value="week2Total" />
		<s:if test="document.isUnionned()">
		    <s:set var="unionned" value="'true'" />
		    <s:set var="week1Flsa" value="week1_flsa" />
		    <s:set var="week2Flsa" value="week2_flsa" />
		</s:if>
		<s:else>
		    <s:set var="unionned" value="'false'" />
		</s:else>
	    </s:else>
	    <s:if test="hasLeaveRequests()">
		<s:set var="leave_requests" value="leaveRequests" />
		<s:set var="leaves_title" value="'Approved Leave in this Pay Period'" />
		<%@ include file="leaves_approved.jsp" %>
	    </s:if>
	    <s:if test="hasMultipleJobs()">
		<s:if test="mjdoc.hasWarnings()">
		    <s:set var="warnings" value="mjdoc.warnings" />
		    <%@ include file="warnings.jsp" %>
		</s:if>
	    </s:if>
	    <s:else>
		<s:if test="hasWarnings()">
		    <s:set var="warnings" value="warnings" />
		    <%@ include file="warnings.jsp" %>
		</s:if>
	    </s:else>
	    <s:if test="isInAltPayPeriodSet()">		
		<%@ include file="dailySummaryAlt.jsp" %>
	    </s:if>
	    <s:else>
		<%@ include file="dailySummary.jsp" %>
	    </s:else>
	    
	    <div class="monetary-hours-tables">											
		<s:if test="hasMultipleJobs()">
		    <s:if test="showAllJobs()">
			<s:if test="mjdoc.hasDocuments()">
			    <s:iterator var="doc" value="mjdoc.documents">
				<s:if test="#doc.hasTmwrpRun()">
				    <s:if test="#doc.tmwrpRun.hasWeek1Rows()">
					<s:set var="rows" value="#doc.tmwrpRun.week1Rows" />
					<s:set var="weeklyTitle" value="'Week 1 (Earn Codes)'" />
					<s:set var="whichWeek" value="'week-one'" />
					<%@ include file="weeklyTmwrp.jsp" %>
				    </s:if>
				    <s:if test="#doc.tmwrpRun.hasWeek2Rows()">
					<s:set var="rows" value="#doc.tmwrpRun.week2Rows" />
					<s:set var="weeklyTitle" value="'Week 2 (Earn Codes)'" />
					<s:set var="whichWeek" value="'week-one'" />
					<%@ include file="weeklyTmwrp.jsp" %>
				    </s:if>
				</s:if>
			    </s:iterator>
			</s:if>
		    </s:if>
		    <s:else> <!-- multiple jobs but show one only -->
			<s:if test="hasTmwrpRun()">
			    <s:if test="hasWeek1Rows()">
				<s:set var="rows" value="week1Rows" />
				<s:set var="weeklyTitle" value="'Week 1 (Earn Codes)'" />
				<s:set var="whichWeek" value="'week-one'" />
				<%@ include file="weeklyTmwrp.jsp" %>
			    </s:if>
			    <s:if test="hasWeek2Rows()">
				<s:set var="rows" value="week2Rows" />
				<s:set var="weeklyTitle" value="'Week 2 (Earn Codes)'" />
				<s:set var="whichWeek" value="'week-one'" />
				<%@ include file="weeklyTmwrp.jsp" %>
			    </s:if>
			    <s:if test="hasCycleRows()">
				<s:set var="rows" value="cycleRows" />
				<%@ include file="cycleRowsTmwrp.jsp" %>
			    </s:if>			    
			</s:if>
			<!-- 
			<s:if test="document.hasReasonTotals()">
			    <s:if test="document.hasReasonWeek1()">
				<s:set var="rows" value="document.reasonWeek1" />
				<s:set var="weeklyTitle" value="'Week 1 (Earn Codes/Reasons)'" />
				<s:set var="whichWeek" value="'week-one'" />
				<%@ include file="weeklyReasons.jsp" %>
			    </s:if>
			    <s:if test="document.hasReasonWeek2()">
				<s:set var="rows" value="document.reasonWeek2" />
				<s:set var="weeklyTitle" value="'Week 2 (Earn Codes/Reasons)'" />
				<s:set var="whichWeek" value="'week-two'" />
				<%@ include file="weeklyReasons.jsp" %>
			    </s:if>
			</s:if>
			-->
		    </s:else>
		</s:if>
		<s:else> <!-- one job only -->
		    <s:if test="hasTmwrpRun()">
			<s:if test="hasWeek1Rows()"> </s:if>
			<s:if test="hasWeek1Rows()">
			    <s:set var="rows" value="week1Rows" />
			    <s:set var="weeklyTitle" value="'Week 1 (Earn Codes)'" />
			    <s:set var="whichWeek" value="'week-one'" />
			    <%@ include file="weeklyTmwrp.jsp" %>
			</s:if>
			<s:if test="hasWeek2Rows()">
			    <s:set var="rows" value="week2Rows" />
			    <s:set var="weeklyTitle" value="'Week 2 (Earn Codes)'" />
			    <s:set var="whichWeek" value="'week-two'" />
			    <%@ include file="weeklyTmwrp.jsp" %>
			</s:if>
			<s:if test="hasCycleRows()">
			    <s:set var="rows" value="cycleRows" />
			    <%@ include file="cycleRowsTmwrp.jsp" %>
			</s:if>
		    </s:if>
		    <!-- 
		    <s:if test="document.hasReasonTotals()">
			<div class="monetary-hours-tables">
			    <s:if test="document.hasReasonWeek1()">
				<s:set var="rows" value="document.reasonWeek1" />
				<s:set var="weeklyTitle" value="'Week 1 (Earn Codes/Reasons)'" />
				<s:set var="whichWeek" value="'week-one'" />
				<%@ include file="weeklyReasons.jsp" %>
			    </s:if>
			    <s:if test="document.hasReasonWeek2()">
				<s:set var="rows" value="document.reasonWeek2" />
				<s:set var="weeklyTitle" value="'Week 2 (Earn Codes/Reasons)'" />
				<s:set var="whichWeek" value="'week-two'" />
				<%@ include file="weeklyReasons.jsp" %>
			    </s:if>
			</div>
		    </s:if>
		    -->
		</s:else>
	    </div>
	    <s:if test="isProcessed()">
		<ul>
		    <s:if test="hasAccruals()">
			<li>
			    <strong>Available Accruals</strong> <s:property value="employeeAccrualsShort" />
			</li>
		    </s:if>
		    <s:if test="hasJob()">
			<li>
			    <strong>Weekly Standard Work Hrs: </strong> <s:property value="weekly_regular_hours" />
			</li>
			<li>
			    <strong>Weekly Compt Time Earned After Hrs: </strong> <s:property value="comp_time_weekly_hours" />
			</li>
		    </s:if>
		</ul>
	    </s:if>
	    <s:else>
		<%@ include file="accrualSummary.jsp" %>
	    </s:else>
	    <%@ include file="timeIssues.jsp" %>
	    <s:if test="hasUnscheduleds()">
		<s:set var="unscheduledTitle" value="'Unscheduled Times'" />				
		<s:set var="unscheduleds" value="unscheduleds" />
		<%@ include file="unscheduledTimes.jsp" %>				
	    </s:if>			
	    <s:if test="canCancelAction()">
		<s:set var="canCancelAction" value="'true'" />
	    </s:if>
	    <s:if test="hasTimeActions">
		<s:set var="timeActions" value="timeActions" />
		<%@ include file="timeActions.jsp" %>		
	    </s:if>
	    <s:if test="hasTimeNotes()">
		<%@ include file="timeNotes.jsp" %>
	    </s:if>
	    <br />
		<h1>Time Log History</h1>
		<a href="<s:property value='#application.url' />timeBlockLog.action?document_id=<s:property value='document_id' />">(view)</a>
	</div>
    </s:if>
</div>

<!-- jQuery Dialog Modal: Add Pay Period Note -->
<div class="modal pay-notes" class="timetrack" style="display: none;"></div>

<%@ include file="footer.jsp" %>
