package kr.kro.dokbaro.server.domain.account.model.service

import kr.kro.dokbaro.server.global.AuthProvider
import kr.kro.dokbaro.server.global.exception.http.status4xx.BadRequestException

class AlreadyExistAccountException(
	id: String,
	provider: AuthProvider,
) : BadRequestException("Account($id : $provider) already exists")