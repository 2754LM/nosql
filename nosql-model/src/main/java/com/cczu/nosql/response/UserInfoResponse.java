package com.cczu.nosql.response;

import com.cczu.nosql.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoResponse {
	private Long userId;
	private String username;
	public UserInfoResponse(User user) {
		this.userId = user.getId();
		this.username = user.getName();
	}
}
