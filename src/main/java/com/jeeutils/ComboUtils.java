package com.jeeutils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import com.webapp.models.CarModel;

public class ComboUtils {

	/**
	 * It is used to get array of option with selected value
	 * 
	 * @param names
	 * @param values
	 * @param selectedValue
	 * @return String
	 */
	public static String getOptionArray(List<String> names, List<String> values, String selectedValue) {

		int nameSize;
		int valueSize;

		StringBuilder optionString;

		nameSize = names.size();
		valueSize = values.size();

		if (nameSize != valueSize) {

			throw new IllegalArgumentException("Option names and values must have equal length");

		} else {

			optionString = new StringBuilder("<option value=\"-1\">Select</option>");

			for (int start = 0; start < nameSize; start++) {

				String value = values.get(start);
				String name = names.get(start);

				if (selectedValue.equalsIgnoreCase(value)) {
					optionString.append("<option value=\"" + value + "\" selected=\"selected\">" + name + "</option>");
				} else {
					optionString.append("<option value=\"" + value + "\">" + name + "</option>");
				}
			}

			return optionString.toString();
		}

	}

	public static String getOptionArrayForListBoxForView(List<String> names, List<String> values, String[] selectedValue) {

		int nameSize;
		int valueSize;

		StringBuilder optionString;

		nameSize = names.size();
		valueSize = values.size();

		if (nameSize != valueSize) {

			throw new IllegalArgumentException("Option names and values must have equal length");

		} else {

			optionString = new StringBuilder();

			for (int start = 0; start < nameSize; start++) {

				String value = values.get(start);
				String name = names.get(start);

				boolean flag = false;

				if (selectedValue != null) {

					for (int i = 0; i < selectedValue.length; i++) {

						if (selectedValue[i].equalsIgnoreCase(value)) {
							optionString.append("<option value=\"" + value + "\" selected=\"selected\">" + name + "</option>");
							flag = true;
						}
					}
				}

				if (flag == false) {

					optionString.append("<option value=\"" + value + "\">" + name + "</option>");
				}

			}

			return optionString.toString();
		}

	}

	public static String getOptionArrayForListBox(List<String> names, List<String> values, String selectedValue) {

		int nameSize;
		int valueSize;

		StringBuilder optionString;

		nameSize = names.size();
		valueSize = values.size();

		if (nameSize != valueSize) {

			throw new IllegalArgumentException("Option names and values must have equal length");

		} else {

			optionString = new StringBuilder();

			for (int start = 0; start < nameSize; start++) {

				String value = values.get(start);
				String name = names.get(start);

				if (selectedValue.equalsIgnoreCase(value)) {
					optionString.append("<option value=\"" + value + "\" selected=\"selected\">" + name + "</option>");
				} else {
					optionString.append("<option value=\"" + value + "\">" + name + "</option>");
				}
			}

			return optionString.toString();
		}
	}


	public static String getOptionArrayForListBoxCarIcons(List<String> names, List<String> values, String selectedValue, String basePath) {

		int nameSize;
		int valueSize;

		StringBuilder optionString;

		nameSize = names.size();
		valueSize = values.size();

		if (nameSize != valueSize) {

			throw new IllegalArgumentException("Option names and values must have equal length");

		} else {

			optionString = new StringBuilder();

			for (int start = 0; start < nameSize; start++) {

				String value = values.get(start);
				String name = names.get(start);

				String icon = basePath + "/assets/image/car_icons/" + value + "/" + value + "_free.png";

				if (selectedValue.equalsIgnoreCase(value)) {
					optionString.append("<option style='background-image:url(" + icon + ");' value=\"" + value + "\" selected=\"selected\">" + name + "</option>");
				} else {
					optionString.append("<option style='background-image:url(" + icon + ");' value=\"" + value + "\">" + name + "</option>");
				}
			}

			return optionString.toString();
		}
	}

	public static String getStatusOption(String selectedValue) {

		List<String> statusName = new ArrayList<String>();
		statusName.add("Active");
		statusName.add("Inactive");

		List<String> statusValues = new ArrayList<String>();
		statusValues.add("1");
		statusValues.add("0");

		return getOptionWithDefaultOption(statusName, statusValues, selectedValue, "Select");
	}

