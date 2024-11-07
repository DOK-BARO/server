package kr.kro.dokbaro.server.core.studygroup.application.service.exception

import kr.kro.dokbaro.server.common.exception.http.status4xx.NotFoundException

class NotFoundStudyGroupException : NotFoundException("스터디 그룹을 찾을 수 없습니다")