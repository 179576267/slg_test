package com.douqu.game.auth.utils;

/**
 * 二元元祖
 * @author FXW
 * 2016年6月24日
 */
public class TwoTuple<A,B> {

	private  A first;
	private  B second;
	
	public TwoTuple() {
		super();
	}

	public void setSecond(B second) {
		this.second = second;
	}

	public void setFirst(A a){
		this.first = a;
	}

	public A getFirst() {
		return first;
	}

	public B getSecond() {
		return second;
	}
	
	
	
	
}
