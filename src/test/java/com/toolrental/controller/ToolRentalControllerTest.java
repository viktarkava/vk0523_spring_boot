package com.toolrental.controller;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.boot.test.context.SpringBootTest;

import com.toolrental.response.RentalAgreement;
import com.toolrental.contoller.ToolController;
import com.toolrental.enity.Tool;
import com.toolrental.exceptions.ToolRentalException;
import com.toolrental.repository.ToolRepository;
import com.toolrental.response.ErrorMessages;
import com.toolrental.service.impl.RentalServiceImpl;
import com.toolrental.utils.CustomRentalCalendar;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;

//vk0523

@SpringBootTest
public class ToolRentalControllerTest {

	@Mock
	private ToolRepository toolRepository;

	@Spy
	CustomRentalCalendar calendar;

	@InjectMocks
	RentalServiceImpl rentalService;

	@InjectMocks
	ToolController toolController;

	List<Tool> toolList;

	private final Tool CHNS = new Tool("CHNS", "Chainsaw", "Stihl", 1.49, true, false, true);
	private final Tool JAKR = new Tool("JAKR", "Jackhammer", "Ridgid", 2.99, true, false, false);
	private final Tool LADW = new Tool("LADW", "Ladder", "Werner", 1.99, true, true, false);
	private final Tool JAKD = new Tool("JAKD", "Jackhammer", "DeWalt", 2.99, true, false, false);

	@BeforeEach
	void setUp() throws Exception {
		calendar = Mockito.spy(new CustomRentalCalendar());
		rentalService = Mockito.spy(new RentalServiceImpl());
		MockitoAnnotations.openMocks(this);
		toolList = new ArrayList<>();
		toolList.add(CHNS);
		toolList.add(JAKR);
		toolList.add(LADW);
		toolList.add(JAKD);
	}

	@Test
	public void Test1() throws ToolRentalException {
		// Tool code JAKR
		// Checkout date 9/3/15
		// Rental days 5
		// Discount 101%

		String rentalDays = "5";
		String checkoutDateString = "9/3/15";
		String discountPercent = "101";

		int chargeDays = 2;
		Exception exception = assertThrows(ToolRentalException.class, () -> {
			checkValues(JAKR, rentalDays, discountPercent, checkoutDateString, chargeDays);
		});
		assertTrue(exception.getMessage().contains(ErrorMessages.RENTAL_DISCOUNT.getErrorMessage()));
	}

	@Test
	public void Test2() throws ToolRentalException, ParseException {
		// Tool code LADW
		// Checkout date 7/2/2020
		// Rental days 3
		// Discount 10%

		String rentalDays = "3";
		String checkoutDateString = "7/2/2020";
		String discountPercent = "10";

		// 7/2/2020 - Th Checkout day. No charge.
		// 7/3/2020 - Fr July 4th observed. Holiday. No charge.
		// 7/4/2020 - Sa Weekend. Charge day.
		// 7/5/2020 - Su Weekend. Charge day.

		int chargeDays = 2;
		checkValues(LADW, rentalDays, discountPercent, checkoutDateString, chargeDays);
	}

	@Test
	public void Test3() throws ToolRentalException, ParseException {
		// Tool code CHNS
		// Checkout date 7/2/2015
		// Rental days 5
		// Discount 25%

		String rentalDays = "5";
		String checkoutDateString = "7/2/2020";
		String discountPercent = "10";

		// 7/2/2015 - Th Checkout day. No charge.
		// 7/3/2015 - Fr July 4th observed. Holiday. Charge day.
		// 7/4/2015 - Sa Weekend. No charge.
		// 7/5/2015 - Su Weekend. No charge.
		// 7/6/2015 - Mo Charge day.
		// 7/7/2015 - Tu Charge day. Due Date.

		int chargeDays = 3;
		checkValues(CHNS, rentalDays, discountPercent, checkoutDateString, chargeDays);
	}

	@Test
	public void Test4() throws ToolRentalException, ParseException {
		// Tool code JAKD
		// Checkout date 9/3/2015
		// Rental days 6
		// Discount 0%

		String rentalDays = "6";
		String checkoutDateString = "9/3/2015";
		String discountPercent = "0";

		// 9/3/2015 - Th Checkout day. No charge.
		// 9/4/2015 - Fr Charge day.
		// 9/5/2015 - Sa Weekend. No charge.
		// 9/6/2015 - Su Weekend. No charge.
		// 9/7/2015 - Mo Labor Day. Holiday. No charge.
		// 9/8/2015 - Tu Charge day.
		// 9/9/2015 - Tu Charge day. Due Date.
		int chargeDays = 3;
		checkValues(JAKD, rentalDays, discountPercent, checkoutDateString, chargeDays);
	}
	
	@Test
	public void Test5() throws ToolRentalException, ParseException {
		// Tool code JAKR
		// Checkout date 7/2/2015
		// Rental days 9
		// Discount 0%

		String rentalDays = "9";
		String checkoutDateString = "7/2/2020";
		String discountPercent = "0";

		// 7/2/2015 - Th Checkout day. No charge.
		// 7/3/2015 - Fr July 4th observed. Holiday. No charge.
		// 7/4/2015 - Sa Weekend. No charge.
		// 7/5/2015 - Su Weekend. No charge.
		// 7/6/2015 - Mo Charge day.
		// 7/7/2015 - Tu Charge day.
		// 7/8/2015 - We Charge day.
		// 7/9/2015 - Th Charge day.
		// 7/10/2015 - Fr Charge day.
		// 7/11/2015 - Sa No charge. Due Date

		int chargeDays = 5;
		checkValues(JAKR, rentalDays, discountPercent, checkoutDateString, chargeDays);
	}
	
