package kr.kro.dokbaro.server.core.bookquiz.domain.exception

import kr.kro.dokbaro.server.common.exception.http.status4xx.BadRequestException

class EmptyAnswerSheetException : BadRequestException("empty answer sheet")