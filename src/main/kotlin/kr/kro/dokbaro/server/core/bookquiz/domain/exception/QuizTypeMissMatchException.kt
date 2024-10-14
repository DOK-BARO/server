package kr.kro.dokbaro.server.core.bookquiz.domain.exception

import kr.kro.dokbaro.server.common.exception.http.status4xx.BadRequestException

class QuizTypeMissMatchException : BadRequestException("변경하려는 타입과 일치하지 않습니다")