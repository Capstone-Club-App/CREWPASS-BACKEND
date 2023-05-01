package Capstone.Crewpass.service;

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
}