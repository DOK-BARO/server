package kr.kro.dokbaro.server.core.auth.email.application.service

import kr.kro.dokbaro.server.common.exception.http.status4xx.NotFoundException

class NotFoundEmailCertificatedAccountException(
	val email: String,
) : NotFoundException("email account not found ($email)")