package com.example.fileupload;

import com.amazonaws.services.s3.AmazonS3;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class FileUploadApplicationTests {

	@Value("${cloud.aws.s3.bucket}")
	private String bucket;

	@Autowired
	private AmazonS3 amazonS3;


	//s3 image 삭제
	@Test
	void contextLoads() {
		amazonS3.deleteObject(bucket, "25bab2d8-f275-4c45-ab18-da14899c12bf25bab2d8-f275-4c45-ab18-da14899c12bf.jpg");
	}

}
