package com.nabeel130.retropost;

public class Response{
	private Data data;
	private Boolean success;
	private Integer status;

	public void setData(Data data){
		this.data = data;
	}

	public Data getData(){
		return data;
	}

	public void setSuccess(Boolean success){
		this.success = success;
	}

	public Boolean isSuccess(){
		return success;
	}

	public void setStatus(Integer status){
		this.status = status;
	}

	public Integer getStatus(){
		return status;
	}
}
