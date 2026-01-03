package com.sudheer.fm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.sudheer.fm.entity.FileEntity;

import java.util.List;

public interface FileRepository extends JpaRepository<FileEntity, Long> {
    List<FileEntity> findByUniversityAndBranchAndSemesterAndSubject(
            String university, String branch, String semester, String subject
    );
}
