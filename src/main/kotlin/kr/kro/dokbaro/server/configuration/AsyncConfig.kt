package kr.kro.dokbaro.server.configuration

import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableAsync

/**
 * 비동기 관련 설정을 수행합니다.
 */
@EnableAsync
@Configuration
class AsyncConfig