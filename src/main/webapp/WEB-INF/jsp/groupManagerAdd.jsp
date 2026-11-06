<%@  include file="header.jsp" %>
<!--
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
	-->

<s:form action="groupManager" id="form_id" method="post">
    <s:hidden name="action2" id="action2" value="" />
    <s:hidden name="groupManager.group_id" value="%{group_id}" />
    <h4>New Group Manager(s) </h4>
    Note: You can assign one or two action to one or two managers at a time <br />
  <s:if test="hasErrors()">
      <div class="errors">
	  <s:actionerror/>
      </div>
  </s:if>
  <s:elseif test="hasMessages()">
      <div class="welcome">
	  <s:actionmessage/>
      </div>
  </s:elseif>
  <dl class="fn1-output-field">
      <dt>Group </dt>
      <dd><a href="<s:property value='#application.url' />group.action?id=<s:property value='group_id' />" /><s:property value="group_name" /></a></dd>
  </dl>
  <dl class="fn1-output-field">
      <dt>1 - Employee: </dt>
      <s:if test="hasEmployees()">
	  <dd><s:select name="groupManager.employee_id" value="" list="employees" listKey="id" listValue="full_name" headerKey="-1" headerValue="Pick Employee" /> Workflow Action:<s:if test="hasNodes()"><s:select name="groupManager.wf_node_id" value="" list="nodes" listKey="id" listValue="name" headerKey="-1" headerValue="Pick Action" /> Workflow Action: <s:select name="groupManager.wf_node_id_2" value="" list="nodes" listKey="id" listValue="name" headerKey="-1" headerValue="Pick Action" /></s:if>
			</dd>
		</s:if>
		<s:else>
			<dd>No managers available to add </dd>
		</s:else>
	</dl>
	<dl class="fn1-output-field">
		<dt>2 - Employee: </dt>
		<s:if test="hasEmployees()">
			<dd><s:select name="groupManager.employee_id2" value="" list="employees" listKey="id" listValue="full_name" headerKey="-1" headerValue="Pick Employee" /> Workflow action:<s:if test="hasNodes()"><s:select name="groupManager.wf_node_id2" value="" list="nodes" listKey="id" listValue="name" headerKey="-1" headerValue="Pick Action" /> Workflow Action:<s:select name="groupManager.wf_node_id2_2" value="" list="nodes" listKey="id" listValue="name" headerKey="-1" headerValue="Pick Action" /></s:if>
			</dd>
		</s:if>
		<s:else>
			<dd>No managers available to add </dd>
		</s:else>
	</dl>	
	<dl class="fn1-output-field">
	    <dt>Start Date </dt>
	    <dd><s:select name="groupManager.start_date" value="" list="payPeriods" listKey="startDate" listValue="startDate"/>* </dd>
	</dl>
	<dl class="fn1-output-field">
	    <dt>Expire Date </dt>
	    <dd><s:select name="groupManager.expire_date" value="" list="payPeriods" listKey="endDate" listValue="endDate" headerKey="-1" headerValue="Pick Expire Date" /> </dd>
	</dl>
	<s:if test="hasEmployees()">		
	    <s:submit name="action" type="button" value="Save" class="fn1-btn"/>
	</s:if>
</s:form>
<s:if test="hasGroupManagers()">
    <s:set var="groupManagers" value="%{groupManagers}" />
    <s:set var="groupManagersTitle" value="'Managers of this Group'" />
    <%@  include file="groupManagers.jsp" %>
</s:if>
<%@  include file="footer.jsp" %>


