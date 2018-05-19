package com.binpo.plugin.hibernate;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class IdEntity implements Serializable {
	@Id
	@Column(unique = true, nullable = false)
	private Long id;
	@Column(name = "addTime")
	private Date addTime;
	@Column(columnDefinition = "bit default false", name = "deleteStatus")
	private boolean deleteStatus;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getAddTime() {
		return addTime;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}

	public boolean isDeleteStatus() {
		return deleteStatus;
	}

	public void setDeleteStatus(boolean deleteStatus) {
		this.deleteStatus = deleteStatus;
	}

	/**
	 * 
	 * 获取时间字符串 2018-04-29 09:25:49
	 * 
	 * @return
	 */
	public String getAddTimeStr() {
		return DateFormat.formatLongDate(this.addTime);
	}

}
