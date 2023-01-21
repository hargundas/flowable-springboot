package com.mediumBlog.Flowabledemo.service;

import java.util.Map;

public interface FlowableService {

	public String makerRequest(String complain);
	public Map<String,Object> getCheckerPendings();
	public String checkerReview(String processId,Boolean review);
	public Map<String,Object> getMakerReturnPendings();
	public String makerReviewReturn(String complaincomplain,String processId, Boolean review);
	}
