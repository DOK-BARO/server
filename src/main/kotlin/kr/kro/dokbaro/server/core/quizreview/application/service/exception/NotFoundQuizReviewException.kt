package kr.kro.dokbaro.server.core.quizreview.application.service.exception

import kr.kro.dokbaro.server.common.exception.http.status4xx.NotFoundException

class NotFoundQuizReviewException(
	val id: Long,
) : NotFoundException("Quiz Review($id) Not Found")