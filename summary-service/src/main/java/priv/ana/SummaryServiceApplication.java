package priv.ana;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;

@SpringBootApplication
@EnableFeignClients
@EnableDiscoveryClient
public class SummaryServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SummaryServiceApplication.class, args);
        System.out.println("成绩与统计服务模块启动了");
    }

}
