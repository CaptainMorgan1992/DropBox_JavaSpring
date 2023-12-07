package me.code.individual_assignment.dto;

import me.code.individual_assignment.model.Image;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

/**
 * Represents data for downloading an image, including the image resource and associated information.
 */
public class DownloadImageData {

    private final Resource resource;
    private final Image image;

    /**
     * Constructs DownloadImageData with the provided resource and image.
     *
     * @param resource The image resource.
     * @param image    The associated image.
     */
    public DownloadImageData(Resource resource, Image image) {
        this.resource = resource;
        this.image = image;
    }

    /**
     * Converts the image data to a ResponseEntity for downloading.
     *
     * @return ResponseEntity containing the image for download.
     */
    public ResponseEntity<Resource> toResponseEntity() {
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + this.image.getName())
                .contentType(MediaType.parseMediaType("image/jpeg"))
                .contentLength(this.image.getSize())
                .body(this.resource);
    }

    /**
     * Gets the name of the image file.
     *
     * @return The image file name.
     */
    public String getFileName() {
        return this.image.getName();
    }

    /**
     * Gets the size of the image file.
     *
     * @return The image file size.
     */
    public int getFileSize() {
        return (int) this.image.getSize();
    }

    /**
     * Gets the content type of the image file.
     *
     * @return The image file content type.
     */
    public String getContentType() {
        return "image/jpeg";
    }

    /**
     * Gets the ID of the associated image.
     *
     * @return The image ID.
     */
    public int getId() {
        return this.image.getId();
    }
}
