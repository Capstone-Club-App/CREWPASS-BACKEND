package Capstone.Crewpass.service;

import Capstone.Crewpass.dto.ApplicationDetail;
import Capstone.Crewpass.dto.ApplicationRecentList;
import Capstone.Crewpass.dto.RecruitmentDetail;
import Capstone.Crewpass.dto.RecruitmentRecentList;
import Capstone.Crewpass.entity.Application;
import Capstone.Crewpass.entity.Question;
import Capstone.Crewpass.repository.ApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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

    // 로그인한 회원이 지원한 지원서 목록 조회
    public List<ApplicationRecentList> checkMyApplicationList(Integer userId) {
        return applicationRepository.findMyApplicationList(userId);
    }



    // 선택한 지원서 상세 조회
    public List<ApplicationDetail> checkApplicationDetail(Integer applicationId) {

        return applicationRepository.getApplicationDetail(applicationId);
    }
}
