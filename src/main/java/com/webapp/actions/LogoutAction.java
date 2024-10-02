package com.webapp.actions;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.utils.LoginUtils;
import com.utils.dbsession.CookieUtils;
import com.utils.dbsession.DbSession;
import com.utils.myhub.UserRoleUtils;
import com.webapp.models.UserLoginOtpModel;

@Path("/logout")
public class LogoutAction extends BusinessAction {

	@GET
	@Path("")
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response addForm(
			@Context HttpServletRequest req, 
			@Context HttpServletResponse res
			) throws ServletException, IOException {
	//@formatter:on

		preprocessRequest(req, res);

		String login_url = "/login.do";

		if (LoginUtils.checkValidSession(req, res)) {

			DbSession dbSession = DbSession.getSession(req, res, false);

			if (dbSession != null) {

				if (UserRoleUtils.isVendorAndSubVendorRole(dbSession.getAttribute(LoginUtils.ROLE_ID))) {
					login_url = "/vendor/login.do";
				}
				
				if (UserRoleUtils.isErpRole(dbSession.getAttribute(LoginUtils.ROLE_ID))) {
					login_url = "/brand/login.do";
				}

				Cookie rememberedCookie = CookieUtils.getCookie(DbSession.REMEMBER_ME_SESSION_COOKIE_NAME, request);

				if (rememberedCookie != null) {

					LoginUtils.deleteRememberMeKey(rememberedCookie.getValue());
				}

				UserLoginOtpModel.deleteVerificationCode(dbSession.getUserId());
			}

			LoginUtils.destoryCurrentSession(req, res);

			Cookie cookie = new Cookie(DbSession.REMEMBER_ME_SESSION_COOKIE_NAME, "");

			CookieUtils.deleteCookie(cookie, request, response);

		}

		return redirectToPage(login_url);
	}

}