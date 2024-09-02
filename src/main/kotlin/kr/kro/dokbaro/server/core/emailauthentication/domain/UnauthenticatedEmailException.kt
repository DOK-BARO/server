package kr.kro.dokbaro.server.core.emailauthentication.domain

import kr.kro.dokbaro.server.common.exception.http.status4xx.BadRequestException

class UnauthenticatedEmailException(
	address: String,
) : BadRequestException("Unauthenticated email ($address)")