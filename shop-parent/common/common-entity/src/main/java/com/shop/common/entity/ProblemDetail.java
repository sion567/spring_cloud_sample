package com.shop.common.entity;

import lombok.Data;

import java.net.URI;

@Data
public class ProblemDetail implements java.io.Serializable {
    private URI type;
    private String title;
    private int status;
    private String detail;
    private URI instance;

    public static ProblemDetail of(int status, String title, String detail) {
        ProblemDetail problem = new ProblemDetail();
        problem.setStatus(status);
        problem.setTitle(title);
        problem.setDetail(detail);
        return problem;
    }

    public static ProblemDetail of(int status, String title, String detail, String instance) {
        ProblemDetail problem = of(status, title, detail);
        if (instance != null) {
            problem.setInstance(URI.create(instance));
        }
        return problem;
    }

    public static ProblemDetail forStatusAndDetail(int status, String detail) {
        return of(status, null, detail);
    }
}
