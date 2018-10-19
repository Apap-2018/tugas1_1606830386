package com.apap.tugas1.controller;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.apap.tugas1.model.JabatanModel;
import com.apap.tugas1.service.JabatanService;

@Controller
public class JabatanController {
	@Autowired
	JabatanService jabatanService;
	/*
	 * fitur 5
	 */
	@RequestMapping(value="/jabatan/tambah", method = RequestMethod.GET)
	private String addJabatan(Model model) {
		model.addAttribute("jabatan", new JabatanModel());
		return "addJabatan";
	}
	
	/*
	 * fitur 5
	 */
	@RequestMapping(value="/jabatan/tambah", method = RequestMethod.POST)
	private String addJabatanBerhasil(@ModelAttribute JabatanModel jabatan) {
		jabatanService.addJabatan(jabatan);
		return "home";
	}
	
	/*
	 * fitur 6
	 */
	@RequestMapping(value = "/jabatan/view", method = RequestMethod.GET)
	private String viewJabatan(@RequestParam long idJabatan, Model model) {
		JabatanModel archive = jabatanService.getJabatanById(idJabatan);
		model.addAttribute("jabatan", archive);
		return "view-jabatan";
	}
	/*
	 * fitur 7
	 */
	@RequestMapping(value = "/jabatan/ubah", method = RequestMethod.GET)
	private String changeJabatan(@RequestParam long idJabatan, Model model) {
		JabatanModel archive = jabatanService.getJabatanById(idJabatan);
		model.addAttribute("jabatan", archive);
		
		return "gantiJabatan";
	}
	
	/*
	 * fitur 7
	 */
	@RequestMapping(value = "/jabatan/ubah", method = RequestMethod.POST)
	private String jabatanChanged(@ModelAttribute JabatanModel newJabatan) {
		long idJabatan = newJabatan.getId();
		jabatanService.getJabatanById(idJabatan).setDeskripsi(newJabatan.getDeskripsi());
		jabatanService.getJabatanById(idJabatan).setGajiPokok(newJabatan.getGajiPokok());
		jabatanService.getJabatanById(idJabatan).setNama(newJabatan.getNama());
		
		
		return "home";
	}
	/*
	 * fitur 8
	 */
	@RequestMapping(value = "/jabatan/hapus", method = RequestMethod.POST)
	private String hapusJabatan(@ModelAttribute JabatanModel jabatan) {
		if(jabatan.getPegawais().isEmpty()) {
			jabatanService.deleteJabatan(jabatan);
			return "jabatanDeleted";
		}
		else {
			return "gagalHapusJabatan";
		}
	}
	
	/*
	 * fitur 9
	 */
	@RequestMapping(value = "/jabatan/viewAll", method = RequestMethod.GET)
	private String viewAllJabatan(Model model) {
		List<JabatanModel> jabatans = jabatanService.findAllJabatan();
		model.addAttribute("jabatans",jabatans);
		return "viewAllJabatan";
	}
	
	/*
	 * fitur 12
	 */
	@RequestMapping(value = "/jabatan/viewTotal", method = RequestMethod.GET)
	private String viewTotal(Model model) {
		Map<JabatanModel, Integer> jumlahJabatan = new HashMap<>();
		for(JabatanModel jabatan: jabatanService.findAllJabatan()) {
			int jumlahPegawai = jabatan.getPegawais().size();
			jumlahJabatan.put(jabatan, jumlahPegawai);
		}
		model.addAttribute("mapJumlah", jumlahJabatan);
		return "totalJabatan";
	}
}
