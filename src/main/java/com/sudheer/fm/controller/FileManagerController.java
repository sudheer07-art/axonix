////package com.sudheer.fm.controller;
////
////import com.sudheer.fm.entity.FileEntity;
////import com.sudheer.fm.model.FileInfo;
////import com.sudheer.fm.repository.FileRepository;
////import com.sudheer.fm.service.FileStorageService;
////import org.springframework.core.io.Resource;
////import org.springframework.http.ResponseEntity;
////import org.springframework.security.access.prepost.PreAuthorize;
////import org.springframework.web.bind.annotation.*;
////import org.springframework.web.multipart.MultipartFile;
////
////import java.util.List;
////
////@RestController
////@RequestMapping("/files")
////@CrossOrigin(origins = "*")
////public class FileManagerController {
////    private final FileRepository repo;
////    private final FileStorageService service;
////    public FileManagerController(FileRepository repo, FileStorageService storage) {
////        this.repo = repo;
////        this.storage = storage;
////    }
////
////    // 👨‍🏫 UPLOAD
////    @PostMapping("/upload")
////    public ResponseEntity<?> upload(
////            @RequestParam MultipartFile file,
////            @RequestParam String university,
////            @RequestParam String branch,
////            @RequestParam String semester,
////            @RequestParam String subject
////    ) throws Exception {
////
////        String path = storage.saveFile(file);
////
////        FileEntity f = new FileEntity();
////        f.setFilename(file.getOriginalFilename());
////        f.setUniversity(university);
////        f.setBranch(branch);
////        f.setSemester(semester);
////        f.setSubject(subject);
////        f.setFilepath(path);
////
////        repo.save(f);
////
////        return ResponseEntity.ok("Uploaded successfully");
////    }
////
////    // 👩‍🎓 FETCH FOR STUDENTS
////    @GetMapping("/list")
////    public List<FileEntity> list(
////            @RequestParam String university,
////            @RequestParam String branch,
////            @RequestParam String semester,
////            @RequestParam String subject
////    ) {
////        return repo.findByUniversityAndBranchAndSemesterAndSubject(
////                university, branch, semester, subject
////        );
////    }
////}
////    public FileManagerController(FileStorageService service) {
////        this.service = service;
////    }
////
////    // ================= UPLOAD (TEACHER ONLY) =================
////    @PostMapping("/upload")
////    @PreAuthorize("hasAuthority('ROLE_TEACHER')")
////    public ResponseEntity<String> upload(@RequestParam("file") MultipartFile file)
////            throws Exception {
////
////        service.saveFile(file);
////        return ResponseEntity.ok("File uploaded successfully");
////    }
////
////    // ================= LIST (STUDENT + TEACHER) =================
////    @GetMapping("/list")
////    @PreAuthorize("hasAnyAuthority('ROLE_STUDENT','ROLE_TEACHER')")
////    public List<FileInfo> listFiles() {
////        return service.getAllFiles();
////    }
////
////    // ================= DOWNLOAD (STUDENT + TEACHER) =================
////    @GetMapping("/download/{id}")
////    @PreAuthorize("hasAnyAuthority('ROLE_STUDENT','ROLE_TEACHER')")
////    public ResponseEntity<Resource> download(@PathVariable Long id) {
////        return service.downloadFile(id);
////    }
////    // ================= DELETE (TEACHER ONLY) =================
////    @DeleteMapping("/delete/{id}")
////    @PreAuthorize("hasAuthority('ROLE_TEACHER')")
////    public ResponseEntity<String> deleteFile(@PathVariable Long id) {
////
////        service.deleteFile(id);
////        return ResponseEntity.ok("File deleted successfully");
////    }
////
////}
//package com.sudheer.fm.controller;
//
//import com.sudheer.fm.entity.FileEntity;
//import com.sudheer.fm.repository.FileRepository;
//import com.sudheer.fm.service.FileStorageService;
//import org.springframework.core.io.Resource;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/files")
//@CrossOrigin(origins = "*")
//public class FileManagerController {
//
//    private final FileRepository repo;
//    private final FileStorageService storage;
//
//    public FileManagerController(FileRepository repo, FileStorageService storage) {
//        this.repo = repo;
//        this.storage = storage;
//    }
//
//    // ===================== UPLOAD (TEACHER ONLY) =====================
//    @PostMapping("/upload")
//    @PreAuthorize("hasAuthority('ROLE_TEACHER')")
//    public ResponseEntity<String> upload(
//            @RequestParam("file") MultipartFile file,
//            @RequestParam String university,
//            @RequestParam String branch,
//            @RequestParam String semester,
//            @RequestParam String subject
//    ) throws Exception {
//
//        String path = storage.saveFile(file);
//
//        FileEntity entity = new FileEntity();
//        entity.setFilename(file.getOriginalFilename());
//        entity.setUniversity(university);
//        entity.setBranch(branch);
//        entity.setSemester(semester);
//        entity.setSubject(subject);
//        entity.setFilepath(path);
//
//        repo.save(entity);
//
//        return ResponseEntity.ok("File uploaded successfully");
//    }
//
//    // ===================== LIST (STUDENT + TEACHER) =====================
//    @GetMapping("/list")
//    @PreAuthorize("hasAnyAuthority('ROLE_STUDENT','ROLE_TEACHER')")
//    public List<FileEntity> list(
//            @RequestParam String university,
//            @RequestParam String branch,
//            @RequestParam String semester,
//            @RequestParam String subject
//    ) {
//        return repo.findByUniversityAndBranchAndSemesterAndSubject(
//                university, branch, semester, subject
//        );
//    }
//
//    // ===================== DOWNLOAD (STUDENT + TEACHER) =====================
//    @GetMapping("/download/{id}")
//    @PreAuthorize("hasAnyAuthority('ROLE_STUDENT','ROLE_TEACHER')")
//    public ResponseEntity<Resource> download(@PathVariable Long id) {
//        return storage.downloadFile(id);
//    }
//
//    // ===================== DELETE (TEACHER ONLY) =====================
//    @DeleteMapping("/delete/{id}")
//    @PreAuthorize("hasAuthority('ROLE_TEACHER')")
//    public ResponseEntity<String> delete(@PathVariable Long id) {
//        storage.deleteFile(id);
//        repo.deleteById(id);
//        return ResponseEntity.ok("File deleted successfully");
//    }
//}
package com.sudheer.fm.controller;

