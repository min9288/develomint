package kr.or.projectTeam.vo;

import lombok.Data;

@Data
public class ProjectReview {
	private int reviewNo;
	private int teamMemberNo;
	private int projectNo;
	private int reviewPoint;
	private String reviewContent;
	private int reviewWriter;
	private String enrollDate;
	private int memberNo;
}
