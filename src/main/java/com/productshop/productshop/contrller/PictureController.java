package com.productshop.productshop.contrller;

import com.productshop.productshop.dto.ProductDto;
import com.productshop.productshop.entity.Picture;
import com.productshop.productshop.security.jwt.JwtUser;
import com.productshop.productshop.service.PictureService;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class PictureController {

    private final PictureService pictureService;

    public PictureController(PictureService pictureService) {
        this.pictureService = pictureService;
    }

    @GetMapping(value = "/pictures/get", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, byte[]> getPicturesByProduct(@RequestBody ProductDto productDto) throws IOException {
        Map<String, byte[]> result = new HashMap<>();
        List<Picture> picturesByProduct = pictureService.getPicturesByProduct(productDto.getId());
        for (Picture picture : picturesByProduct) {
            File file = new File(picture.getPath());
            if (file.exists()) {
                File absoluteFile = file.getAbsoluteFile();
                byte[] content = Files.readAllBytes(absoluteFile.toPath());
                result.put(file.getName(), content);
            }
        }
        return result;
    }

    @DeleteMapping(value = "/pictures/delete", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void streamVideoFile(Long pictureId, Long productId,
                                @AuthenticationPrincipal JwtUser user) {
        pictureService.removePictureByName(pictureId, productId, user);
    }
}
