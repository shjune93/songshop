package com.userj.message;

// 상태 메시지를 model에 담거나 json으로 만들때 사용.
public class Result {

	public static final String MESSAGE_KEY = "detailMessage";
	private Boolean state; // ok, error
	private String detailMessage;

	public Result(String detailMessage) {
		this.detailMessage = detailMessage;
	}

	public Result(Boolean state, String detailMessage) {
		this.state = state;
		this.detailMessage = detailMessage;
	}

	public Boolean getState() {
		return state;
	}

	public String getDetailMessage() {
		return detailMessage;
	}

	@Override
	public String toString() {
		return detailMessage;
	}
}
