package com.core.fmservice.services;
import com.core.fmservice.repositories.*;

import antlr.StringUtils;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

import com.core.fmservice.iservices.IFileService;
import com.core.fmservice.models.*;
import com.core.fmservice.models.ui.AuthunticationModel;

import org.springframework.beans.factory.annotation.Autowired;
import java.util.*;
import javax.persistence.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;

import java.rmi.server.UID;
import java.time.LocalDate ;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import com.core.fmservice.responses.UploadFileResponse;
import java.net.MalformedURLException;
import java.nio.file.*;
import java.util.UUID;
import com.core.fmservice.iservices.*;
import com.core.fmservice.models.FileModel;
import com.core.fmservice.helpers.ConfigHelper;
import com.core.fmservice.helpers.PathHelper;

import org.springframework.core.env.Environment;

@Service
public class FileService implements IFileService{

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
    private FileRepository repo;

    @Autowired
    private EntityManager emf;

    @Autowired
    Environment env;

    @Override
    public List<FileModel> GetAll(AuthunticationModel auth){
        try{
            return repo.findByOwner(auth.getUsername());
        }catch(Exception e){
            return null;
        }
    }

    @Override
    public FileModel Add(FileModel model, AuthunticationModel auth){
        try{

            String newID="";
            if (model.getId()==null)
            newID =UUID.randomUUID().toString();

            model.setId(UUID.fromString(newID)); 
            model.setFlag(0);
            LocalDate localDate = LocalDate.now();
            String currentDate = localDate.toString();
            model.setCreatedAt(currentDate);
            model.setUpdatedAt(currentDate);
            model.setOwner(auth.getUsername());

            String fullDir= model.getOwner();//PathHelper.getPathDir(model);
            Path cuurentDir = Path.of(ConfigHelper.props(env).getDIR()+"/"+fullDir);
            if(!Files.exists(cuurentDir))
                Files.createDirectories(cuurentDir);


                Path targetLocation = cuurentDir.resolve(newID);
                if(model.getType().equals("file")){
                    Files.createFile(targetLocation);
                }else{
                    Files.createDirectories(targetLocation);
                }
              
                fullDir=fullDir+"/"+newID;

            model.setPath(fullDir);

            FileModel data= repo.save(model);
            return data;
         }catch(Exception e){
            return null;
        }
    }
    


    @Override
    public boolean Delete(List<UUID> ids, AuthunticationModel auth) {
        try{
            List<FileModel> lst  =repo.findAllById(ids);
            for (int i=0;i<lst.size();i++){
                FileModel model = lst.get(i);
                String newID = lst.get(i).getId().toString();

                String fullDir= model.getOwner();//PathHelper.getPathDir(model);
                Path cuurentDir = Path.of(ConfigHelper.props(env).getDIR()+"/"+fullDir);
                Path targetLocation = cuurentDir.resolve(newID);
                Files.deleteIfExists(targetLocation);
            }

            repo.deleteAllByIdInBatch(ids);
            return true;
        }catch(Exception e){
            return false;
        }
    }

