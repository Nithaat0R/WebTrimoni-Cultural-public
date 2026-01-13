package com.aaronalcocer.webtrimoni_backend.dto;

public class CommentRequest {
  private Long idPatrimoni;
  private int rating;
  private String comment;
  private String date;

  public Long getIdPatrimoni() {
    return idPatrimoni;
  }
  public int getRating() {
    return rating;
  }
  public String getComment() {
    return comment;
  }
  public String getDate() {
    return date;
  }

}
