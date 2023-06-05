package com.project.capstone_project.model.feedback;

import com.project.capstone_project.model.customer.Customer;

import javax.persistence.*;
@Entity
public class Feedback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(columnDefinition = "date")
    private String commentDate;
    private Boolean status;
    private String comment;
    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    private Customer customer;

    public Feedback() {
    }

    public Feedback(Integer id, String commentDate, Boolean status,
                    String comment, Customer customer) {
        this.id = id;
        this.commentDate = commentDate;
        this.status = status;
        this.comment = comment;
        this.customer = customer;
    }

    public Feedback(String commentDate, Boolean status, String comment, Customer customer) {
        this.commentDate = commentDate;
        this.status = status;
        this.comment = comment;
        this.customer = customer;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCommentDate() {
        return commentDate;
    }

    public void setCommentDate(String commentDate) {
        this.commentDate = commentDate;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}
