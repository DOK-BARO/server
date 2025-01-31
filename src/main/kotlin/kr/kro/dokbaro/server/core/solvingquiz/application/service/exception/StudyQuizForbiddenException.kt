package kr.kro.dokbaro.server.core.solvingquiz.application.service.exception

import kr.kro.dokbaro.server.common.exception.http.status4xx.ForbiddenException

class StudyQuizForbiddenException : ForbiddenException("스터디 퀴즈를 풀 권한이 없습니다.")