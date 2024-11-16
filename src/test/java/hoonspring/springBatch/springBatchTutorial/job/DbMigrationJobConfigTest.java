package hoonspring.springBatch.springBatchTutorial.job;

import hoonspring.springBatch.springBatchTutorial.domain.accounts.AccountsRepository;
import hoonspring.springBatch.springBatchTutorial.domain.orders.OrderRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBatchTest
@SpringBootTest
class DbMigrationJobConfigTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private AccountsRepository accountsRepository;

    @Autowired
    @Qualifier("dbMigrationJob")
    private Job dbMigrationJobConfig;

    @Test
    public void success_data() throws Exception{
        jobLauncherTestUtils.setJob(dbMigrationJobConfig);
        JobExecution execution = jobLauncherTestUtils.launchJob();

        Assertions.assertThat(execution.getExitStatus()).isEqualTo(ExitStatus.COMPLETED);
        Assertions.assertThat(accountsRepository.count()).isEqualTo(8);
    }
}