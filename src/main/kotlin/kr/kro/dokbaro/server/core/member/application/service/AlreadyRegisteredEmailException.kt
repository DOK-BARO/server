package kr.kro.dokbaro.server.core.member.application.service

import kr.kro.dokbaro.server.common.exception.http.status4xx.BadRequestException

class AlreadyRegisteredEmailException(
	email: String,
) : BadRequestException("이미 등록된 email($email)입니다")