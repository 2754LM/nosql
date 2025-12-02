package com.cczu.nosql.entity;

import io.ebean.annotation.Index;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Entity
@Data
@Table(name = "md_user")
public class User extends BaseModel{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Index(name = "idx_user_name")
	@Column(nullable = false)
	private String name;

	private String password;
}
