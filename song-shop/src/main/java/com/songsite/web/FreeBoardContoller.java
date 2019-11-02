package com.songsite.web;


import java.io.Console;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.songsite.domain.User;
import com.songsite.domain.FreeBoard;
import com.songsite.domain.FreeBoardFile;
import com.songsite.fileservice.FileUploadDownloadService;
import com.songsite.message.Result;
import com.songsite.repository.FreeBoardFileRepository;
import com.songsite.repository.FreeBoardRepository;
import com.songsite.session.HttpSessionUtils;

import lombok.Getter;
import lombok.Setter;


@Controller
@Getter
@Setter
@RequestMapping(value="freeboard", method = {RequestMethod.GET, RequestMethod.POST})
public class FreeBoardContoller {
	private static final Logger logger = LoggerFactory.getLogger(FreeBoardContoller.class);

	@Autowired
	private FileUploadDownloadService service;

	@Autowired
	private FreeBoardRepository freeBoardRepository;
	@Autowired
	private FreeBoardFileRepository freeBoardFileRepository;



	private Result valid(HttpSession session,FreeBoard freeboard) {
		if(!HttpSessionUtils.isLoginUser(session)) {

			return Result.fail("로그인이 필요합니다.");
			//throw new IllegalStateException("로그인이 필요합니다.");
		}
		User loginUser=HttpSessionUtils.getUserSession(session);
		if(!freeboard.isSameWriter(loginUser)) {
			return Result.fail("자신이 쓴 글만 수정,삭제 가능합니다.");
			//throw new IllegalStateException("자신이 쓴 글만 수정,삭제 가능합니다.");
		}
		return Result.ok();

	}

	//리스트 보기
	@GetMapping("list")
	public String list(HttpSession session, HttpServletRequest request,Model model) {
		model.addAttribute("freeboards",freeBoardRepository.findAll());
		return "/freeboard/list";
	}


	//상세내용 보기
	@GetMapping("{id}")
	public String show(@PathVariable Long id,Model model) {
		FreeBoard freeboard=freeBoardRepository.findById(id).get();
		model.addAttribute("freeboard",freeboard);
		return "/freeboard/show";
	}


	//#로그인 확인후 게시판 적기 이동
	@GetMapping("form")
	public String loginForm(HttpSession session, HttpServletRequest request) {
		if (session.getAttribute(HttpSessionUtils.User_SESSION_KEY) == null) {
			// 이미 로그인 상태일 경우
			return "redirect:/login";
		}
		return "/freeboard/form";
	}


	//게시판 수정 폼으로 이동
	@PostMapping("{id}/updateform")
	public String updateForm(HttpSession session,@PathVariable Long id,Model model) {

		FreeBoard freeboard=freeBoardRepository.findById(id).get();
		Result result=valid(session,freeboard);
		if(!result.isValid()) {
			model.addAttribute("errorMessage",result.getErrorMessage());
			return "redirect:/freeboard/"+id;
		}
		model.addAttribute("freeboard",freeboard);
		return "/freeboard/updateForm";
	}
	//redirect는 주소로 없으면 templates 폴더의 html파일로

	//게시판 수정
	@PostMapping("{id}/update")
	public String update(HttpSession session,@PathVariable Long id,String title,String contents,@RequestParam("file") MultipartFile file,Model model) {

		FreeBoard freeboard=freeBoardRepository.findById(id).get();
		Result result=valid(session,freeboard);
		if(!result.isValid()) {
			model.addAttribute("errorMessage",result.getErrorMessage());
			return "redirect:/freeboard/"+id;
		}

		freeboard.update(title,contents);
		freeBoardRepository.save(freeboard);
		if(!file.isEmpty()) {
			if(freeboard.getFile()!=null) {
				String path="C:\\Users\\shjun\\git\\songshop\\song-shop\\files\\"+freeboard.getId().toString()+"\\"+freeboard.getFile().getFileName();
				System.out.println("파일경로"+path);
				File beforeFile= new File(path);
				if(beforeFile.exists()==true) {

					beforeFile.delete();
					System.out.println(path+"삭제 완료");
				}
				freeBoardFileRepository.deleteById(freeboard.getFile().getId());
				//freeBoardFileRepository.deleteById(id);
			}
			
			String i=id.toString();
			String fileName = service.storeFile(file,i);


			String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
					.path("/freeboard/downloadFile/")	
					.path(i+"/")
					.path(fileName)
					.toUriString();

			FreeBoardFile freeboardFile=new FreeBoardFile(fileName, fileDownloadUri, file.getContentType(), file.getSize(),freeboard);
			freeBoardFileRepository.save(freeboardFile);

		}
		return "redirect:/freeboard/"+id;
	}

