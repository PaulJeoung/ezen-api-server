package code.code_api.util;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component // 자동 bean 생성
@Slf4j
@RequiredArgsConstructor
public class CustomFileUtil {
    @Value("${org.zerock.upload.path}")
    private String uploadPath;

    @PostConstruct // beans 가 생성 되고 DI 완료 후 아래 method를 자동 호출해서 폴더 생성
    public void init() {
        File tempFolder = new File(uploadPath);
        if (!tempFolder.exists()) {
            tempFolder.mkdirs();
        }
        String uploadPath = tempFolder.getAbsolutePath();
        log.info("upload_folder_path : {}", uploadPath);
    }

    public List<String> saveFiles(List<MultipartFile> files) {
        if (files == null || files.size() == 0) {
            return null;
        }
        List<String> uploadNames = new ArrayList<>();
        for(MultipartFile multipartFile : files) { // random 하게 저장할 파일이름 생성 (upload/uuid-cc1-2is-i35h-h13_filename.jpg 로 저장됨)
            String savedName = UUID.randomUUID().toString() + "_" + multipartFile.getOriginalFilename();
            Path savePath = Paths.get(uploadPath, savedName);
            try {
                Files.copy(multipartFile.getInputStream(), savePath);
                String contentType = multipartFile.getContentType();
                if(contentType != null && contentType.startsWith("image")){ // thumbnail 만들기
                    Path thumbnailPath = Paths.get(uploadPath, "s_" + savedName);
                    Thumbnails.of(savePath.toFile())
                            .size(200,200)
                            .toFile(thumbnailPath.toFile());
                }
                
                uploadNames.add(savedName);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return uploadNames;
    }

    public ResponseEntity<Resource> getFile(String fileName) { // 업로드 파일 보여주기  /api/products/view/file_name
        Resource resource = new FileSystemResource(uploadPath + File.separator + fileName);
        log.info("");
        if(!resource.exists()){
            new FileSystemResource(uploadPath + File.separator + "default.jpg");
        }
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.add("Content-Type", Files.probeContentType(resource.getFile().toPath()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok().headers(headers).body(resource);
    }

    public void deleteFiles(List<String> filenames) {
        if(filenames == null && filenames.isEmpty()) {
            return;
        }
        filenames.forEach(filename -> {
            String thumbnailFilenames = "s_" + filename;
            Path thumbnailPath = Paths.get(uploadPath, thumbnailFilenames);
            Path filePath = Paths.get(uploadPath, filename);
            try {
                Files.deleteIfExists(filePath);
                Files.deleteIfExists(thumbnailPath);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
