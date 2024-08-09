package kr.kro.dokbaro.server.configuration

import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.Configuration

@EnableFeignClients(basePackages = ["kr.kro.dokbaro.server.core"])
@Configuration
class FeignClientConfig