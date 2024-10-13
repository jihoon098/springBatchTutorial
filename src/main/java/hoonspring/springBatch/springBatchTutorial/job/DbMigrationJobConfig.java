package hoonspring.springBatch.springBatchTutorial.job;

import hoonspring.springBatch.springBatchTutorial.domain.accounts.Accounts;
import hoonspring.springBatch.springBatchTutorial.domain.accounts.AccountsRepository;
import hoonspring.springBatch.springBatchTutorial.domain.orders.OrderRepository;
import hoonspring.springBatch.springBatchTutorial.domain.orders.Orders;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.batch.item.data.builder.RepositoryItemWriterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Collections;

/*
 * Description: 주문 테이블 -> 정산 테이블 데이터 이관
 * run: --spring.batch.job.name=dbMigrationJob
 */
@Slf4j
@Configuration
public class DbMigrationJobConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private AccountsRepository accountsRepository;

    public DbMigrationJobConfig(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
    }

    @Bean
    public Job dbMigrationJob(@Qualifier("dbMigrationStep") Step dbMigrationStep) {
        // JobBuilder를 사용하여 하나의 작업 단위인 Job 생성
        return new JobBuilder("dbMigrationJob", jobRepository)
                .incrementer(new RunIdIncrementer()) // 해당 Job이 실행될 때 고유한 ID인 Sequence를 순차적으로 부여
                .start(dbMigrationStep)
                .build();
    }

    @Bean
    @JobScope
    public Step dbMigrationStep(
            ItemReader<Orders> itemReader, ItemProcessor<Orders,Accounts> itemProcessor, ItemWriter<Accounts> itemWriter) {
        // StepBuilder를 사용하여 Job의 작은 실행 단위인 Step을 구성
        return new StepBuilder("DbMigrationStep", jobRepository)
                .<Orders, Accounts>chunk(5, transactionManager)
                .reader(itemReader)
                .processor(itemProcessor)
                .writer(itemWriter)
                .build();
    }

    @Bean
    @StepScope
    public RepositoryItemReader<Orders> dbOrdersReader(){
        return new RepositoryItemReaderBuilder<Orders>()
                .name("dbOrdersReader") // Reader의 이름을 정의. 해당 이름은 stepExecutionContext 에서도 활용
                .repository(orderRepository) // DB와 연결된 JPA repository 객체를 지정
                .methodName("findAll")
                .pageSize(5) // 한 번에 읽어올 데이터의 크기 지정. 일반적으로 chunk 사이즈와 맞춤
                //.arguments(Arrays.asList()) // arguments()로 호출할 메서드의 파라미터를 지정할 수 있다
                .sorts(Collections.singletonMap("id", Sort.Direction.ASC))
                .build();
    }

    @Bean
    @StepScope
    public ItemProcessor<Orders, Accounts> dbOrdersProcessor() {
        return new ItemProcessor<Orders, Accounts>() {
            @Override
            public Accounts process(@NonNull Orders item) throws Exception {
                // item 파라미터에는 itemReader 를 통해 읽어온 데이터가 전달된다.
                return new Accounts(item);
            }
        };
    }

    @Bean
    @StepScope
    public RepositoryItemWriter<Accounts> dbOrdersWriter() {
        return new RepositoryItemWriterBuilder<Accounts>()
                .repository(accountsRepository)
                .methodName("save")
                .build();
    }

}