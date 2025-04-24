package com.linkedin;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Component
public class BatchJobCompletedListener implements JobExecutionListener {

    @Override
    public void afterJob(JobExecution jobExecution){
        if(jobExecution.getStatus() == BatchStatus.COMPLETED){
            String filePath = "/Users/chai/Downloads/masked-data.csv";
            try {
                Files.lines(Paths.get(filePath)).forEach(System.out::println);
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }

}
