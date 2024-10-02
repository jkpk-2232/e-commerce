package com.webapp.actions;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.jeeutils.DateUtils;
import com.jeeutils.FrameworkConstants;
import com.utils.myhub.MyHubUtils;
import com.utils.myhub.WebappPropertyUtils;
import com.webapp.ProjectConstants;
import com.webapp.UrlConstants;
import com.webapp.models.AdminSettingsModel;
import com.webapp.models.InvoiceModel;
import com.webapp.models.TourModel;
import com.webapp.models.TourTaxModel;
import com.webapp.models.UserProfileModel;

public abstract class Action {

	public static Logger LOGGER = Logger.getLogger(Action.class);

	protected Map<String, String> data = new HashMap<String, String>();

	protected HttpServletRequest request;

	protected HttpServletResponse response;

	protected ServletContext applicationContext;

	public static DecimalFormat df = new DecimalFormat("##.##");

	public static DecimalFormat df_new = new DecimalFormat("#######0.00");

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}

	public ServletContext getApplicationContext() {
		return applicationContext;
	}

	public void setApplicationContext(ServletContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	public boolean isProductionEnvironment() {

		String productionEnvironmentEnabled = WebappPropertyUtils.getWebAppProperty(FrameworkConstants.ENVIRONMENT);

		if (productionEnvironmentEnabled == null) {
			productionEnvironmentEnabled = FrameworkConstants.ENVIRONMENT_DEVELOPMENT;
		}

		return productionEnvironmentEnabled.equalsIgnoreCase(FrameworkConstants.ENVIRONMENT_PRODUCTION);
	}

	//@formatter:off
	public static String startDefualtMessage = "<html> " + "<head>" 
			+ "<meta content='text/html; charset=ISO-8859-1' http-equiv='content-type'>" 
			+ "</head>" + "<body>" + "<div>" + "<div style='width:639px;margin:0 auto;padding:0px'>"
			+ "<div style='margin:0px;padding:0px;float:left;width:639px;background-color:"+ProjectConstants.TAXI_SOL_THEME_COLOR+"'>" 
			+ "<div style='padding:0px;float:left;width:15px;min-height:63px'> </div>"
			+ "<div style='margin: 0px; padding: 0 0 0 5px; float: left; width: 609px; min-height: 63px; font-family: arial;color:white;'>" 
			+ "<center><h1>"+WebappPropertyUtils.PROJECT_NAME+"</h1></center></div></div>"
			+ "<div style='padding:20px 19px 20px;border-left:1px solid #ccc;border-right:1px solid #ccc;clear:both;'>" 
			+ "<div style='border-bottom:2px dotted #c8c8c8;padding:0px 19px 20px;;font-family:arial;font-size:14px;'>";
	
	public static String endDefualtMessageTemplete = "</div><div style='border-bottom:2px dotted #c8c8c8;padding:20px 19px 20px;font-family:arial;font-size:14px;'>" 
			+ "<p style='color:#141414;font-family:arial;font-size:14px;padding-top:0px'> "
			+ "If you need any help or have feedback about "+WebappPropertyUtils.PROJECT_NAME+", please email us at</p>" 
			+ "<a href='mailto:"+WebappPropertyUtils.PROJECT_EMAIL+"' target='_blank' style='color: #333;font-size: 12px;color: #15c;'>"+WebappPropertyUtils.PROJECT_EMAIL+"</a>"
			+ "<p>Warm wishes, <br>" 
			+ "The "+WebappPropertyUtils.PROJECT_NAME+" Team</p>" + "</div>" + "</div>" 
			+ "<div style='margin:0px;padding:0px;float:left;width:639px;background-color:"+ProjectConstants.TAXI_SOL_THEME_COLOR+"'>"
			+ "<div style='padding:0px;float:left;width:15px;min-height:63px'> </div>" 
			+ "<div style='margin:0px;padding:0px;float:left;width:609px;min-height:63px;'>" 
			+ "<p style='color:#ffffff;text-decoration:none;font-size:14px;font-family:arial;text-align:center'>"
			+ "You are receiving this email because you are registered for a "+WebappPropertyUtils.PROJECT_NAME+" account.<br>" 
			+ "&copy; 2017 Copyright "+WebappPropertyUtils.PROJECT_NAME+". All rights reserved.</p>" + "</div>" 
			+ "<div style='padding:0px;margin:0px;float:left;width:15px;min-height:63px'>" 
			+ "</div>" + "</div>" + "</div>" + "</div>"
			+ "</body>" + "</html>";
	
	public static String startInvoiceDefaultMessage="<html><head>" +
			"<meta content='text/html; charset=ISO-8859-1' http-equiv='content-type'>" +
			"</head><body><div><div style='width:639px;margin:0 auto;padding:0px'>" +
			"<div style='margin:0px;padding:0px;float:left;width:639px;background-color:"+ProjectConstants.TAXI_SOL_THEME_COLOR+"'>" +
			"<div style='padding:0px;float:left;width:15px;min-height:63px'> </div>" +
			"<div style='margin: 0px; padding: 0 0 0 5px; float: left; width: 609px; min-height: 63px; font-family: arial;color:white;'>" +
			"<center><h1>"+WebappPropertyUtils.PROJECT_NAME+"</h1></center></div></div>" +
			"<div style='padding:20px 19px 20px;border-left:1px solid #ccc;border-right:1px solid #ccc;clear:both;'> " +
			"<div style='padding:0px 19px 20px;;font-family:arial;font-size:14px;'>";
	
	public static String endInvoiceDefaultMessage="</div><div style='clear:both; height:0; overflow:hidden'></div><div style='border-bottom:2px dotted #c8c8c8;'></div>" +
			"<div style='border-bottom:2px dotted #c8c8c8;padding:20px 19px 20px;font-family:arial;font-size:14px;'>" +
			"<p style='color:#141414;font-family:arial;font-size:14px;padding-top:0px'>" +
			"If you need any help or have feedback about "+WebappPropertyUtils.PROJECT_NAME+", please email us at</p>" +
			"<a href='mailto:"+WebappPropertyUtils.PROJECT_EMAIL+"' target='_blank' style='color: #333;font-size: 12px;color: #15c;'>"+WebappPropertyUtils.PROJECT_EMAIL+"</a><p>Warm wishes, <br>" +
			"The "+WebappPropertyUtils.PROJECT_NAME+" Team</p></div></div>" +
			"<div style='margin:0px;padding:0px;float:left;width:639px;background-color:"+ProjectConstants.TAXI_SOL_THEME_COLOR+"'>" +
			"<div style='padding:0px;float:left;width:15px;min-height:63px'> </div>" +
			"<div style='margin:0px;padding:0px;float:left;width:609px;min-height:63px;'>" +
			"<p style='color:#ffffff;text-decoration:none;font-size:14px;font-family:arial;text-align:center'>" +
			"You are receiving this email because you are registered for a "+WebappPropertyUtils.PROJECT_NAME+" account.<br>" +
			"&copy; 2017 Copyright "+WebappPropertyUtils.PROJECT_NAME+". All rights reserved.</p></div> " +
			"<div style='padding:0px;margin:0px;float:left;width:15px;min-height:63px'></div></div></div></div>" +
			"</body></html>";
	
	public static String endInvoiceDefaultMessageSpanish="</div><div style='clear:both; height:0; overflow:hidden'></div><div style='border-bottom:2px dotted #c8c8c8;'></div>" +
			"<div style='border-bottom:2px dotted #c8c8c8;padding:20px 19px 20px;font-family:arial;font-size:14px;'>" +
			"<p style='color:#141414;font-family:arial;font-size:14px;padding-top:0px'>" +
			"Si necesita ayuda o tiene comentarios acerca "+WebappPropertyUtils.PROJECT_NAME+", por favor envíenos un email a</p>" +
			"<a href='mailto:"+WebappPropertyUtils.PROJECT_EMAIL+"' target='_blank' style='color: #333;font-size: 12px;color: #15c;'>"+WebappPropertyUtils.PROJECT_EMAIL+"</a><p>Los mejores deseos, <br>" +
			"The "+WebappPropertyUtils.PROJECT_NAME+" Team</p></div></div>" +
			"<div style='margin:0px;padding:0px;float:left;width:639px;background-color:"+ProjectConstants.TAXI_SOL_THEME_COLOR+"'>" +
			"<div style='padding:0px;float:left;width:15px;min-height:63px'> </div>" +
			"<div style='margin:0px;padding:0px;float:left;width:609px;min-height:63px;'>" +
			"<p style='color:white;text-decoration:none;font-size:14px;font-family:arial;text-align:center'>" +
			"Usted está recibiendo este correo electrónico porque se ha abonado a una "+WebappPropertyUtils.PROJECT_NAME+" cuenta.<br>" +
			"&copy; 2017 Copyright "+WebappPropertyUtils.PROJECT_NAME+". Todos los derechos reservados.</p></div> " +
			"<div style='padding:0px;margin:0px;float:left;width:15px;min-height:63px'></div></div></div></div>" +
			"</body></html>";
	
	public static String startInvoiceDefaultMessageForPrint="<div><div style='width:639px;margin:0 auto;padding:0px'>" +
			"<div style='margin:0px;padding:0px;float:left;width:639px;background-color:"+ProjectConstants.TAXI_SOL_THEME_COLOR+"height:70px;'>" +
			"<div style='padding:0px;float:left;width:15px;min-height:63px'> </div>" +
			"<div style='margin: 0px; padding: 0 0 0 5px; float: left; width: 609px; min-height: 63px; font-family: arial;color:white;'>" +
			"<center><h1>"+WebappPropertyUtils.PROJECT_NAME+"</h1></center></div></div>" +
			"<div style='padding:20px 19px 20px;border-left:1px solid #ccc;border-right:1px solid #ccc;clear:both;'> " +
			"<div style='padding:0px 19px 20px;font-family:arial;font-size:14px;'>";
	
	public static String endInvoiceDefaultMessageForPrint="</div><div style='clear:both; height:0; overflow:hidden'></div>" +
			"</div>" +
			"<div style='margin:0px;padding:0px;float:left;width:639px;background-color:"+ProjectConstants.TAXI_SOL_THEME_COLOR+"'>" +
			"<div style='padding:0px;float:left;width:15px;min-height:63px'> </div>" +
			"<div style='margin:0px;padding:0px;float:left;width:609px;min-height:63px;'>" +
			"<center><p style='color:white;text-decoration:none;font-size:14px;font-family:arial;text-align:center;line-height:30px;'>" +
			"&copy; 2017 Copyright "+WebappPropertyUtils.PROJECT_NAME+". All rights reserved.</p></center></div> " +
			"<div style='padding:0px;margin:0px;float:left;width:15px;min-height:63px'>" +
			"</div></div></div></div>";
	
	public static String startInvoiceDefaultMessageForNewTemplate="<html> <head>"+
			"<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"> <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">"+
			"<style type=\"text/css\"> div, p, a, li, td { -webkit-text-size-adjust:none; } ul {padding-left: 5px !important;} ul > li {margin-left: 0 !important;}"+
			"#outlook a {padding:0;}  html{width: 100%; } body{width:100% !important; -webkit-text-size-adjust:100%; -ms-text-size-adjust:100%; margin:0; padding:0;}"+
			".ExternalClass {width:100%;} /* Force Hotmail to display emails at full width */ .ExternalClass, .ExternalClass p, .ExternalClass span, .ExternalClass font, .ExternalClass td, .ExternalClass div {line-height: 100%;}"+
			"#backgroundTable {margin:0; padding:0; width:100% !important; line-height: 100% !important;} img {outline:none; text-decoration:none;border:none; -ms-interpolation-mode: bicubic;}"+
			"a img {border:none;} .image_fix {display:block;} p {margin: 0px 0px !important;} table td {border-collapse: collapse;}"+
			"table { border-collapse:collapse; mso-table-lspace:0pt; mso-table-rspace:0pt; } a {color: #33b9ff;text-decoration: none;text-decoration:none!important;}"+
			"table[class=full] { width: 100%; clear: both; } @media only screen and (max-width: 640px) { a[href^=\"tel\"], a[href^=\"sms\"] { text-decoration: none;"+
			"color: #33b9ff; /* or whatever your want */ pointer-events: none; cursor: default; } .mobile_link a[href^=\"tel\"], .mobile_link a[href^=\"sms\"] {"+
			"text-decoration: default; color: #33b9ff !important; pointer-events: auto; cursor: default; } table[class=devicewidth] {width: 440px!important;text-align:center!important;}"+
			"table[class=devicewidthinner] {width: 420px!important;text-align:center!important;} img[class=banner] {width: 440px!important;height:220px!important;}"+
			"img[class=col2img] {width: 440px!important;height:220px!important;} } @media only screen and (max-width: 480px) { a[href^=\"tel\"], a[href^=\"sms\"] {"+
			"text-decoration: none; color: #33b9ff; pointer-events: none; cursor: default; } .mobile_link a[href^=\"tel\"], .mobile_link a[href^=\"sms\"] {"+
			"text-decoration: default; color: #33b9ff !important;  pointer-events: auto; cursor: default; } table[class=devicewidth] {width: 280px!important;text-align:center!important;}"+
			"table[class=devicewidthinner] {width: 260px!important;text-align:center!important;} img[class=banner] {width: 280px!important;height:140px!important;}"+
			"img[class=col2img] {width: 280px!important;height:140px!important;} } </style> </head> <body> "+
			"<table width=\"100%\" bgcolor=\"#fcfcfc\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" id=\"backgroundTable\" st-sortable=\"preheader\">"+
			"<tbody>"+
			"   <tr>"+
			"      <td>"+
			"<table width=\"600\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\" class=\"devicewidth\">"+
			"   <tbody>"+
			"      <tr>"+
			"<td width=\"100%\">"+
			"   <table width=\"600\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\" class=\"devicewidth\">"+
			"      <tbody>"+
			"<!-- Spacing -->"+
			"<tr>"+
			"   <td width=\"100%\" height=\"20\"></td>"+
			"</tr>"+
			"<!-- Spacing -->"+
			"<tr>"+
			"   <td width=\"100%\" align=\"left\" valign=\"middle\" style=\"font-family: Helvetica, arial, sans-serif; font-size: 13px;color: #282828\" st-content=\"preheader\">"+
			"      Can't see this Email? View it in your <a href=\"#\" style=\"text-decoration: none; color: #eacb3c\">Browser </a> "+
			"   </td>"+
			"</tr>"+
			"<!-- Spacing -->"+
			"<tr>"+
			"   <td width=\"100%\" height=\"20\"></td>"+
			"</tr>"+
			"<!-- Spacing -->"+
			"      </tbody>"+
			"   </table>"+
			"</td>"+
			"      </tr>"+
			"   </tbody>"+
			"</table>"+
			"      </td>"+
			"   </tr>"+
			"</tbody>"+
			"      </table>"+
			"      <!-- End of preheader -->       "+
			"      <!-- Start of header -->"+
			"      <table width=\"100%\" bgcolor=\"#fcfcfc\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" id=\"backgroundTable\" st-sortable=\"header\">"+
			"<tbody>"+
			"   <tr>"+
			"      <td>"+
			"<table width=\"600\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\" class=\"devicewidth\">"+
			"   <tbody style=\"border-left: 10px solid #dddddd;border-right: 10px solid #dddddd;border-top: 10px solid #dddddd;\">"+
			"      <tr>"+
			"<td width=\"100%\">"+
			"   <table width=\"600\" bgcolor=\"#f9f9f9\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\" class=\"devicewidth\">"+
			"      <tbody>"+
			"<tr>"+
			"   <td>"+
			"      <!-- logo -->"+
			"      <table bgcolor=\"#f9f9f9\" width=\"140\" align=\"left\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"devicewidth\">"+
			"<tbody>"+
			"   <tr>"+
			"      <td width=\"140\" height=\"50\" align=\"center\">"+
			"<div class=\"imgpop\">"+
			"   <a target=\"_blank\" href=\"#\">"+
			"   <img src=\""+ WebappPropertyUtils.BASE_PATH +"/assets/image/header_login_logo.png\" alt=\"\" border=\"0\" width=\"175\" height=\"60\" style=\"/*display:block;*/ border:none; outline:none; text-decoration:none;\">"+
			"   </a>"+
			"</div>"+
			"      </td>"+
			"   </tr>"+
			"</tbody>"+
			"      </table>"+
			"      <!-- end of logo -->";
				
	
	public static String endInvoiceDefaultMessageForNewTemplate="<!-- content -->"+
					"            <tr>"+
					"               <td style=\"font-family: Helvetica, arial, sans-serif; font-size: 14px; color: #889098; text-align:left; line-height: 24px;\">"+
					"                  If you need any help or have feedback about "+ WebappPropertyUtils.PROJECT_NAME +", please email us at<br />"+
					""+
					"" + WebappPropertyUtils.PROJECT_EMAIL + "<br />"+
					"                  Warm wishes, <br />"+
					"                  The "+ WebappPropertyUtils.PROJECT_NAME +" Team"+
					"               </td>"+
					"            </tr>"+
					"            <!-- End of content -->"+
					"            <!-- Spacing -->"+
					"            <tr>"+
					"            </tr>"+
					"            <!-- Spacing -->"+
					"         </tbody>"+
					"      </table>"+
					"   </td>"+
					"</tr>"+
					"                     </tbody>"+
					"                  </table>"+
					"               </td>"+
					"            </tr>"+
					"         </tbody>"+
					"      </table>"+
					"      <!-- End of Header -->"+
					"      <table width=\"100%\" bgcolor=\"#fcfcfc\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" id=\"backgroundTable\" st-sortable=\"header\">"+
					"         <tbody>"+
					"            <tr>"+
					"               <td>"+
					"                  <table width=\"600\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\" class=\"devicewidth\">"+
					"                     <tbody style=\"border-left: 10px solid "+ ProjectConstants.TAXI_SOL_THEME_COLOR +";border-right: 10px solid "+ ProjectConstants.TAXI_SOL_THEME_COLOR +";border-bottom: 10px solid "+ ProjectConstants.TAXI_SOL_THEME_COLOR +";\">"+
					"<tr>"+
					"   <td width=\"100%\">"+
					"      <table bgcolor=\""+ ProjectConstants.TAXI_SOL_THEME_COLOR +"\" width=\"600\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\" class=\"devicewidth\">"+
					"         <tbody>"+
					"            <!-- Spacing -->"+
					"            <tr>"+
					"            </tr>"+
					"            <!-- Spacing -->"+
					"            <tr>"+
					"               <td>"+
					"                  <table width=\"560\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" class=\"devicewidthinner\">"+
					"                     <tbody>"+
					"<!-- content -->"+
					"<tr>"+
					"   <td style=\"font-family: Helvetica, arial, sans-serif; font-size: 14px; color: #ffffff; text-align:center; line-height: 24px;\">"+
					"      You are receiving this email because you are registered for a "+WebappPropertyUtils.PROJECT_NAME+" account.<br/>"+
					"      © 2017 Copyright "+ WebappPropertyUtils.PROJECT_NAME +". All rights reserved."+
					"   </td>"+
					"</tr>"+
					"<!-- End of content -->"+
					"<!-- Spacing -->"+
					"<tr>"+
					"</tr>"+
					"<!-- Spacing -->"+
					"                     </tbody>"+
					"                  </table>"+
					"               </td>"+
					"            </tr>"+
					"            <!-- Spacing -->"+
					"            <tr>"+
					"            </tr>"+
					"            <!-- Spacing -->"+
					"         </tbody>"+
					"      </table>"+
					"   </td>"+
					"</tr>"+
					"                     </tbody>"+
					"                  </table>"+
					"               </td>"+
					"            </tr>"+
					"         </tbody>"+
					"      </table>"+
					"      "+
					"      <!-- Start of Postfooter -->"+
					"      "+
					"      <!-- End of postfooter -->      "+
					"   </body>"+
					"</html>";
	
	//@formatter:on

	public static String getCrateAccountMessage(String fullUserName, String email, String password) {
		//@formatter:off
		String message = startDefualtMessage 
				+ " <h1>Dear " + fullUserName 
				+ ",</h1>" 
				+ " <p>Thank you for using <strong>"+WebappPropertyUtils.PROJECT_NAME+"</strong>.<br/><br/> " 
				+ " Your <strong>"+WebappPropertyUtils.PROJECT_NAME+"</strong> account has been created.<br>"
				+ "Your login credentials are:<br><br>"
				+ "Email : "
				+ email
				+"<br>Password : "
				+ password 
				+ " </p>" 
				+ endDefualtMessageTemplete;
		//@formatter:on
		return message;
	}

	public static String getCreateVendorAccountMessage(String fullUserName, String email, String password) {
		//@formatter:off
		String message = startDefualtMessage 
				+ " <h1>Dear " + fullUserName 
				+ ",</h1>" 
				+ " <p>Thank you for using <strong>"+WebappPropertyUtils.PROJECT_NAME+"</strong>.<br/><br/> " 
				+ " Your <strong>"+WebappPropertyUtils.PROJECT_NAME+"</strong> account has been created.<br>"
				+ "Your login credentials are:<br><br>"
				+ "Login URL : "
				+ "<a href='"+UrlConstants.VENDOR_LOGIN_URL+"' target='_blank' style='color: #333;color: #15c;'>"+UrlConstants.VENDOR_LOGIN_URL+"</a>"
				+ "<br>Email : "
				+ email
				+"<br>Password : "
				+ password 
				+ " </p>" 
				+ endDefualtMessageTemplete;
		//@formatter:on
		return message;
	}

	public static String driverApplicationAcceptMessage(String fullUserName, String verificationCode, String email, String password) {
		//@formatter:off
		String message = startDefualtMessage 
				+ " <h1>Dear " + fullUserName 
				+ ",</h1>" 
				+ " <p>Thank you for using <strong>"+WebappPropertyUtils.PROJECT_NAME+"</strong>.<br/><br/> " 
				+ " Your application has been accepted. <br><br>"
				+ "Your login credentials are:<br><br>"
				+ "Email : "
				+ email
				+"<br>Password : "
				+ password 
				+ " </p>" 
				+ "Use below code to verify your phone number.<br><br>"
				+ "Verification code : "
				+ verificationCode
				
				+ " </p>" 
				+ endDefualtMessageTemplete;
		//@formatter:on
		return message;
	}

	public static String addDriver(String fullUserName, String verificationCode, String email, String password) {
		//@formatter:off
		String message = startDefualtMessage 
				+ " <h1>Dear " + fullUserName 
				+ ",</h1>" 
				+ " <p>Thank you for using <strong>"+WebappPropertyUtils.PROJECT_NAME+"</strong>.<br/><br/> " 
				+ " You have been added to "+WebappPropertyUtils.PROJECT_NAME+" <br><br>"
				+ "Your login credentials are:<br><br>"
				+ "Email : "
				+ email
				+"<br>Password : "
				+ password 
				+ " </p>" 
				+ "Use below code to verify your phone number.<br><br>"
				+ "Verification code : "
				+ verificationCode
				+ " </p>" 
				+ endDefualtMessageTemplete;
		//@formatter:on
		return message;
	}

	public static String getForgotPasswordMessage(String email, String password) {
		//@formatter:off		
		String message = startDefualtMessage 
				+ "Hello,<br/><br/>" 
				+ "This is system generated email. Please do not reply to this mail.<br/>" 
				+ "<br/>" 
				+ "Login Details are :<br/><br/>"
				+ "&nbsp;&nbsp;&nbsp;&nbsp;" 
				+ "User Name" + " : " 
				+ email 
				+ "<br/>&nbsp;&nbsp;&nbsp;&nbsp;" 
				+ "Password" + " : " 
				+ password 
				+ "<br/><br/>" 
				+ "Thank You"
				+ endDefualtMessageTemplete;
		//@formatter:on
		return message;
	}

	public static String getForgotPasswordMessageForVendor(String email, String password) {
		//@formatter:off		
		String message = startDefualtMessage 
				+ "Hello,<br/><br/>" 
				+ "This is system generated email. Please do not reply to this mail.<br/>" 
				+ "<br/>" 
				+ "Login Details are :<br/><br/>"
				+ "&nbsp;&nbsp;&nbsp;&nbsp;" 
				+ "Login URL : "
				+ "<a href='"+UrlConstants.VENDOR_LOGIN_URL+"' target='_blank' style='color: #333;color: #15c;'>"+UrlConstants.VENDOR_LOGIN_URL+"</a>"
				+ "User Name" + " : " 
				+ email 
				+ "<br/>&nbsp;&nbsp;&nbsp;&nbsp;" 
				+ "Password" + " : " 
				+ password 
				+ "<br/><br/>" 
				+ "Thank You"
				+ endDefualtMessageTemplete;
		//@formatter:on
		return message;
	}

	public static String getInvoiceMessage(TourModel tourModel, InvoiceModel invoiceModel, String language) {

		df.setRoundingMode(RoundingMode.DOWN);

		AdminSettingsModel adminSettings = AdminSettingsModel.getAdminSettingsDetails();

		//@formatter:off
		
		if ((tourModel.getBookingType().equals(ProjectConstants.BUSINESS_OWNER_BOOKING) 
				&& tourModel.getCardOwner() != null && tourModel.getCardOwner().equals(ProjectConstants.BUSINESS_OWNER_BOOKING_B)) 
				|| (tourModel.getBookingType().equals(ProjectConstants.ADMIN_BOOKING) 
						&& tourModel.getCardOwner() != null && tourModel.getCardOwner().equals(ProjectConstants.ADMIN_BOOKING_A))) {
			
		} else {
			language = tourModel.getLanguage();			
		}
		
		double totalAmt = invoiceModel.getTotal();
		if(invoiceModel.getMinimumFare() > invoiceModel.getTotal() ){
			totalAmt = invoiceModel.getMinimumFare();
		}
		
		UserProfileModel userProfileModel = UserProfileModel.getAdminUserAccountDetailsById(tourModel.getDriverId());
				
		long millis = Long.parseLong(new DecimalFormat("0").format(invoiceModel.getDuration()));

		String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis), TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
				TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
		
		String message = startInvoiceDefaultMessage 
					+"<div>" +
						BusinessAction.messageForKeyAdmin("labelHello", language)+",<br/><br/>" +
						BusinessAction.messageForKeyAdmin("labelInvoiceDetailsareasfollowing", language)+" :<br/><br/> " +
						"<h3>"+BusinessAction.messageForKeyAdmin("labelTripDetails", language)+"</h3>" +
						"<p>" +
							BusinessAction.messageForKeyAdmin("labelTripRequestDate", language)+" : <strong>"+DateUtils.getDateFromMilliSecond(tourModel.getCreatedAt(), "MM-dd-yyyy")+"</strong><br/><br/>" +
							BusinessAction.messageForKeyAdmin("labelPickUpLocation", language)+" : <strong>"+tourModel.getSourceAddress()+"</strong><br/><br/>" +
							BusinessAction.messageForKeyAdmin("labelDropOffLocation", language)+" : <strong>"+tourModel.getDestinationAddress()+"</strong><br/><br/>" +
							BusinessAction.messageForKeyAdmin("labelTotalFare", language)+" : <strong>"+ adminSettings.getCurrencySymbolHtml()+df.format(totalAmt)+"</strong>" +
						
							"<img style='width: 560px; height: 425px; margin-top: 10px;' src='"+ WebappPropertyUtils.BASE_PATH + invoiceModel.getStaticMapImgUrl() +"' />" +

						"</p>" +
						"<div style='border-bottom:2px dotted #c8c8c8;'></div>" +

						"<div style='width: 528px;margin-right: 110px;'>" +
							"<h3>"+BusinessAction.messageForKeyAdmin("labelPassengerDetails", language)+"</h3>" +
							"<p>" +
								BusinessAction.messageForKeyAdmin("labelName", language)+" : <strong style='float: right;'> "+tourModel.getpFirstName()+" "+tourModel.getpLastName()+ "</strong><br/><br/>" +
								BusinessAction.messageForKeyAdmin("labelPhone", language)+" : <strong style='float: right;'> "+tourModel.getpPhoneCode()+tourModel.getpPhone()+"</strong><br/><br/>" +
								BusinessAction.messageForKeyAdmin("labelEmail", language)+" : <strong style='float: right;'> "+tourModel.getpEmail()+"</strong><br/>" +
							"</p>" +
						"</div>" +
							
						"<div style='width: 522px;float:left;'>" +
							"<h3>"+BusinessAction.messageForKeyAdmin("labelDriverDetails", language)+"</h3>" +
							"<p>" +
								BusinessAction.messageForKeyAdmin("labelName", language)+" : <strong style='float: right;'>"+userProfileModel.getFirstName()+"</strong><br/><br/>" +
							"</p>" +
						"</div>" +
		
						"<div style='overflow: hidden; height: 0;clear:both;'></div>"+
						
						"<div style='border-bottom:2px dotted #c8c8c8;'></div>" +

						"<div style='width: 228px; float:left;margin-right: 110px;'>" +
							"<h3>"+BusinessAction.messageForKeyAdmin("labelFareBreakdown", language)+"</h3>" +
							"<p>" +
								BusinessAction.messageForKeyAdmin("labelBaseFare", language)+": <strong style='float: right;'> "+adminSettings.getCurrencySymbolHtml()+df.format(invoiceModel.getInitialFare()+invoiceModel.getBookingFees())+"</strong><br/>" +
								BusinessAction.messageForKeyAdmin("labelActualFareForDistance", language)+": <strong style='float: right;'> "+adminSettings.getCurrencySymbolHtml()+df.format(invoiceModel.getDistanceFare())+"</strong><br/>" +
								BusinessAction.messageForKeyAdmin("labelAdditionalCharges", language)+": <strong style='float: right;'> "+adminSettings.getCurrencySymbolHtml()+df.format(invoiceModel.getTimeFare()) +"</strong><br/>" +
								"<strong>"+BusinessAction.messageForKeyAdmin("labelTotalFare", language)+"</strong>: <strong style='float: right;'> "+adminSettings.getCurrencySymbolHtml()+df.format(invoiceModel.getTotal()) +"</strong><br/>" +
								BusinessAction.messageForKeyAdmin("labelPromoCode", language)+": <strong style='float: right;'> "+adminSettings.getCurrencySymbolHtml()+df.format(invoiceModel.getPromoDiscount()) +"</strong><br/>" +
								BusinessAction.messageForKeyAdmin("labelTollCharges", language)+": <strong style='float: right;'> "+adminSettings.getCurrencySymbolHtml()+df.format(invoiceModel.getTollAmount()) +"</strong><br/>" +
								"<strong>"+BusinessAction.messageForKeyAdmin("labelTotalCharges", language)+"</strong> : <strong style='float: right;'> "+ adminSettings.getCurrencySymbolHtml()+df.format(invoiceModel.getCharges())+"</strong><br/>" +
								BusinessAction.messageForKeyAdmin("labelCredits", language)+": <strong style='float: right;'> "+adminSettings.getCurrencySymbolHtml()+df.format(invoiceModel.getUsedCredits()) +"</strong><br/>" +
								
								"<strong>"+BusinessAction.messageForKeyAdmin("labelTotal", language)+"</strong> : <strong style='float: right;'> "+ adminSettings.getCurrencySymbolHtml()+df.format(invoiceModel.getFinalAmountCollected() )+"</strong><br/>" +
							"</p>" +
						"</div>" +
						"<div style='width: 222px;float:left;'>" +
							"<h3>"+BusinessAction.messageForKeyAdmin("labelTripStatistics", language)+"</h3>" +
							"<p>";

								if("0".equalsIgnoreCase(df.format(invoiceModel.getDistance()))) {
									message += BusinessAction.messageForKeyAdmin("labelDistance", language)+" : <strong>"+BusinessAction.messageForKeyAdmin("labelUnavailable", language)+"</strong><br/><br/>";
								} else {
									message += BusinessAction.messageForKeyAdmin("labelDistance", language)+" : <strong>"+df.format(MyHubUtils.getDistanceInProjectUnitFromMeters(invoiceModel.getDistance()))+" miles</strong><br/><br/>";
								}
						
								message += BusinessAction.messageForKeyAdmin("labelDuration", language)+" : <strong>"+hms+" (hh:mm:ss)</strong><br/><br/>";
								
								message += "</p>" +
						"</div>" +
					"</div>";
								message += endInvoiceDefaultMessage;
					
		//@formatter:on
		return message;
	}

	public static String getInvoiceMessageNewTemplate(TourModel tourModel, InvoiceModel invoiceModel, String language) {

		df.setRoundingMode(RoundingMode.DOWN);

		//@formatter:off
		
		if ((tourModel.getBookingType().equals(ProjectConstants.BUSINESS_OWNER_BOOKING) 
				&& tourModel.getCardOwner() != null && tourModel.getCardOwner().equals(ProjectConstants.BUSINESS_OWNER_BOOKING_B)) 
				|| (tourModel.getBookingType().equals(ProjectConstants.ADMIN_BOOKING) 
						&& tourModel.getCardOwner() != null && tourModel.getCardOwner().equals(ProjectConstants.ADMIN_BOOKING_A))) {
			
		} else {
			
			language = tourModel.getLanguage();			
		}
		
		UserProfileModel userProfileModel = UserProfileModel.getAdminUserAccountDetailsById(tourModel.getDriverId());
		
		AdminSettingsModel adminSettingModel = AdminSettingsModel.getAdminSettingsDetails();
		
		// Duration ----------------------------------------
		long millis = Long.parseLong(new DecimalFormat("0").format(invoiceModel.getDuration()));

		String durationHms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis), TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
				TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
		
		// Arrived waiting time ----------------------------------------
		long arriverWaitingTimeMillis = Long.parseLong(new DecimalFormat("0").format(invoiceModel.getArrivedWaitingTime()));
					
		String arriverWaitingTimeHms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(arriverWaitingTimeMillis), TimeUnit.MILLISECONDS.toMinutes(arriverWaitingTimeMillis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(arriverWaitingTimeMillis)),
										TimeUnit.MILLISECONDS.toSeconds(arriverWaitingTimeMillis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(arriverWaitingTimeMillis)));
					
		// Total time Duration ----------------------------------------
		long totalTimeMillis = Long.parseLong(new DecimalFormat("0").format(invoiceModel.getArrivedWaitingTime() + invoiceModel.getDuration()));
					
		String totalTimeHms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(totalTimeMillis), TimeUnit.MILLISECONDS.toMinutes(totalTimeMillis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(totalTimeMillis)),
										TimeUnit.MILLISECONDS.toSeconds(totalTimeMillis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(totalTimeMillis)));
		
		String usedCreditsStr = df_new.format(invoiceModel.getUsedCredits()) + "";

		if (usedCreditsStr.contains("-")) {
			if (invoiceModel.getUsedCredits() != 0) {
				usedCreditsStr = usedCreditsStr.replace("-", "+");
			}
		} else {
			if (invoiceModel.getUsedCredits() != 0) {
				usedCreditsStr = "-" + usedCreditsStr;
			}
		}
		
		String message = startInvoiceDefaultMessageForNewTemplate +
				"<!-- start of menu -->"+
				"                  <table bgcolor=\"#f9f9f9\" width=\"250\" height=\"50\" border=\"0\" align=\"right\" valign=\"middle\" cellpadding=\"0\" cellspacing=\"0\" class=\"devicewidth\">"+
				"                     <tbody>"+
				"<tr>"+
				"   <td height=\"50\" align=\"center\" style=\"font-family: Helvetica, arial, sans-serif;color: #282828;text-align:right;padding-right:10px;\" st-content=\"menu\">"+
				"      <h2 style=\"margin-top: 10%;\">"+ ProjectConstants.DEFAUALT_CURRENCY_HTML_CODE +" "+ invoiceModel.getFinalAmountCollected() +"</h2>"+
				"   </td>"+
				"</tr>"+
				"                     </tbody>"+
				"                  </table>"+
				"                  <!-- end of menu -->"+
				"               </td>"+
				"            </tr>"+
				"         </tbody>"+
				"      </table>"+
				"   </td>"+
				"</tr>"+
				"<tr>"+
				"</tr>"+
				"<tr>"+
				"   <td width=\"100%\">"+
				"      <table width=\"600\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\" class=\"devicewidth\">"+
				"         <tbody>"+
				"            <tr>"+
				"               <td>"+
				"                  <!-- Start of left column -->"+
				"                  <table width=\"300\" align=\"left\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"devicewidth\">"+
				"                     <tbody>"+
				"<tr>"+
				"   <td>"+
				"      <table width=\"280\" align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"devicewidth\">"+
				"         <tbody>"+
				"            <!-- title -->"+
				"            <tr>"+
				"               <td style=\"font-family: Helvetica, arial, sans-serif; font-size: 18px; color: #282828; text-align:left; line-height: 24px;padding:5px;\">"+
				"                  Hello"+
				"               </td>"+
				"            </tr>"+
				"            "+
				"            <tr>"+
				"               <td style=\"font-family: Helvetica, arial, sans-serif; font-size: 14px; color: #889098; text-align:left; line-height: 24px;padding:5px;\">"+
				"                  Invoice details are as following:"+
				"               </td>"+
				"            </tr>"+
				"            "+
				"         </tbody>"+
				"      </table>"+
				"   </td>"+
				"</tr>"+
				"                     </tbody>"+
				"                  </table>"+
				"                  <!-- end of left column -->"+
				"                  <!-- spacing for mobile devices-->"+
				"                  <table align=\"left\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"mobilespacing\">"+
				"                     <tbody>"+
				"<tr>"+
				"</tr>"+
				"                     </tbody>"+
				"                  </table>"+
				"                  <!-- end of for mobile devices-->"+
				"                  <!-- start of right column -->"+
				"                  <table width=\"300\" align=\"left\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"devicewidth\">"+
				"                     <tbody>"+
				"<tr>"+
				"   <td>"+
				"      <table width=\"280\" align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"devicewidth\">"+
				"         <tbody>"+
				"            <!-- title -->"+
				"            <tr>"+
				"               <td style=\"font-family: Helvetica, arial, sans-serif; font-size: 18px; color: #282828; text-align:right; line-height: 24px;padding:5px;\">"+
				"                  "+ DateUtils.dbTimeStampToSesionDate(tourModel.getCreatedAt(), WebappPropertyUtils.CLIENT_TIMEZONE, "dd MMM, yyyy") +""+
				"               </td>"+
				"            </tr>"+
				"            "+
				"            <tr>"+
				"               <td style=\"font-family: Helvetica, arial, sans-serif; font-size: 14px; color: #889098; text-align:right; line-height: 24px;padding:5px;\">"+
				"                  "+ DateUtils.dbTimeStampToSesionDate(tourModel.getCreatedAt(), WebappPropertyUtils.CLIENT_TIMEZONE, "hh:mm aaa") +""+
				"               </td>"+
				"            </tr>"+
				"            "+
				"         </tbody>"+
				"      </table>"+
				"   </td>"+
				"</tr>"+
				"                     </tbody>"+
				"                  </table>"+
				"                  <!-- end of right column -->"+
				"               </td>"+
				"            </tr>"+
				"            <tr>"+
				"               <td style=\"font-family: Helvetica, arial, sans-serif; font-size: 14px; color: #889098; text-align:left; line-height: 24px;padding:5px;\">"+
				"                  Trip id: "+ tourModel.getUserTourId() +""+
				"               </td>"+
				"            </tr>"+
				"         </tbody>"+
				"      </table>"+
				"   </td>"+
				"</tr>"+
				"<tr>"+
				"</tr>"+
				"<tr>"+
				"   <td width=\"100%\">"+
				"      <table width=\"600\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\" class=\"devicewidth\">"+
				"         <tbody>"+
				"            <tr>"+
				"               <td>"+
				"                  <!-- Start of left column -->"+
				"                  <table width=\"300\" align=\"left\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"devicewidth\">"+
				"                     <tbody>"+
				"<tr>"+
				"   <td>"+
				"      <table width=\"280\" align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"devicewidth\">"+
				"         <tbody>"+
				"            <!-- title -->"+
				"            <tr>"+
				"               <td style=\"font-family: Helvetica, arial, sans-serif; font-size: 18px; color: #282828; text-align:left; line-height: 24px;padding:5px;\">"+
				"                  <div>"+
				"                    <img style=\"vertical-align:middle\" src=\""+ WebappPropertyUtils.BASE_PATH +"/assets/image/green_radio_icon.png\">"+
				"                    <span style=\"\">"+ DateUtils.dbTimeStampToSesionDate(tourModel.getCreatedAt(), WebappPropertyUtils.CLIENT_TIMEZONE, "hh:mm aaa") +"</span>"+
				"                  </div>"+
				"               </td>"+
				"            </tr>"+
				"            <!-- end of title -->"+
				"            "+
				"            <!-- content -->"+
				"            <tr>"+
				"               <td style=\"font-family: Helvetica, arial, sans-serif; font-size: 14px; color: #889098; text-align:left; line-height: 24px;padding:5px;\">"+
				"                 "+ tourModel.getSourceAddress() +""+
				"               </td>"+
				"            </tr>"+
				"            <!-- end of content -->"+
				""+
				"            <!-- Spacing -->"+
				"            <tr>"+
				"            </tr>"+
				"            <!-- /Spacing -->"+
				""+
				"            <!-- title -->"+
				"            <tr>"+
				"               <td style=\"font-family: Helvetica, arial, sans-serif; font-size: 18px; color: #282828; text-align:left; line-height: 24px;padding:5px;\">"+
				"                  <div>"+
				"                    <img style=\"vertical-align:middle\" src=\""+ WebappPropertyUtils.BASE_PATH +"/assets/image/red_radio_icon.png\">"+
				"                    <span style=\"\">"+  DateUtils.dbTimeStampToSesionDate(tourModel.getCreatedAt() + millis, WebappPropertyUtils.CLIENT_TIMEZONE, "hh:mm aaa") +"</span>"+
				"                  </div>"+
				"               </td>"+
				"            </tr>"+
				"            <!-- end of title -->"+
				"            "+
				"            <!-- content -->"+
				"            <tr>"+
				"               <td style=\"font-family: Helvetica, arial, sans-serif; font-size: 14px; color: #889098; text-align:left; line-height: 24px;padding:5px;\">"+
				"                  "+ tourModel.getDestinationAddress() +""+
				"               </td>"+
				"            </tr>"+
				"            <!-- end of content -->"+
				"         </tbody>"+
				"      </table>"+
				"   </td>"+
				"</tr>"+
				"                     </tbody>"+
				"                  </table>"+
				"                  "+
				"                  <!-- end of left column -->"+
				"                  <!-- spacing for mobile devices-->"+
				"                  <table align=\"left\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"mobilespacing\">"+
				"                     <tbody>"+
				"<tr>"+
				"</tr>"+
				"                     </tbody>"+
				"                  </table>"+
				"                  <!-- end of for mobile devices-->"+
				"                  <!-- start of right column -->"+
				"                  <table width=\"300\" align=\"left\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"devicewidth\">"+
				"                     <tbody>"+
				"<!-- image -->"+
				"<tr>"+
				"      <img src=\" "+ WebappPropertyUtils.BASE_PATH + invoiceModel.getStaticMapImgUrl() +" \" alt=\"\" border=\"0\" width=\"280\" height=\"140\" style=\"display:block; border:none; outline:none; text-decoration:none; margin-top: 20px; margin-bottom: 20px; \" class=\"col2img\">"+
				"   </td>"+
				"</tr>"+
				"<!-- /image -->"+
				"                     </tbody>"+
				"                  </table>"+
				"                  <!-- end of right column -->"+
				"               </td>"+
				"            </tr>"+
				"         </tbody>"+
				"      </table>"+
				"   </td>"+
				"</tr>"+
				"<tr>"+
				"</tr>"+
				"<tr>"+
				"   <td width=\"100%\">"+
				"      <table width=\"600\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\" class=\"devicewidth\">"+
				"         <tbody>"+
				"            <tr>"+
				"               <td>"+
				"                  <!-- Start of left column -->"+
				"                  <table width=\"300\" align=\"left\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"devicewidth\">"+
				"                     <tbody>"+
				"<tr>"+
				"   <td>"+
				"      <table width=\"280\" align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"devicewidth\">"+
				"         <tbody>"+
				"            <!-- title -->"+
				"            <tr>"+
				"               <td style=\"font-family: Helvetica, arial, sans-serif; font-size: 18px; color: #282828; text-align:center; line-height: 24px;background-color:#dddddd;padding:5px;\">"+
				"                  Customer details"+
				"               </td>"+
				"            </tr>"+
				"            <!-- end of title -->"+
				"            "+
				"            <!-- content -->"+
				"            <tr>"+
				"               <td style=\"font-family: Helvetica, arial, sans-serif; font-size: 14px; color: #889098; text-align:left; line-height: 24px;\">"+
				"                  <ul style=\"list-style: none;margin-top: 10px;\">";
				
				if(tourModel.getBookingType().equalsIgnoreCase(ProjectConstants.ADMIN_BOOKING)){
				
					message += 	"                     <li>Name: "+ tourModel.getpFirstName() + " " + tourModel.getpLastName() +"</li>"+
							"                     <li>Phone: "+ tourModel.getpPhoneCode() + " " + tourModel.getpPhone() +"</li>"+
							"                     <li>Email: "+ tourModel.getpEmail() +"</li>";
				}else{
					
					message += 	"                     <li>Name: "+ tourModel.getpFirstName() + " " + tourModel.getpLastName() +"</li>"+
							"                     <li>Phone: "+ tourModel.getpPhoneCode() + " " + tourModel.getpPhone() +"</li>"+
							"                     <li>Email: "+ tourModel.getpEmail() +"</li>";
				}
				
				message += "                  </ul>"+
				"               </td>"+
				"            </tr>"+
				"            <!-- end of content -->"+
				""+
				"            <!-- Spacing -->"+
				"            <tr>"+
				"            </tr>"+
				"            <!-- /Spacing -->"+
				""+
				"            <!-- title -->"+
				"            <tr>"+
				"               <td style=\"font-family: Helvetica, arial, sans-serif; font-size: 18px; color: #282828; text-align:center; line-height: 24px;background-color:#dddddd;padding:5px;\">"+
				"                  Fare Breakdown"+
				"               </td>"+
				"            </tr>"+
				"            <!-- end of title -->"+
				"            "+
				"            <!-- content -->"+
				"            <tr>"+
				"               <td style=\"font-family: Helvetica, arial, sans-serif; font-size: 14px; color: #889098; text-align:left; line-height: 24px;\">"+
				"                  <ul style=\"list-style: none;margin-top: 10px;\">";
				if(tourModel.isAirportFixedFareApplied()) {
					message += "          <li>Airport Fare       : "+ ProjectConstants.DEFAUALT_CURRENCY_HTML_CODE + " " + df_new.format(invoiceModel.getTotal()) +"</li>";
				} else {
					message += "          <li>Base Fare       : "+ ProjectConstants.DEFAUALT_CURRENCY_HTML_CODE + " " + df_new.format(invoiceModel.getInitialFare()+invoiceModel.getBookingFees() + invoiceModel.getMarkupFare()) +"</li>"+
					"                     <li>Distance fare       : "+ ProjectConstants.DEFAUALT_CURRENCY_HTML_CODE + " " + df_new.format(invoiceModel.getDistanceFare()) +"</li>"+
					"                     <li>Time fare       : "+ ProjectConstants.DEFAUALT_CURRENCY_HTML_CODE + " " +  df_new.format(invoiceModel.getTimeFare()) +"</li>";
				} 
				
				if (!tourModel.isRentalBooking()) {
				
					message +=  "                     <li>Waiting Fare       : "+ ProjectConstants.DEFAUALT_CURRENCY_HTML_CODE + " " + df_new.format(invoiceModel.getArrivedWaitingTimeFare()) +"</li>";
				}				
				
				
				message += "                     <li><strong>Fare       : "+ ProjectConstants.DEFAUALT_CURRENCY_HTML_CODE + " " + df_new.format(invoiceModel.getTotal()) +"</strong></li>"+
				"                     <li>Promo Code       : "+ ProjectConstants.DEFAUALT_CURRENCY_HTML_CODE + " " + df_new.format(invoiceModel.getPromoDiscount()) +"</li>";
				
				// Tour tax details -----------------------------
				List<TourTaxModel> tourTaxModelList = TourTaxModel.getTourTaxListByTourId(tourModel.getTourId());
				for (TourTaxModel tourTaxModel : tourTaxModelList) {
					message += "                     <li>"+ tourTaxModel.getTaxName() +"       : "+ ProjectConstants.DEFAUALT_CURRENCY_HTML_CODE + " " + df_new.format(tourTaxModel.getTaxAmount()) +"</li>";
				}
				//------------------------------------------------
				
				message += "                     <li>Tax Amount       : "+ ProjectConstants.DEFAUALT_CURRENCY_HTML_CODE + " " + df_new.format(invoiceModel.getTotalTaxAmount()) +"</li>"+
				"                     <li><strong>Total       : "+ ProjectConstants.DEFAUALT_CURRENCY_HTML_CODE + " " + df_new.format(invoiceModel.getCharges()) +"</strong></li>"+
				//				"                     <li>Credits       : "+ ProjectConstants.DEFAUALT_CURRENCY_HTML_CODE + " " + df_new.format(invoiceModel.getUsedCredits()) +"</li>"+
				"                     <li>Credits       : "+ ProjectConstants.DEFAUALT_CURRENCY_HTML_CODE + " " + usedCreditsStr +"</li>"+
				"                     <li><strong>Amount collected       : "+ ProjectConstants.DEFAUALT_CURRENCY_HTML_CODE + " " + df_new.format(invoiceModel.getFinalAmountCollected()) +"</strong></li>"+
				"                  </ul>"+
				"               </td>"+
				"            </tr>"+
				"            <!-- end of content -->"+
				"         </tbody>"+
				"      </table>"+
				"   </td>"+
				"</tr>"+
				"                     </tbody>"+
				"                  </table>"+
				"                  "+
				"                  <!-- end of left column -->"+
				"                  <!-- spacing for mobile devices-->"+
				"                  <table align=\"left\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"mobilespacing\">"+
				"                     <tbody>"+
				"<tr>"+
				"</tr>"+
				"                     </tbody>"+
				"                  </table>"+
				"                  <!-- end of for mobile devices-->"+
				"                  <!-- start of right column -->"+
				"                  <table width=\"300\" align=\"left\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"devicewidth\">"+
				"                     <tbody>"+
				"<tr>"+
				"   <td>"+
				"      <table width=\"280\" align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"devicewidth\">"+
				"         <tbody>"+
				"            <!-- title -->"+
				"            <tr>"+
				"               <td style=\"font-family: Helvetica, arial, sans-serif; font-size: 18px; color: #282828; text-align:center; line-height: 24px;background-color:#dddddd;padding:5px;\">"+
				"                  Driver details"+
				"               </td>"+
				"            </tr>"+
				"            <!-- end of title -->"+
				"            "+
				"            <!-- content -->"+
				"            <tr>"+
				"               <td style=\"font-family: Helvetica, arial, sans-serif; font-size: 14px; color: #889098; text-align:left; line-height: 24px;\">"+
				"                  <ul style=\"list-style: none;margin-top: 10px;\">"+
				"                     <li>Name: "+ userProfileModel.getFirstName() +" "+ userProfileModel.getLastName()+"</li>"+
				"                     <li>Phone: "+ userProfileModel.getPhoneNoCode() +" "+ userProfileModel.getPhoneNo()+"</li>"+
				"                     <li>Email: "+ userProfileModel.getEmail() +"</li>"+
				"                  </ul>"+
				"               </td>"+
				"            </tr>"+
				"            <!-- end of content -->"+
				""+
				"            <!-- Spacing -->"+
				"            <tr>"+
				"            </tr>"+
				"            <!-- /Spacing -->"+
				""+
				"            <!-- title -->"+
				"            <tr>"+
				"               <td style=\"font-family: Helvetica, arial, sans-serif; font-size: 18px; color: #282828; text-align:center; line-height: 24px;background-color:#dddddd;padding:5px;\">"+
				"                  Trip statistics"+
				"               </td>"+
				"            </tr>"+
				"            <!-- end of title -->"+
				"            "+
				"            <!-- content -->"+
				"            <tr>"+
				"               <td style=\"font-family: Helvetica, arial, sans-serif; font-size: 14px; color: #889098; text-align:left; line-height: 24px;\">"+
				"                  <ul style=\"list-style: none;margin-top: 10px;\">";
				
				String tourTypeStatus = "";
				
				if (tourModel.isRentalBooking()) {
					tourTypeStatus += "Rental- ";
				} else {
					tourTypeStatus += "Taxi- ";
				}

				if (tourModel.isRideLater()) {
					tourTypeStatus += ProjectConstants.RIDE_LATER_STRING;
				} else {
					tourTypeStatus += ProjectConstants.RIDE_NOW_STRING;
				}
				
				if(tourModel.isAirportFixedFareApplied()) {
					tourTypeStatus += " - " + tourModel.getAirportBookingType();
				}
				
				message += "                     <li>Ride Type    :     "+ tourTypeStatus +"</li>";
				
				if (tourModel.isRentalBooking()) {
					
					double packageDistance = tourModel.getFreeDistance();
					long packageTime = tourModel.getRentalPackageTime();

					String rentalPackage = "";

					if (packageTime == 1) {

						rentalPackage = packageTime + " Hour";

					} else {

						rentalPackage = packageTime + " Hours";
					}

					rentalPackage += ", " + df_new.format((packageDistance / adminSettingModel.getDistanceUnits())) + " " + adminSettingModel.getDistanceType().toUpperCase();

					
					message += "                     <li>Rental Package    :     "+ rentalPackage +"</strong></li>";
				}
				
				
				message += "                     <li>Distance    :     "+ df.format(MyHubUtils.getDistanceInProjectUnitFromMeters(invoiceModel.getDistance())) + " "+ adminSettingModel.getDistanceType() +"</li>"+
					"                     <li>Duration    :     "+ durationHms +"</li>";
				
				
				if (!tourModel.isRentalBooking()) {
				
					message += "                     <li>Waiting Time    :     "+ arriverWaitingTimeHms +"</li>"+
						"                     <li><strong>Total Time    :     "+ totalTimeHms +"</strong></li>";
				}
				
				
				message += "                  </ul>"+
				"               </td>"+
				"            </tr>"+
				"            <!-- end of content -->"+
				"         </tbody>"+
				"      </table>"+
				"   </td>"+
				"</tr>"+
				"                     </tbody>"+
				"                  </table>"+
				"                  <!-- end of right column -->"+
				"               </td>"+
				"            </tr>"+
				"         </tbody>"+
				"      </table>"+
				"   </td>"+
				"</tr>"+
				"<tr>"+
				"</tr>"+
				"<tr>"+
				"   <td>"+
				"      <table width=\"560\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" class=\"devicewidthinner\">"+
				"         <tbody>"+
				"            <!-- Title -->"+
				"            <tr>"+
				"               <td style=\"font-family: Helvetica, arial, sans-serif; font-size: 18px; color: #282828; text-align:left; line-height: 24px;\">"+
				"                  Amount collected   "+ ProjectConstants.DEFAUALT_CURRENCY_HTML_CODE + " " + df_new.format(invoiceModel.getFinalAmountCollected()) +""+
				"               </td>"+
				"            </tr>"+
				"            <!-- End of Title -->"+
				"            <!-- spacing -->"+
				"            <tr>"+
				"            </tr>"+
				"            <!-- End of spacing -->";
								
				message += endInvoiceDefaultMessageForNewTemplate;
								
								/*if(language.equalsIgnoreCase(ProjectConstants.SPANISH_ID)){
									message+= endInvoiceDefaultMessageSpanish;
								}else{
									message += endInvoiceDefaultMessage;
								}*/
					
		//@formatter:on
		return message;
	}
}