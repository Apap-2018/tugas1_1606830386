package com.apap.tugas1.controller;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.apap.tugas1.model.InstansiModel;
import com.apap.tugas1.model.JabatanModel;
import com.apap.tugas1.model.PegawaiModel;
import com.apap.tugas1.model.ProvinsiModel;
import com.apap.tugas1.service.InstansiService;
import com.apap.tugas1.service.JabatanService;
import com.apap.tugas1.service.PegawaiService;
import com.apap.tugas1.service.ProvinsiService;

@Controller
public class PegawaiController {
	@Autowired
	PegawaiService pegawaiService;
	
	@Autowired
	ProvinsiService provinsiService;
	
	@Autowired
	InstansiService instansiService;
	
	@Autowired
	JabatanService jabatanService;
	@RequestMapping(value="/")
	private String home(Model model) {
		List<JabatanModel> jabatans = jabatanService.findAllJabatan();
		model.addAttribute("jabatans",jabatans);
		List<ProvinsiModel> allProvinsi = provinsiService.findAllProvinsi();
		List<InstansiModel> allInstansi = instansiService.findAllInstansi();
		model.addAttribute("listProvinsi", allProvinsi);
		model.addAttribute("listInstansi", allInstansi);
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
	
	/*
	 * fitur 2 tapi belum selesai
	 */
	@RequestMapping(value = "/pegawai/tambah", method = RequestMethod.GET)
	private String add(Model model) {
		model.addAttribute("pegawai", new PegawaiModel());
		List<InstansiModel> allInstansi = instansiService.findAllInstansi();
		model.addAttribute("listInstansi", allInstansi);
		return "addPegawai"; //bikin add pegawai
	}
	
	/*
	 * fitur 2 tapi belum selesai
	 */
	@RequestMapping(value = "/pegawai/tambah", method = RequestMethod.POST)
	private String addPilotSubmit(@ModelAttribute PegawaiModel pegawai) {
		//pegawaiService.addPegawai(pegawai);
		return "addPegawaiBerhasil"; //bikin add buat semuanya
	}
	
	/*
	 * fitur 10 
	 */
	@RequestMapping(value = "/pegawai/tertua-termuda", method = RequestMethod.GET)
	private String viewTertuaTermuda(@RequestParam long idInstansi, Model model) {
		InstansiModel instansi = instansiService.getById(idInstansi);
		System.out.println(instansi.getNama());
		List<PegawaiModel> pegawaiInstansi = instansi.getPegawaiInstansi();
		
		Collections.sort(pegawaiInstansi, new compareAge());
		model.addAttribute("tertua", pegawaiInstansi.get(0));
		model.addAttribute("termuda", pegawaiInstansi.get(pegawaiInstansi.size()-1));
		return "viewPegawaiTuaMuda";
	}
	
	public static class compareAge implements Comparator<PegawaiModel>{
		public int compare(PegawaiModel pegawai1, PegawaiModel pegawai2) {
			if(pegawai1.getTanggalLahir().compareTo(pegawai2.getTanggalLahir())<0) {
				return -1;
			}
			else if(pegawai1.getTanggalLahir().compareTo(pegawai2.getTanggalLahir())>0) {
				return 1;
			}
			else {
				return 0;
			}
		}
	}
	
	public String generateNip(PegawaiModel pegawai) {
		  InstansiModel instansi = pegawai.getInstansi();
		  ProvinsiModel provinsi = instansi.getProvinsi();
		  
		  long idProvinsi = provinsi.getId();
		  String idProvDiNip = Long.toString(idProvinsi);
		  
		  long idInstansi = instansi.getId();
		  String idInsta = Long.toString(idInstansi);
		  String idInstansiDiNip = idInsta.substring(Math.max(idInsta.length() - 2, 0));
		  
		  Date tanggalLahir = pegawai.getTanggalLahir();
		  DateFormat dateFormat = new SimpleDateFormat("ddMMYY");
		  String birthDateDiNip = dateFormat.format(tanggalLahir);
		  
		  String tanggalMasukDiNip = pegawai.getTahunMasuk();
		  
		  int count = 0;
		  List<PegawaiModel> pegawaiDisini = instansi.getPegawaiInstansi();
		  for(PegawaiModel employee: pegawaiDisini) {
			  if(employee.getTahunMasuk().equals(pegawai.getTahunMasuk()) && employee.getTanggalLahir().equals(pegawai.getTanggalLahir())) {
				  count+=1;
			  }
		  }
		  
		  return idProvDiNip+idInstansiDiNip+birthDateDiNip+tanggalMasukDiNip+count;
	}
}
