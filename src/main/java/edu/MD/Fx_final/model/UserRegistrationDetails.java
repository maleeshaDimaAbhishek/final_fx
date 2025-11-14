package edu.MD.Fx_final.model;

import lombok.*;

import java.sql.Date;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserRegistrationDetails {
 private String NIC;
 private String name;
 private LocalDate dob;
 private String mail;
 private String phoneNumber;
 private String address;
}
