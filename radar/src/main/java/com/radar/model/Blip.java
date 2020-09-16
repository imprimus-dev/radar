package com.radar.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document
public class Blip {
	@Id
	private Long id;
	private String name;
	private String ring;
	private String quadrant;
	private Boolean isNew;
	private String description;
}
