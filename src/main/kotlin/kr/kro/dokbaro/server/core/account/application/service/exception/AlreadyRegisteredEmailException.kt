package kr.kro.dokbaro.server.core.account.application.service.exception

import kr.kro.dokbaro.server.common.exception.http.status4xx.BadRequestException

/**
 * email 계정이 이미 등록되어있으면 발생하는 exception 입니다.
 */
class AlreadyRegisteredEmailException(
	email: String,
) : BadRequestException("이미 등록된 email($email)입니다")