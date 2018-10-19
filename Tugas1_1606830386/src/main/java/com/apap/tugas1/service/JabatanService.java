package com.apap.tugas1.service;

import java.util.List;

import com.apap.tugas1.model.JabatanModel;

public interface JabatanService {
	void addJabatan(JabatanModel jabatan);
	
	void deleteJabatan(JabatanModel jabatan);
	
	JabatanModel getJabatanById(long idJabatan);
	
	List<JabatanModel> findAllJabatan();
}
