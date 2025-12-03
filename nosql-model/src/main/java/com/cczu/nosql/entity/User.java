package com.cczu.nosql.entity;

import io.ebean.annotation.Index;
import io.ebean.annotation.NotNull;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@EqualsAndHashCode(callSuper = false)
@Entity
@Data
@Table(name = "md_user")
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseModel{
	@Id
	private Long id;

	private String name;

	private String password;

	@OneToMany(mappedBy = "fromUser", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<UserFollow> userFollows;

	@OneToMany(mappedBy = "toUser", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<UserFollow> followers;

	@OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Article> articles;
}
