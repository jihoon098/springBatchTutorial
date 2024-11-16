package hoonspring.springBatch.springBatchTutorial.job;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

/*
 * @ExtendWith(SpringExtension.class)
 * : JUnit 5에서 제공하는 애노테이션으로, 특정 "확장(Extension)" 클래스를 테스트에 적용하여 실행 중 추가적인 기능을 설정할 수 있도록 함.
 *   테스트가 실행될 때 스프링 컨텍스트(Spring ApplicationContext)가 로드.
 *   따라서 스프링이 관리하는 빈을 테스트 클래스에서 주입받아 사용할 수 있게 된다.
 *
 * @SpringBatchTest
 * : Spring Batch의 테스트 환경을 구성하기 위해 사용되는 애노테이션.
 *  1. JobLauncherTestUtils, JobRepositoryTestUtils와 같은 유틸리티 클래스를 사용할 수 있도록 설정.
 *  2. Spring Batch가 관리하는 메타데이터(JobRepository, JobExecution, StepExecution 등)와 테스트 데이터의 격리 환경 제공
 *
 * @SpringBootTest
 * : Spring Boot 기반 애플리케이션의 통합 테스트 환경을 구성하는 애노테이션.
 *   모든 Bean과 환경 설정을 로드하므로, 실제 애플리케이션 실행 시와 유사한 환경에서 테스트할 수 있게 함.
 *   >> classes 속성을 사용하면 테스트에 필요한 특정 config 클래스만 스프링 컨테이너에 로드
 */
@ExtendWith(SpringExtension.class)
@SpringBatchTest
@SpringBootTest(classes = {SpringBatchTestConfig.class, HelloWorldJobConfig.class})
class HelloWorldJobConfigTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Test
    public void success() throws Exception{
        JobExecution execution = jobLauncherTestUtils.launchJob();

        Assertions.assertThat(execution.getExitStatus()).isEqualTo(ExitStatus.COMPLETED);
    }
}