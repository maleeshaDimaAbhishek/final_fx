package edu.MD.Fx_final.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Data
public class BookCardDTO {
    private String title;
    private String author;
    private String publisher;
    private String published_year;
    private String category;
    private int available_copies;
    private String imageLink;
}
