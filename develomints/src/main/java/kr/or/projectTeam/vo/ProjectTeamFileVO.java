package kr.or.projectTeam.vo;

import lombok.Data;

@Data
public class ProjectTeamFileVO {
	private int fileNo;
	private int memberNo;
	private int boardType;
	private int boardNo;
	private String fileName;
	private String filePath;
}
