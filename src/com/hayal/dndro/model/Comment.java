package com.hayal.dndro.model;

import java.util.List;

public class Comment {
	private String comment;
	private String author;
	private Integer vote;
	private Integer depth;
	private List<Comment> commentList;

	public Comment(String comment, String author, Integer vote, Integer depth) {
		this.comment = comment;
		this.author = author;
		this.vote = vote;
		this.depth = depth;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public Integer getVote() {
		return vote;
	}

	public void setVote(Integer vote) {
		this.vote = vote;
	}

	public Integer getDepth() {
		return depth;
	}

	public void setDepth(Integer depth) {
		this.depth = depth;
	}

	public List<Comment> getCommentList() {
		return commentList;
	}

	public void setCommentList(List<Comment> commentList) {
		this.commentList = commentList;
	}

}
