package com.toolrental.contoller;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.toolrental.enity.Tool;
import com.toolrental.exceptions.ToolRentalException;
import com.toolrental.repository.ToolRepository;
import com.toolrental.response.RentalAgreement;
import com.toolrental.service.RentalService;

@RestController
@RequestMapping("/tools")
public class ToolController {

	@Autowired
	private final ToolRepository toolRepository;

	@Autowired
	RentalService rentalService;

	public ToolController(ToolRepository toolRepository) {
		this.toolRepository = toolRepository;
	}

	@GetMapping
	Iterable<Tool> getCoffees() {
		return toolRepository.findAll();
	}
	
	@RequestMapping("/")
	public @ResponseBody String greeting() {
		return "Hello, World";
	}

	// @GetMapping("/{code}/{rentalDays}/{discountPercent}/{checkoutDate}")
	@GetMapping("/rent")
	public RentalAgreement getToolByCode(@RequestParam Map<String, String> customQuery) throws ToolRentalException {

		String code = customQuery.get("code");
		String rentalDays = customQuery.get("rentalDays");
		String discountPercent = customQuery.get("discountPercent");
		String checkoutDate = customQuery.get("checkoutDate");
		return rentalService.rentTool(code, Integer.valueOf(rentalDays), Integer.valueOf(discountPercent), checkoutDate);
	}
}
