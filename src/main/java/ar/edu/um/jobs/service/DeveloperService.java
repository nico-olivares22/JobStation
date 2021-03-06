package ar.edu.um.jobs.service;

import ar.edu.um.jobs.model.Application;
import ar.edu.um.jobs.model.Developer;
import ar.edu.um.jobs.model.Interview;
import ar.edu.um.jobs.model.User;
import ar.edu.um.jobs.repository.ApplicationRepository;
import ar.edu.um.jobs.repository.InterviewRepository;
import ar.edu.um.jobs.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DeveloperService extends GenericServiceImpl<User>{

    private final UserRepository developerRepository;
    private final InterviewRepository interviewRepository;
    private final ApplicationRepository applicationRepository;
    private final PasswordEncoder passwordEncoder;

    public DeveloperService(UserRepository developerRepository, InterviewRepository interviewRepository, ApplicationRepository applicationRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.developerRepository = developerRepository;
        this.interviewRepository = interviewRepository;
        this.applicationRepository = applicationRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User create(User entity) {
        if (developerRepository.findByEmail(entity.getEmail()).isPresent()) return null;

        entity.setPassword(passwordEncoder.encode(entity.getPassword()));
        return developerRepository.save(entity);
    }

    @Override
    JpaRepository<User, Long> getRepository() {
        return developerRepository;
    }

    public List<Interview> listInterviews() {

        Long developerId = developerRepository.getCurrentUser().get().getId();

        return interviewRepository.findByDeveloperOrderByDate(this.get(developerId).get()).stream()
                .filter(interview -> interview.getDate().isAfter(LocalDate.now().minusDays(1)))
                .collect(Collectors.toList());

    }


    public List<Application> listApplications() {

        Long developerId = developerRepository.getCurrentUser().get().getId();

        return applicationRepository.findByDeveloper(this.get(developerId).get());
    }

    public Developer getCurrentDeveloper() {
        return (Developer) developerRepository.getCurrentUser().get();
    }
}
