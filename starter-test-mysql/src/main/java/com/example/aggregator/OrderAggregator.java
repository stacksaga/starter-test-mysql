package com.example.aggregator;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.mono.stacksaga.SagaAggregate;
import org.mono.stacksaga.core.annotation.Aggregator;

import java.io.Serializable;
import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Aggregator(version = "1.0.3", versionUpdateNote = "just for fun")
@Data
@ToString
public class OrderAggregator extends SagaAggregate {
    private String updatedStatus;
    private Date time;
    private Type type;

    public enum Type {
        revert_complete,
        process_complete,
        revert_error,
    }

}
