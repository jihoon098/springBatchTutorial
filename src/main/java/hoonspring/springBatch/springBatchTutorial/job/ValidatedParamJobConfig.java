package hoonspring.springBatch.springBatchTutorial.job;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

/*
 * Description: 스프링배치 실행시 외부로부터 파라미터 전달받기
 * run: --spring.batch.job.name=validatedParamJob fileName=test.csv
 */
@Slf4j
@Configuration
public class ValidatedParamJobConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    public ValidatedParamJobConfig(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
    }

    @Bean
    public Job validatedParamJob(@Qualifier("validatedParamStep") Step validatedParamStep) {
        return new JobBuilder("validatedParamJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(validatedParamStep)
                .build();
    }

    @Bean
    @JobScope
    public Step validatedParamStep(@Value("#{jobParameters['fileName']}") String fileName) {
        return new StepBuilder("validatedParamStep", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("validated Param Tasklet");
                    System.out.println("fileName 값은 ? : " + fileName);
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }
}
