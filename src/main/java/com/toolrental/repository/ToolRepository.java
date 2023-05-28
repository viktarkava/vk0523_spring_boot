package com.toolrental.repository;

import org.springframework.data.repository.CrudRepository;

import com.toolrental.enity.Tool;

public interface ToolRepository extends CrudRepository<Tool, String> {

}
