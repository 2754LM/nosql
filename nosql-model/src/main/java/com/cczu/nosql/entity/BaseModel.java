package com.cczu.nosql.entity;

import io.ebean.DB;
import io.ebean.annotation.WhenCreated;
import io.ebean.annotation.WhenModified;
import io.ebean.annotation.WhoCreated;
import io.ebean.annotation.WhoModified;
import lombok.Data;

import java.time.LocalDateTime;
@Data
public class BaseModel {
	// 创建人
	@WhoCreated
	protected Integer crtUser;

	// 创建时间
	@WhenCreated
	protected LocalDateTime crtTime;

	// 最新修改人
	@WhoModified
	protected Integer recUser;

	// 最新修改时间
	@WhenModified
	protected LocalDateTime recTime;

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
