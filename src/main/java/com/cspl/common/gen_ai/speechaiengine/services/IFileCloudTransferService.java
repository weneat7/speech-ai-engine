package com.cspl.common.gen_ai.speechaiengine.services;

public interface IFileCloudTransferService {
    public String transferFileFromUrl(String fileUrl, String newFileName, String encodedAuth);
    public String getSignedUrl(String fileName,int timeInHours);
}
