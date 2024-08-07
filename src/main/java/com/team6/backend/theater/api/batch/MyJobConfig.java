package com.team6.backend.theater.api.batch;


import com.team6.backend.theater.api.service.TheaterDetailService;
import com.team6.backend.theater.api.service.TheaterListService;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableBatchProcessing
public class MyJobConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final TheaterListService theaterListService;
    private final TheaterDetailService theaterDetailService;

    public MyJobConfig(JobRepository jobRepository,
                       PlatformTransactionManager transactionManager,
                       TheaterListService theaterListService,
                       TheaterDetailService theaterDetailService) {
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
        this.theaterListService = theaterListService;
        this.theaterDetailService = theaterDetailService;
    }

    @Bean
    public Job myJob() {
        return new JobBuilder("myJob", jobRepository)
                .start(fetchTheaterListStep())
                .next(fetchTheaterDetailsStep())
                .build();
    }

    @Bean
    public Step fetchTheaterListStep() {
        return new StepBuilder("fetchTheaterListStep", jobRepository)
                .tasklet(fetchTheaterListTasklet(), transactionManager)
                .build();
    }

    @Bean
    public Step fetchTheaterDetailsStep() {
        return new StepBuilder("fetchTheaterDetailsStep", jobRepository)
                .tasklet(fetchTheaterDetailsTasklet(), transactionManager)
                .build();
    }

    @Bean
    public Tasklet fetchTheaterListTasklet() {
        return (contribution, chunkContext) -> {
            theaterListService.fetchAndSaveTheaterList();
            return RepeatStatus.FINISHED;
        };
    }

    @Bean
    public Tasklet fetchTheaterDetailsTasklet() {
        return (contribution, chunkContext) -> {
            theaterDetailService.fetchAndSaveTheaterDetails();
            return RepeatStatus.FINISHED;
        };
    }
}
