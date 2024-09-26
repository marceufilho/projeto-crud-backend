package br.edu.ibmec.projeto.backend.task_manager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import br.edu.ibmec.projeto.backend.task_manager.model.Manufacturer;
import br.edu.ibmec.projeto.backend.task_manager.repository.ManufacturerRepository;

@Controller
@RequestMapping("/manufacturer")
public class ManufacturerController {
    
    @Autowired
    private ManufacturerRepository manufacturerRepository;
    
    @GetMapping("/list")
    public String listManufacturers(Model model) {
        model.addAttribute("manufacturers", manufacturerRepository.findAll());
        return "manufacturer-list";
    }
    
    @GetMapping("/create")
    public String createManufacturerForm(Model model) {
        model.addAttribute("manufacturer", new Manufacturer());
        return "manufacturer-create";
    }
    
    @PostMapping("/save")
    public String saveManufacturer(@ModelAttribute Manufacturer manufacturer) {
        manufacturerRepository.save(manufacturer);
        return "redirect:/manufacturer/list";
    }
    
    @GetMapping("/edit/{id}")
    public String editManufacturer(@PathVariable Long id, Model model) {
        model.addAttribute("manufacturer", manufacturerRepository.findById(id).get());
        return "manufacturer-edit";
    }
    
    @PostMapping("/update/{id}")
    public String updateManufacturer(@PathVariable Long id, @ModelAttribute Manufacturer manufacturer) {
        Manufacturer existingManufacturer = manufacturerRepository.findById(id).get();
        existingManufacturer.setName(manufacturer.getName());
        existingManufacturer.setCreationDate(manufacturer.getCreationDate());
        manufacturerRepository.save(existingManufacturer);
        return "redirect:/manufacturer/list";
    }
    
    @GetMapping("/delete/{id}")
    public String deleteManufacturer(@PathVariable Long id) {
        manufacturerRepository.deleteById(id);
        return "redirect:/manufacturer/list";
    }
}
