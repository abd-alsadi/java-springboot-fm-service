package com.core.fmservice.iservices;

import java.util.List;
import java.util.*;

import com.core.fmservice.models.*;
import com.core.fmservice.models.ui.*;



public interface IFileService {
	
	public List<FileModel> GetAll(AuthunticationModel auth);

	public FileModel Add(FileModel model,AuthunticationModel auth);
		
	public FileModel GetByID(UUID id,AuthunticationModel auth);

    public FileModel Rename(UUID id,FileModel model,AuthunticationModel auth);

	public boolean Delete(List<UUID> ids,AuthunticationModel auth);	
	public boolean Move(HashMap<String,String> ids,AuthunticationModel auth);	
	public List<FileModel> Copy(HashMap<String,String> ids,AuthunticationModel auth);	
}