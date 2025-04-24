package com.linkedin;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

@SpringBootApplication
public class LinkedinBatchApplication {

	@Bean
	protected FlatFileItemReader<String> reader(){
		return new FlatFileItemReaderBuilder<String>()
				.resource(new ClassPathResource("data-file.csv"))
				.name("csv-reader")
				.lineMapper((line,lineNumber) -> line)
				.build();
	}

	@Bean
	protected FlatFileItemWriter<String> writer(){
		String fileLocation= "/Users/chai/Downloads/masked-data.csv";
		return new FlatFileItemWriterBuilder<String>()
				.name("csv-writer")
				.resource(new FileSystemResource(fileLocation))
				.lineAggregator(item -> item) // lambda to directly return the string
				.build();
	}

	@Bean
	protected Step maskingStep(JobRepository jobRepo, PlatformTransactionManager manager,
							   FlatFileItemReader<String> reader,TextItemProcessor processor,
							   FlatFileItemWriter<String> writer){
		return new StepBuilder("masking-step",jobRepo)
				.<String,String> chunk(2,manager)
				.reader(reader)
				.processor(processor)
				.writer(writer)
				.build();
	}

	@Bean
	protected Job maskingJob(JobRepository jobRepository,Step maskingStep,
							 BatchJobCompletedListener jobCompleted){
		return new JobBuilder("masking-job",jobRepository)
				.start(maskingStep)
				.listener(jobCompleted)
				.build();
	}

	public static void main(String[] args) {
		SpringApplication.run(LinkedinBatchApplication.class, args);
	}

}
