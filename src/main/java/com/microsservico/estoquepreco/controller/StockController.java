package com.microsservico.estoquepreco.controller;

import com.microsservico.estoquepreco.constants.RabbitMQConstants;
import com.microsservico.estoquepreco.dto.StockDTO;
import com.microsservico.estoquepreco.service.RabbitMQService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "stock")
public class StockController {

    @Autowired
    private RabbitMQService rabbitMQService;

    @PutMapping
    private ResponseEntity changeStock(@RequestBody StockDTO stockDTO) {
        this.rabbitMQService.sendMessage(RabbitMQConstants.STOCK_QUEUE, stockDTO);
        return new ResponseEntity(HttpStatus.CREATED);
    }
}
