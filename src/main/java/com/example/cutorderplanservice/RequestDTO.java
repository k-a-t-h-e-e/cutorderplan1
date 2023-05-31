package com.example.cutorderplanservice;

import lombok.Data;

@Data
public class RequestDTO {
    private int[] cut_order_matrix;
    private int hmin=0;
    private int hmax=0;
    private int gmax=0;
    private int gmin=0;
    private int runningCount=40;

}
