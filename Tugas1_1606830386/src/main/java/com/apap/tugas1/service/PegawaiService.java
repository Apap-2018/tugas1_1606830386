package com.apap.tugas1.service;

import java.util.List;

import com.apap.tugas1.model.PegawaiModel;

public interface PegawaiService {
	void addPegawai(PegawaiModel pegawai);
	
	PegawaiModel getPegawailByNip(String nip);
	
	List<PegawaiModel> getAllPegawai();

}
