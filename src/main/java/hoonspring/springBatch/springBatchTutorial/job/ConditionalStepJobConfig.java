package hoonspring.springBatch.springBatchTutorial.job;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

/*
 * Description: 주문 테이블 -> Step별 실행 상태에 따른 분기처리
 * run: --spring.batch.job.name=conditionalStepJob
 */
@Slf4j
@Configuration
public class ConditionalStepJobConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    public ConditionalStepJobConfig(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
    }

    @Bean
    public Job conditionalStepJob(@Qualifier("conditionalStartStep") Step conditionalStartStep,
                                  @Qualifier("conditionalFailStep") Step conditionalFailStep,
                                  @Qualifier("conditionalCompletedStep") Step conditionalCompletedStep,
                                  @Qualifier("conditionalElseStep") Step conditionalElseStep) {
        return new JobBuilder("conditionalStepJob", jobRepository)
                .incrementer(new RunIdIncrementer()) // 해당 Job이 실행될 때 고유한 ID인 Sequence를 순차적으로 부여
                .start(conditionalStartStep).on("FAILED").to(conditionalFailStep) // Step의 실행 결과가 실패일 때
                .from(conditionalStartStep).on("COMPLETED").to(conditionalCompletedStep) // Step의 실행 결과가 성공했을 때
                .from(conditionalStartStep).on("*").to(conditionalElseStep) // Step이 fail, completed 모두 아닌 다른 상태의 결과를 리턴했을 때
                .end()
                .build();
    }

    @Bean
    public Step conditionalStartStep() {
        return new StepBuilder("conditionalStartStep", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("conditional Start Step");
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }

    @Bean
    public Step conditionalFailStep() {
        return new StepBuilder("conditionalFailStep", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("conditional Fail Step");
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }

    @Bean
    public Step conditionalCompletedStep() {
        return new StepBuilder("conditionalCompletedStep", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("conditional Completed Step");
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }

    @Bean
    public Step conditionalElseStep() {
        // StepBuilder를 사용하여 Job의 작은 실행 단위인 Step을 구성
        return new StepBuilder("conditionalElseStep", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("conditional Else Step");
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }
}
