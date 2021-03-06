package com.alex.homework.beans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author Alex Shen
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BeanByXml implements Serializable {
    private String name;
    private int id;
}
