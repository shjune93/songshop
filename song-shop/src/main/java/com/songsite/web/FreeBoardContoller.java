package com.songsite.web;


import java.io.Console;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.songsite.domain.Customer;
import com.songsite.domain.FreeBoard;
import com.songsite.domain.FreeBoardFile;
import com.songsite.fileservice.FileUploadDownloadService;
import com.songsite.repository.FreeBoardFileRepository;
import com.songsite.repository.FreeBoardRepository;
import com.songsite.session.HttpSessionUtils;


@Controller
@RequestMapping("freeboard")
public class FreeBoardContoller {
	 private static final Logger logger = LoggerFactory.getLogger(FreeBoardContoller.class);
	    
	    @Autowired
	    private FileUploadDownloadService service;
	    
	    @Autowired
	    private FreeBoardRepository freeBoardRepository;
	    @Autowired
	    private FreeBoardFileRepository freeBoardFileRepository;
	    
	    //게시판등록
	    @PostMapping("create")
	    public String create(String title,String contents,@RequestParam("file") MultipartFile file,HttpSession session){
	    	
	    	
	    	
	    	if(!HttpSessionUtils.isLoginUser(session)) {
				return "/login";
			}
	    	
	    	
	    	Customer sessionUser=HttpSessionUtils.getUserFromSession(session);
			FreeBoard freeboard=new FreeBoard(sessionUser,title,contents);
			
			FreeBoard savedFreeboard=freeBoardRepository.save(freeboard);
			
			if(!file.isEmpty()) {
				String i=savedFreeboard.getId().toString();
				String fileName = service.storeFile(file,i);
				
				
				String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                        .path("/downloadFile/")	
                        .path(i+"/")
                        .path(fileName)
                        .toUriString();
				
				FreeBoardFile freeboardFile=new FreeBoardFile(fileName, fileDownloadUri, file.getContentType(), file.getSize(),savedFreeboard);
				freeBoardFileRepository.save(freeboardFile);
				
		        
		    	
			}
	    	return "/freeboard/list";
	    }
	   	/////////////////////////////////////////////////////////////////////////// 파일업로드 원본
	    @PostMapping("{id}/uploadFile")
	    public FreeBoardFile uploadFile(@RequestParam("file") MultipartFile file,@PathVariable Long id) {
	    	String i="";
	        String fileName = service.storeFile(file,i);
	        
	        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
	                                .path("/downloadFile/")
	                                .path(fileName)
	                                .toUriString();
	        
	        return new FreeBoardFile(fileName, fileDownloadUri, file.getContentType(), file.getSize());
	    }
	    
	    ///////////////////////////////////////////////////////////////////////////////////////////////
	    
	    @PostMapping("{id}/uploadMultipleFiles")
	    public List<FreeBoardFile> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files,@PathVariable Long id){
	        return Arrays.asList(files)
	                .stream()
	                .map(file -> uploadFile(file,id))
	                .collect(Collectors.toList());
	    }
	    
	    @GetMapping("{id}/downloadFile/{fileName:.+}")
	    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request){
	         // Load file as Resource
	        Resource resource = service.loadFileAsResource(fileName);
	 
	        // Try to determine file's content type
	        String contentType = null;
	        try {
	            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
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
	    
		// #로그인 확인후 게시판 적기 이동
		@GetMapping("/form")
		public String loginForm(HttpSession session, HttpServletRequest request) {
			if (session.getAttribute(HttpSessionUtils.CUSTOMER_SESSION_KEY) == null) {
				// 이미 로그인 상태일 경우
				return "redirect:/login";
			}
			return "/freeboard/form";
		}
		
		@GetMapping("/list")
		public String list(HttpSession session, HttpServletRequest request,Model model) {
			model.addAttribute("freeboards",freeBoardRepository.findAll());
			return "/freeboard/list";
		}
		
}
