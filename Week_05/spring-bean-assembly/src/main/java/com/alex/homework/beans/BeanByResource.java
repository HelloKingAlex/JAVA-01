package com.alex.homework.beans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author Alex Shen
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BeanByResource {
    private String name;
    private int id;
}
