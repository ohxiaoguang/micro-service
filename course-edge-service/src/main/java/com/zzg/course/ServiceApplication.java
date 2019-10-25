package com.zzg.course;

import com.zzg.course.filter.CourseFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author ZZG
 * @Date 2019/10/7 22:10
 * @Version v1.0.0
 */
@SpringBootApplication
public class ServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceApplication.class,args);
    }

    //配置Filter
    @Bean
    public FilterRegistrationBean filterRegistrationBean(){
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        CourseFilter courseFilter = new CourseFilter();
        filterRegistrationBean.setFilter(courseFilter);

        List<String> urlPatterns = new ArrayList<>();
        urlPatterns.add("/*");
        filterRegistrationBean.setUrlPatterns(urlPatterns);
        return filterRegistrationBean;
    }
}
