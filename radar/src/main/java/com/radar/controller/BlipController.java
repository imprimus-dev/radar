package com.radar.controller;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.radar.model.Blip;
import com.radar.service.BlipService;
import com.radar.util.ExcelHelper;

@Controller
@CrossOrigin
public class BlipController {
	
	@Autowired
	private BlipService blipService;
	
	@PostMapping("/api/upload")
	ResponseEntity<?> saveBlips(@RequestParam("file") MultipartFile file) {
		String message = "";

		if (ExcelHelper.hasExcelFormat(file)) {
			try {
				blipService.save(file);
				
				message = "Uploaded the file successfully: " + file.getOriginalFilename();
				return ResponseEntity.status(HttpStatus.OK).body(message);
			} catch (Exception e) {
				e.printStackTrace();
				message = "Could not upload the file: " + file.getOriginalFilename() + "!";
				return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(message);
			}
	    }
		message = "Please upload an excel file!";
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
	}
	
	@RequestMapping("/api/blips")
	ResponseEntity<?> getBlips(){
		return new ResponseEntity<>(blipService.getAllBlips(), HttpStatus.OK);
	}
	
	@RequestMapping("/api/blip/{blipId}")
	ResponseEntity<?> getUser(@PathVariable(value="blipId") long blipId){
		Optional<Blip> blip = blipService.findBlip(blipId);
		return blip.map(response -> ResponseEntity.ok().body(response)).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}
	
	@RequestMapping(value = "/api/blips.csv")
	public void blipsAsCSV(HttpServletResponse response) {         
	    try {
	    	List<Blip> blips = blipService.getAllBlips();
	    	StringBuilder strResponse = new StringBuilder();
	    	strResponse.append("name,ring,quadrant,isNew,description\n");
	    	for (Blip blip : blips) {
				strResponse.append(blip.getName()+",").append(blip.getRing()+",").append(blip.getQuadrant()+",").append(blip.getIsNew()+",").append(blip.getDescription()).append("\n");
			}
	    	response.setContentType("text/plain; charset=utf-8");
			//response.getWriter().print("a,b,c\n1,2,3\n3,4,5");
	    	response.getWriter().print(strResponse);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
