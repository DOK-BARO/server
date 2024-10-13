package kr.kro.dokbaro.server.core.solvingquiz.application.service.exception

import kr.kro.dokbaro.server.common.exception.http.status4xx.NotFoundException

class NotFoundSolvingQuizException(
	solvingQuizId: Long,
) : NotFoundException("Solving quiz $solvingQuizId was not found")