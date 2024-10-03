package hoonspring.springBatch.springBatchTutorial.job;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@Configuration
public class HelloWorldJobConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    public HelloWorldJobConfig(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
    }

    @Bean
    public Job helloWorldJob() {
        log.info("===== START Job =====");
        // JobBuilder를 사용하여 하나의 작업 단위인 Job 생성
        return new JobBuilder("helloWorldJob", jobRepository)
                .incrementer(new RunIdIncrementer()) // 해당 Job이 실행될 때 고유한 ID인 Sequence를 순차적으로 부여
                .start(helloWorldStep())
                .build();
    }

    @Bean
    public Step helloWorldStep() {
        log.info("===== START Step =====");
        // StepBuilder를 사용하여 Job의 작은 실행 단위인 Step을 구성
        return new StepBuilder("helloWorldStep", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("Hello, Spring Batch!");
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }
}
