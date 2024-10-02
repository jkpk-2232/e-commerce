<%@page import="com.webapp.viewutils.*,com.jeeutils.*,com.webapp.models.*"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.utils.view.ButtonUtils"%>
<%@page import="com.utils.view.ActionButton"%>
<%@page import="java.util.List,java.util.ArrayList"%>

<%
		HashMap it1 = (HashMap) request.getAttribute("it");
%>
									
<ul class="nav nav-tabs" id="customUlClass">
    
    <li class="customLiClass active">
    	<a data-toggle="tab" href="#menu1">
    		<i class="icon-user"></i>
    		Personal Details
    		<span style='color:red;'>*</span>
    	</a>
    </li>
    
    <li class="customLiClass">
    	<a data-toggle="tab" href="#menu2">
    		<i class="icon-envelope"></i>
    		Mailing Address
    	</a>
    </li>
    
</ul>

<div class="tab-content">

	<div id="menu1" class="tab-pane fade in active">
	
		<div class="dashboardSeperator"></div>
     	<div class="dashboardSeperator"></div>
	
      	<%=NewUiViewUtils.outputGlobalInputField("firstName", "First name", true, 3, 30, it1, "text") %>
								
		<%=NewUiViewUtils.outputGlobalInputField("lastName", "Last name", true, 4, 30, it1, "text") %>
										
		<%=NewUiViewUtils.outputGlobalInputField("emailAddress", "Email address", true, 5, 30, it1, "text") %>
										
		<%=NewUiViewUtils.outputGlobalInputFieldWithSelectAndMultiSelect("phone","countryCode", "Phone number", true, 6, 30, it1, "number") %>
		
    </div>
   
    <div id="menu2" class="tab-pane fade">
    
    	<div class="dashboardSeperator"></div>
     	<div class="dashboardSeperator"></div>
	
      	<%=NewUiViewUtils.outputGlobalInputField("street1", "Street 1", false, 1, 30, it1, "text") %>
      	
      	<%=NewUiViewUtils.outputGlobalInputField("street2", "Street 2", false, 1, 30, it1, "text") %>
      	
      	<%=NewUiViewUtils.outputGlobalInputField("country", "Country", false, 1, 30, it1, "text") %>
      	
      	<%=NewUiViewUtils.outputGlobalInputField("city", "City", false, 1, 30, it1, "text") %>
      	
      	<div class="buttonDiv">
		
			<div class="control-group">
               	<div class="controls">
        			<input id="btnCancel" type="button" value="Cancel" class="btn btn-primary" />
				</div>
			</div>
			
			<div class="control-group">
               	<div class="controls">
                	<input id="btnSave" type="button" value="Save" class="btn btn-danger" />
				</div>
			</div>
			
		</div>
      
    </div>

</div>