package br.edu.ibmec.projeto.backend.task_manager.controller;

import br.edu.ibmec.projeto.backend.task_manager.model.Manufacturer;
import br.edu.ibmec.projeto.backend.task_manager.model.Vehicle;
import br.edu.ibmec.projeto.backend.task_manager.repository.ManufacturerRepository;
import br.edu.ibmec.projeto.backend.task_manager.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/vehicle")
public class VehicleController {

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private ManufacturerRepository manufacturerRepository;

    @GetMapping("/list")
    public String listVehicles(Model model) {
        List<Vehicle> vehicles = vehicleRepository.findAll();
        model.addAttribute("vehicles", vehicles);
        return "vehicle-list";
    }

    @GetMapping("/create")
    public String createVehicleForm(Model model) {
        model.addAttribute("vehicle", new Vehicle());
        model.addAttribute("manufacturers", manufacturerRepository.findAll());
        return "vehicle-create";
    }

    @PostMapping("/save")
    public String saveVehicle(@ModelAttribute Vehicle vehicle,
                              @RequestParam("imageFile") MultipartFile imageFile,
                              @RequestParam("manufacturer") Long manufacturerId,
                              Model model) {
        try {
            // Fetch the manufacturer by its ID
            Manufacturer manufacturer = manufacturerRepository.findById(manufacturerId)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid manufacturer ID: " + manufacturerId));

            vehicle.setManufacturer(manufacturer);

            // Set the image if present
            if (!imageFile.isEmpty()) {
                vehicle.setImage(imageFile.getBytes());
            }

            vehicleRepository.save(vehicle);
        } catch (IOException e) {
            e.printStackTrace();
            model.addAttribute("error", "Failed to upload image");
            return "vehicle-create";
        }
        return "redirect:/vehicle/list";
    }

    @GetMapping("/edit/{id}")
    public String editVehicle(@PathVariable Long id, Model model) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid vehicle ID: " + id));

        model.addAttribute("vehicle", vehicle);
        model.addAttribute("manufacturers", manufacturerRepository.findAll());
        return "vehicle-edit";
    }

    @PostMapping("/update/{id}")
    public String updateVehicle(@PathVariable Long id,
                                @ModelAttribute Vehicle vehicle,
                                @RequestParam("imageFile") MultipartFile imageFile,
                                Model model) {
        try {
            Vehicle existingVehicle = vehicleRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid vehicle ID: " + id));

            // Since vehicle.getManufacturer().getId() contains the selected manufacturer ID,
            // fetch the full Manufacturer entity from the database
            Manufacturer manufacturer = manufacturerRepository.findById(vehicle.getManufacturer().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid manufacturer ID: " + vehicle.getManufacturer().getId()));

            existingVehicle.setName(vehicle.getName());
            existingVehicle.setType(vehicle.getType());
            existingVehicle.setCreationDate(vehicle.getCreationDate());
            existingVehicle.setDescription(vehicle.getDescription());
            existingVehicle.setManufacturer(manufacturer);

            if (!imageFile.isEmpty()) {
                existingVehicle.setImage(imageFile.getBytes());
            }

            vehicleRepository.save(existingVehicle);
        } catch (IOException e) {
            e.printStackTrace();
            model.addAttribute("error", "Failed to update image");
            return "vehicle-edit";
        }
        return "redirect:/vehicle/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteVehicle(@PathVariable Long id) {
        vehicleRepository.deleteById(id);
        return "redirect:/vehicle/list";
    }


}
