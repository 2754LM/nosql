package com.cczu.nosql.result;

import io.ebean.PagedList;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Schema(hidden = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageResult <T>{
	//记录列表
	@Schema(description = "记录列表")
	private List<T> records;
	//总记录数
	@Schema(description = "总记录数")
	private long total;
	//每页记录数
	@Schema(description = "每页记录数")
	private long size;
	//当前页，从1开始
	@Schema(description = "当前页，从1开始")
	private long current;
	//总页数
	@Schema(description = "总页数")
	private long pages;

	public static <E, T> PageResult<T> of(List<T> records, PagedList<E> pagedList) {
		return new PageResult<>(
				records,
				pagedList.getTotalCount(),
				pagedList.getPageSize(),
				pagedList.getPageIndex() + 1,
				pagedList.getTotalPageCount()
		);
	}
}
