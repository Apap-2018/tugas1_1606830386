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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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
	 * Fitur 1 Done
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
	 * fitur 2 sudah bisa nambah, namun fitur filter by provinsi belum benar(9.44 - 28 Oktober 2018)
	 */
	@RequestMapping(value = "/pegawai/tambah", method = RequestMethod.GET)
	private String add(Model model) {
		model.addAttribute("pegawai", new PegawaiModel());
		List<InstansiModel> allInstansi = instansiService.findAllInstansi();
		model.addAttribute("listInstansi", allInstansi);
		List<ProvinsiModel> allProvinsi = provinsiService.findAllProvinsi();
		model.addAttribute("listProvinsi", allProvinsi);
		List<JabatanModel> allJabatan = jabatanService.findAllJabatan();
		model.addAttribute("listJabatan", allJabatan);
		return "addPegawai"; 
	}
	@RequestMapping(value = "/pegawai/tambah", method = RequestMethod.POST)
	private String addPilotSubmit(@ModelAttribute PegawaiModel pegawai, Model model) {
		String nip = generateNip(pegawai);
		pegawai.setNip(nip);
		pegawaiService.addPegawai(pegawai);
		model.addAttribute("nip", nip);
		return "addPegawaiBerhasil";
	}
	@RequestMapping(value = "/pegawai/instansi", method = RequestMethod.GET)
	public @ResponseBody  List<InstansiModel> getAllSubcategories(@PathVariable("provinsi") int id_provinsi) {
		
	    return provinsiService.getAllInstansi(id_provinsi);
	}
	
	//Generate NIP untuk fitur 2 sudah berhasil
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
		  
		  List<String> nipSama = new ArrayList<>();
		  
		  String nipCalonPegawai = idProvDiNip+idInstansiDiNip+birthDateDiNip+tanggalMasukDiNip;
		  
		  for(PegawaiModel pegawaiDisini: instansi.getPegawaiInstansi()) {
			  System.out.println(pegawaiDisini.getNip().substring(0, 14) + " = "+nipCalonPegawai);
			  System.out.println(pegawaiDisini.getNip().substring(0, 14).equals(nipCalonPegawai));
			  if(pegawaiDisini.getNip().substring(0, 14).equals(nipCalonPegawai)) {
				  nipSama.add(pegawaiDisini.getNip());
				  
			  }
		  }
		  String count = "";
		  if(!nipSama.isEmpty()) {
			  System.out.println("MASUK SINIIIIIII");
			  System.out.println(nipSama.size());
			  Collections.sort(nipSama);
			  System.out.println(nipSama.get(nipSama.size()-1));
			  int count1 = Integer.parseInt(nipSama.get(nipSama.size()-1).substring(nipSama.get(nipSama.size()-1).length()-2));
			  System.out.println("kesini ga");
			  
			  int count2 = count1+1;
			  
			  System.out.println(count1+" + "+ 1);
			  System.out.println(count2);
			  if(count2>=10) {
				  count = nipCalonPegawai+Integer.toString(count2);  
			  }
			  else {
				  count = nipCalonPegawai+"0"+Integer.toString(count2);
			  }
			  
		  }
		  else {
			  count = nipCalonPegawai +"01";
		  }		  
		  return count;
	}
	
	/*
	 * Fitur 3 baru mulai
	 */
	@RequestMapping(value = "/pegawai/ubah", method = RequestMethod.GET)
	private String ubahPegawai(@RequestParam String nip, Model model) {
		PegawaiModel pegawai = pegawaiService.getPegawailByNip(nip);
		System.out.println(pegawai.getNama());
		model.addAttribute("pegawai",pegawai);
		List<InstansiModel> allInstansi = instansiService.findAllInstansi();
		model.addAttribute("listInstansi", allInstansi);
		List<ProvinsiModel> allProvinsi = provinsiService.findAllProvinsi();
		model.addAttribute("listProvinsi", allProvinsi);
		List<JabatanModel> allJabatan = jabatanService.findAllJabatan();
		model.addAttribute("listJabatan", allJabatan);
		return "ubahPegawai";
	}
	@RequestMapping(value = "/pegawai/ubah", method = RequestMethod.POST)
	private String ubahPegawaiBerhasil(@ModelAttribute PegawaiModel pegawai, Model model) {
		String nipPegawai = pegawai.getNip();
		
		System.out.println(pegawaiService.getPegawailByNip(nipPegawai)+" - " + pegawai.getNip());
		
		pegawaiService.getPegawailByNip(nipPegawai).setNama(pegawai.getNama());
		pegawaiService.getPegawailByNip(nipPegawai).setTempatLahir(pegawai.getTempatLahir());
		pegawaiService.getPegawailByNip(nipPegawai).setInstansi(pegawai.getInstansi());
		pegawaiService.getPegawailByNip(nipPegawai).setTahunMasuk(pegawai.getTahunMasuk());
		pegawaiService.getPegawailByNip(nipPegawai).setTanggalLahir(pegawai.getTanggalLahir());
		
		String nipBaru = generateNip(pegawai);
		
		pegawaiService.getPegawailByNip(nipPegawai).setNip(nipBaru);
		
		
		
		
		model.addAttribute("nip",nipBaru);
		return "ubahPegawaiBehasil";
	}
	
	/*
	 * fitur 10 Done
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
	
	
}
