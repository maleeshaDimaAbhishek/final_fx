package edu.MD.Fx_final.model.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserRegistrationDTO {
 private String NIC;
 private String name;
 private LocalDate dob;
 private String mail;
 private String phoneNumber;
 private String address;
}
