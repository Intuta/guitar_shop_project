package com.myproject.guitar_shop.controller;

import com.myproject.guitar_shop.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class CartController extends AbstractController {

    private final CartService service;

}
