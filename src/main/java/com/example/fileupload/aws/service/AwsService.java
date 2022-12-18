package com.example.fileupload.aws.service;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AwsService {
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final AmazonS3 amazonS3;

    public String sendFileToS3Bucket(MultipartFile file) throws IOException {

        String originalFileName = file.getOriginalFilename();

        //파일의 확장자명 떼기
        String ext = file.getOriginalFilename().substring(originalFileName.lastIndexOf("."));

        //랜덤 파일명 생성
        String filename = UUID.randomUUID().toString();
        
        //랜덤 파일명 + 업로드 파일 확장자 합체
        filename += filename + ext;

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(file.getInputStream().available());

        amazonS3.putObject(bucket, filename, file.getInputStream(), objectMetadata);

        //S3 객체에 넣어 줌.
        return amazonS3.getUrl(bucket, filename).toString();
    }


}
