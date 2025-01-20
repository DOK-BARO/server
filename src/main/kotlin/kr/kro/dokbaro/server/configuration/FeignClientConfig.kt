package kr.kro.dokbaro.server.configuration

import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.Configuration

/**
 * feign client 관련 설정을 수행합니다.
 */
@EnableFeignClients(basePackages = ["kr.kro.dokbaro.server.core"])
@Configuration
class FeignClientConfig