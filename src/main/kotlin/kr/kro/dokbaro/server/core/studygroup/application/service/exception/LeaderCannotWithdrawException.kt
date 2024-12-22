package kr.kro.dokbaro.server.core.studygroup.application.service.exception

import kr.kro.dokbaro.server.common.exception.http.status4xx.BadRequestException

class LeaderCannotWithdrawException : BadRequestException("study group leader는 탈퇴할 수 없습니다")