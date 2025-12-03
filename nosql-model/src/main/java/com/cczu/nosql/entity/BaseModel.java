package com.cczu.nosql.entity;

import io.ebean.DB;
import io.ebean.annotation.*;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;

import java.time.LocalDateTime;
@Data
@MappedSuperclass
public class BaseModel {
	// 创建时间
	@WhenCreated
	private LocalDateTime crtTime;

	// 最新修改时间
	@WhenModified
	private LocalDateTime recTime;

	//是否删除
	@Column(columnDefinition = "boolean default false")
	private Boolean deleted = false;

	public void save() {
		DB.save(this);
	}

	public void update() {
		DB.update(this);
	}

	public void insert() {
		DB.insert(this);
	}

	public boolean delete() {
		return DB.delete(this);
	}

	public boolean deletePermanent() {
		return DB.deletePermanent(this);
	}
}
