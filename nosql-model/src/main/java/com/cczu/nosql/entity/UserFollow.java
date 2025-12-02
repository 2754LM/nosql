package com.cczu.nosql.entity;

import jakarta.persistence.*;
import lombok.*;
import io.ebean.annotation.Index;

@Entity
@Data
@Table(name = "md_user_follow")
@NoArgsConstructor
@AllArgsConstructor
public class UserFollow {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "from_user_id", foreignKey = @ForeignKey(name = "fk_uf_from_user"))
	private User fromUser;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "to_user_id", foreignKey = @ForeignKey(name = "fk_uf_to_user"))
	private User toUser;

	public UserFollow(Long fromUserId, Long toUserId) {
		this.fromUser = new User();
		this.fromUser.setId(fromUserId);
		this.toUser = new User();
		this.toUser.setId(toUserId);
	}

}
