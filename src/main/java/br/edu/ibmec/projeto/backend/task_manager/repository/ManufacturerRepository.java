package br.edu.ibmec.projeto.backend.task_manager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import br.edu.ibmec.projeto.backend.task_manager.model.Manufacturer;

public interface ManufacturerRepository extends JpaRepository<Manufacturer, Long> {
}
