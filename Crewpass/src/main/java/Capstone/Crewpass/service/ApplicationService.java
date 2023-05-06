package Capstone.Crewpass.service;

import Capstone.Crewpass.entity.Application;
import Capstone.Crewpass.entity.Question;
import Capstone.Crewpass.repository.ApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ApplicationService {
    private final ApplicationRepository applicationRepository;

    @Autowired
    public ApplicationService(ApplicationRepository applicationRepository) {
        this.applicationRepository = applicationRepository;
    }

    // 지원서 등록
    public String registerApplication(Application application) {
        if (validateDuplicateApplication(application) != null) {
            applicationRepository.save(application);
            return "registerApplication - success";
        } else {
            return null;
        }
    }

    // 중복 지원서 검증
    private String validateDuplicateApplication(Application application) {
        Optional<Application> optionalApplication = applicationRepository.findByApplicationId(application.getApplicationId());
        if (optionalApplication.isPresent()) {
            return null;
        } else {
            return "validateDuplicateApplication - success";
        }
    }
}
