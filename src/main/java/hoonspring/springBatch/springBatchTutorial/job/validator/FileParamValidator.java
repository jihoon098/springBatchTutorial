package hoonspring.springBatch.springBatchTutorial.job.validator;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.JobParametersValidator;
import org.springframework.util.StringUtils;

import java.util.Optional;

public class FileParamValidator implements JobParametersValidator {

    @Override
    public void validate(JobParameters parameters) throws JobParametersInvalidException {

        String fileName =
            Optional.ofNullable(parameters.getString("fileName"))
                    .orElseThrow(() -> new JobParametersInvalidException("fileName is missing or null"));

        // fileName 값의 postfix가 csv인지 확인
        if ( !StringUtils.endsWithIgnoreCase(fileName, "csv") ) {
            throw new JobParametersInvalidException("This is not CSV File");
        }
    }
}
