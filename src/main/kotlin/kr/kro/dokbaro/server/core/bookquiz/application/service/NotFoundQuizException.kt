package kr.kro.dokbaro.server.core.bookquiz.application.service

import kr.kro.dokbaro.server.common.exception.http.status4xx.NotFoundException

class NotFoundQuizException(
	id: Long,
) : NotFoundException("quiz ($id) not found")