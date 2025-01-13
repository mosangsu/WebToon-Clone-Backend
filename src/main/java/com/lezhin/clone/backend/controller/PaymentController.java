package com.lezhin.clone.backend.controller;

import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lezhin.clone.backend.dto.Response;
import com.lezhin.clone.backend.service.PaymentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/payment")
public class PaymentController {

    private final PaymentService paymentService;

    /**
     * 
     * @param 
     * @return com.lezhin.clone.backend.dto.Response
     */
    @GetMapping("")
    public Response getPayment(@RequestAttribute @NonNull Long memberId) {
        Response response = new Response();
        try {
            response.setData(paymentService.getPayment(memberId));
            response.setCode("S");
            response.setMessage("상품정보입니다.");
        }
        catch(Exception e){
            response.setCode("F");
            response.setMessage("상품정보를 가져오는 도중 오류가 발생했습니다.");
            response.setData(e.getMessage());
        }
        
        return response;
    }

    /**
     * 
     * @param 
     * @return com.lezhin.clone.backend.dto.Response
     */
    @PostMapping("/{coinPackageId}/purchase")
    public Response purchaseCoin(@RequestAttribute @NonNull Long memberId, @PathVariable Long coinPackageId) {
        Response response = new Response();
        try {
            response.setData(paymentService.purchaseCoin(memberId, coinPackageId));
            response.setCode("S");
            response.setMessage("상품을 구매했습니다.");
        }
        catch(Exception e){
            response.setCode("F");
            response.setMessage("상품을 구매하는 도중 오류가 발생했습니다.");
            response.setData(e.getMessage());
        }
        
        return response;
    }
}