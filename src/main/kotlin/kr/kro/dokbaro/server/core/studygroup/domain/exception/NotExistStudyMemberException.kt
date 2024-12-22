package kr.kro.dokbaro.server.core.studygroup.domain.exception

import kr.kro.dokbaro.server.common.exception.http.status4xx.BadRequestException

class NotExistStudyMemberException : BadRequestException("not found study member")