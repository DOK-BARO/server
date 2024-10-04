package kr.kro.dokbaro.server.core.termsofservice.application.service

import kr.kro.dokbaro.server.common.exception.http.status4xx.NotFoundException

class NotFoundTermsOfServiceException(
	id: Long,
) : NotFoundException("$id 에 해당 하는 이용약관을 찾을 수 없습니다")