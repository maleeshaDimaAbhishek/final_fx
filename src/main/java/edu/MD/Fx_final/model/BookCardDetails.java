package edu.MD.Fx_final.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Data
public class BookCardDetails {
    private String title;
    private String author;
    private String publisher;
    private String published_year;
    private String category;
    private int available_copies;
    private String imageLink;
}
