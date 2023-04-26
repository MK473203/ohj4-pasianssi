package com.ohjelmointi4;

import java.time.LocalDateTime;

public class LeaderboardItem implements java.io.Serializable {

	public String name;
	public int gameSeconds;
	public int moves;
	public LocalDateTime dateTime;

	public LeaderboardItem(String name, int gameSeconds, int moves, LocalDateTime dateTime) {
		this.name = name;
		this.gameSeconds = gameSeconds;
		this.moves = moves;
		this.dateTime = dateTime;
	}
	
}
