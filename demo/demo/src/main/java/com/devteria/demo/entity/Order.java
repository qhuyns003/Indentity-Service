package com.devteria.demo.entity;

import com.devteria.demo.enums.OrderStatus;
import com.devteria.demo.enums.PaymentMethod;
import com.devteria.demo.enums.ShippingMethod;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Order extends BaseEntity{
    OrderStatus orderStatus;
    String address;
    ShippingMethod shippingMethod;
    PaymentMethod paymentMethod;
    @ManyToOne
    @JoinColumn(name = "id")
    UserEntity user;

}
