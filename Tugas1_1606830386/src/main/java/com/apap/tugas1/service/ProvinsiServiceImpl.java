package com.apap.tugas1.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.apap.tugas1.model.InstansiModel;
import com.apap.tugas1.model.ProvinsiModel;
import com.apap.tugas1.repository.ProvinsiDB;

@Service
@Transactional
public class ProvinsiServiceImpl implements ProvinsiService{
	@Autowired
	ProvinsiDB provinsiDb;

	@Override
	public List<ProvinsiModel> findAllProvinsi() {
		return provinsiDb.findAll();
	}

	@Override
	public List<InstansiModel> getAllInstansi(int idProvinsi) {
		return provinsiDb.findById(idProvinsi);
	}

	@Override
	public ProvinsiModel getById(long id) {
		return provinsiDb.findProvinsiById(id);
	}
	
	
}
