package com.mediumBlog.Flowabledemo.serviceImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.flowable.task.api.Task;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mediumBlog.Flowabledemo.dao.TaskDetails;
import com.mediumBlog.Flowabledemo.entity.Complaint;
import com.mediumBlog.Flowabledemo.repo.FlowableRepo;
import com.mediumBlog.Flowabledemo.service.FlowableService;

@Service
public class FlowableServiceImpl implements FlowableService {

	@Autowired
	private RepositoryService repositoryService;

	@Autowired
	private RuntimeService runtimeService;
	@PersistenceContext
	private EntityManager entityManager;
	@Autowired
	private TaskService taskService;

	@Autowired
	private FlowableRepo flowableRepo;
	
	ProcessEngine processEngine;
	

	@Override
	public String makerRequest(String complain) {
		Map<String, Object> variables = new HashMap<String, Object>();
		String response = "";
		try {

			Deployment deployment = repositoryService.createDeployment()
					.addClasspathResource("flowableProcess.bpmn20.xml").deploy();

			ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("FlowableProcess", variables);
			Task task = taskService.createTaskQuery().processDefinitionKey("FlowableProcess")
					.processInstanceId(processInstance.getId()).singleResult();

			variables.put("RequestBy", "maker");
			variables.put("processId", processInstance.getProcessInstanceId() + "");
			variables.put("complain", complain);

			taskService.setVariables(task.getId(), variables);
			taskService.setVariable(task.getId(), "makerApproved", true);
			taskService.complete(task.getId());
			response = "Succesfully Done with processId :" + processInstance.getProcessInstanceId() + "";

		} catch (Exception e) {
			// TODO: handle exception
			response = "Error Occured While Maker Requesting :" + e.getMessage();

		}
		return response;
	}

	@Override
	public Map<String, Object> getCheckerPendings() {
		// TODO Auto-generated method stub
		Map<String, Object> var = new HashMap<String, Object>();

		try {

			List<Task> tasks = new ArrayList<Task>();

			tasks = taskService.createTaskQuery().processDefinitionKey("FlowableProcess").taskOwner("checker")
					.orderByTaskCreateTime().desc().list();

			List<TaskDetails> taskList = getTaskDetails(tasks);
			var.put("data", taskList);
			var.put("00", "Process Ok");
		} catch (Exception e) {
			var.put("01", "process failed" + e.getMessage());
		}
		return var;
	}

	@Override
	public String checkerReview(String processId, Boolean approve) {
		String response = "";
		try {
			Task task = taskService.createTaskQuery().processDefinitionKey("FlowableProcess").taskOwner("checker").processInstanceId(processId)
					.singleResult();
			if (task == null) {
				return response = "There is no task available with processId :" + processId;
			}
			// getting variable data which came from maker
			Map<String, Object> variables = taskService.getVariables(task.getId());
			String complain = (String) variables.get("complain");
			variables.put("reviewBy", "checker");
			taskService.setVariables(task.getId(), variables);
			taskService.setVariable(task.getId(), "checkerApproved", approve);
			taskService.complete(task.getId());
			response = "Checker Successfully reviewed";
			if(approve==true){
				Complaint complaint = new Complaint();
				complaint.setComplaint(variables.get("complain")+"");
 				complaint.setComplaintInitiator(variables.get("RequestBy")+"");
				complaint.setComplaintApprover(variables.get("reviewBy")+"");
				complaint.setDate(new Date());
				flowableRepo.save(complaint);
				}
		
		} catch (Exception e) {
			response = "Error Occured While Maker Requesting :" + e.getMessage();
		}

		return response;
	}

	@Override
	public Map<String, Object> getMakerReturnPendings() {
		// TODO Auto-generated method stub
		Map<String, Object> var = new HashMap<String, Object>();

		try {

			List<Task> tasks = new ArrayList<Task>();

			tasks = taskService.createTaskQuery().processDefinitionKey("FlowableProcess").taskOwner("maker")
					.orderByTaskCreateTime().desc().list();

			List<TaskDetails> taskList = getTaskDetails(tasks);
			var.put("data", taskList);
			var.put("00", "Process Ok");
		} catch (Exception e) {
			var.put("01", "process failed" + e.getMessage());
		}
		return var;
	}

	@Override
	public String makerReviewReturn(String complain,String processId, Boolean approve) {
		String response = "";
		try {
			Task task = taskService.createTaskQuery().processDefinitionKey("FlowableProcess").taskOwner("maker").processInstanceId(processId)
					.singleResult();
			if (task == null) {
				return 	response = "There is no task available with processId :" + processId;
			}
			
			
			
			// getting variable data which came from maker
			Map<String, Object> variables = taskService.getVariables(task.getId());
	        variables.put("complain", complain.equals("") ? variables.get("complain") : complain);
			 
			taskService.setVariables(task.getId(), variables);
			taskService.setVariable(task.getId(), "makerApproved", approve);
			taskService.complete(task.getId());
		
			response = "Maker Successfully reviewed";
		} catch (Exception e) {
			response = "Error Occured While Maker Requesting :" + e.getMessage();
		} 
		return response;
	} 
	public List<TaskDetails> getTaskDetails(List<Task> tasks) { 
		List<TaskDetails> taskDetail = new ArrayList<>(); 
		for (Task task : tasks) {
			Map<String, Object> variables = new HashMap<String, Object>();
			Map<String, Object> processVariable = taskService.getVariables(task.getId());
			taskDetail.add(new TaskDetails(task.getId(), task.getName(), task.getCreateTime(), processVariable));

		}

		return taskDetail;
	}
}
