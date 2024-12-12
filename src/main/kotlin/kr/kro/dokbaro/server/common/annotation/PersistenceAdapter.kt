package kr.kro.dokbaro.server.common.annotation

import org.springframework.stereotype.Component

/**
 * 도메인 로직과 데이터 저장소 간 연결을 담당합니다.
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Component
annotation class PersistenceAdapter