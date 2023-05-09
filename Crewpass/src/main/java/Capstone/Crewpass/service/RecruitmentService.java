package Capstone.Crewpass.service;

import Capstone.Crewpass.dto.RecruitmentDeadlineListInterface;
import Capstone.Crewpass.dto.RecruitmentListInterface;
import Capstone.Crewpass.entity.Recruitment;
import Capstone.Crewpass.repository.RecruitmentRepository;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
public class RecruitmentService {
    private final RecruitmentRepository recruitmentRepository;

    public RecruitmentService(RecruitmentRepository recruitmentRepository) {
        this.recruitmentRepository = recruitmentRepository;
    }

    // 사진 파일 업로드
    public String uploadImage(MultipartFile image) throws IOException {
        //GCP에 저장할 파일의 이름 생성
        String fileName = UUID.randomUUID().toString().concat(image.getOriginalFilename());

        //GCP에 파일 upload
        ClassPathResource resource = new ClassPathResource("capstone-design-385100-a6607b6659e7.json");
        GoogleCredentials credentials = GoogleCredentials.fromStream(resource.getInputStream());
        String projectId = "capstone-design-385100";
        Storage storage = StorageOptions.newBuilder()
                .setProjectId(projectId)
                .setCredentials(credentials)
                .build()
                .getService();

        storage.create(
                BlobInfo.newBuilder("crewpass-cloud-storage", fileName)
                        .build(),
                image.getInputStream()
        );

        // 서버에 등록한 파일명을 반환
        return fileName;
    }

    // 모집글 등록
    public int registerRecruitment(Recruitment recruitment) {
        validateDuplicateRecruitment(recruitment);
        recruitmentRepository.save(recruitment);
        return recruitment.getRecruitmentId();
    }

    // 중복 글 검증
    private void validateDuplicateRecruitment(Recruitment recruitment) {
        recruitmentRepository.findByRecruitmentId(recruitment.getRecruitmentId())
                .ifPresent(r -> {
                    try {
                        throw new IllegalAccessException("이미 존재하는 모집글입니다.");
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                });
    }

    // 로그인한 동아리가 작성한 모집글 목록 조회
    public List<RecruitmentListInterface> checkMyRecruitmentList(Integer crewId) {
        return recruitmentRepository.findMyRecruitmentList(crewId);
    }

    // 동아리 분야 별 최신순으로 모집글 목록 조회
    public List<RecruitmentListInterface> checkRecruitmentListByNewest() {
        return recruitmentRepository.findAllRecruitmentListByNewest();
    }

    // 동아리 분야 별 마감임박순으로 모집글 목록 조회
    public List<RecruitmentDeadlineListInterface> checkRecruitmentListByDeadline() {
        return recruitmentRepository.findAllRecruitmentListByDeadline();
    }
}
