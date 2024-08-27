package kr.kro.dokbaro.server.core.member.application.service

import kr.kro.dokbaro.server.common.exception.http.status4xx.NotFoundException

class NotFoundMemberException : NotFoundException("member를 찾을 수 없습니다.")