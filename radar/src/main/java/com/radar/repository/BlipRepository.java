package com.radar.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.radar.model.Blip;

public interface BlipRepository extends MongoRepository<Blip, Long>{

}
