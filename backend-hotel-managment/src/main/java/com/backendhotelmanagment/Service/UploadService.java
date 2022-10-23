package com.backendhotelmanagment.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.UUID;

@Service
public class UploadService {

    private final Logger log = LoggerFactory.getLogger(UploadService.class);
    private final static String DIR_UPLOADS = "uploads";

    public Resource load(String fileName) throws MalformedURLException {

        Path fileRoute = getPath(fileName);

        Resource resource = null;
        log.info(fileRoute.toString());

        resource = new UrlResource(fileRoute.toUri());

        if(!resource.exists() && !resource.isReadable()) throw new RuntimeException("No fue posible cargar el documento: " + fileName);

        return resource;
    }

    public String copyFile(MultipartFile file) throws IOException{

        String fileName = UUID.randomUUID() + "_" + Objects.requireNonNull(file.getOriginalFilename()).replace(" ", "");
        Path fileRoute = getPath(fileName);

        log.info(fileRoute.toString());
        //This will copy selected file to selected route, it means, it will save the image in uploads directory.
        Files.copy(file.getInputStream(), fileRoute);

        return fileName;
    }

    public boolean deleteFile(String fileName){
        //Ask if exists a previous photo.
        if(fileName != null && fileName.length() > 0){
            Path previousDocumentRoute = getPath(fileName);
            File previousDocumentFile = previousDocumentRoute.toFile();

            //And if it exists here, delete it.
            if(previousDocumentFile.exists() && previousDocumentFile.canRead()){
                return previousDocumentFile.delete();
            }
        }
        return false;
    }

    public Path getPath(String fileName){
        return Paths.get(DIR_UPLOADS).resolve(fileName).toAbsolutePath();
    }
}
