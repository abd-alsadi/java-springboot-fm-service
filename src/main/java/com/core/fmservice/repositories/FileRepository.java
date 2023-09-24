package com.core.fmservice.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import javax.transaction.Transactional;
import com.core.fmservice.models.*;
import java.util.*;


@Repository
@Transactional
public interface FileRepository extends JpaRepository<FileModel,UUID>{
    List<FileModel> findByOwner(String name);
}