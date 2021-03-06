package kr.or.projectTeam.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import kr.or.comment.vo.Report;
import kr.or.projectTeam.service.ProjectTeamService;
import kr.or.projectTeam.vo.DevelopLanguage;
import kr.or.projectTeam.vo.ProjectEntry;
import kr.or.projectTeam.vo.ProjectReview;
import kr.or.projectTeam.vo.ProjectTask;
import kr.or.projectTeam.vo.ProjectTaskViewData;
import kr.or.projectTeam.vo.ProjectTeam;
import kr.or.projectTeam.vo.ProjectTeamApplicantViewData;
import kr.or.projectTeam.vo.ProjectTeamApplyPageData;
import kr.or.projectTeam.vo.ProjectTeamMember;
import kr.or.projectTeam.vo.ProjectTeamNoticeViewData;
import kr.or.projectTeam.vo.Shortcuts;
import kr.or.projectTeam.vo.TaskShortcuts;
import kr.or.projectTeam.vo.projectDevLanguage;
import kr.or.projectTeam.vo.projectTeamMainPageData;


@Controller
public class ProjectTeamController {
	@Autowired
	private ProjectTeamService service;
	
	DateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd");
	Date nowDate = new Date();
	String today = sdFormat.format(nowDate);
	
	@RequestMapping(value="/recruitTeamMember_mainPage.do")
	public String recruitTeamMember(Model model, int reqPage) {
		int result1 = service.updateStatus();
		if(result1 > 0) {
			int result2 = service.projectStartProcess();
		}
		projectTeamMainPageData ptmpd = service.selectAllrecruitProject(reqPage);
		model.addAttribute("list", ptmpd.getList());
		model.addAttribute("pageNavi", ptmpd.getPageNavi());
		model.addAttribute("start", ptmpd.getStart());
		model.addAttribute("pdLangList", ptmpd.getPdLangList());
		model.addAttribute("developLangList", ptmpd.getDevelopLangList());
		return "recruitCrue/recruitTeamMember_mainPage";
	}
	
	@RequestMapping(value="/recruitTeamMember_mainSelectPage.do")
	public String recruitTeamMember(Model model, int reqPage, int viewValue, int checkValue, String[] langValue) {
		
		if(langValue == null) {
			projectTeamMainPageData ptmpd = service.selectAllrecruitSelectProject(reqPage, viewValue, checkValue);
			model.addAttribute("list", ptmpd.getList());
			model.addAttribute("pageNavi", ptmpd.getPageNavi());
			model.addAttribute("start", ptmpd.getStart());
			model.addAttribute("pdLangList", ptmpd.getPdLangList());
			model.addAttribute("developLangList", ptmpd.getDevelopLangList());
			model.addAttribute("viewValue", viewValue);
			model.addAttribute("checkValue", checkValue);
			model.addAttribute("selectLangList", langValue);
			return "recruitCrue/recruitTeamMember_mainPage";
		} else {
			ArrayList<String> langList = new ArrayList<String>(Arrays.asList(langValue));
			projectTeamMainPageData ptmpd = service.selectAllrecruitSelectProject(reqPage, viewValue, checkValue, langList);
			model.addAttribute("list", ptmpd.getList());
			model.addAttribute("pageNavi", ptmpd.getPageNavi());
			model.addAttribute("start", ptmpd.getStart());
			model.addAttribute("pdLangList", ptmpd.getPdLangList());
			model.addAttribute("developLangList", ptmpd.getDevelopLangList());
			model.addAttribute("viewValue", viewValue);
			model.addAttribute("checkValue", checkValue);
			model.addAttribute("selectLangList", langValue);
			return "recruitCrue/recruitTeamMember_mainPage";
		}
	}
	
	@RequestMapping(value="/recruitNotice_writeForm.do")
	public String recruitTeamMemberDatail(int memberNo, Model model) {
		ArrayList<DevelopLanguage> dlList = service.selectAllDevelopLang();
		if(memberNo > 0) {
			model.addAttribute("memberNo", memberNo);
			model.addAttribute("dlList", dlList);
			return "recruitCrue/recruitNotice_writeForm";
		} else {
			model.addAttribute("title", "????????? ??????");
			model.addAttribute("msg", "????????? ??? ????????? ???????????????.");
			model.addAttribute("icon", "warning");	
		}
		model.addAttribute("loc","/");
		return "member/swalMsg";
	}
	
