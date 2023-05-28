package com.toolrental.service;

import com.toolrental.exceptions.ToolRentalException;
import com.toolrental.response.RentalAgreement;

//vk0523

public interface RentalService {
	RentalAgreement rentTool(String code, int rentalDays, int discountPercent, String checkoutDate)
			throws ToolRentalException;
}
