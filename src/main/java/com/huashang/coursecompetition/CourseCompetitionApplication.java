package com.huashang.coursecompetition;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.huashang.coursecompetition.dao")
public class CourseCompetitionApplication {

	public static void main(String[] args) {
		SpringApplication.run(CourseCompetitionApplication.class, args);
	}

}
