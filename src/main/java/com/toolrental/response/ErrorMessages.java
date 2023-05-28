package com.toolrental.response;

//vk0523

public enum ErrorMessages {
	RENTAL_DAY_COUNT("Rental day count must be 1 or greater."), INCORRECT_DATE("Enter correct date MM/dd/yyyy"),
	RENTAL_DISCOUNT("Rental discount must be between 0 and 100"),
	TOOL_CODE_IS_NOT_PRESENT(" Tool with this code is not available. Please try again.");

	private String errorMessage;

	ErrorMessages(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
}
