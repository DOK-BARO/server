package kr.kro.dokbaro.server.core.studygroup.application.service.exception

import kr.kro.dokbaro.server.common.exception.http.status4xx.NotFoundException

class NotFoundStudyGroupException(
	inviteCode: String,
) : NotFoundException("초대코드 ($inviteCode) 에 해당하는 그룹을 찾을 수 없습니다")