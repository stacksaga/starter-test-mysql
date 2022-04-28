package com.example.service;

import com.google.common.base.Stopwatch;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.SerializationUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mono.stacksaga.EnableStackSaga;
import org.mono.stacksaga.common.comiunication.InstanceDetail;
import org.mono.stacksaga.core.ProcessMode;
import org.mono.stacksaga.core.service.ExecutorBinaryFileService;
import org.mono.stacksaga.common.comiunication.ExecutorExecutionType;
import org.mono.stacksaga.redis.publisher.ExecutorBinaryTransformerService;
import org.mono.stacksaga.redis.template.StringStringTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StopWatch;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;

import static org.mono.stacksaga.common.Resources.Keys.ADMIN_SERVER_KEY_FORMATTER;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableStackSaga
@Slf4j
public class ExecutorBinaryFileServiceTest {

    @Autowired
    private ExecutorBinaryFileService executorFileSavingService;

    @Autowired
    private ExecutorBinaryTransformerService executorBinaryTransformerService;

    @Autowired
    private StringStringTemplate stringStringTemplate;

    @Test
    void saveAsFileAndSendToAdminServerTest() throws IOException {
        /*stringStringTemplate.getTemplate().opsForValue().set(String.format(ADMIN_SERVER_KEY_FORMATTER, "1"), "test");
        InstanceDetail instanceDetail = new InstanceDetail();
        instanceDetail.setStart_up_datetime(new Date().getTime());
        String transactionUid = UUID.randomUUID().toString();
        String executorUid = UUID.randomUUID().toString();
        String chuckUid = UUID.randomUUID().toString();
        StopWatch stopWatch = new StopWatch();
        stopWatch.start("saveAsFile");
        File file = executorFileSavingService.saveAsFile(
                chuckUid,
                transactionUid,
                executorUid,
                ExecutorExecutionType.INIT_EXECUTION,
                ProcessMode.PROCESS,
                SerializationUtils.serialize(instanceDetail)
        );
        Assertions.assertNotNull(file);
        stopWatch.stop();
        log.info("AbsolutePath = {} ", file.getAbsolutePath());
        log.info("Path = {} ", file.getPath());
        log.info("Name = {} ", file.getName());
        //send file
        stopWatch.start("sendIndividualFile");
        executorBinaryTransformerService.sendIndividualFile(file);
        stopWatch.stop();
        //delete after
        stopWatch.start("deleteFile");
        executorFileSavingService.deleteFile(file.getName());
        stopWatch.stop();
        for (StopWatch.TaskInfo taskInfo : stopWatch.getTaskInfo()) {
            log.info("Time [ms] : {} : [{}]", taskInfo.getTimeMillis(), taskInfo.getTaskName());
        }
        log.info("Total : {}", stopWatch.getTotalTimeMillis());*/
    }

    @Test
    void getFilesTest() {
        executorFileSavingService.getFiles().forEach(file -> {
            log.info("file : {}", file.getName());
        });
    }
}
