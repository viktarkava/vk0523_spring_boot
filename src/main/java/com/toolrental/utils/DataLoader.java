package com.toolrental.utils;

import java.util.List;

import org.springframework.stereotype.Component;

import com.toolrental.enity.Tool;
import com.toolrental.repository.ToolRepository;

import jakarta.annotation.PostConstruct;

@Component
public class DataLoader {
	private final ToolRepository toolRepository;

	public DataLoader(ToolRepository coffeeRepository) {
		this.toolRepository = coffeeRepository;
	}

	@PostConstruct
	private void loadData() {
		toolRepository.saveAll(List.of(new Tool("CHNS", "Chainsaw", "Stihl", 1.49, true, false, true),
				new Tool("LADW", "Ladder", "Werner", 1.99, true, true, false),
				new Tool("JAKD", "Jackhammer", "DeWalt", 2.99, true, false, false),
				new Tool("JAKR", "Jackhammer", "Ridgid", 2.99, true, false, false)));
	}
}
