package kr.kro.dokbaro.server.common.annotation

import org.springframework.stereotype.Component

/**
 * 데이터 변환과 관련된 역할을 수행합니다. DTO <-> entity 혹은 record <-> DTO 변환을 수행합니다.
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Component
annotation class Mapper