package hoonspring.springBatch.springBatchTutorial.scheduler;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Date;

@Component
public class HelloJobScheduler {

    @Autowired
    @Qualifier("helloWorldJob")
    private Job helloWorldJob;

    @Autowired
    private JobLauncher jobLauncher;

    // 1분마다 배치 실행
    @Scheduled(cron = "0 */1 * * * *")
    public void jobSchedulerRun() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        JobParameters jobParameters = new JobParameters(
                Collections.singletonMap("requestTime", new JobParameter<>(new Date(), Date.class))
        );

        jobLauncher.run(helloWorldJob, jobParameters);
    }




}
