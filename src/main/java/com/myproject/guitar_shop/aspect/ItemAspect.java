package com.myproject.guitar_shop.aspect;

import com.myproject.guitar_shop.domain.Item;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ItemAspect {
    @Pointcut("execution(* com.myproject.guitar_shop.repository.ItemRepository.save(..))")
    public void callTotal(){

    }
    /**
     * @param item The method counts final cost of the item
     */
    @Around("callTotal() && args(item)")
    public Item totalBefore(ProceedingJoinPoint pjp, Item item) throws Throwable {
        item.setSum(item.getPrice() * item.getQuantity());
        pjp.proceed();
        return item;
    }
}
