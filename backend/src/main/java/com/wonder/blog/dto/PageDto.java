package com.wonder.blog.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;

@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
public class PageDto {
  private List content;
  private int number;
  private int size;
  private long totalElements;

  public PageDto(Page page) {
    this.content = page.getContent();
    this.number = page.getNumber();
    this.size = page.getSize();
    this.totalElements = page.getTotalElements();
  }
}