	public static String getAccountTypeOption(String selectedValue) {

		List<String> statusName = new ArrayList<String>();
		statusName.add("Checking");
		statusName.add("Saving");

		List<String> statusValues = new ArrayList<String>();
		statusValues.add("Checking");
		statusValues.add("Saving");

		return getOptionWithDefaultOption(statusName, statusValues, selectedValue, "Select");
	}

	public static String getOptionWithDefaultOption(List<String> names, List<String> values, String selectedValue, String defaultOption) {

		int nameSize;
		int valueSize;

		StringBuilder optionString;

		nameSize = names.size();
		valueSize = values.size();

		if (nameSize != valueSize) {

			throw new IllegalArgumentException("Option names and values must have equal length");

		} else {

			optionString = new StringBuilder("<option value=\"-1\">" + defaultOption + "</option>");

			for (int start = 0; start < nameSize; start++) {

				String value = values.get(start);
				String name = names.get(start);

				if (selectedValue.equalsIgnoreCase(value)) {
					optionString.append("<option value=\"" + value + "\" selected=\"selected\">" + name + "</option>");
				} else {
					optionString.append("<option value=\"" + value + "\">" + name + "</option>");
				}
			}

			return optionString.toString();
		}
	}

	public static String getMultipleOptions(List<String> names, List<String> values, List<String> selectedValue, String defaultOption) {

		int nameSize;
		int valueSize;
		int selectedValueSize;

		boolean flag = true;

		StringBuilder optionString;

		nameSize = names.size();
		valueSize = values.size();
		selectedValueSize = selectedValue.size();

		if (nameSize != valueSize) {

			throw new IllegalArgumentException("Option names and values must have equal length");

		} else {

			optionString = new StringBuilder();

			for (int start = 0; start < nameSize; start++) {

				String value = values.get(start);
				String name = names.get(start);

				for (int i = 0; i < selectedValueSize; i++) {

					String Selected = selectedValue.get(i);

					if (Selected.equalsIgnoreCase(value)) {

						optionString.append("<option value=\"" + value + "\" selected=\"selected\">" + name + "</option>");
						flag = false;
					}
				}

				if (flag) {
					optionString.append("<option value=\"" + value + "\">" + name + "</option>");
				}

				flag = true;
			}

			return optionString.toString();
		}
	}

	public static String getMonthOption(String selectedValue) {

		List<String> statusName = new ArrayList<String>();
		statusName.add("01");
		statusName.add("02");
		statusName.add("03");
		statusName.add("04");
		statusName.add("05");
		statusName.add("06");
		statusName.add("07");
		statusName.add("08");
		statusName.add("09");
		statusName.add("10");
		statusName.add("11");
		statusName.add("12");

		List<String> statusValues = new ArrayList<String>();
		statusValues.add("01");
		statusValues.add("02");
		statusValues.add("03");
		statusValues.add("04");
		statusValues.add("05");
		statusValues.add("06");
		statusValues.add("07");
		statusValues.add("08");
		statusValues.add("09");
		statusValues.add("10");
		statusValues.add("11");
		statusValues.add("12");

		return getOptionWithDefaultOption(statusName, statusValues, selectedValue, "Month");
	}

	public static String getYearOption(String selectedValue) {

		List<String> statusName = new ArrayList<String>();
		statusName.add("2016");
		statusName.add("2017");
		statusName.add("2018");
		statusName.add("2019");
		statusName.add("2020");
		statusName.add("2021");
		statusName.add("2022");
		statusName.add("2023");
		statusName.add("2024");
		statusName.add("2025");
		statusName.add("2026");
		statusName.add("2027");
		statusName.add("2028");

		List<String> statusValues = new ArrayList<String>();
		statusValues.add("2016");
		statusValues.add("2017");
		statusValues.add("2018");
		statusValues.add("2019");
		statusValues.add("2020");
		statusValues.add("2021");
		statusValues.add("2022");
		statusValues.add("2023");
		statusValues.add("2024");
		statusValues.add("2025");
		statusValues.add("2026");
		statusValues.add("2027");
		statusValues.add("2028");

		return getOptionWithDefaultOption(statusName, statusValues, selectedValue, "Year");
	}

