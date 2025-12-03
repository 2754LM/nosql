package com.cczu.nosql.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PageParam {
	@Min(1)
	private long current = 1;
	@Min(1)
	@Max(1000)
	private long size    = 20;
}
