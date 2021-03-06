package com.apap.tugas1.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.apap.tugas1.model.InstansiModel;
import com.apap.tugas1.model.ProvinsiModel;

@Repository
public interface ProvinsiDB extends JpaRepository<ProvinsiModel, Long>{
	List<InstansiModel> findById(int idProvinsi);
	
	ProvinsiModel findProvinsiById(long id);
}
