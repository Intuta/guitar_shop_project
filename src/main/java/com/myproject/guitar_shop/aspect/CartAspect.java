package com.myproject.guitar_shop.aspect;

import com.myproject.guitar_shop.domain.Cart;
import com.myproject.guitar_shop.domain.Item;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.text.DecimalFormat;

@Aspect
@Component
public class CartAspect {
    @Pointcut("execution(* com.myproject.guitar_shop.repository.CartRepository.save(..))")
    public void callTotalCart() {

    }

    /**
     * @param cart The method counts sum of all items in the cart
     */
    @Around("callTotalCart() && args(cart)")
    public Cart total(ProceedingJoinPoint pjp, Cart cart) throws Throwable {
        DecimalFormat df = new DecimalFormat("#.##");
        double sum = cart.getItems().stream()
                .mapToDouble(Item::getSum)
                .sum();
        cart.setSum(Double.valueOf(df.format(sum).replace(",", ".")));
        pjp.proceed();
        return cart;
    }

}
