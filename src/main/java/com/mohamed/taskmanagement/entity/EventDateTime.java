package com.mohamed.taskmanagement.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "event_date")
@Getter
@Setter
public class EventDateTime {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private int id;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "date_time")
    private String dateTime;

    @Column(name = "time_zone")
    private String timeZone;
}
