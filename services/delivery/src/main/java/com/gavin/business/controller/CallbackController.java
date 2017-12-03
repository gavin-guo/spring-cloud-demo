package com.gavin.business.controller;

import com.gavin.business.service.DeliveryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class CallbackController {

    @Autowired
    private DeliveryService deliveryService;

}
