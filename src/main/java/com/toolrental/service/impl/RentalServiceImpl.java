package com.toolrental.service.impl;

import java.time.LocalDate;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Date;

import com.toolrental.response.RentalAgreement;
import com.toolrental.enity.Tool;
import com.toolrental.exceptions.ToolRentalException;
import com.toolrental.repository.ToolRepository;
import com.toolrental.response.ErrorMessages;
import com.toolrental.service.RentalService;
import com.toolrental.utils.CustomRentalCalendar;

//vk0523

@Service
public class RentalServiceImpl implements RentalService {

	@Autowired
	ToolRepository toolRepository;

	@Autowired
	CustomRentalCalendar calendar;

	@Override
	public RentalAgreement rentTool(String code, int rentalDays, int discoutPercent, String checkoutDateString)
			throws ToolRentalException {
		Tool tool = null;
		for (Tool t : toolRepository.findAll()) {
			if (t.getCode().equals(code.toUpperCase())) {
				tool = t;
				break;
			}
		}
		if (tool == null) {
			throw new ToolRentalException(code + ErrorMessages.TOOL_CODE_IS_NOT_PRESENT.getErrorMessage());
		}
		verifyDaysOfRental(rentalDays);
		verifyDiscount(discoutPercent);
		Double dailyCharge = tool.getDailyCharge();
		verifyDaysOfRental(rentalDays);
		verifyDiscount(discoutPercent);
		LocalDate checkoutDate = getCheckoutDate(checkoutDateString);
		int chargeDays = getChargeDays(tool, rentalDays, checkoutDate);

		RentalAgreement agr = new RentalAgreement(tool.getCode(), tool.getType(), tool.getBrand(), rentalDays);
		DecimalFormat df = new DecimalFormat("##,##0.00");

		agr.setRentalDays(rentalDays);
		agr.setCheckoutDate(checkoutDate.toString());
		agr.setDueDate(checkoutDate.plusDays(rentalDays).toString());
		agr.setDailyRentalCharge(dailyCharge);
		agr.setChargeDays(chargeDays);
		double rentPreDiscount = calculateRentPreDiscount(tool, chargeDays);
		agr.setPreDiscountCharge("$" + df.format(rentPreDiscount));
		agr.setDiscountPercent(discoutPercent + "%");
		double discountAmount = rentPreDiscount * discoutPercent / 100;
		agr.setDiscountAmount("$" + df.format(discountAmount));
		agr.setFinalCharge("$" + df.format(rentPreDiscount - discountAmount));
		return agr;
	}

	private void verifyDaysOfRental(int daysOfRental) throws ToolRentalException {
		if (daysOfRental < 1) {
			throw new ToolRentalException(ErrorMessages.RENTAL_DAY_COUNT.getErrorMessage());
		}
	}

	public void verifyDiscount(int discount) throws ToolRentalException {
		if (discount > 100 || discount < 0) {
			throw new ToolRentalException(ErrorMessages.RENTAL_DISCOUNT.getErrorMessage());
		}
	}

	double calculateRentPreDiscount(Tool tool, int chargeDays) throws ToolRentalException {
		return roundUp(chargeDays * tool.getDailyCharge());
	}

	private int getChargeDays(Tool tool, int daysOfRental, LocalDate startDate) {
		int ret = 0;
		// Charge days - Count of chargeable days, from day after checkout through and
		// including due date, excluding “no charge” days as specified by the tool type.
		startDate = startDate.plusDays(1);
		LocalDate endDate = startDate.plusDays(daysOfRental - 1);
		calendar.calculateHolidays(startDate.getYear());
		if (tool.isWeekdayCharge()) {
			int numberOfWorkDays = calendar.getNumberOfWorkdaysBetween(startDate, endDate);
			ret += numberOfWorkDays;
		}
		if (tool.isWeekendCharge()) {
			int numberOfWeekends = calendar.getNumberOfWeekendsBetween(startDate, endDate);
			ret += numberOfWeekends;
		}
		if (tool.isHolidayCharge()) {
			int numberOfHolidays = calendar.getNumberOfHolidaysBetween(startDate, endDate);
			ret += numberOfHolidays;
		}
		return ret;
	}

	public LocalDate getCheckoutDate(String date) throws ToolRentalException {
		Date ret;
		SimpleDateFormat fromUser = new SimpleDateFormat("MM/dd/yyyy");
		fromUser.setLenient(false);
		try {
			ret = fromUser.parse(date);
		} catch (ParseException e) {
			throw new ToolRentalException(ErrorMessages.INCORRECT_DATE.getErrorMessage());
		}
		return ret.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	}
	
	public double roundUp(double in) {
		return Math.round(in * 100.0) / 100.0;
	}
}
