package com.alex.homework.components;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * @author Alex Shen
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Component("beanByComponent")
public class BeanByComponent implements Serializable {
    public int id;
    public String name;
}
