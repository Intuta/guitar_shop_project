package com.myproject.guitar_shop.controller;

import com.myproject.guitar_shop.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ItemController extends AbstractController {

    private final ItemService service;
}
