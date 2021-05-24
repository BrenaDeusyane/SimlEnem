package com.example.simulenem;

public class CategoryModel {

    private String document_id;
    private String name;
    private int num_of_tests;

    public CategoryModel(String document_id, String name, int num_of_tests) {
        this.document_id = document_id;
        this.name = name;
        this.num_of_tests = num_of_tests;
    }

    public String getDocumentID() {
        return document_id;
    }

    public void setDocumentID(String document_id) {
        this.document_id = document_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumOfTests() {
        return num_of_tests;
    }

    public void setNumOfTests(int num_of_tests) {
        this.num_of_tests = num_of_tests;
    }
}
