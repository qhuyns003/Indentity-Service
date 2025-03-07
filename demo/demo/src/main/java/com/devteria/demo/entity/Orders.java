package com.devteria.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import com.devteria.demo.enums.OrderStatus;
import com.devteria.demo.enums.PaymentMethod;
import com.devteria.demo.enums.ShippingMethod;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Orders extends BaseEntity {
    OrderStatus orderStatus;
    String address;
    ShippingMethod shippingMethod;
    PaymentMethod paymentMethod;

    @ManyToOne
    @JoinColumn(name = "userId")
    UserEntity user;
}
