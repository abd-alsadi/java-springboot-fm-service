package com.core.fmservice.controllers;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.core.fmservice.services.*;
import com.core.fmservice.models.*;
import com.core.fmservice.responses.DataResponse;
import com.core.fmservice.responses.FilterDataResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import java.util.*;

import com.core.fmservice.models.ui.*;
import com.core.fmservice.constants.ModConstant;
import com.core.fmservice.helpers.AuthHelper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
 import org.springframework.core.io.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/File")
@RequiredArgsConstructor
public class FileController{

    @Autowired
    private FileService service;
    
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @GetMapping("/GetAll")
    public ResponseEntity<DataResponse> GetAll(Authentication auth){
        DataResponse response=new DataResponse();
		try{
            AuthunticationModel authModel = AuthHelper.getAuthunticationUser(auth);
			List<FileModel> data= service.GetAll(authModel);
			response = new DataResponse(null, data, ModConstant.StatusCode.SUCCESS);
		}catch(Exception e){
			response = new DataResponse(e.getMessage(), null, ModConstant.StatusCode.ERROR);
			logger.info(ModConstant.NOT_SUCCESS_TAG);
		}
        return new ResponseEntity(response, HttpStatus.OK);
    }

   
    @GetMapping("/GetByID/{id}")
    public ResponseEntity<DataResponse> GetByID(  @PathVariable("id") UUID id,Authentication auth){
        DataResponse response=new DataResponse();
        try{
            AuthunticationModel authModel = AuthHelper.getAuthunticationUser(auth);
            FileModel data= service.GetByID(id,authModel);
             response = new DataResponse(null, data, ModConstant.StatusCode.SUCCESS);
        }catch(Exception e){
             response = new DataResponse(e.getMessage(), null, ModConstant.StatusCode.ERROR);
        }
        return new ResponseEntity(response, HttpStatus.OK);
    }
    @PostMapping("/Add")
     public ResponseEntity<DataResponse> Add( @RequestBody FileModel object,Authentication auth){
         DataResponse response=new DataResponse();
         try{
            AuthunticationModel authModel = AuthHelper.getAuthunticationUser(auth);
            FileModel data= service.Add(object,authModel);
             response = new DataResponse(null, data, ModConstant.StatusCode.SUCCESS);
         }catch(Exception e){
             response = new DataResponse(e.getMessage(), null, ModConstant.StatusCode.ERROR);
         }
         return new ResponseEntity(response, HttpStatus.OK);
    }
    @PostMapping("/Delete")
    public ResponseEntity<DataResponse> Delete(@RequestBody List<UUID> id,Authentication auth){
        DataResponse response=new DataResponse();
        try{
            AuthunticationModel authModel = AuthHelper.getAuthunticationUser(auth);
            boolean data= service.Delete(id,authModel);
            response = new DataResponse(null, data, ModConstant.StatusCode.SUCCESS);
        }catch(Exception e){
           response = new DataResponse(e.getMessage(), null, ModConstant.StatusCode.ERROR);
        }
        return new ResponseEntity(response, HttpStatus.OK);
    }
    @PutMapping("/Rename/{id}")
    public ResponseEntity<DataResponse> Rename(  @PathVariable("id") UUID id,  @RequestBody FileModel object,Authentication auth){
        DataResponse response=new DataResponse();
       try{
        AuthunticationModel authModel = AuthHelper.getAuthunticationUser(auth);
        FileModel data= service.Rename(id,object,authModel);
            response = new DataResponse(null, data, ModConstant.StatusCode.SUCCESS);
       }catch(Exception e){
            response = new DataResponse(e.getMessage(), null, ModConstant.StatusCode.ERROR);
       }
       return new ResponseEntity(response, HttpStatus.OK);
    }

    @PostMapping("/Move")
    public ResponseEntity<DataResponse> Move( @RequestBody HashMap<String,String> ids,Authentication auth){
        DataResponse response=new DataResponse();
       try{
        AuthunticationModel authModel = AuthHelper.getAuthunticationUser(auth);
        boolean data= service.Move(ids,authModel);
            response = new DataResponse(null, data, ModConstant.StatusCode.SUCCESS);
       }catch(Exception e){
            response = new DataResponse(e.getMessage(), null, ModConstant.StatusCode.ERROR);
       }
       return new ResponseEntity(response, HttpStatus.OK);
    }
    @PostMapping("/Copy")
    public ResponseEntity<DataResponse> Copy( @RequestBody HashMap<String,String> ids,Authentication auth){
        DataResponse response=new DataResponse();
       try{
        AuthunticationModel authModel = AuthHelper.getAuthunticationUser(auth);
        List<FileModel> data= service.Copy(ids,authModel);
            response = new DataResponse(null, data, ModConstant.StatusCode.SUCCESS);
       }catch(Exception e){
            response = new DataResponse(e.getMessage(), null, ModConstant.StatusCode.ERROR);
       }
       return new ResponseEntity(response, HttpStatus.OK);
    }

    @PostMapping("/Upload/{parentId}")
    public ResponseEntity<DataResponse> Upload(@RequestParam("file") MultipartFile file,@PathVariable("parentId") String parentId ,Authentication auth) {  
        DataResponse response=new DataResponse();
        try{
         AuthunticationModel authModel = AuthHelper.getAuthunticationUser(auth);
         FileModel data= service.Upload(file,parentId,authModel);
             response = new DataResponse(null, data, ModConstant.StatusCode.SUCCESS);
        }catch(Exception e){
             response = new DataResponse(e.getMessage(), null, ModConstant.StatusCode.ERROR);
        }
        return new ResponseEntity(response, HttpStatus.OK);
    }

    @PostMapping("/Download")
    public ResponseEntity<Resource> Download(@RequestParam String ids,Authentication auth, HttpServletRequest request) {
        String contentType = null;
        Resource resource= null;
        try {
            String[] lstParam= ids.split(",");
            List<String> IDS = new ArrayList<String>();
            for (int i= 0 ;i<lstParam.length;i++){
                IDS.add(lstParam[i]);
            }
            AuthunticationModel authModel = AuthHelper.getAuthunticationUser(auth);
            resource = service.Download(IDS,authModel);
            if(resource!=null)
                contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            logger.info("Could not determine file type.");
        }

        if(contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}