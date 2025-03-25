package greenjangtanji.yeosuro.image.controller;

import greenjangtanji.yeosuro.global.exception.BusinessLogicException;
import greenjangtanji.yeosuro.global.exception.ExceptionCode;
import greenjangtanji.yeosuro.image.entity.ImageType;
import greenjangtanji.yeosuro.image.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/images")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    @PostMapping("/upload")
    public ResponseEntity<List<String>> uploadImages( @RequestParam(value = "file") List<MultipartFile> file,
                                                      @RequestParam("type") String type) {
        // imageFiles가 null인 경우 처리
        if (file == null) {
            throw new BusinessLogicException(ExceptionCode.FILE_UPLOAD_ERROR);
        }
        // 최대 이미지 파일 수 제한
        if (file.size() > 5) {
            throw new BusinessLogicException(ExceptionCode.FILE_SIZE_ERROR);
        }

        ImageType imageType = ImageType.valueOf(type.toUpperCase());
        List<String> imageUrls = imageService.uploadFile(file,imageType);
        return ResponseEntity.ok(imageUrls);
    }


    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteFile(@RequestParam String imageUrl) {
        imageService.deleteImage(imageUrl);
        return ResponseEntity.noContent().build();
    }
}