    @Override
    public boolean Move(HashMap<String,String> ids,AuthunticationModel auth){
        try{
            List<UUID> uids = new ArrayList<UUID>();
            for (Map.Entry<String, String> set :ids.entrySet()) {
                uids.add(UUID.fromString(set.getKey()));
            }
            List<FileModel> lst  =repo.findAllById(uids);
            for (int i=0;i<lst.size();i++){
                String u = lst.get(i).getId().toString();
                String value = ids.get(u);
                if(value==null || value.equals("")){
                    lst.get(i).setParentId(null);
                }else{
                    lst.get(i).setParentId(UUID.fromString(ids.get(u)));
                }
              
            }
            repo.saveAll(lst);
            return true;
        }catch(Exception e){
            return false;
        }
    }
    @Override
    public List<FileModel> Copy(HashMap<String,String> ids,AuthunticationModel auth){
        try{
            List<UUID> uids = new ArrayList<UUID>();
            for (Map.Entry<String, String> set :ids.entrySet()) {
                uids.add(UUID.fromString(set.getKey()));
            }
            List<FileModel> lst  =repo.findAllById(uids);
            List<FileModel> newList =new ArrayList<FileModel>();
            for (int i=0;i<lst.size();i++){
                FileModel file = lst.get(i);
                LocalDate localDate = LocalDate.now();
                String currentDate = localDate.toString();
                FileModel newFile = new FileModel(file.getId(), file.getParentId(), file.getOwner(), file.getType(), file.getName(), file.getContentType(), file.getExtenstion(), file.getPath(), file.getFileDownloadUri(), file.getSize(), file.getFlag(), currentDate, currentDate);

                String u = newFile.getId().toString();
                String value = ids.get(u);
                if(value==null || value.equals("")){
                    newFile.setParentId(null);
                }else{
                    newFile.setParentId(UUID.fromString(ids.get(u)));
                }
                String newID=UUID.randomUUID().toString();
                newFile.setId(UUID.fromString(newID));

                String fullDir= newFile.getOwner();//PathHelper.getPathDir(model);
                Path cuurentDir = Path.of(ConfigHelper.props(env).getDIR()+"/"+fullDir);
                if(!Files.exists(cuurentDir))
                    Files.createDirectories(cuurentDir);
    
    
                    Path targetLocation = cuurentDir.resolve(newID);
                    if(newFile.getType().equals("file")){
                        Files.createFile(targetLocation);
                    }else{
                        Files.createDirectories(targetLocation);
                    }
                    fullDir=fullDir+"/"+newID;
    
                    newFile.setPath(fullDir);
              newList.add(newFile);
            }
            repo.saveAll(newList);
            return newList;
        }catch(Exception e){
            return null;
        }
    }
    @Override
    public FileModel GetByID(UUID id, AuthunticationModel auth) {
        try{
            FileModel data= repo.findById(id).orElse(null);
            return data;
        }catch(Exception e){
            return null;
        }
    }

    @Override
    public FileModel Rename(UUID id, FileModel object, AuthunticationModel auth) {
        try{
            object.setId(id);
            LocalDate localDate = LocalDate.now();
            String currentDate = localDate.toString();
            object.setUpdatedAt(currentDate);
            FileModel data= repo.save(object);
            return data;
        }catch(Exception e){
            return null;
        }
    }


        public FileModel Upload(MultipartFile file,String parentId, AuthunticationModel auth) {
        try{
                FileModel model = new FileModel();
                LocalDate localDate = LocalDate.now();
                String currentDate = localDate.toString();

                String newID = UUID.randomUUID().toString();
                String fileName = file.getOriginalFilename();

                model.setId(UUID.fromString(newID));
                model.setName(fileName);
                model.setSize(file.getSize());
                model.setContentType(file.getContentType());
                model.setFileDownloadUri("");
                model.setType("file");
                model.setExtenstion("");
                model.setFlag(0);
                model.setCreatedAt(currentDate);
                model.setUpdatedAt(currentDate);
                model.setOwner(auth.getUsername());

                if(parentId==null || parentId.equals("null")){
                    model.setParentId(null);
                }else{
                    model.setParentId(UUID.fromString(parentId));
                }
              
                    String fullDir= model.getOwner();//PathHelper.getPathDir(model);
                    Path cuurentDir = Path.of(ConfigHelper.props(env).getDIR()+"/"+fullDir);
                    if(!Files.exists(cuurentDir))
                        Files.createDirectories(cuurentDir);
        
                    Path targetLocation = cuurentDir.resolve(newID);
                    Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
                    fullDir=fullDir+"/"+newID;
                    model.setPath(fullDir);
                    FileModel result =repo.save(model);
                return result;
        }catch(Exception e){
            return null;
        } 
    }

        public Resource Download(List<String> ids , AuthunticationModel auth) {
        try {
            String id=ids.get(0);
            FileModel model=GetByID(UUID.fromString(id),auth);
            if (model == null){
                return null;
            }
            Path cuurentPath = Path.of(ConfigHelper.props(env).getDIR()+"/"+model.getPath());
            //Path filePath = cuurentPath.resolve(id).normalize();
            Resource resource = new UrlResource(cuurentPath.toUri());
            if(resource.exists()) {
                return resource;
            } else {
                return null;
            }
        } catch (MalformedURLException ex) {
           return null;
        }
    }
 
}
