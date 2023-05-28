package com.toolrental.response;

import java.time.LocalDate;

public class RentalAgreement {
	private String toolCode;
	private String toolType;
	private String toolBrand;
	private Integer rentalDays;
	private String checkoutDate;
	private String dueDate;
	private double dailyRentalCharge;
	private int chargeDays;
	private String preDiscountCharge;
	private String discountPercent;
	private String discountAmount;
	private String finalCharge;

	public RentalAgreement(String toolCode, String toolType, String toolBrand, Integer rentalDays) {
		super();
		this.toolCode = toolCode;
		this.toolType = toolType;
		this.toolBrand = toolBrand;
		this.rentalDays = rentalDays;
	}

	public String getToolCode() {
		return toolCode;
	}

	public void setToolCode(String toolCode) {
		this.toolCode = toolCode;
	}

	public String getToolType() {
		return toolType;
	}

	public void setToolType(String toolType) {
		this.toolType = toolType;
	}

	public String getToolBrand() {
		return toolBrand;
	}

	public void setToolBrand(String toolBrand) {
		this.toolBrand = toolBrand;
	}

	public Integer getRentalDays() {
		return rentalDays;
	}

	public void setRentalDays(Integer rentalDays) {
		this.rentalDays = rentalDays;
	}

	public String getCheckoutDate() {
		return checkoutDate;
	}

	public void setCheckoutDate(String checkoutDate) {
		this.checkoutDate = checkoutDate;
	}

	public String getDueDate() {
		return dueDate;
	}

	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}

	public double getDailyRentalCharge() {
		return dailyRentalCharge;
	}

	public void setDailyRentalCharge(Double dailyCarge) {
		this.dailyRentalCharge = dailyCarge;
	}

	public int getChargeDays() {
		return chargeDays;
	}

	public void setChargeDays(int chargeDays) {
		this.chargeDays = chargeDays;
	}

	public String getPreDiscountCharge() {
		return preDiscountCharge;
	}

	public void setPreDiscountCharge(String preDiscountCharge) {
		this.preDiscountCharge = preDiscountCharge;
	}

	public String getDiscountPercent() {
		return discountPercent;
	}

	public void setDiscountPercent(String discountPercent) {
		this.discountPercent = discountPercent;
	}

	public String getDiscountAmount() {
		return discountAmount;
	}

	public void setDiscountAmount(String discountAmount) {
		this.discountAmount = discountAmount;
	}

	public String getFinalCharge() {
		return finalCharge;
	}

	public void setFinalCharge(String finalCharge) {
		this.finalCharge = finalCharge;
	}

}
