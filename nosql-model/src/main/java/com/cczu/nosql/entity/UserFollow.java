package com.cczu.nosql.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import io.ebean.annotation.Index;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Entity
@Data
@Table(
		name = "md_user_follow",
		uniqueConstraints = {
				@UniqueConstraint(
						name = "uk_user_follow_pair",
						columnNames = {"from_user_id", "to_user_id"}
				)
		}
)
@NoArgsConstructor
public class UserFollow extends BaseModel{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Index(name = "idx_user_follow_from_user_id")
	private Long fromUserId;

	@Index(name = "idx_user_follow_to_user_id")
	private Long toUserId;

	public UserFollow(Long fromUserId, Long toUserId) {
		this.fromUserId = fromUserId;
		this.toUserId = toUserId;
	}

}
