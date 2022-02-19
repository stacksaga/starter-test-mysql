package com.example.controller;

import com.example.aggregator.AddCustomerAggregator;
import org.mono.stacksaga.core.lsitener.AggregatorEventListener;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomerAddController implements AggregatorEventListener<AddCustomerAggregator> {

}