import com.sudheer.fm.entity.FileEntity;
import com.sudheer.fm.repository.FileRepository;
import com.sudheer.fm.service.FileStorageService;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/files")
public class FileManagerController {

    private final FileRepository repo;
    private final FileStorageService storage;

    public FileManagerController(FileRepository repo, FileStorageService storage) {
        this.repo = repo;
        this.storage = storage;
    }

    // 👨‍🏫 UPLOAD
    @PostMapping("/upload")
    @PreAuthorize("hasAuthority('ROLE_TEACHER')")
    public ResponseEntity<String> upload(
            @RequestParam MultipartFile file,
            @RequestParam String university,
            @RequestParam String branch,
            @RequestParam String semester,
            @RequestParam String subject
    ) throws Exception {

        String path = storage.saveFile(file);

        FileEntity f = new FileEntity();
        f.setFilename(file.getOriginalFilename());
        f.setUniversity(university);
        f.setBranch(branch);
        f.setSemester(semester);
        f.setSubject(subject);
        f.setFilepath(path);

        repo.save(f);
        return ResponseEntity.ok("Uploaded");
    }

    // 👩‍🎓 LIST
    @PreAuthorize("hasAnyAuthority('ROLE_STUDENT','ROLE_TEACHER')")
    @GetMapping("/list")
    public List<FileEntity> list(
            @RequestParam String university,
            @RequestParam String branch,
            @RequestParam String semester,
            @RequestParam String subject
    ) {
        return repo.findByUniversityAndBranchAndSemesterAndSubject(
                university, branch, semester, subject
        );
    }

    // 📥 DOWNLOAD
    @PreAuthorize("hasAnyAuthority('ROLE_STUDENT','ROLE_TEACHER')")
    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> download(@PathVariable Long id) throws Exception {
        FileEntity f = repo.findById(id).orElseThrow();
        Resource res = storage.loadFile(f.getFilepath());
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=\"" + f.getFilename() + "\"")
                .body(res);
    }

    // ❌ DELETE (TEACHER)
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('ROLE_TEACHER')")
    public ResponseEntity<String> delete(@PathVariable Long id) throws Exception {
        FileEntity f = repo.findById(id).orElseThrow();
        storage.deleteFile(f.getFilepath());
        repo.delete(f);
        return ResponseEntity.ok("Deleted");
    }
}
