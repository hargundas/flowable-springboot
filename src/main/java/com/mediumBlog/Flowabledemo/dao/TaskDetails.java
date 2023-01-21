package com.mediumBlog.Flowabledemo.dao;

import java.util.Date;
import java.util.Map;

public class TaskDetails {
	   String taskId;
	    String taskName;
	    Date updatedDate;
	    public Map<String, Object> taskData;

	    public TaskDetails(String taskId, String taskName, Date updatedDate, Map<String, Object> taskData) {
	        super();
	        this.taskId = taskId;
	        this.taskName = taskName;
	        this.updatedDate = updatedDate;
	        this.taskData = taskData;
	    }

	    public TaskDetails() {
	        super();
	        // TODO Auto-generated constructor stub
	    }

	    public String getTaskId() {
	        return taskId;
	    }

	    public void setTaskId(String taskId) {
	        this.taskId = taskId;
	    }

	    public String getTaskName() {
	        return taskName;
	    }

	    public void setTaskName(String taskName) {
	        this.taskName = taskName;
	    }

	    public Date getUpdatedDate() {
	        return updatedDate;
	    }

	    public void setUpdatedDate(Date updatedDate) {
	        this.updatedDate = updatedDate;
	    }

	    public Map<String, Object> getTaskData() {
	        return taskData;
	    }

	    public void setTaskData(Map<String, Object> taskData) {
	        this.taskData = taskData;
	    }

	    @Override
	    public String toString() {
	        return "TaskDetails [taskId=" + taskId + ", taskName=" + taskName + ", updatedDate=" + updatedDate
	                + ", taskData=" + taskData + "]";
	    }

}