	@Test
	public void Test6() throws ToolRentalException, ParseException {
		// Tool code JAKR
		// Checkout date 7/2/2020
		// Rental days 4
		// Discount 50%

		String rentalDays = "4";
		String checkoutDateString = "7/2/2020";
		String discountPercent = "50";

		// 7/2/2020 - Th Checkout day. No charge.
		// 7/3/2020 - Fr July 4th observed. Holiday. No charge.
		// 7/4/2020 - Sa Weekend. No charge.
		// 7/5/2020 - Su Weekend. No charge.
		// 7/5/2020 - Mo Charge day. Due Date.

		int chargeDays = 1;
		checkValues(JAKR, rentalDays, discountPercent, checkoutDateString, chargeDays);
	}
	
	@Test
	public void NegativeRentalDaysTest() throws ToolRentalException {
		// Tool code JAKR
		// Checkout date 9/3/15
		// Rental days 5
		// Discount 101%

		String rentalDays = "-5";
		String checkoutDateString = "9/3/15";
		String discountPercent = "10";

		int chargeDays = 2;
		Exception exception = assertThrows(ToolRentalException.class, () -> {
			checkValues(JAKR, rentalDays, discountPercent, checkoutDateString, chargeDays);
		});
		assertTrue(exception.getMessage().contains(ErrorMessages.RENTAL_DAY_COUNT.getErrorMessage()));
	}
	
	@Test
	public void NonExistentToolTest() throws ToolRentalException {
		// Tool code JAKR
		// Checkout date 9/3/15
		// Rental days 5
		// Discount 101%

		String rentalDays = "5";
		String checkoutDateString = "9/3/15";
		String discountPercent = "10";

		int chargeDays = 2;
		
		Tool AAA = new Tool("AAA", "AAA", "AAA", 2.99, true, false, false);
		
		Exception exception = assertThrows(ToolRentalException.class, () -> {
			checkValues(AAA, rentalDays, discountPercent, checkoutDateString, chargeDays);
		});
		assertTrue(exception.getMessage().contains(ErrorMessages.TOOL_CODE_IS_NOT_PRESENT.getErrorMessage()));
	}
	
	@Test
	public void IncorrectDateTest() throws ToolRentalException {
		// Tool code JAKR
		// Checkout date 9/3/15
		// Rental days 5
		// Discount 101%

		String rentalDays = "5";
		String checkoutDateString = "date";
		String discountPercent = "10";

		int chargeDays = 2;
		Exception exception = assertThrows(ToolRentalException.class, () -> {
			checkValues(JAKR, rentalDays, discountPercent, checkoutDateString, chargeDays);
		});
		assertTrue(exception.getMessage().contains(ErrorMessages.INCORRECT_DATE.getErrorMessage()));
	}

	private void checkValues(Tool tool, String rentalDays, String discountPercent, String checkoutDateString,
			int chargeDays) throws ToolRentalException, ParseException {

		SimpleDateFormat dateFormatter = new SimpleDateFormat("MM/dd/yyyy");
		dateFormatter.setLenient(false);

		LocalDate checkoutDate = rentalService.getCheckoutDate(checkoutDateString);
		
		String dueDateString = checkoutDate.plusDays(Integer.valueOf(rentalDays)).toString();
		DecimalFormat df = new DecimalFormat("##,##0.00");
		df.setRoundingMode(RoundingMode.UP);
		double preDiscountCharge = chargeDays * tool.getDailyCharge();
		String preDiscountChargeString = "$" + df.format(preDiscountCharge);
		double discountAmount = chargeDays * tool.getDailyCharge() * Integer.valueOf(discountPercent) / 100;
		String discountAmountString = "$" + df.format(discountAmount);
		double finalCharge = preDiscountCharge - discountAmount;
		String finalChargeString = "$" + df.format(finalCharge);

		when(toolRepository.findAll()).thenReturn(toolList);
		Map<String, String> customQuery = createCustomQuery(tool.getCode(), rentalDays, discountPercent,
				checkoutDateString);
		RentalAgreement agreement = toolController.getToolByCode(customQuery);

		assertEquals(tool.getCode(), agreement.getToolCode());
		assertEquals(tool.getType(), agreement.getToolType());
		assertEquals(tool.getBrand(), agreement.getToolBrand());
		assertEquals(rentalDays, String.valueOf(agreement.getRentalDays()));
		assertEquals(checkoutDate.toString(), agreement.getCheckoutDate());
		assertEquals(dueDateString, agreement.getDueDate());
		assertEquals(tool.getDailyCharge(), agreement.getDailyRentalCharge());
		assertEquals(chargeDays, agreement.getChargeDays());
		assertEquals(preDiscountChargeString, agreement.getPreDiscountCharge());
		assertEquals(discountPercent + "%", agreement.getDiscountPercent());
		assertEquals(discountAmountString, agreement.getDiscountAmount());
		assertEquals(finalChargeString, agreement.getFinalCharge());
	}

	private Map<String, String> createCustomQuery(String toolCode, String rentalDays, String discountPercent,
			String checkoutDate) {
		Map<String, String> customQuery = new HashMap<>();
		customQuery.put("code", toolCode);
		customQuery.put("rentalDays", rentalDays);
		customQuery.put("discountPercent", discountPercent);
		customQuery.put("checkoutDate", checkoutDate);
		return customQuery;
	}

}
