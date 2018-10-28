package com.apap.tugas1.service;

import java.util.List;

import com.apap.tugas1.model.InstansiModel;
import com.apap.tugas1.model.ProvinsiModel;

public interface ProvinsiService {
	List<ProvinsiModel> findAllProvinsi();
	
	List<InstansiModel> getAllInstansi(int idProvinsi);
	
}
