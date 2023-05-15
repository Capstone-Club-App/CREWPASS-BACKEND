package Capstone.Crewpass.repository;

import Capstone.Crewpass.entity.Scrap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
@Repository
public interface ScrapRepository extends JpaRepository<Scrap, Integer> {
//    // Scrap Id로 찾기
//    Optional<Scrap> findByScrapId(Integer scrapId);
}
