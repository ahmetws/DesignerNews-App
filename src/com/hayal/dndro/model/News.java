package com.hayal.dndro.model;

import android.os.Parcel;
import android.os.Parcelable;

public class News implements Parcelable {
	private Integer id;
	private String title;
	private String author;
	private String url;
	private Integer commentCount;
	private Integer voteCount;

	public News(Parcel in) {
		this.id=in.readInt();
		this.title = in.readString();
		this.author = in.readString();
		this.url = in.readString();
		this.commentCount = in.readInt();
		this.voteCount = in.readInt();
	}

	public News(Integer id,String title, String author, String url, Integer commentCount,
			Integer voteCount) {
		this.id=id;
		this.title = title;
		this.author = author;
		this.url = url;
		this.commentCount = commentCount;
		this.voteCount = voteCount;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public Integer getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(Integer commentCount) {
		this.commentCount = commentCount;
	}

	public Integer getVoteCount() {
		return voteCount;
	}

	public void setVoteCount(Integer voteCount) {
		this.voteCount = voteCount;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeInt(id);
		out.writeString(title);
		out.writeString(author);
		out.writeString(url);
		out.writeInt(commentCount);
		out.writeInt(voteCount);
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public static final Parcelable.Creator<News> CREATOR = new Parcelable.Creator<News>() {
		public News createFromParcel(Parcel in) {
			return new News(in);
		}

		public News[] newArray(int size) {
			return new News[size];
		}
	};
}