	@RequestMapping(value="/rUploadImage.do")
	@ResponseBody
	public String rUploadImage( MultipartFile file, HttpServletRequest request) {
		 /*
			* String fileRoot = "C:\\summernote_image\\"; // ??????????????? ????????? ????????????.
		*/
		// ??????????????? ??????
		String saveRoot = request.getSession().getServletContext().getRealPath("/resources/upload/projectTeam/editor/");
		
		String originalFileName = file.getOriginalFilename(); 	//???????????? ?????????
		String onlyFilename = originalFileName.substring(0,originalFileName.indexOf("."));
		String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
		// ?????? ?????????
		String savedFileName = null;	//????????? ?????? ???
		int count=0;
		while(true) {
			if(count == 0 ) {
				savedFileName = onlyFilename+extension;
			}else {
				savedFileName = onlyFilename+"_"+count+extension;
			}
			File checkFile = new File(saveRoot+savedFileName);
			if(!checkFile.exists()) {
				break;
			}
			count++;
		}
		try {
			FileOutputStream fos = new FileOutputStream(new File(saveRoot + savedFileName));
			//????????? ??????????????? ?????? ???????????????
			BufferedOutputStream bos = new BufferedOutputStream(fos);
			//?????? ?????????
			byte[] bytes = file.getBytes();
			bos.write(bytes);
			bos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "/resources/upload/projectTeam/editor/"+savedFileName;
	}
	
	@RequestMapping(value="/writeRecruitTeam.do")
	public String writeRecruitTeam(HttpServletRequest request, Model model, ProjectTeam pt, int memberNo, String[] chk) {
		ArrayList<String> langList = new ArrayList<String>(Arrays.asList(chk));
		
		int result = service.writeRecruitTeam(pt, memberNo, langList);
		if(result > 0) {
			model.addAttribute("title", "?????? ??????");
			model.addAttribute("msg", "???????????? ?????? ?????????????????????.");
			model.addAttribute("icon", "success");
		} else {
			model.addAttribute("title", "?????? ??????");
			model.addAttribute("msg", "???????????? ?????? ??????[??????????????? ?????? ??????????????????.]");
			model.addAttribute("icon", "warning");
		}
		model.addAttribute("loc","/recruitTeamMember_mainPage.do?reqPage=1");
		return "member/swalMsg";
	}
	
	@RequestMapping(value="/selectOneNotice.do")
	public String selectOneNotice(Model model, Integer projectNo, Integer memberNo) {
		/*
		 * int result1 = service.updateStatus(); if(result1 > 0) { int result2 =
		 * service.projectStartProcess(); }
		 */
		if(memberNo == null) {
			memberNo = -1;
		}
		int entryNo = 0;
		int applyCheckValue = service.applyCheckValue(projectNo, memberNo);
		if(applyCheckValue > 0) {
			entryNo = service.searchEntryNo2(projectNo, memberNo);
		}
		ProjectTeamNoticeViewData ptnvd = service.selectOneNotice(projectNo, memberNo);
		model.addAttribute("commentList", ptnvd.getList());
		model.addAttribute("pt", ptnvd.getPt());
		model.addAttribute("pdLangList", ptnvd.getPdLangList());
		model.addAttribute("memberNo", memberNo);
		model.addAttribute("applyCheckValue",applyCheckValue);
		if(entryNo > 0) {
			model.addAttribute("entryNo",entryNo);
		}
		return "recruitCrue/recruitTeamMember_detail";
	}
	
	@RequestMapping(value="/updateRecruitFrm.do")
	public String updateOneNoticeFrm(Model model, int projectNo, Integer memberNo) {
		ArrayList<DevelopLanguage> dlList = service.selectAllDevelopLang();
		if(memberNo == null) {
			memberNo = -1;
		}
		ProjectTeamNoticeViewData ptnvd = service.selectOneNotice(projectNo, memberNo);
		model.addAttribute("commentList", ptnvd.getList());
		model.addAttribute("pt", ptnvd.getPt());
		model.addAttribute("pdLangList", ptnvd.getPdLangList());
		model.addAttribute("memberNo", memberNo);
		model.addAttribute("dlList", dlList);
		return "recruitCrue/recruitNoticeUpdateForm";
	}
	
	
	  @RequestMapping(value="/updateRecruitNotice.do") 
	  public String updateOneNotice(HttpServletRequest request, Model model, ProjectTeam pt, int memberNo, String[] chk, int projectNo) { 
		  ArrayList<String> langList = new ArrayList<String>(Arrays.asList(chk));
		  int result = service.updateRecruitTeam(pt, memberNo, langList, projectNo); 
		  if(result > 0) { 
			  model.addAttribute("title", "????????????!");
			  model.addAttribute("msg", "?????? ?????????????????????.");
			  model.addAttribute("loc","/selectOneNotice.do?projectNo="+projectNo+"&memberNo="+memberNo);
			  model.addAttribute("icon", "success");
		  } else {
			  model.addAttribute("title", "????????????");
			  model.addAttribute("msg", "?????? ?????????????????????.");
			  model.addAttribute("loc","/selectOneNotice.do?projectNo="+projectNo+"&memberNo="+memberNo);
			  model.addAttribute("icon", "warning");
		  }
		  return "member/swalMsg"; 
	  }
	  
	  @RequestMapping(value="/deleteRecruit.do") 
	  public String deleteOneNotice(Model model, int projectNo, int memberNo) { 
		  int result = service.deleteOneNotice(projectNo); 
		  if(result > 0) { 
			  model.addAttribute("title", "?????? ??????");
			  model.addAttribute("msg", "?????? ?????????????????????.");
			  model.addAttribute("loc","/recruitTeamMember_mainPage.do?reqPage=1");
			  model.addAttribute("icon", "success");
		  } else {
			  model.addAttribute("title", "?????? ??????");
			  model.addAttribute("msg", "?????? ?????????????????????.");
			  model.addAttribute("loc","/recruitTeamMember.do?projectNo="+projectNo+"&memberNo="+memberNo);
			  model.addAttribute("icon", "warning");
		  }
		  return "member/swalMsg"; 
	  }
	  
	  @RequestMapping(value="/insertComment.do") 
	  public String insertComment(Model model, String commentContent, int boardNo, String memberId, int memberNo, int boardType, int checkMemberNo) {
		  int entryNo = 0;
		  int result = 0;
		  if(boardType == 3) {
			  result = service.insertComment(commentContent, boardNo, memberId, boardType); 
		  }else {
				  entryNo = service.searchEntryNo(boardNo, memberNo);
				  result = service.insertComment(commentContent, boardNo, memberId, boardType, entryNo);
					/*
					 * if(checkMemberNo != memberNo) { } else { result =
					 * service.insertComment(commentContent, boardNo, memberId, boardType); }
					 */
		  }
		  if(result > 0) { 
			  model.addAttribute("title", "?????? ?????? ??????");
			  model.addAttribute("msg", "?????? ?????? ?????????????????????.");
			  if(boardType == 3) {
				  model.addAttribute("loc","/selectOneNotice.do?projectNo="+boardNo+"&memberNo="+memberNo);
			  } else {
				  entryNo = service.searchEntryNo(boardNo, memberNo);
				  model.addAttribute("loc","/selectOneApplicant.do?projectNo="+boardNo+"&memberNo="+memberNo+"&entryNo="+entryNo);
			  }
			  model.addAttribute("icon", "success");
		  } else {
			  model.addAttribute("title", "?????? ?????? ??????");
			  model.addAttribute("msg", "?????? ?????? ?????????????????????.");
			  if(boardType == 3) {
				  model.addAttribute("loc","/selectOneNotice.do?projectNo="+boardNo+"&memberNo="+memberNo);
			  } else {
				  entryNo = service.searchEntryNo(boardNo, memberNo);
				  model.addAttribute("loc","/selectOneApplicant.do?projectNo="+boardNo+"&memberNo="+memberNo+"&entryNo="+entryNo);
			  }
			  model.addAttribute("icon", "warning");
		  }
		  return "member/swalMsg"; 
	  }
	  
	  @ResponseBody
	  @RequestMapping(value="/deleteComment.do") 
	  public int deleteComment(Model model, int commentNo) {
		  int result = service.deleteComment(commentNo); 
		  return result; 
	  }
	  
	  @ResponseBody
	  @RequestMapping(value="/updateComment.do") 
	  public int updateComment(Model model, int commentNo, String commentContent) {
		  int result = service.updateComment(commentNo, commentContent); 
		  return result; 
	  }
	  
	  @RequestMapping(value="/reCommentInsert.do")   
	  public String reCommentInsert(Model model, String commentContent, int boardNo, String memberId, int memberNo, int commentNo, int boardType) {
		  int result = 0;
		  if(boardType == 3) {
			  result = service.reCommentInsert(commentContent, boardNo, memberId, commentNo, boardType); 
		  } else {
			  int entryNo = service.searchEntryNo(boardNo, memberNo);
			  result = service.reCommentInsert(commentContent, boardNo, memberId, commentNo, boardType, entryNo); 
		  }
		  if(result > 0) { 
			  model.addAttribute("title", "????????? ?????? ??????");
			  model.addAttribute("msg", "????????? ?????? ?????????????????????.");
			  if(boardType == 3) {
				  model.addAttribute("loc","/selectOneNotice.do?projectNo="+boardNo+"&memberNo="+memberNo);
			  } else {
				  int entryNo = service.searchEntryNo(boardNo, memberNo);
				  model.addAttribute("loc","/selectOneApplicant.do?projectNo="+boardNo+"&memberNo="+memberNo+"&entryNo="+entryNo);
			  }
			  model.addAttribute("icon", "success");
		  } else {
			  model.addAttribute("title", "????????? ?????? ??????");
			  model.addAttribute("msg", "????????? ?????? ?????????????????????.");
			  if(boardType == 3) {
				  model.addAttribute("loc","/selectOneNotice.do?projectNo="+boardNo+"&memberNo="+memberNo);
			  } else {
				  int entryNo = service.searchEntryNo(boardNo, memberNo);
				  model.addAttribute("loc","/selectOneApplicant.do?projectNo="+boardNo+"&memberNo="+memberNo+"&entryNo="+entryNo);
			  }
			  model.addAttribute("icon", "warning");
		  }
		  return "member/swalMsg"; 
	  }
	  
	  @RequestMapping(value="/insertDibCount.do")   
	  public String insertDibCount(Model model, int projectNo, int memberNo) {
		  int result = service.insertDibCount(projectNo, memberNo); 
		  if(result > 0) { 
			  model.addAttribute("title", "??? ?????? ??????");
			  model.addAttribute("msg", "?????? ???????????? ???????????????!");
			  model.addAttribute("loc","/selectOneNotice.do?projectNo="+projectNo+"&memberNo="+memberNo);
			  model.addAttribute("icon", "success");
		  } else {
			  model.addAttribute("title", "??? ?????? ??????");
			  model.addAttribute("msg", "??? ????????? ?????????????????????.");
			  model.addAttribute("loc","/selectOneNotice.do?projectNo="+projectNo+"&memberNo="+memberNo);
			  model.addAttribute("icon", "warning");
		  }
		  return "member/swalMsg"; 
	  }
	  
	  @RequestMapping(value="/deleteDibCount.do")   
	  public String deleteDibCount(Model model, int projectNo, int memberNo) {
		  int result = service.deleteDibCount(projectNo, memberNo); 
		  if(result > 0) { 
			  model.addAttribute("title", "??? ??????");
			  model.addAttribute("msg", "?????? ???????????? ?????? ????????? ??????????????????.");
			  model.addAttribute("loc","/selectOneNotice.do?projectNo="+projectNo+"&memberNo="+memberNo);
			  model.addAttribute("icon", "success");
		  } else {
			  model.addAttribute("title", "??? ?????? ??????");
			  model.addAttribute("msg", "??? ???????????? ?????? ?????????????????????.");
			  model.addAttribute("loc","/selectOneNotice.do?projectNo="+projectNo+"&memberNo="+memberNo);
			  model.addAttribute("icon", "warning");
		  }
		  return "member/swalMsg"; 
	  }
	  
	  @RequestMapping(value="/insertApplyProjectFrm.do")
		public String insertApplyProjectFrm(int memberNo, Model model, int projectNo, int writeReviewCheck) {
			ArrayList<DevelopLanguage> dlList = service.selectAllDevelopLang();
			if(memberNo > 0) {
				model.addAttribute("memberNo", memberNo);
				model.addAttribute("dlList", dlList);
				model.addAttribute("projectNo", projectNo);
				return "recruitCrue/applyTeam_writeForm";
			} else {
				model.addAttribute("title", "????????? ????????? ?????? ??????");
				model.addAttribute("msg", "????????? ??? ????????? ???????????????.");
				model.addAttribute("loc","/selectOneNotice.do?projectNo="+projectNo+"&memberNo="+memberNo);
				model.addAttribute("icon", "warning");
				return "member/swalMsg";
			}
		}
	  
	  @RequestMapping(value="/insertApplyProject.do")
		public String insertApplyProject(HttpServletRequest request, Model model, ProjectEntry pta, int memberNo, String[] chk, int projectNo) {
			ArrayList<String> langList = new ArrayList<String>(Arrays.asList(chk));
			
			int result = service.insertApplyProject(pta, langList, projectNo);
			if(result > 0) { 
				  model.addAttribute("title", "?????? ??????");
				  model.addAttribute("msg", "??????????????? ???????????? ????????? ?????????????????????.");
				  model.addAttribute("loc","/selectOneNotice.do?projectNo="+projectNo+"&memberNo="+memberNo);
				  model.addAttribute("icon", "success");
			  } else {
				  model.addAttribute("title", "?????? ??????");
				  model.addAttribute("msg", "???????????? ????????? ?????????????????????.");
				  model.addAttribute("loc","/selectOneNotice.do?projectNo="+projectNo+"&memberNo="+memberNo);
				  model.addAttribute("icon", "warning");
			  }
			  return "member/swalMsg"; 
		}
	  
	  @RequestMapping(value="/manageEntry.do")
		public String manageEntry(Model model, int reqPage, int viewValue, int memberNo, int projectNo) {
			
			ProjectTeamApplyPageData ptapd = service.selectAllManageEntry(reqPage, viewValue, projectNo);
			model.addAttribute("entryList", ptapd.getEntryList());
			model.addAttribute("pageNavi", ptapd.getPageNavi());
			model.addAttribute("start", ptapd.getStart());
			model.addAttribute("udLangList", ptapd.getUdLangList());
			model.addAttribute("developLangList", ptapd.getDevelopLangList());
			model.addAttribute("viewValue", viewValue);			
			model.addAttribute("availableNum", ptapd.getEntryList().get(0).getAvailableNum());
			model.addAttribute("memberNo", memberNo);
			model.addAttribute("projectNo", projectNo);
			return "recruitCrue/manageEntry";
			
		}
	  
	  @RequestMapping(value="/selectTeamMember.do")
		public String selectMember(Model model, int entryNo, int memberNo, int projectNo) {
			int result = service.selectMember(entryNo, memberNo);
			if(result > 0) { 
				  model.addAttribute("title", "?????? ???????????? ?????? ??????");
				  model.addAttribute("msg", "???????????? ??????????????? ?????? ???????????????????????? ?????????????????????.");
				  model.addAttribute("loc","/manageEntry.do?memberNo="+memberNo+"&projectNo="+projectNo+"&reqPage=1&viewValue=1");
				  model.addAttribute("icon", "success");
			  } else {
				  model.addAttribute("title", "???????????? ?????? ??????");
				  model.addAttribute("msg", "?????? ???????????????????????? ??????????????? ?????????????????????.");
				  model.addAttribute("loc","/manageEntry.do?memberNo="+memberNo+"&projectNo="+projectNo+"&reqPage=1&viewValue=1");
				  model.addAttribute("icon", "warning");
			  }
			  return "member/swalMsg"; 
		}
	  
	  @RequestMapping(value="/closeRecruitTeam.do")
	  public String closeRecruitTeam(Model model, int projectNo, int memberNo) {
		  int result = service.closeRecruitTeam(projectNo, memberNo);
			if(result > 0) { 
				  model.addAttribute("title", "?????? ?????? ??????");
				  model.addAttribute("msg", "?????? ??????????????? ???????????? ??????????????? ???????????????.");
				  model.addAttribute("loc","/selectOneNotice.do?projectNo="+projectNo+"&memberNo="+memberNo);
				  model.addAttribute("icon", "success");
			  } else {
				  model.addAttribute("title", "?????? ?????? ??????");
				  model.addAttribute("msg", "?????? ????????? ?????????????????????.");
				  model.addAttribute("loc","/selectOneNotice.do?projectNo="+projectNo+"&memberNo="+memberNo);
				  model.addAttribute("icon", "warning");
			  }
			  return "member/swalMsg"; 
	  }
	  
	  @RequestMapping(value="/deleteTeamMember.do")
	  public String deleteTeamMember(Model model, int projectNo, int memberNo, int entryNo) {
		  int result = service.deleteTeamMember(entryNo);
			if(result > 0) { 
				  model.addAttribute("title", "????????? ?????? ??????");
				  model.addAttribute("msg", "?????? ???????????? ?????????????????????. ");
				  model.addAttribute("loc","/manageEntry.do?memberNo="+memberNo+"&projectNo="+projectNo+"&reqPage=1&viewValue=1");
				  model.addAttribute("icon", "success");
			  } else {
				  model.addAttribute("title", "????????? ?????? ??????");
				  model.addAttribute("msg", "????????? ?????? ????????? ????????? ?????????????????????.");
				  model.addAttribute("loc","/manageEntry.do?memberNo="+memberNo+"&projectNo="+projectNo+"&reqPage=1&viewValue=1");
				  model.addAttribute("icon", "warning");
			  }
			  return "member/swalMsg"; 
	  }
	 
	  @RequestMapping(value="/manageFinalEntryFrm.do")
		public String manageFinalEntryFrm(Model model, int projectNo, int memberNo, int viewValue) {
		  	ProjectTeamApplyPageData ptapd = service.manageFinalEntryFrm(projectNo, viewValue);
			model.addAttribute("entryList", ptapd.getEntryList());
			model.addAttribute("udLangList", ptapd.getUdLangList());
			model.addAttribute("developLangList", ptapd.getDevelopLangList());
			model.addAttribute("availableNum", ptapd.getEntryList().get(0).getAvailableNum());
			model.addAttribute("memberNo", memberNo);
			model.addAttribute("projectNo", projectNo);
			model.addAttribute("viewValue", viewValue);	
			return "recruitCrue/manageFinalEntry";
		}
	  
	  @RequestMapping(value="/selectFinalTeamMember.do")
	  public String selectFinalTeamMember(Model model, int projectNo, int memberNo, int entryNo, int viewValue, int pageTransValue) {
		  int result = service.insertFinalTeamMember(entryNo, projectNo, memberNo);
			if(result > 0) { 
				  model.addAttribute("title", "????????? ?????? ??????");
				  model.addAttribute("msg", "?????? ???????????? ???????????? ???????????? ?????? ?????????????????????.");
				  if(pageTransValue - 1 == 0) {
					  model.addAttribute("loc","/manageEntry.do?memberNo="+memberNo+"&projectNo="+projectNo+"&reqPage=1&viewValue=1");
				  } else {
					  model.addAttribute("loc","/manageFinalEntryFrm.do?memberNo="+memberNo+"&projectNo="+projectNo+"&viewValue=1");
				  }
				  model.addAttribute("icon", "success");
			  } else {
				  model.addAttribute("title", "????????? ?????? ?????? ??????");
				  model.addAttribute("msg", "????????? ?????? ?????? ????????? ????????? ?????????????????????.");
				  if(pageTransValue - 1 == 0) {
					  model.addAttribute("loc","/manageEntry.do?memberNo="+memberNo+"&projectNo="+projectNo+"&reqPage=1&viewValue=1");
				  } else {
					  model.addAttribute("loc","/manageFinalEntryFrm.do?memberNo="+memberNo+"&projectNo="+projectNo+"&viewValue=1");
				  }
				  model.addAttribute("icon", "warning");
			  }
			  return "member/swalMsg"; 
	  }
	  
	  @RequestMapping(value="/returnTeamMember.do")
	  public String returnTeamMember(Model model, int projectNo, int memberNo, int entryNo, int viewValue, int pageTransValue) {
		  int result = service.returnTeamMember(entryNo);
			if(result > 0) { 
				  model.addAttribute("title", "?????? ????????? ??????");
				  model.addAttribute("msg", "?????? ???????????? ????????? ???????????? ?????? ???????????? ??? ????????????.");
				  if(pageTransValue - 1 == 0) {
					  model.addAttribute("loc","/manageEntry.do?memberNo="+memberNo+"&projectNo="+projectNo+"&reqPage=1&viewValue=1");
				  } else {
					  model.addAttribute("loc","/manageFinalEntryFrm.do?memberNo="+memberNo+"&projectNo="+projectNo+"&viewValue=1");
				  }
				  model.addAttribute("icon", "success");
			  } else {
				  model.addAttribute("title", "????????? ?????? ??????");
				  model.addAttribute("msg", "????????? ?????? ???????????? ????????? ?????????????????????.");
				  if(pageTransValue - 1 == 0) {
					  model.addAttribute("loc","/manageEntry.do?memberNo="+memberNo+"&projectNo="+projectNo+"&reqPage=1&viewValue=1");
				  } else {
					  model.addAttribute("loc","/manageFinalEntryFrm.do?memberNo="+memberNo+"&projectNo="+projectNo+"&viewValue=1");
				  }
				  model.addAttribute("icon", "warning");
			  }
			  return "member/swalMsg"; 
	  }
	  
	  @RequestMapping(value="/selectOneApplicant.do")
		public String selectOneApplicant(Model model, int projectNo, int memberNo, int entryNo) {
			ProjectTeamApplicantViewData ptavd = service.selectOneApplicant(entryNo);
			model.addAttribute("commentList", ptavd.getCommentList());
			model.addAttribute("pe", ptavd.getPe());
			model.addAttribute("udlList", ptavd.getUdlList());
			model.addAttribute("entryNo", entryNo);
			model.addAttribute("memberNo", memberNo);
			model.addAttribute("projectNo", projectNo);
			return "recruitCrue/applicant_detail";
		}
	  
	  @RequestMapping(value="/cancelApplyProject.do")
		public String cancelApplyProject(Model model, int entryNo, int memberNo, int projectNo, int applicantNo) {
			int result = service.cancelApplyProject(entryNo, applicantNo, projectNo);
			if(result > 0) { 
				  model.addAttribute("title", "?????? ??????");
				  model.addAttribute("msg", "???????????? ????????? ?????????????????????.");
				  model.addAttribute("loc","/recruitTeamMember_mainPage.do?reqPage=1");
				  model.addAttribute("icon", "success");
			  } else {
				  model.addAttribute("title", "?????? ?????? ??????");
				  model.addAttribute("msg", "????????? ????????? ?????? ?????? ??????????????? ????????? ?????????????????????. ");
				  model.addAttribute("loc","/selectOneApplicant.do?projectNo="+projectNo+"&memberNo="+memberNo+"&entryNo="+entryNo);
				  model.addAttribute("icon", "warning");
			  }
			  return "member/swalMsg"; 
		}
	  
	  @RequestMapping(value="/updateProjectApplyFrm.do")
		public String updateProjectApplyFrm(Model model, int projectNo, int memberNo, int entryNo) {
			ProjectTeamApplicantViewData ptavd = service.selectOneApplicant(entryNo);
			ArrayList<DevelopLanguage> dlList = service.selectAllDevelopLang();
			model.addAttribute("dlList", dlList);
			model.addAttribute("commentList", ptavd.getCommentList());
			model.addAttribute("pe", ptavd.getPe());
			model.addAttribute("udlList", ptavd.getUdlList());
			model.addAttribute("entryNo", entryNo);
			model.addAttribute("memberNo", memberNo);
			model.addAttribute("projectNo", projectNo);
			return "recruitCrue/applyTeamUpdateForm";
		}
	  
	  @RequestMapping(value="/updateApplyForm.do") 
	  public String updateApplyForm(HttpServletRequest request, Model model, ProjectEntry pe, String[] chk, int projectNo, int entryNo, int sessionMemberNo) { 
		  ArrayList<String> langList = new ArrayList<String>(Arrays.asList(chk));
		  int result = service.updateApplyForm(pe, langList, projectNo); 
		  if(result > 0) { 
			  model.addAttribute("title", "?????? ??????!");
			  model.addAttribute("msg", "?????? ?????????????????????.");
			  model.addAttribute("loc","/selectOneApplicant.do?projectNo="+projectNo+"&memberNo="+sessionMemberNo+"&entryNo="+entryNo);
			  model.addAttribute("icon", "success");
		  } else {
			  model.addAttribute("title", "?????? ??????");
			  model.addAttribute("msg", "?????? ?????????????????????.");
			  model.addAttribute("loc","/selectOneApplicant.do?projectNo="+projectNo+"&memberNo="+sessionMemberNo+"&entryNo="+entryNo);
			  model.addAttribute("icon", "warning");
		  }
		  return "member/swalMsg"; 
	  }
	  
	  @RequestMapping(value="/enterMyProject.do") 
	  public String enterMyProject(Model model, int projectNo, int memberNo ) {
		  	ProjectTeam pt = service.projectInfo(projectNo, memberNo);
		  	ArrayList<ProjectTeamMember> memberList = service.memberInfoList(projectNo, memberNo);
		  	ProjectTask recentTask = service.recentTask(projectNo);
		  	ProjectTask toDoTask = service.toDoTask(projectNo);
		  	ArrayList<projectDevLanguage> pdLangList = service.selectAllprojectLangList();
		  	ArrayList<DevelopLanguage> dlList = service.selectAllDevelopLang();
		  	ArrayList<Shortcuts> scList = service.shortcutList(projectNo);
		  	
		  	ArrayList<ProjectTask> allPtk = service.projectTaskList(projectNo);
		  	for(int i = 0; i < allPtk.size(); i++) {
		  		int compare = today.compareTo(allPtk.get(i).getStartDate());
		  		if(compare <= 0 && allPtk.get(i).getProcessSort() == 1) {
		  			int updateResult = service.taskDateUpdate(allPtk.get(i));
		  			if(updateResult < 0) {
		  				model.addAttribute("title", "?????? ???????????? ??????");
		  			  model.addAttribute("loc","enterMyProject.do?projectNo="+projectNo+"&memberNo="+memberNo);
		  			  model.addAttribute("icon", "warning");
		  			  return "member/swalMsg";
		  			}
		  		}
		  	}
		  	
		  	model.addAttribute("pt", pt);
			model.addAttribute("memberList", memberList);
			model.addAttribute("recentTask", recentTask);
			model.addAttribute("toDoTask", toDoTask);
			model.addAttribute("pdLangList", pdLangList);
			model.addAttribute("dlList", dlList);
			model.addAttribute("scList", scList);
			return "recruitCrue/projectManageOutline";
	  }
	  
	  @RequestMapping(value="/addShortcut.do")
	  public String addShortcut(Model model, String shortcutAddr, String shortcutName, Integer projectNo, int memberNo ) {
		  int result = service.addShortcut(shortcutAddr, shortcutName, projectNo); 
		  if(result > 0) { 
			  model.addAttribute("title", "???????????? ?????? ??????!");
			  model.addAttribute("msg", "???????????? ????????? ??????????????? ?????????????????????.");
			  model.addAttribute("loc","enterMyProject.do?projectNo="+projectNo+"&memberNo="+memberNo);
			  model.addAttribute("icon", "success");
		  } else {
			  model.addAttribute("title", "???????????? ?????? ??????");
			  model.addAttribute("msg", "????????? ????????? ?????? ?????????????????????.");
			  model.addAttribute("loc","enterMyProject.do?projectNo="+projectNo+"&memberNo="+memberNo);
			  model.addAttribute("icon", "warning");
		  }
		  return "member/swalMsg"; 
	  }
	  
	  @RequestMapping(value="/deleteShortcut.do")
	  public String deleteShortcut(Model model, int shortcutNo, Integer projectNo, int memberNo ) {
		  int result = service.deleteShortcut(shortcutNo); 
		  if(result > 0) { 
			  model.addAttribute("title", "???????????? ?????? ??????!");
			  model.addAttribute("msg", "???????????? ????????? ??????????????? ?????????????????????.");
			  model.addAttribute("loc","enterMyProject.do?projectNo="+projectNo+"&memberNo="+memberNo);
			  model.addAttribute("icon", "success");
		  } else {
			  model.addAttribute("title", "???????????? ?????? ??????");
			  model.addAttribute("msg", "????????? ????????? ?????? ?????????????????????.");
			  model.addAttribute("loc","enterMyProject.do?projectNo="+projectNo+"&memberNo="+memberNo);
			  model.addAttribute("icon", "warning");
		  }
		  return "member/swalMsg"; 
	  }
	  
	  @RequestMapping(value="/endProject.do")
	  public String endProject(Model model, Integer[] memberNo, Integer[] teamMemberNo, Integer[] reviewPoint, String[] reviewContent, int backMemberNo, int backProjectNo, String sessionMemberId, String projectReader) {
		  int result = 0;
		  ArrayList<ProjectReview> reviewlist = new ArrayList<ProjectReview>();
		  for(int i=0;i<teamMemberNo.length;i++) {
			  ProjectReview pr = new ProjectReview();
			  pr.setProjectNo(backProjectNo);
			  pr.setReviewContent(reviewContent[i]);
			  pr.setReviewPoint(reviewPoint[i]);
			  pr.setTeamMemberNo(teamMemberNo[i]);
			  pr.setReviewWriter(backMemberNo);
			  pr.setMemberNo(memberNo[i]);
			  reviewlist.add(pr);
		  }
		  if(sessionMemberId.equals(projectReader)) {
			  result = service.endProject(reviewlist, backProjectNo, backMemberNo);
			  if(result > 0) { 
				  model.addAttribute("title", "???????????? ??????");
				  model.addAttribute("msg", "??? ?????? ???????????? ????????????????????????..!");
				  model.addAttribute("loc","/enterMyProject.do?projectNo="+backProjectNo+"&memberNo="+backMemberNo);
				  model.addAttribute("icon", "success");
			  } else {
				  model.addAttribute("title", "???????????? ?????? ??????");
				  model.addAttribute("msg", "????????? ????????? ?????? ?????????????????????.");
				  model.addAttribute("loc","/enterMyProject.do?projectNo="+backProjectNo+"&memberNo="+backMemberNo);
				  model.addAttribute("icon", "warning");
			  }
			  return "member/swalMsg"; 
		  } else {
			  result = service.insertReview(reviewlist, backProjectNo, backMemberNo);
			  if(result > 0) { 
				  model.addAttribute("title", "?????? ?????? ??????!");
				  model.addAttribute("msg", "??? ?????? ???????????? ????????????????????????..!");
				  model.addAttribute("loc","/enterMyProject.do?projectNo="+backProjectNo+"&memberNo="+backMemberNo);
				  model.addAttribute("icon", "success");
			  } else {
				  model.addAttribute("title", "?????? ?????? ??????");
				  model.addAttribute("msg", "????????? ????????? ?????? ?????????????????????.");
				  model.addAttribute("loc","/enterMyProject.do?projectNo="+backProjectNo+"&memberNo="+backMemberNo);
				  model.addAttribute("icon", "warning");
			  }
			  return "member/swalMsg"; 
		  }
		   
	  }
	
	  @RequestMapping(value="/updateProjectOutline.do")
	  public String updateProjectOutline(Model model, String[] chk, ProjectTeam pt, String crueRoll, int sessionMemberNo, int projectNo) {
		  ArrayList<String> langList = new ArrayList<String>();
		  if(chk != null) {
			  langList = new ArrayList<String>(Arrays.asList(chk));
		  }
		  int result = service.updateProjectOutline(langList, pt, crueRoll, sessionMemberNo, projectNo); 
		  if(result > 0) { 
			  model.addAttribute("title", "???????????? ?????? ??????!");
			  model.addAttribute("msg", "???????????? ???????????? ????????? ?????????????????????.");
			  model.addAttribute("loc","/enterMyProject.do?projectNo="+projectNo+"&memberNo="+sessionMemberNo);
			  model.addAttribute("icon", "success");
		  } else {
			  model.addAttribute("title", "???????????? ?????? ??????");
			  model.addAttribute("msg", "????????? ????????? ?????? ?????????????????????.");
			  model.addAttribute("loc","/enterMyProject.do?projectNo="+projectNo+"&memberNo="+sessionMemberNo);
			  model.addAttribute("icon", "warning");
		  }
		  return "member/swalMsg"; 
	  }
	  
		/*
		 * @RequestMapping(value="/enterProjectTask.do") public String
		 * enterProjectTask(Model model, int projectNo) { ArrayList<ProjectTask> ptk =
		 * service.projectTaskList(projectNo); model.addAttribute("ptk", ptk);
		 * model.addAttribute("projectNo", projectNo); return
		 * "recruitCrue/projectManageTask"; }
		 */
	  
	  @RequestMapping(value="/enterProjectTaskM.do")
		public String enterProjectTaskM(Model model, int projectNo, int reqPage) {
		  	ProjectTaskViewData ptvd = service.enterProjectTaskM(projectNo, reqPage);
		  	ArrayList<ProjectTask> allPtk = service.projectTaskList(projectNo);
		  	ArrayList<Shortcuts> scList = service.shortcutList(projectNo);
		  	ArrayList<TaskShortcuts> tscList = service.taskShortcutList(projectNo);
		  	
		  	model.addAttribute("ptk", ptvd.getTasklist());
		  	model.addAttribute("ptkk", ptvd.getTasklist());
			model.addAttribute("projectNo", projectNo);
			model.addAttribute("pageNavi", ptvd.getPageNavi());
			model.addAttribute("start", ptvd.getStart());
			model.addAttribute("ptm", ptvd.getPtmList());
			model.addAttribute("ptmGet0", ptvd.getPtmList().get(0));
			model.addAttribute("scList", scList);
			model.addAttribute("allPtk", allPtk);
			model.addAttribute("allPtkk", allPtk);
			model.addAttribute("tscList", tscList);
			return "recruitCrue/projectManageTaskM";
		}
	  
	  @RequestMapping(value="/addIssue.do")
	  public String addIssue(Model model, int projectNo, int memberNo, int taskType, String issueTitle, String taskStartDate) {
		   int compare = today.compareTo(taskStartDate);
		   if(compare > 0) {
			   	model.addAttribute("title", "?????? ?????? ??????");
				model.addAttribute("msg", "???????????? ???????????? ???????????? ????????? ??? ????????????. ");
				model.addAttribute("icon", "warning");
				if(taskType == 1) {
					  model.addAttribute("loc","/enterProjectTaskM.do?projectNo="+projectNo+"&reqPage=1");
			  } else if(taskType == 2) {
					  model.addAttribute("loc","/enterProjectTaskT.do?projectNo="+projectNo+"&reqPage=1");
			  } else if(taskType == 3) {
					  model.addAttribute("loc","/enterProjectTaskB.do?projectNo="+projectNo+"&reqPage=1");
			  }
		   } else if (compare == 0) {
			   int result = service.addIssueToday(projectNo, memberNo, taskType, issueTitle, taskStartDate); 
			     if(result > 0) { 
					  model.addAttribute("title", "?????? ?????? ??????!");
					  model.addAttribute("msg", "????????? ?????? ???????????????.");
					  model.addAttribute("icon", "success");
				  } else {
					  model.addAttribute("title", "?????? ?????? ??????");
					  model.addAttribute("msg", "????????? ????????? ?????? ?????????????????????.");
					  model.addAttribute("icon", "warning");
				  }
				  if(taskType == 1) {
						  model.addAttribute("loc","/enterProjectTaskM.do?projectNo="+projectNo+"&reqPage=1");
				  } else if(taskType == 2) {
						  model.addAttribute("loc","/enterProjectTaskT.do?projectNo="+projectNo+"&reqPage=1");
				  } else if(taskType == 3) {
						  model.addAttribute("loc","/enterProjectTaskB.do?projectNo="+projectNo+"&reqPage=1");
				  }
		   } else {
			   int result = service.addIssue(projectNo, memberNo, taskType, issueTitle, taskStartDate); 
			  if(result > 0) { 
				  model.addAttribute("title", "?????? ?????? ??????!");
				  model.addAttribute("msg", "????????? ?????? ???????????????.");
				  model.addAttribute("icon", "success");
			  } else {
				  model.addAttribute("title", "?????? ?????? ??????");
				  model.addAttribute("msg", "????????? ????????? ?????? ?????????????????????.");
				  model.addAttribute("icon", "warning");
			  }
			  if(taskType == 1) {
				  model.addAttribute("loc","/enterProjectTaskM.do?projectNo="+projectNo+"&reqPage=1");
			  } else if(taskType == 2) {
				  model.addAttribute("loc","/enterProjectTaskT.do?projectNo="+projectNo+"&reqPage=1");
			  } else if(taskType == 3) {
				  model.addAttribute("loc","/enterProjectTaskB.do?projectNo="+projectNo+"&reqPage=1");
			  } 
		   }
		   return "member/swalMsg"; 
		 }
	  
	@RequestMapping(value="/updateIssue.do")
	  public String updateIssue(Model model, String taskNo, int projectNo,int taskType, String modalcontent, String selectCharUser, int selectPriority, int selectProcessSort) {
		int result = service.updateIssue(taskNo, projectNo, taskType, modalcontent, selectCharUser, selectPriority, selectProcessSort);
		  if(result > 0) { 
			  model.addAttribute("title", "???????????? ?????? ??????!");
			  model.addAttribute("msg", "???????????? ???????????? ????????? ?????????????????????.");
			  model.addAttribute("icon", "success");
		  } else {
			  model.addAttribute("title", "???????????? ?????? ??????");
			  model.addAttribute("msg", "????????? ????????? ?????? ?????????????????????.");
			  model.addAttribute("icon", "warning");
		  }
		  if(taskType == 1) {
			  model.addAttribute("loc","/enterProjectTaskM.do?projectNo="+projectNo+"&reqPage=1");
		  } else if(taskType == 2) {
			  model.addAttribute("loc","/enterProjectTaskT.do?projectNo="+projectNo+"&reqPage=1");
		  } else if(taskType == 3) {
			  model.addAttribute("loc","/enterProjectTaskB.do?projectNo="+projectNo+"&reqPage=1");
		  } else {
			  model.addAttribute("loc","/enterProjectTaskH.do?projectNo="+projectNo+"&reqPage=1");
		  }
		  return "member/swalMsg"; 
	  }
	
	@ResponseBody
	@RequestMapping(value="/connectIssue.do")
	  public String connectIssue(Model model, String taskNo, String connectIssue) {
		int result = service.connectIssue(taskNo, connectIssue);
		String realResult = "-1";
		if(result > 0) {
			realResult = taskNo;
		}
		  return realResult; 
	}
	
	@RequestMapping(value="/deleteConnectIssue.do")
	  public String deleteConnectIssue(Model model, String taskNo, int projectNo,int taskType) {
		int result = service.deleteConnectIssue(taskNo);
		  if(result > 0) { 
			  model.addAttribute("title", "?????? ??????");
			  model.addAttribute("msg", "?????? ????????? ????????? ?????????????????????.");
			  model.addAttribute("icon", "success");
		  } else {
			  model.addAttribute("title", "?????? ?????? ??????");
			  model.addAttribute("msg", "????????? ????????? ?????? ?????????????????????.");
			  model.addAttribute("icon", "warning");
		  }
		  if(taskType == 1) {
			  model.addAttribute("loc","/enterProjectTaskM.do?projectNo="+projectNo+"&reqPage=1");
		  } else if(taskType == 2) {
			  model.addAttribute("loc","/enterProjectTaskT.do?projectNo="+projectNo+"&reqPage=1");
		  } else if(taskType == 3) {
			  model.addAttribute("loc","/enterProjectTaskB.do?projectNo="+projectNo+"&reqPage=1");
		  } else {
			  model.addAttribute("loc","/enterProjectTaskH.do?projectNo="+projectNo+"&reqPage=1");
		  }
		  return "member/swalMsg"; 
	  }
	
	@RequestMapping(value="/connectLink.do")
	  public String connectLink(Model model, String taskNo, int projectNo, int taskType, String shortcutName, String shortcutAddr) {
		int result = service.connectLink(taskNo, shortcutName, shortcutAddr, projectNo);
		  if(result > 0) { 
			  model.addAttribute("title", "?????? ??????");
			  model.addAttribute("msg", "????????? ?????? ???????????????.");
			  model.addAttribute("icon", "success");
		  } else {
			  model.addAttribute("title", "?????? ?????? ??????");
			  model.addAttribute("msg", "?????? ?????? ?????????????????????.");
			  model.addAttribute("icon", "warning");
		  }
		  if(taskType == 1) {
			  model.addAttribute("loc","/enterProjectTaskM.do?projectNo="+projectNo+"&reqPage=1");
		  } else if(taskType == 2) {
			  model.addAttribute("loc","/enterProjectTaskT.do?projectNo="+projectNo+"&reqPage=1");
		  } else if(taskType == 3) {
			  model.addAttribute("loc","/enterProjectTaskB.do?projectNo="+projectNo+"&reqPage=1");
		  } else {
			  model.addAttribute("loc","/enterProjectTaskH.do?projectNo="+projectNo+"&reqPage=1");
		  }
		  return "member/swalMsg"; 
	  }
	
	@RequestMapping(value="/deleteConnectLink.do")
	  public String deleteConnectLink(Model model, int taskShortcutNo, int projectNo, int taskType) {
		int result = service.deleteConnectLink(taskShortcutNo);
		  if(result > 0) { 
			  model.addAttribute("title", "?????? ??????");
			  model.addAttribute("msg", "?????? ????????? ????????? ?????????????????????.");
			  model.addAttribute("icon", "success");
		  } else {
			  model.addAttribute("title", "?????? ?????? ??????");
			  model.addAttribute("msg", "????????? ????????? ?????? ?????????????????????.");
			  model.addAttribute("icon", "warning");
		  }
		  if(taskType == 1) {
			  model.addAttribute("loc","/enterProjectTaskM.do?projectNo="+projectNo+"&reqPage=1");
		  } else if(taskType == 2) {
			  model.addAttribute("loc","/enterProjectTaskT.do?projectNo="+projectNo+"&reqPage=1");
		  } else if(taskType == 3) {
			  model.addAttribute("loc","/enterProjectTaskB.do?projectNo="+projectNo+"&reqPage=1");
		  } else {
			  model.addAttribute("loc","/enterProjectTaskH.do?projectNo="+projectNo+"&reqPage=1");
		  }
		  return "member/swalMsg"; 
	  }
	
	@RequestMapping(value="/deleteTask.do")
	  public String deleteTask(Model model, String taskNo, int projectNo, int taskType) {
		int result = service.deleteTask(taskNo);
		  if(result > 0) { 
			  model.addAttribute("title", "?????? ??????");
			  model.addAttribute("msg", "?????? ????????? ?????????????????????.");
			  model.addAttribute("icon", "success");
		  } else {
			  model.addAttribute("title", "?????? ?????? ??????");
			  model.addAttribute("msg", "????????? ????????? ?????? ?????????????????????.");
			  model.addAttribute("icon", "warning");
		  }
		  if(taskType == 1) {
			  model.addAttribute("loc","/enterProjectTaskM.do?projectNo="+projectNo+"&reqPage=1");
		  } else if(taskType == 2) {
			  model.addAttribute("loc","/enterProjectTaskT.do?projectNo="+projectNo+"&reqPage=1");
		  } else if(taskType == 3) {
			  model.addAttribute("loc","/enterProjectTaskB.do?projectNo="+projectNo+"&reqPage=1");
		  } else {
			  model.addAttribute("loc","/enterProjectTaskH.do?projectNo="+projectNo+"&reqPage=1");
		  }
		  return "member/swalMsg"; 
	  }
	
	@RequestMapping(value="/enterTaskMSelect.do")
	public String enterTaskMSelect(Model model, int reqPage, int viewValue, int checkValue, int projectNo, int teamMemberNo) {
		
		ProjectTaskViewData ptvd = service.enterProjectTaskSelectM(projectNo, reqPage, viewValue, checkValue, teamMemberNo);
	  	ArrayList<ProjectTask> allPtk = service.projectTaskList(projectNo);
	  	ArrayList<Shortcuts> scList = service.shortcutList(projectNo);
	  	ArrayList<TaskShortcuts> tscList = service.taskShortcutList(projectNo);
	  	
	  	model.addAttribute("ptk", ptvd.getTasklist());
	  	model.addAttribute("ptkk", ptvd.getTasklist());
		model.addAttribute("projectNo", projectNo);
		model.addAttribute("pageNavi", ptvd.getPageNavi());
		model.addAttribute("start", ptvd.getStart());
		model.addAttribute("ptm", ptvd.getPtmList());
		model.addAttribute("ptmGet0", ptvd.getPtmList().get(0));
		model.addAttribute("scList", scList);
		model.addAttribute("allPtk", allPtk);
		model.addAttribute("tscList", tscList);
		model.addAttribute("allPtkk", allPtk);
		model.addAttribute("viewValue", viewValue);
		model.addAttribute("checkValue", checkValue);
		return "recruitCrue/projectManageTaskM";
	}
	
	@RequestMapping(value="/enterProjectTaskT.do")
	public String enterProjectTaskT(Model model, int projectNo, int reqPage) {
	  	ProjectTaskViewData ptvd = service.enterProjectTaskT(projectNo, reqPage);
	  	ArrayList<ProjectTask> allPtk = service.projectTaskList(projectNo);
	  	ArrayList<Shortcuts> scList = service.shortcutList(projectNo);
	  	ArrayList<TaskShortcuts> tscList = service.taskShortcutList(projectNo);
	  	
	  	model.addAttribute("ptk", ptvd.getTasklist());
	  	model.addAttribute("ptkk", ptvd.getTasklist());
		model.addAttribute("projectNo", projectNo);
		model.addAttribute("pageNavi", ptvd.getPageNavi());
		model.addAttribute("start", ptvd.getStart());
		model.addAttribute("ptm", ptvd.getPtmList());
		model.addAttribute("ptmGet0", ptvd.getPtmList().get(0));
		model.addAttribute("scList", scList);
		model.addAttribute("allPtk", allPtk);
		model.addAttribute("allPtkk", allPtk);
		model.addAttribute("tscList", tscList);
		return "recruitCrue/projectManageTaskT";
	}
	
	@RequestMapping(value="/enterTaskTSelect.do")
	public String enterTaskTSelect(Model model, int reqPage, int viewValue, int checkValue, int projectNo, int teamMemberNo) {
		
		ProjectTaskViewData ptvd = service.enterProjectTaskSelectT(projectNo, reqPage, viewValue, checkValue, teamMemberNo);
	  	ArrayList<ProjectTask> allPtk = service.projectTaskList(projectNo);
	  	ArrayList<Shortcuts> scList = service.shortcutList(projectNo);
	  	ArrayList<TaskShortcuts> tscList = service.taskShortcutList(projectNo);
	  	
	  	model.addAttribute("ptk", ptvd.getTasklist());
	  	model.addAttribute("ptkk", ptvd.getTasklist());
		model.addAttribute("projectNo", projectNo);
		model.addAttribute("pageNavi", ptvd.getPageNavi());
		model.addAttribute("start", ptvd.getStart());
		model.addAttribute("ptm", ptvd.getPtmList());
		model.addAttribute("ptmGet0", ptvd.getPtmList().get(0));
		model.addAttribute("scList", scList);
		model.addAttribute("allPtk", allPtk);
		model.addAttribute("tscList", tscList);
		model.addAttribute("allPtkk", allPtk);
		model.addAttribute("viewValue", viewValue);
		model.addAttribute("checkValue", checkValue);
		return "recruitCrue/projectManageTaskT";
	}
	
	@RequestMapping(value="/enterProjectTaskB.do")
	public String enterProjectTaskB(Model model, int projectNo, int reqPage) {
	  	ProjectTaskViewData ptvd = service.enterProjectTaskB(projectNo, reqPage);
	  	ArrayList<ProjectTask> allPtk = service.projectTaskList(projectNo);
	  	ArrayList<Shortcuts> scList = service.shortcutList(projectNo);
	  	ArrayList<TaskShortcuts> tscList = service.taskShortcutList(projectNo);
	  	
	  	model.addAttribute("ptk", ptvd.getTasklist());
	  	model.addAttribute("ptkk", ptvd.getTasklist());
		model.addAttribute("projectNo", projectNo);
		model.addAttribute("pageNavi", ptvd.getPageNavi());
		model.addAttribute("start", ptvd.getStart());
		model.addAttribute("ptm", ptvd.getPtmList());
		model.addAttribute("ptmGet0", ptvd.getPtmList().get(0));
		model.addAttribute("scList", scList);
		model.addAttribute("allPtk", allPtk);
		model.addAttribute("allPtkk", allPtk);
		model.addAttribute("tscList", tscList);
		return "recruitCrue/projectManageTaskB";
	}
	
	@RequestMapping(value="/enterTaskBSelect.do")
	public String enterTaskBSelect(Model model, int reqPage, int viewValue, int checkValue, int projectNo, int teamMemberNo) {
		
		ProjectTaskViewData ptvd = service.enterProjectTaskSelectB(projectNo, reqPage, viewValue, checkValue, teamMemberNo);
	  	ArrayList<ProjectTask> allPtk = service.projectTaskList(projectNo);
	  	ArrayList<Shortcuts> scList = service.shortcutList(projectNo);
	  	ArrayList<TaskShortcuts> tscList = service.taskShortcutList(projectNo);
	  	
	  	model.addAttribute("ptk", ptvd.getTasklist());
	  	model.addAttribute("ptkk", ptvd.getTasklist());
		model.addAttribute("projectNo", projectNo);
		model.addAttribute("pageNavi", ptvd.getPageNavi());
		model.addAttribute("start", ptvd.getStart());
		model.addAttribute("ptm", ptvd.getPtmList());
		model.addAttribute("ptmGet0", ptvd.getPtmList().get(0));
		model.addAttribute("scList", scList);
		model.addAttribute("allPtkk", allPtk);
		model.addAttribute("allPtk", allPtk);
		model.addAttribute("tscList", tscList);
		model.addAttribute("viewValue", viewValue);
		model.addAttribute("checkValue", checkValue);
		return "recruitCrue/projectManageTaskB";
	}
	
	@RequestMapping(value="/enterProjectTaskH.do")
	public String enterProjectTaskH(Model model, int projectNo, int reqPage) {
	  	ProjectTaskViewData ptvd = service.enterProjectTaskH(projectNo, reqPage);
	  	ArrayList<ProjectTask> allPtk = service.projectTaskList(projectNo);
	  	ArrayList<Shortcuts> scList = service.shortcutList(projectNo);
	  	ArrayList<TaskShortcuts> tscList = service.taskShortcutList(projectNo);
	  	
	  	model.addAttribute("ptk", ptvd.getTasklist());
	  	model.addAttribute("ptkk", ptvd.getTasklist());
		model.addAttribute("projectNo", projectNo);
		model.addAttribute("pageNavi", ptvd.getPageNavi());
		model.addAttribute("start", ptvd.getStart());
		model.addAttribute("ptm", ptvd.getPtmList());
		model.addAttribute("ptmGet0", ptvd.getPtmList().get(0));
		model.addAttribute("scList", scList);
		model.addAttribute("allPtkk", allPtk);
		model.addAttribute("allPtk", allPtk);
		model.addAttribute("tscList", tscList);
		return "recruitCrue/projectManageTaskH";
	}
	
	@RequestMapping(value="/enterTaskHSelect.do")
	public String enterTaskHSelect(Model model, int reqPage, int checkValue, int projectNo, int teamMemberNo) {
		
		ProjectTaskViewData ptvd = service.enterProjectTaskSelectH(projectNo, reqPage, checkValue, teamMemberNo);
	  	ArrayList<ProjectTask> allPtk = service.projectTaskList(projectNo);
	  	ArrayList<Shortcuts> scList = service.shortcutList(projectNo);
	  	ArrayList<TaskShortcuts> tscList = service.taskShortcutList(projectNo);
	  	
	  	model.addAttribute("ptk", ptvd.getTasklist());
	  	model.addAttribute("ptkk", ptvd.getTasklist());
		model.addAttribute("projectNo", projectNo);
		model.addAttribute("pageNavi", ptvd.getPageNavi());
		model.addAttribute("start", ptvd.getStart());
		model.addAttribute("ptm", ptvd.getPtmList());
		model.addAttribute("ptmGet0", ptvd.getPtmList().get(0));
		model.addAttribute("scList", scList);
		model.addAttribute("allPtkk", allPtk);
		model.addAttribute("allPtk", allPtk);
		model.addAttribute("tscList", tscList);
		model.addAttribute("checkValue", checkValue);
		return "recruitCrue/projectManageTaskH";
	}
	
	//????????????
		@RequestMapping(value="/reportProjectNoticeComment.do")
		public String reportComment(Report rp, Model model, int projectNo, int memberNo, int boardType, int applicantMemberNo) {
			int result = service.reportProjectNoticeComment(rp);
			if(result>0) {
				model.addAttribute("title","?????? ?????? ??????");
				model.addAttribute("msg","?????? ????????? ?????????????????????.");
				model.addAttribute("icon","success");
			
			}else {
				model.addAttribute("title","?????? ?????? ??????");
				model.addAttribute("msg","?????? ?????? ????????? ?????????????????????. ??????????????? ??????????????????.");
				model.addAttribute("icon","error");
			}
			if(boardType == 1) {
				model.addAttribute("loc","/selectOneNotice.do?projectNo="+projectNo+"&memberNo="+memberNo);
			} else {
				int entryNo = service.searchEntryNo(projectNo, applicantMemberNo);
				model.addAttribute("loc","/selectOneApplicant.do?projectNo="+projectNo+"&memberNo="+memberNo+"&entryNo="+entryNo);
			}
			return "member/swalMsg";
			
		}
  
	  
}
