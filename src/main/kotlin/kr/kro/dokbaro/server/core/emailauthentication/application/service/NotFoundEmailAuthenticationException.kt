package kr.kro.dokbaro.server.core.emailauthentication.application.service

import kr.kro.dokbaro.server.common.exception.http.status4xx.NotFoundException

class NotFoundEmailAuthenticationException(
	email: String,
) : NotFoundException("not found email address $email")