package com.apap.tugas1.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.apap.tugas1.model.JabatanModel;
import com.apap.tugas1.model.PegawaiModel;
import com.apap.tugas1.service.JabatanService;
import com.apap.tugas1.service.PegawaiService;
import com.apap.tugas1.service.ProvinsiService;

@Controller
public class PegawaiController {
	@Autowired
	PegawaiService pegawaiService;
	
	/*@Autowired
	ProvinsiService provinsiService;
	*/
	
	@Autowired
	JabatanService jabatanService;
	@RequestMapping(value="/")
	private String home(Model model) {
		List<JabatanModel> jabatans = jabatanService.findAllJabatan();
		model.addAttribute("jabatans",jabatans);
		return "home";
	}
	/*
	 * fitur 1
	 */
	@RequestMapping(value = "/pegawai", method = RequestMethod.GET)
	private String viewPegawai(@RequestParam String nip, Model model) {
		PegawaiModel archive = pegawaiService.getPegawailByNip(nip);
		
		Set<JabatanModel> jabatanKu = archive.getJabatans();
		double count = 0;
		for(JabatanModel jabatan: jabatanKu) {
			if(jabatan.getGajiPokok() > count) {
				count = jabatan.getGajiPokok();
			}
		}
		
		int tunjangan = (int) ((archive.getInstansi().getProvinsi().getPresentaseTunjangan()/100) * (int)count);
		int count2 = (int)count + tunjangan;
		model.addAttribute("pegawai", archive);
		model.addAttribute("jabatans",jabatanKu);
		model.addAttribute("gaji", count2);
		return "view-pegawai";
	}
	
	@RequestMapping(value = "/pegawai/tambah", method = RequestMethod.GET)
	private String add(Model model) {
		model.addAttribute("pegawai", new PegawaiModel());
		
		return "addPegawai"; //bikin add pegawai
	}
	
	@RequestMapping(value = "/pegawai/tambah", method = RequestMethod.POST)
	private String addPilotSubmit(@ModelAttribute PegawaiModel pegawai) {
		//pegawaiService.addPegawai(pegawai);
		return "addPegawaiBerhasil"; //bikin add buat semuanya
	}
	
}
