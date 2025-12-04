package com.cczu.nosql.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "md_user_follow")
@NoArgsConstructor
@AllArgsConstructor
public class UserFollow extends BaseModel {
	@Id
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "from_user_id")
	private User fromUser;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "to_user_id")
	private User toUser;

	public UserFollow(Long fromUserId, Long toUserId) {
		this.fromUser = new User();
		this.fromUser.setId(fromUserId);
		this.toUser = new User();
		this.toUser.setId(toUserId);
	}

}