	//게시판 삭제
	@PostMapping("{id}/delete")
	public String delete(HttpSession session,HttpServletRequest request,@PathVariable Long id,Model model){

		FreeBoard freeboard=freeBoardRepository.findById(id).get();
		Result result=valid(session,freeboard);
		if(!result.isValid()) {
			model.addAttribute("errorMessage",result.getErrorMessage());
			return "redirect:/freeboard/"+id;
		}
		if(freeboard.getFile()!=null) {
			//파일경로 하드코딩
			String path="C:\\Users\\shjun\\git\\songshop\\song-shop\\files\\"+freeboard.getId().toString()+"\\"+freeboard.getFile().getFileName();
			//
			System.out.println("파일경로"+path);
			File file= new File(path);
			if(file.exists()==true) {
				System.out.println("여기 들어옴");
				file.delete();
			}
			freeBoardFileRepository.deleteById(freeboard.getFile().getId());
		}


		freeBoardRepository.deleteById(id);
		return "redirect:/freeboard/list";
	}


	//게시판등록
	@PostMapping("create")
	public String create(String title,String contents,@RequestParam("file") MultipartFile file,HttpSession session){
		if(!HttpSessionUtils.isLoginUser(session)) {
			return "/login";
		}


		User sessionUser=HttpSessionUtils.getUserSession(session);
		FreeBoard freeboard=new FreeBoard(sessionUser,title,contents);

		FreeBoard savedFreeboard=freeBoardRepository.save(freeboard);
		//파일 저장 파일 db저장
		if(!file.isEmpty()) {
			String i=savedFreeboard.getId().toString();
			String fileName = service.storeFile(file,i);
			System.out.println("파일 이름 "+fileName);


			String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
					.path("/freeboard/downloadFile/")	
					.path(i+"/")
					.path(fileName)
					.toUriString();

			FreeBoardFile freeboardFile=new FreeBoardFile(fileName, fileDownloadUri, file.getContentType(), file.getSize(),savedFreeboard);
			freeBoardFileRepository.save(freeboardFile);



		}
		return "redirect:/freeboard/list";
	}

	//파일 다운로드
	@GetMapping("downloadFile/{id}/{fileName:.+}")
	public ResponseEntity<Resource> downloadFile(@PathVariable String fileName,@PathVariable String id, HttpServletRequest request){
		// Load file as Resource
		//System.out.println(fileName);
		Resource resource = service.loadFileAsResource(id+"/"+fileName);
		//System.out.println(resource.getFilename());


		// Try to determine file's content type
		String contentType = null;
		try {
			contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
			System.out.println("컨텐츠 타입"+contentType);
			// System.out.println(contentType);
		} catch (IOException ex) {
			logger.info("Could not determine file type.");
		}

		// Fallback to the default content type if type could not be determined
		if(contentType == null) {
			contentType = "application/octet-stream";
		}

		return ResponseEntity.ok()
				.contentType(MediaType.parseMediaType(contentType))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
				.body(resource);
	}





}



/////////////////////////////////////////////////////////////////////////// 파일업로드 원본
//@PostMapping("{id}/uploadFile")
//public FreeBoardFile uploadFile(@RequestParam("file") MultipartFile file,@PathVariable Long id) {
//	String i="";
//    String fileName = service.storeFile(file,i);
//    
//    String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
//                            .path("/downloadFile/")
//                            .path(fileName)
//                            .toUriString();
//    
//    return new FreeBoardFile(fileName, fileDownloadUri, file.getContentType(), file.getSize());
//}

///////////////////////////////////////////////////////////////////////////////////////////////

//@PostMapping("{id}/uploadMultipleFiles")
//public List<FreeBoardFile> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files,@PathVariable Long id){
//    return Arrays.asList(files)
//            .stream()
//            .map(file -> uploadFile(file,id))
//            .collect(Collectors.toList());
//}