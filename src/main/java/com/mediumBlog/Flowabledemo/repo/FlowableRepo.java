package com.mediumBlog.Flowabledemo.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mediumBlog.Flowabledemo.entity.Complaint;

public interface FlowableRepo extends JpaRepository<Complaint, Long>{

}
