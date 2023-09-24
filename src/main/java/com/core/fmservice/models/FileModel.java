package com.core.fmservice.models;

import lombok.*;
import javax.persistence.*;
import java.io.Serializable;
import java.util.*;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
@Entity
@Table(name = "file")
public class FileModel implements Serializable {
    public static final String TableName="file";

    @Id
    @Column(name = "id")
    private UUID  id;

    @Column(name = "parent_id",nullable=true)
    private UUID  parentId;

    @Column(name = "owner",nullable=false)
    private String owner;

    @Column(name = "type",nullable=false)
    private String type;

    @Column(name = "name",nullable=false)
    private String name;

    @Column(name = "contentType",nullable=true)
    private String contentType;

    @Column(name = "extenstion",nullable=true)
    private String extenstion;

    @Column(name = "path",nullable=false)
    private String path;

    @Column(name = "filedownloaduri",nullable=true)
    private String fileDownloadUri;
    

    @Column(name = "size",nullable=true)
    private long size;


    @Column(name = "flag",nullable=true)
    private int flag;


    @Column(name = "createdAt",nullable=false)
    private String createdAt;

    @Column(name = "updatedAt",nullable=false)
    private String updatedAt;

}
