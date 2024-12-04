package kr.kro.dokbaro.server.security.annotation

import org.springframework.security.core.annotation.AuthenticationPrincipal

@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.ANNOTATION_CLASS)
@Retention(AnnotationRetention.RUNTIME)
@AuthenticationPrincipal
annotation class LoginUser(
	val errorOnInvalidType: Boolean = true,
)