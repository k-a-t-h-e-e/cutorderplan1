package com.example.cutorderplanservice;

import lombok.Data;

@Data
public class RequestDTO {
    private int[] cut_order_matrix;

    private int A;
    private int B;
    private int C;
    private int D;
    private int n;
    private int p;
    private int q;



}
