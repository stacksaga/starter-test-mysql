package com.example.controller;

import com.example.aggregator.AddCustomerAggregator;
import org.mono.stacksaga.core.lsitener.AggregatorListener;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomerAddController implements AggregatorListener<AddCustomerAggregator> {


}
