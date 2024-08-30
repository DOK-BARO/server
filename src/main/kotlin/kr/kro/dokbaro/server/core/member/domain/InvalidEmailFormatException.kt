package kr.kro.dokbaro.server.core.member.domain

import kr.kro.dokbaro.server.common.exception.http.status4xx.BadRequestException

class InvalidEmailFormatException(
	address: String,
) : BadRequestException("invalid email format - $address")