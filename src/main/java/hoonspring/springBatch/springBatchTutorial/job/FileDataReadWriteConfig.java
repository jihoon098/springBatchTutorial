package hoonspring.springBatch.springBatchTutorial.job;

import hoonspring.springBatch.springBatchTutorial.domain.Player;
import hoonspring.springBatch.springBatchTutorial.domain.PlayerYears;
import hoonspring.springBatch.springBatchTutorial.fieldMapper.PlayerFieldSetMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.lang.NonNull;
import org.springframework.transaction.PlatformTransactionManager;




/*
 * Description: 파일(File) 읽고 쓰기
 * run: --spring.batch.job.name=FileReadWriteJob
 */
@Slf4j
@Configuration
public class FileDataReadWriteConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    public FileDataReadWriteConfig(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
    }

    @Bean
    public Job FileReadWriteJob(@Qualifier("FileReadWriteStep")Step step) {
        // JobBuilder를 사용하여 하나의 작업 단위인 Job 생성
        return new JobBuilder("FileReadWriteJob", jobRepository)
                .incrementer(new RunIdIncrementer()) // 해당 Job이 실행될 때 고유한 ID인 Sequence를 순차적으로 부여
                .start(step)
                .build();
    }

    @Bean
    public Step FileReadWriteStep(ItemReader<Player> itemReader,
                                  ItemProcessor<Player, PlayerYears> itemProcessor,
                                  ItemWriter<PlayerYears> itemWriter) {
        // StepBuilder를 사용하여 Job의 작은 실행 단위인 Step을 구성
        return new StepBuilder("FileReadWriteStep", jobRepository)
                .<Player, PlayerYears>chunk(5, transactionManager)
                .reader(itemReader)
                .processor(itemProcessor)
                .writer(itemWriter)
                .build();
    }

    @Bean
    @StepScope
    public FlatFileItemReader<Player> playerItemReader() {
        return new FlatFileItemReaderBuilder<Player>()
                .name("playerItemReader")
                .resource(new FileSystemResource("src/main/resources/Players.csv")) // 읽어올 파일의 위치 지정
                .lineTokenizer(new DelimitedLineTokenizer()) // File을 읽을 때 각 Line 별 데이터를 분할할 토큰(token)을 지정
                .fieldSetMapper(new PlayerFieldSetMapper()) // File의 한 줄에서 추출한 필드 값인 FieldSet 객체를 특정 도메인 객체로 변환하는 Mapper 지정
                .linesToSkip(1) // 파일의 읽을 때 Skip 할 라인 수 지정
                .build();
    }

    @Bean
    @StepScope
    public ItemProcessor<Player, PlayerYears> playerItemProcessor() {
        return new ItemProcessor<Player, PlayerYears>() {
            @Override
            public PlayerYears process(@NonNull Player item) throws Exception {
                return new PlayerYears(item);
            }
        };
    }

    // FlatFileItemWriter : 데이터를 평문 file(텍스트 파일)로 작성하는 Spring Batch의 기본 Writer
    @Bean
    @StepScope
    public FlatFileItemWriter<PlayerYears> playerItemWriter() {

        // BeanWrapperFieldExtractor : 파일을 작성할 때 VO 의 어떤 field 를 추출할 지 배열로 입력
        BeanWrapperFieldExtractor<PlayerYears> fieldExtractor = new BeanWrapperFieldExtractor<>();
        fieldExtractor.setNames(new String[]{"ID", "lastName", "position", "yearExperience"});
        fieldExtractor.afterPropertiesSet(); // 필드 추출기가 제대로 구성되었는지 검증

        // DelimitedLineAggregator : 작성할 한 줄을 생성하는 방식을 설정
        DelimitedLineAggregator<PlayerYears> lineAggregator = new DelimitedLineAggregator<>();
        lineAggregator.setDelimiter(","); // 각 필드 값들의 연결에 사용할 구분자를 지정
        lineAggregator.setFieldExtractor(fieldExtractor);

        // FileSystemResource : 파일 경로와 파일 자체를 지정하는 Spring의 리소스 객체
        FileSystemResource outputResource = new FileSystemResource("player_output.txt");

        return new FlatFileItemWriterBuilder<PlayerYears>()
                .name("playerItemWriter")
                .resource(outputResource)
                .lineAggregator(lineAggregator)
                .build();
    }
}
