package com.cczu.nosql.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "md_user")
public class User {
	@Id
	private Long id;

	private String name;

	private String password;

}
