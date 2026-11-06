<%@  include file="header.jsp" %>
<!--
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
	-->
<div class="internal-page">
<s:form action="groupManager" id="form_id" method="post">
    <s:hidden name="action2" id="action2" value="" />
    <s:hidden name="groupManager.group_id" value="%{group_id}" />
    <s:hidden name="groupManager.id" value="%{id}" />	
    <h1>Edit Group Manager </h1>
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
  <div class="width-one-half">
      <div class="form-group">
	  <label>ID </label>
	  <s:property value="id" />
      </div>
      <div class="form-group">
	  <label>Group </label>
	  <a href="<s:property value='#application.url' />group.action?id=<s:property value='group_id' />" /><s:property value="group_name" /></a>
      </div>
      <div class="form-group">
	  <label>Employee </label>
	  <a href="<s:property value='#application.url' />employee.action?emp_id=<s:property value='gm_emp_id' />" /><s:property value="%{gm_emp_name}" /></a>
      </div>
      <div class="form-group">
	  <label>Workflow Action </label>
	  <s:if test="hasNodes()">
	      <div class="date-range-picker">
		  <s:select name="groupManager.wf_node_id" value="%{wf_node_id}" list="nodes" listKey="id" listValue="name" headerKey="-1" headerValue="Pick Action" />
	      </div>
	  </s:if>
      </div>
      <div class="form-group">
	  <label>Start Date </label>
	  <div class="date-range-picker">
	      <s:textfield name="groupManager.start_date" value="%{start_date}" size="10" maxlength="10" required="true" cssClass="date" />*
	  </div>
      </div>
      <div class="form-group">
	  <label>Primary? </label>
	  <s:checkbox name="groupManager.primary" value="%{primary}"  /> 
      </div>
      <div class="form-group">
	  <label>Expire Date </label>
	  <s:if test="hasExpireDate()">
	      <s:textfield name="groupManager.expire_date" value="%{expire_date}" size="10" maxlength="10" cssClass="date" />
	  </s:if>
	  <s:else>
	      <div class="date-range-picker">
		  <s:select name="groupManager.expire_date" value="" list="payPeriods" listKey="endDate" listValue="endDate" headerKey="-1" headerValue="Pick Expire Date" /> (End pay period date)
	      </div>
	  </s:else>
      </div>
      <div class="form-group">
	  <label>Inactive? </label>
	  <s:checkbox name="groupManager.inactive" value="%{inactive}"  /> (check to dissable)
      </div>
      <div class="button-group">	
	  <s:submit name="action" type="button" value="Save Changes" class="button"/>
	  <button type="button" class="button" onclick="window.location.href='<s:property value='#application.url' />groupManager.action?id=<s:property value='gm_id' />&action=Delete'">Delete</button>
	  <a href="<s:property value='#application.url' />groupManagerAdd.action?group_id=<s:property value='group_id' />" class="button">Assign managers to group </a>
      </div>
  </div>
</s:form>
</div>
<s:if test="hasGroupManagers()">
    <s:set var="groupManagers" value="%{groupManagers}" />
    <s:set var="groupManagersTitle" value="'Managers of this Group'" />
    <%@  include file="groupManagers.jsp" %>
</s:if>
<%@  include file="footer.jsp" %>


