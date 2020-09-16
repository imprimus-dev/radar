package com.radar.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.radar.model.Blip;
import com.radar.repository.BlipRepository;
import com.radar.util.ExcelHelper;

@Service
public class BlipService {
	
	@Autowired
	private BlipRepository blipRepo;
	
	public void save(MultipartFile file) {
		try {
			List<Blip> blips = ExcelHelper.excelToBlips(file.getInputStream());
			blipRepo.saveAll(blips);
			System.out.println("Uploaded Blips : " + blips.size());
		} catch (IOException e) {
			// TODO: handle exception
			throw new RuntimeException("fail to store excel data: " + e.getMessage());
		}
	}
	
	public List<Blip> getAllBlips(){
		return blipRepo.findAll();
	}
	
	public Optional<Blip> findBlip(long blipId) {
		return blipRepo.findById(blipId);
				
	}
}
