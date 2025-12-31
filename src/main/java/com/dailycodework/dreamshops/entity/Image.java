package com.dailycodework.dreamshops.entity;

import lombok.Data;

import java.sql.Blob;

@Data
public class Image {
    private Long id;
    private String fileName;
    private String fileType;
    private Blob image;
    private String downloadUrl;
}
