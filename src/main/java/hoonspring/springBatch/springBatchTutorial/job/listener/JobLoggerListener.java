package hoonspring.springBatch.springBatchTutorial.job.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

@Slf4j
public class JobLoggerListener implements JobExecutionListener {

    private static String BEFORE_MESSAGE = "{} Job is Start";
    private static String AFTER_MESSAGE = "{} Job is Done. (Status: {})";

    // job 실행 전에 Listener 실행
    @Override
    public void beforeJob(JobExecution jobExecution) {
        log.info(BEFORE_MESSAGE, jobExecution.getJobInstance().getJobName());
    }
    
    // job 실행 후에 Listener 실행
    @Override
    public void afterJob(JobExecution jobExecution) {
        log.info(AFTER_MESSAGE
                , jobExecution.getJobInstance().getJobName()
                , jobExecution.getStatus());

        if (jobExecution.getStatus() == BatchStatus.FAILED) {
            // 이메일 or 메신저 필요에 따라서 바로 결과를 받을 수 있도록 구현 가능
            log.info("Job is Failed!!!");
        }
    }
}
