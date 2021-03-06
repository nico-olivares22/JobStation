package ar.edu.um.jobs.repository;

import ar.edu.um.jobs.model.Company;
import ar.edu.um.jobs.model.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {

    List<Job> findByCompany(Company company);

    List<Job> findByJobTitleContainingOrDescriptionContaining(String title, String description);
}
