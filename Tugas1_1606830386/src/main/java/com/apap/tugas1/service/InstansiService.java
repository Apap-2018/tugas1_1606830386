package com.apap.tugas1.service;

import java.util.List;

import com.apap.tugas1.model.InstansiModel;


public interface InstansiService {
	
	List<InstansiModel> findAllInstansi();
	InstansiModel getById(long id);
}
