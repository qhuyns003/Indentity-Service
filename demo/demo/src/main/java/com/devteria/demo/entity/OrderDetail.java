package com.devteria.demo.entity;

import com.devteria.demo.enums.OrderStatus;
import com.devteria.demo.enums.PaymentMethod;
import com.devteria.demo.enums.ShippingMethod;
import jakarta.persistence.Entity;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderDetail extends BaseEntity{
    Long quantity;
    Long price;

}
