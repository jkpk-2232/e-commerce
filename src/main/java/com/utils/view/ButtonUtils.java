package com.utils.view;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ButtonUtils {

	private HttpServletRequest request;

	private int tabIndexStart;

	public ButtonUtils(HttpServletRequest request, HttpServletResponse response, int tabIndexStart) {

		this.request = request;

		this.tabIndexStart = tabIndexStart;

		if (request != null && response != null) {
			//dbSession = DbSession.getSession(request, response, false);
		}
	}

	public String outputFormButton(ActionButton button) {

		String html = "";

		if (button.getActionUrl() != null) {
			//			if (!checkButtonAccess(button.getActionUrl())) {
			//				return html;
			//			}
		}

		html += "<a href='#' name='" + button.getBtnId() + "' tabindex='" + (tabIndexStart++) + "' id='" + button.getBtnId() + "' title='" + button.getTitle() + "'>";

		html += "<img src='" + request.getContextPath() + "/assets/images/" + button.getBtnImageName() + "' />";

		html += "</a>";

		return html;
	}

	public String outputFormButtonTD(ActionButton button, String cssClass) {

		String html = "";

		html += "<td class='" + cssClass + "'>";

		html += outputFormButton(button);

		html += "</td>";

		return html;
	}

	public String outputFormButtonsTD(List<ActionButton> buttons, String cssClass) {

		String html = "";

		html += "<td class='" + cssClass + "'>";

		for (ActionButton button : buttons) {
			html += outputButtonWithImage(button);
		}

		html += "</td>";

		return html;
	}

	public String outputFormButtonsTable(List<ActionButton> leftButtons, List<ActionButton> rightButtons, String tableId, String cssClass) {

		String html = "<table class='form-table-buttons' id='" + tableId + "'>";

		html += "<tr>";

		String leftStr = outputFormButtonsTD(rightButtons, "right-buttons");

		html += outputFormButtonsTD(leftButtons, "left-buttons");

		html += leftStr;

		html += "</tr>";

		html += "</table>";

		return html;
	}

	public String outputFormButtonsTableSaveBack(String tableId, String cssClass) {

		String html = "";

		List<ActionButton> left = new ArrayList<ActionButton>();

		left.add(new ActionButton("btnBack", "back.gif", "Back", null));

		List<ActionButton> right = new ArrayList<ActionButton>();

		right.add(new ActionButton("btnSave", "save.gif", "Save", null));

		html += outputFormButtonsTable(left, right, tableId, cssClass);

		return html;
	}

	public String outputButtonWithImage(ActionButton button) {

		String html = "";

		if (button.getActionUrl() != null) {
			//			if (!checkButtonAccess(button.getActionUrl())) {
			//				return html;
			//			}
		}

		html = "<div id='" + button.getBtnId() + "' name='" + button.getBtnId() + "' tabindex='" + (tabIndexStart++) + "' class='buttonClass " + button.getCssClass() + "' value='' style='float:left;' title='" + button.getTitle() + "'>";

		if (button.hasInnerImg() == true) {
			html += "<div class='iconImagesCss imageFor" + button.getBtnId() + "' style='float:left;'></div>";
			html += "<div class='commonLabelCss iconLableCss labelFor" + button.getBtnId() + "' style=''>" + button.getTitle() + "</div>" + "</div>";
		} else {
			html += "<div class='commonLabelCss labelFor" + button.getBtnId() + "' style=''>" + button.getTitle() + "</div>" + "</div>";
		}

		return html;
	}

}