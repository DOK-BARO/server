package kr.kro.dokbaro.server.domain.account.model.service

import kr.kro.dokbaro.server.global.exception.http.status4xx.BadRequestException

class AlreadyExistAccountException(
	id: String,
	provider: String,
) : BadRequestException("Account($id : $provider) already exists")