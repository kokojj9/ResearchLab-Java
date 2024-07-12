package com.example.researchlab.investment.model.vo;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class MyStockList {
    private int settingNo;
    private String listName;
    private List<StockItem> items;

}
