package me.code.individual_assignment.DownloadImageData;

import me.code.individual_assignment.model.Image;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

public class DownloadImageData {

    private Resource resource;
    private Image image;

    public DownloadImageData(Resource resource, Image image) {
        this.resource = resource;
        this.image = image;
    }

    public ResponseEntity<Resource> toResponseEntity() {
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + this.image.getName())
                .contentType(MediaType.parseMediaType("image/jpeg"))
                .contentLength(this.image.getSize())
                .body(this.resource);
    }
}
