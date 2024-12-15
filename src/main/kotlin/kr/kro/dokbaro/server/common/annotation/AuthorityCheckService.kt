package kr.kro.dokbaro.server.common.annotation

import org.springframework.stereotype.Service

/**
 * 권한을 체크하는 service 입니다.
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Service
annotation class AuthorityCheckService