	public static String getPastFifteenYearOptionForLocalization(String selectedValue, String year) {

		Calendar localCalendar = Calendar.getInstance(TimeZone.getDefault());

		int currentYear = localCalendar.get(Calendar.YEAR);

		List<String> statusName = new ArrayList<String>();
		List<String> statusValues = new ArrayList<String>();

		for (int i = currentYear; i > 1979; i--) {
			statusName.add(i + "");
			statusValues.add(i + "");
		}

		return getOptionWithDefaultOption(statusName, statusValues, selectedValue, year);
	}

	public static String getCarOption(String selectedValue) throws SQLException {

		List<String> statusName = new ArrayList<String>();
		List<String> statusValues = new ArrayList<String>();

		List<CarModel> carTypeModel = CarModel.getCarList();

		for (CarModel carTypeModel2 : carTypeModel) {
			statusValues.add(carTypeModel2.getCarId());
			statusName.add(carTypeModel2.getMake() + ", " + carTypeModel2.getModelName() + ", " + carTypeModel2.getCarPlateNo() + ", " + carTypeModel2.getCarType());
		}

		return getOptionWithDefaultOption(statusName, statusValues, selectedValue, "Select");
	}

	public static String getOptionArrayForListBoxForMultiSelect(List<String> names, List<String> values, List<String> selectedIdList) {

		int nameSize;
		int valueSize;

		StringBuilder optionString;

		nameSize = names.size();
		valueSize = values.size();

		if (nameSize != valueSize) {

			throw new IllegalArgumentException("Option names and values must have equal length");

		} else {

			optionString = new StringBuilder();

			for (int start = 0; start < nameSize; start++) {

				String value = values.get(start);
				String name = names.get(start);

				boolean isSelected = false;

				if ((selectedIdList != null) && (selectedIdList.size() > 0)) {

					for (String selectedValue : selectedIdList) {

						if (selectedValue.equalsIgnoreCase(value)) {
							isSelected = true;
						}
					}
				}

				if (isSelected) {
					optionString.append("<option value=\"" + value + "\" selected=\"selected\">" + name + "</option>");
				} else {
					optionString.append("<option value=\"" + value + "\">" + name + "</option>");
				}
			}

			return optionString.toString();
		}
	}

	public static String getOptionArrayForListBoxForDisableMultiSelect(List<String> names, List<String> values, List<String> selectedIdList) {

		int nameSize;
		int valueSize;

		StringBuilder optionString;

		nameSize = names.size();
		valueSize = values.size();

		if (nameSize != valueSize) {

			throw new IllegalArgumentException("Option names and values must have equal length");

		} else {

			optionString = new StringBuilder();

			for (int start = 0; start < nameSize; start++) {

				String value = values.get(start);
				String name = names.get(start);

				boolean isSelected = false;

				if ((selectedIdList != null) && (selectedIdList.size() > 0)) {

					for (String selectedValue : selectedIdList) {

						if (selectedValue.equalsIgnoreCase(value)) {
							isSelected = true;
						}
					}
				}

				if (isSelected) {
					optionString.append("<option value=\"" + value + "\" selected=\"selected\" disabled>" + name + "</option>");
				} else {
					optionString.append("<option value=\"" + value + "\"disabled>" + name + "</option>");
				}
			}

			return optionString.toString();
		}
	}

	public static String getDisableOptionArrayForListBox(List<String> names, List<String> values, String selectedValue) {

		int nameSize;
		int valueSize;

		StringBuilder optionString;

		nameSize = names.size();
		valueSize = values.size();

		if (nameSize != valueSize) {

			throw new IllegalArgumentException("Option names and values must have equal length");

		} else {

			optionString = new StringBuilder();

			for (int start = 0; start < nameSize; start++) {

				String value = values.get(start);
				String name = names.get(start);

				if (selectedValue.equalsIgnoreCase(value)) {
					optionString.append("<option value=\"" + value + "\" selected=\"selected\">" + name + "</option>");
				} else {
					optionString.append("<option value=\"" + value + "\"disabled>" + name + "</option>");
				}
			}

			return optionString.toString();
		}
	}
}