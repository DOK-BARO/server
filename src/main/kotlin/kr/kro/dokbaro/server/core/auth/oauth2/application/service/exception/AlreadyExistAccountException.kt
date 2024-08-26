package kr.kro.dokbaro.server.core.auth.oauth2.application.service.exception

import kr.kro.dokbaro.server.common.exception.http.status4xx.BadRequestException
import kr.kro.dokbaro.server.common.type.AuthProvider

class AlreadyExistAccountException(
	id: String,
	provider: AuthProvider,
) : BadRequestException("Account($id : $provider) already exists")