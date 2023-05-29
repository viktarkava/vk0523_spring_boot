package com.toolrental.service;

import com.toolrental.response.RentalAgreement;
import com.toolrental.exceptions.ToolRentalException;

//vk0523

public interface RentalService {
	RentalAgreement rentTool(String code, int rentalDays, int discountPercent, String checkoutDate)
			throws ToolRentalException;
}
