package com.mediumBlog.Flowabledemo.controller;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.flowable.engine.ProcessEngine;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mediumBlog.Flowabledemo.service.FlowableService;

@RestController
@RequestMapping("/api/")
public class Controller {
	@Autowired
	private FlowableService flowableService;

	/*
	 * This api is for to initiate a complain request and assigend to checker
	 */
	@PostMapping("maker/request/{complain}")
	public String makerRequest(@PathVariable("complain") String complaint) {
		String respons = flowableService.makerRequest(complaint);
		return respons;
	}

	/*
	 * This api is for get checker pending tasks which are assigned by maker
	 */
	@GetMapping("checker/pending/tasks")
	public Map<String, Object> checkerPending() {
		Map<String, Object> response = new HashMap<String, Object>();
		response = flowableService.getCheckerPendings();
		return response;
	}
	/*
	 * This api is for review the task which are pending at checker 
	 */
	@PostMapping("/checker/review/task/{processId}/{approve}")
	public String checkerReviewReview(@PathVariable("processId") String processId,
			@PathVariable("approve") Boolean approve) {
		String respons = flowableService.checkerReview(processId, approve);
		return respons;
	}

	/*
	 * This api is for get maker return pending tasks which are assigned and rejected by checker level
	 */
	@GetMapping("maker/return/pending/tasks")
	public Map<String, Object> getMakerReturnPendings() {
		Map<String, Object> response = new HashMap<String, Object>();
		response = flowableService.getMakerReturnPendings();
		return response;
	}
	
	/*
	 * This api is for review the maker return pending tasks which are pending at maker level 
	 */ 
	@PostMapping("/makerReview/{complain}/{processId}/{approve}")
	public String makerReviewReturn(@PathVariable("complain") String complain,
			@PathVariable("processId") String processId, @PathVariable("approve") Boolean approve) {
		String respons = flowableService.makerReviewReturn(complain, processId, approve);
		return respons;
	}

}
