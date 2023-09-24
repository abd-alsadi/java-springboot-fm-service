// package com.core.fmservice.services;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.core.io.Resource;
// import org.springframework.core.io.UrlResource;
// import org.springframework.stereotype.Service;
// import org.springframework.util.StringUtils;
// import org.springframework.web.multipart.MultipartFile;
// import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
// import com.core.fmservice.responses.UploadFileResponse;
// import java.net.MalformedURLException;
// import java.nio.file.*;
// import java.util.UUID;
// import com.core.fmservice.iservices.*;
// import com.core.fmservice.models.FileModel;
// import com.core.fmservice.helpers.ConfigHelper;
// import org.springframework.core.env.Environment;

// @Service
// public class FileStorageService implements IFileStorageService{

//     @Autowired
//     Environment env;

//     @Autowired
//     FileService fileService;
//     public UploadFileResponse upload(MultipartFile file) {
//         try{
//             String newID = UUID.randomUUID().toString();
//             String fileName = StringUtils.cleanPath(file.getOriginalFilename());
//             Path cuurentPath = Path.of(ConfigHelper.props(env).getDIR()+"/"+newID);

//             Files.createDirectories(cuurentPath);

//             Path targetLocation = cuurentPath.resolve(newID);
//             Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

//             String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
//             .path("/api/FileStorage/downloadFile/").path(newID).toUriString();

//             FileModel model = new FileModel();
//             model.setId(UUID.fromString(newID));
//             model.setFileName(fileName);
//             model.setSize(file.getSize());
//             model.setContentType(file.getContentType());
//             model.setFileDownloadUri(fileDownloadUri);
//             model.setPath(cuurentPath.toString());
//             model=fileService.Add(model);

//             return new UploadFileResponse(model.getId().toString(),model.getFileName(), fileDownloadUri,
//             file.getContentType(), model.getSize());

//         }catch(Exception e){
//             return null;
//         }
      
//     }

//     public Resource download(String id) {
//         try {
//             FileModel model=fileService.GetByID(UUID.fromString(id));
//             if (model == null){
//                 return null;
//             }
//             Path cuurentPath = Path.of(model.getPath());
//             Path filePath = cuurentPath.resolve(id).normalize();
//             Resource resource = new UrlResource(filePath.toUri());
//             if(resource.exists()) {
//                 return resource;
//             } else {
//                 return null;
//             }
//         } catch (MalformedURLException ex) {
//            return null;
//         }
//     }
// }
